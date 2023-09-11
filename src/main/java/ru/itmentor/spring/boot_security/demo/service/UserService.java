package ru.itmentor.spring.boot_security.demo.service;


import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserService {
    private final UserRepository usersRepository;
    private final UserRole userRole;

    @Autowired
    public UserService(UserRepository usersRepository, UserRole userRole) {
        this.usersRepository = usersRepository;
        this.userRole = userRole;
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public User getById(int id) {
        User user = userRole.findByIdWithRoles(id);
        if (user != null) {
            Hibernate.initialize(user.getRoles());
        }
        return user;
    }

    public User getByUsername(String username) {
        return (User) usersRepository.getUserByUsername(username);
    }

    public void save(User user) {
        usersRepository.save(user);
    }

    public void update(User user) {
        Optional<User> existingUser = usersRepository.findById(user.getId());
        existingUser.ifPresent(userToUpdate -> {
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setRoles(user.getRoles());
            usersRepository.save(userToUpdate);
        });
    }

    public void delete(int id) {
        usersRepository.deleteById(id);
    }
}
