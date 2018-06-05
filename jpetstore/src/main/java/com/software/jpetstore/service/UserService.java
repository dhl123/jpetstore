package com.software.jpetstore.service;

import com.software.jpetstore.domain.User;
import com.software.jpetstore.domain.User;

import java.sql.SQLException;

public interface UserService {
    void addUser(User user);
    void changePassword(String userName, String password);
    boolean check(User user) throws SQLException;
}
