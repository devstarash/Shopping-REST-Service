package ru.starashchuk.shopping.service.servises;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.UserDAO;
import ru.starashchuk.shopping.service.DTO.UserDTO;
import ru.starashchuk.shopping.service.exceptions.InvalidPasswordException;
import ru.starashchuk.shopping.service.exceptions.UserAlreadyExistsException;
import ru.starashchuk.shopping.service.models.User;

@Component
public class UserService {
    private UserDAO userDAO;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserDAO userDAO, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userDAO = userDAO;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    public void register(UserDTO userDTO) {
        if (userDAO.existsByUsername(userDTO.getUsername())) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }
        if (userDAO.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        userDAO.save(user);
    }
    public User login(String username, String password){
        User user = userDAO.findByUsername(username);
        if (!encoder.matches(password, user.getPassword())){
            throw new InvalidPasswordException("Неверный пароль");
        }
        return user;
    }

}
