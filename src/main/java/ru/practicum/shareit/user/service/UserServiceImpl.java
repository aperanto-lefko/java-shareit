package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UpdateUserRequest;
import ru.practicum.shareit.user.repository.UserRepository;

import java.security.URIParameter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto getUser(Long id) {
        return UserMapper.toUserDto(userRepository.getUserById(id).get());
    }

    public UserDto createUser(User user) {
        return UserMapper.toUserDto(userRepository.createUser(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteUserById(id);
    }

    public UserDto updateUser(UpdateUserRequest request) {
        User user = userRepository.getUserById(request.getId()).get();
        if (request.hasEmail() &&
                !request.getEmail().equals(user.getEmail()) &&
                isEmailRegistered(request.getEmail())) {
            throw new BadRequestException("Данный e-mail уже зарегистрирован");
        }
        UserMapper.updateUserFields(user, request);
        return UserMapper.toUserDto(user);
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.isEmailRegistered(email);
    }


    public boolean isUserRegistered(Long id) {
        return userRepository.getUserById(id).isPresent();
    }
}

