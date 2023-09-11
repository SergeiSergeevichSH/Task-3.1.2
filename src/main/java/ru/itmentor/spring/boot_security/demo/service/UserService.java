package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getById(int id);

    User getByUsername(String username);

    void save(User user);

    void delete(int id);

    User createUser(
            String name,
            String username,
            int age,
            String surname,
            String password,
            List<String> roleNames);

    void update(User user);
}
