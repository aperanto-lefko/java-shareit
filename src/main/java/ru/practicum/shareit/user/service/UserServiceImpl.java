package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UpdateUserRequest;
import ru.practicum.shareit.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto getUser(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).get()); } //findById предоставляется JpaRepository

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    } //deleteById предоставляется JpaRepository
    @Transactional
    public UserDto updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.getId()).get();
        if (request.hasEmail() &&
                !request.getEmail().equals(user.getEmail()) &&
                isEmailRegistered(request.getEmail())) {
            throw new BadRequestException("Данный e-mail уже зарегистрирован");
        }
        UserMapper.updateUserFields(user, request);
        userRepository.save(user);
        return UserMapper.toUserDto(userRepository.findById(user.getId()).get());
    }

    public boolean isEmailRegistered(String email) {
        return userRepository.existsByEmail(email);
    }


    public boolean isUserRegistered(Long id) {
        return userRepository.findById(id).isPresent();
    }
}

