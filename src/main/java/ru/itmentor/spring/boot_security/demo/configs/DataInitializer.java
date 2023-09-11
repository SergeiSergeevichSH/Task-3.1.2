package ru.itmentor.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.itmentor.spring.boot_security.demo.repository.UserRole;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import javax.transaction.Transactional;


@Component
public class DataInitializer implements CommandLineRunner {

    private UserService userService;
    private UserRole userRole;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserService userService, UserRole userRole, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRole = userRole;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) {

        Role roleUser = new Role("ROLE_USER");
        userRole.save(roleUser);

        Role roleAdmin = new Role("ROLE_ADMIN");
        userRole.save(roleAdmin);

        User user1 = new User();
        user1.setName("Антон");
        user1.setSurname("Антонов");
        user1.setUsername("anton");
        user1.setAge(30);
        user1.setPassword(passwordEncoder.encode("4321"));
        user1.addRole(roleUser);

        User admin = new User();
        admin.setName("Сергей");
        admin.setSurname("Сергеев");
        admin.setUsername("shish81");
        admin.setAge(35);
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.addRole(roleAdmin);

        userService.save(admin);
        userService.save(user1);

    }
}
