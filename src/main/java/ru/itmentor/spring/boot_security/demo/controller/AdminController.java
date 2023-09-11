package ru.itmentor.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("admin")
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")

    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/all-users";
    }

    @GetMapping("{id}/edit")
    public String editUserPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getById(id));
        return "/users/edit-user";
    }

    @GetMapping("new")
    public String newUserPage(Model model) {
        model.addAttribute(new User());
        return "/users/add-new-user";
    }

    @PatchMapping("{id}")
    public String editUser(@PathVariable("id") int id, @ModelAttribute("user") User updatedUser) {
        User existingUser = userService.getById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setAge(updatedUser.getAge());
        userService.update(existingUser);
        return "redirect:/admin";
    }

    @PostMapping("")
    public String addNewUser(
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("age") int age,
            @RequestParam("surname") String surname,
            @RequestParam("password") String password,
            @RequestParam("roles") String role
    ) {
        // Создайте объект User на основе полученных параметров
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setPassword(passwordEncoder.encode(password));
        System.out.println(user);

        // Добавьте выбранные роли, если они были выбраны
        if (role.equals("ROLE_USER")) {
            Role userRole = new Role();
            userRole.setRole("ROLE_USER");
            userRole.setUser(user);
            user.addRole(userRole);
        } else if (role.equals("ROLE_ADMIN")) {
            Role adminRole = new Role();
            adminRole.setRole("ROLE_ADMIN");
            adminRole.setUser(user);
            user.addRole(adminRole);
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
