package com.tradingsim.service.user;

import com.tradingsim.model.User;
import com.tradingsim.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }

        User user = new User();
        user.setUsername(username);

        return userRepository.save(user);
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User with id " +
                                        id +
                                        " does not exist"
                        ));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUsername(int id, String newUsername) {
        if (newUsername == null || newUsername.isBlank()) {
            throw new IllegalArgumentException("Username must not be empty");
        }

        User user = getUserById(id);
        user.setUsername(newUsername);

        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
