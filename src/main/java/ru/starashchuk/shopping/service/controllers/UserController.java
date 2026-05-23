package ru.starashchuk.shopping.service.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.starashchuk.shopping.service.DTO.LoginDTO;
import ru.starashchuk.shopping.service.DTO.UserDTO;
import ru.starashchuk.shopping.service.exceptions.UserNotCreatedException;
import ru.starashchuk.shopping.service.models.User;
import ru.starashchuk.shopping.service.servises.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            result.getFieldErrors().forEach(error ->
                    errorMessage
                            .append(error.getField())
                            .append(": ")
                            .append(error.getDefaultMessage())
                            .append("; ")
            );
            throw new UserNotCreatedException(errorMessage.toString());
        }
        userService.register(userDTO);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto, HttpSession session) {
        User user = userService.login(dto.getUsername(), dto.getPassword());
        session.setAttribute("user", user);
        return ResponseEntity.ok("Успешный вход");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Вы вышли из системы");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getSessionUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Не авторизован"));
        }
        return ResponseEntity.ok(user);
    }
}