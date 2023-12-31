package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getById(int id);

    User getByUsername(String username);

    void save(User user);

    void delete(int id);

    void update(User user);
}
