package ru.starashchuk.shopping.service.DTO;

import jakarta.validation.constraints.*;

public class UserDTO {

    @NotEmpty
    @Size(min = 3, max = 30, message = "Логин должен быть от 3 до 30 символов")
    private String username;

    @NotEmpty
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String firstName;

    @Size(max = 30, message = "Фамилия не более 30 символов")
    private String lastName;

    @NotEmpty
    @Email(message = "Введите корректный email")
    private String email;

    @NotEmpty
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}