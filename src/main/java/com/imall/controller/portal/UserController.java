package com.imall.controller.portal;

import com.imall.common.ServerResponse;
import com.imall.pojo.User;
import com.imall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
@SessionAttributes(value = "currentUser")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(User user, Model model,HttpSession session){
        ServerResponse<User> response=iUserService.login(user);
        if(response.isSuccess()){
            model.addAttribute("currentUser",response.getData());

        }
        return response;
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }

    @RequestMapping(value = "checkValid",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }
    @RequestMapping(value = "getUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(Model model){
        User user= (User) model.asMap().get("currentUser");
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("登录失效或者没有登录！");
    }

    @RequestMapping(value = "getQuestion",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getQuestion(User user){
        return iUserService.getQuestion(user);
    }

    @RequestMapping(value = "validQuestion",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> validQuestion(User user){
        return iUserService.validQuestion(user);
        
    }
    @RequestMapping(value = "forgetPassword",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetPassword(@RequestParam(value = "username",required = true) String username,
                                                @RequestParam(value = "password",required = true) String password,
                                                @RequestParam(value = "token",required = true) String token){
        return  iUserService.forgetPassword(username,password,token);
    }
    @RequestMapping(value = "resetPassword",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(@RequestParam(value = "username",required = true) String username,
                                                 @RequestParam(value = "password",required = true) String password,
                                                 @RequestParam(value = "newPassword",required = true) String newPassword,
                                                Model model){
        User user= (User) model.asMap().get("currentUser");
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登陆！");
        }
        return  iUserService.resetPassword(user,password,newPassword);
    }
    @RequestMapping(value = "getUserInfobyKey",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfobyKey(User user,Model model){
        User currentUser= (User) model.asMap().get("currentUser");
        if(currentUser!=null){
            iUserService.getUserInfobyKey(user);
        }
        return ServerResponse.createByErrorMessage("登录失效或者没有登录！");

    }


}
