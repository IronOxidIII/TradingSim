package com.tradingsim.service.user;

import com.tradingsim.model.User;

import java.util.List;

public interface UserService {

    User createUser(String username);

    User getUserById(int id);

    List<User> getAllUsers();

    User updateUsername(int id, String newUsername);

    void deleteUser(int id);
}