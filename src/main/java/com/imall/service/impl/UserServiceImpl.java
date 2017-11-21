package com.imall.service.impl;

import com.imall.common.ServerResponse;
import com.imall.dao.UserMapper;
import com.imall.pojo.User;
import com.imall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServerResponse<User> login(User user) {
        int result=userMapper.checkUserName(user);
        if(result==0){
            return ServerResponse.createByErrorMessage("用户名不存在！");
        }
        //密码的MD5加密
        User userResult=userMapper.checkLogin(user);
        if(userResult==null){
            return ServerResponse.createByErrorMessage("用户名密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登陆成功！",userResult);

    }
}
