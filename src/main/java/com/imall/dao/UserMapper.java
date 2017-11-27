package com.imall.dao;

import com.imall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.junit.runners.Parameterized;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(User user);
    User checkLogin(User user);
    int checkEmail(User user);

    String getQuestion(User user);

    int getAnswer(User user);
    int modifyPassword(@Param("username") String username, @Param("password") String password);
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);

    User selectByKeySelective(User user);
}