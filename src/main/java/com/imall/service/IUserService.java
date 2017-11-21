package com.imall.service;

import com.imall.common.ServerResponse;
import com.imall.pojo.User;

public interface IUserService {
    ServerResponse<User> login(User user);
}
