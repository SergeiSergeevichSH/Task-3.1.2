package ru.itmentor.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public User getById(int id) {

        Optional<User> userOptional = usersRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public User getByUsername(String username) {
        return (User) usersRepository.getUserByUsername(username);
    }


    @Override
    public void save(User user) {
        usersRepository.save(user);
    }

    @Override
    public void update(User user) {
        Optional<User> existingUser = usersRepository.findById(user.getId());
        existingUser.ifPresent(userToUpdate -> {
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setRoles(user.getRoles());
            usersRepository.save(userToUpdate);
        });
    }

    @Override
    public void delete(int id) {
        usersRepository.deleteById(id);
    }

    @Override
    public User createUser(User user, List<String> roleNames, String password ) {

        // Обработка пароля
        user.setPassword(passwordEncoder.encode(password));

        // Обработка ролей
        Set<Role> userRoles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = new Role();
            role.setRole(roleName);
            role.setUser(user);
            userRoles.add(role);
        }

        user.setRoles(userRoles);

        return user;
    }
}
