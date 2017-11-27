package com.imall.service;

import com.imall.common.ServerResponse;
import com.imall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(User user);
    ServerResponse<String> register(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> getQuestion(User user);
    ServerResponse<String> validQuestion(User user);
    ServerResponse<String> forgetPassword(String username, String password, String token);
    ServerResponse<String> resetPassword(User user, String oldPassword,String newPassword);
    ServerResponse<User> getUserInfobyKey(User user);
}
