package com.imall.service.impl;

import com.imall.common.Const;
import com.imall.common.ServerResponse;
import com.imall.common.TokenCatch;
import com.imall.dao.UserMapper;
import com.imall.pojo.User;
import com.imall.service.IUserService;
import com.imall.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service("iUserService")
public class UserServiceImpl implements IUserService {
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(User user) {
        int result = userMapper.checkUserName(user);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }
        //密码的MD5加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword()));
        User userResult = userMapper.checkLogin(user);
        if (userResult == null) {
            return ServerResponse.createByErrorMessage("用户名密码错误！");
        }
        userResult.setPassword(StringUtils.EMPTY);
        userResult.setAnswer(StringUtils.EMPTY);
        userResult.setQuestion(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功！", userResult);

    }

    @Override
    public ServerResponse<String> register(User user) {
        logger.info("============开始检查用户名和邮箱");

        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getUsername(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_USER);
        logger.info("=============进行MD5加密");
        //MD5加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            logger.debug("============注册失败");
            return ServerResponse.createByErrorMessage("注册失败，服务器异常！");

        }
        return ServerResponse.createBySuccessMessage("注册成功！");
    }

    public ServerResponse<String> checkValid(String str, String type) {
        User user = new User();
        int result;
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                user.setUsername(str);
                result = userMapper.checkUserName(user);
                if (result > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在！");
                }
            }
            else if (Const.EMAIL.equals(type)) {
                user.setEmail(type);
                result = userMapper.checkEmail(user);
                if (result > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已被注册！");
                }
            }
            else {
                return ServerResponse.createByErrorMessage("参数错误！");
            }
        }
        return ServerResponse.createBySuccessMessage("校验成功！");
    }

    public ServerResponse<String> getQuestion(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (validResponse.isSuccess()) {
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在！");
        }
        String result = userMapper.getQuestion(user);
        if (StringUtils.isNotBlank(result)) {
            return ServerResponse.createByErrorMessage("没有设置安全问题！");
        }
        return ServerResponse.createBySuccess(result);
    }

    @Override
    public ServerResponse<String> validQuestion(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在！");
        }
        int result = userMapper.getAnswer(user);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("答案错误！");
        }
        String token = UUID.randomUUID().toString();
        TokenCatch.setKey(Const.TOKEN_PREFIX + user.getUsername(), token);
        return ServerResponse.createBySuccessMessage("校验通过！");
    }

    @Override
    public ServerResponse<String> forgetPassword(String username, String password, String token) {
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在！");
        }
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token不能为空！");
        }
        String tokenValid = TokenCatch.getKey(Const.TOKEN_PREFIX + username);
        if (StringUtils.isEmpty(tokenValid)) {
            return ServerResponse.createByErrorMessage("token无效或者过期！");
        }
        if (StringUtils.equals(token, tokenValid)) {
            String md5Password = MD5Util.MD5Encode(password);
            int rowCount = userMapper.modifyPassword(username, md5Password);
            if (rowCount>0){
                TokenCatch.removeKey(Const.TOKEN_PREFIX+username);
                return ServerResponse.createBySuccessMessage("修改成功！");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误，请从新认证！");
        }
        return ServerResponse.createByErrorMessage("修改失败，请重试！");
    }

    @Override
    public ServerResponse<String> resetPassword(User user, String password,String NewPassword) {
        int result=userMapper.checkPassword(password,user.getId());
        if(result==0){
            return  ServerResponse.createByErrorMessage("旧密码错误！");

        }
        user.setPassword(MD5Util.MD5Encode(NewPassword));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功！");
        }
        return ServerResponse.createByErrorMessage("密码更新失败！");
    }

    @Override
    public ServerResponse<User> getUserInfobyKey(User user) {
        User userResult=userMapper.selectByKeySelective(user);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在！");
        }

        return ServerResponse.createBySuccess(user);
    }

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user) {
        if(user!=null&& user.getRole().intValue()==Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }


}
