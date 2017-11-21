package com.imall.controller.portal;

import com.imall.common.ServerResponse;
import com.imall.pojo.User;
import com.imall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/user/")
@SessionAttributes("currentUser")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(User user, Model model){
        ServerResponse<User> response=iUserService.login(user);
        if(response.isSuccess()){
            model.addAttribute("currentUser",response.getData());
        }
        return response;
    }

}
