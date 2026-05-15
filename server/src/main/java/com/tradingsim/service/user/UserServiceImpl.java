package com.tradingsim.service.user;

import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.User;
import com.tradingsim.repository.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username must not be empty");
        }

        User user = new User();
        user.setUsername(username);

        return userRepository.create(user);
    }

    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
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
            throw new ValidationException("Username must not be empty");
        }

        User user = getUserById(id);
        user.setUsername(newUsername);

        return userRepository.update(user);
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }
}
