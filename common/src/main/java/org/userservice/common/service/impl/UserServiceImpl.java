package org.userservice.common.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;
import org.userservice.common.model.entity.User;
import org.userservice.common.model.exception.UserServiceApiException;
import org.userservice.common.repository.UserRepository;
import org.userservice.common.service.UserService;
import org.userservice.common.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final int INTEGER_1 = 1;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(CreateRequestUserDto userDto) {
        Optional<User> findByEmail = userRepository.findByEmail(userDto.email());
        if (findByEmail.isPresent()) {
            throw new UserServiceApiException("User with email: " + userDto.email() + " already exist",
                    String.valueOf(HttpStatus.BAD_REQUEST.value()));
        }
        User newUser = userRepository.addUser(userMapper.mapToModel(userDto));
        return userMapper.mapToDto(newUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        trustUser(userId, null)
        return userMapper.mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser =  trustUser(userId, userDto.getEmail());
        updateFieldsOfUser(existingUser, userDto);
        existingUser = userRepository.updateUser(userId, existingUser);
        return userMapper.mapToDto(existingUser);
    }
    
    @Override
    public UserDto updateAllUser(Long userId, CreateRequestUserDto userDto) {
        trustUser(userId, userDto.email());
        User user = userMapper.mapToModel(userDto);
        userRepository.updateUser(userId, user);
        return userMapper.mapToDto(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return userRepository.deleteUser(userId);
    }

    @Override
    public List<UserDto> searchUsersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        List<User> allUsers = userRepository.getAllUsers();

        List<User> filteredUsers = allUsers.stream()
                .filter(user -> user.getBirthDate()
                            .isAfter(fromDate.minusDays(INTEGER_1))
                        && user.getBirthDate().isBefore(toDate.plusDays(INTEGER_1)))
                .toList();

        return filteredUsers.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());

    }

    private void updateFieldsOfUser(User existingUser, UserDto userDto) {
        String email = userDto.getEmail();
        if (email != null && !email.isEmpty()) {
            existingUser.setEmail(email);
        }

        String firstName = userDto.getFirstName();
        if (firstName != null && !firstName.isEmpty()) {
            existingUser.setFirstName(firstName);
        }

        String lastName = userDto.getLastName();
        if (lastName != null && !lastName.isEmpty()) {
            existingUser.setLastName(lastName);
        }

        LocalDate birthDate = userDto.getBirthDate();
        if (birthDate != null) {
            existingUser.setBirthDate(birthDate);
        }

        String address = userDto.getAddress();
        if (address != null && !address.isEmpty()) {
            existingUser.setAddress(address);
        }

        String phoneNumber = userDto.getPhoneNumber();
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            existingUser.setPhoneNumber(phoneNumber);
        }
    }

    private User trustUser(Long userId, String email) {
        User existingUser = userRepository.getUser(userId);
        if (existingUser == null) {
            throw new UserServiceApiException("User with id: " + userId + " not found",
                    String.valueOf(HttpStatus.NOT_FOUND.value()));
        }
        if (email != null) {
            Optional<User> findByEmail = userRepository.findByEmail(email);
            if (findByEmail.isPresent() && findByEmail.get().getId() != userId) {
                throw new UserServiceApiException("User with email: " + email + " already exist",
                        String.valueOf(HttpStatus.BAD_REQUEST.value()));
            }
        }
        return existingUser;
    }
}
