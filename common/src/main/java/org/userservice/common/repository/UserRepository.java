package org.userservice.common.repository;

import org.springframework.stereotype.Component;
import org.userservice.common.model.entity.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {
    private static final Long FIRST_ID = 1L;
    private static final String INVALID_ID = "Id can't be negative";
    private final List<User> userTable = new LinkedList<>();
    private Long lastUserID = FIRST_ID;

    public User addUser(User user) {
        user.setId(lastUserID);
        userTable.add(user);
        lastUserID++;
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return getAllUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public User getUser(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException(INVALID_ID);
        }
        int index = id.intValue() - 1;
        if (id > lastUserID || userTable.get(index).isDeleted()) {
            return null;
        }
        return userTable.get(index);
    }

    public List<User> getAllUsers() {
        return userTable.stream()
                .filter(user -> !user.isDeleted())
                .toList();
    }

    public boolean deleteUser(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException(INVALID_ID);
        }
        if (id > lastUserID) {
            return false;
        }
        int index = id.intValue() - 1;
        User user = userTable.get(index);
        user.setIsDeleted(true);
        return true;
    }

    public User updateUser(Long id, User updatedUser) {
        if (id < 1) {
            throw new IllegalArgumentException(INVALID_ID);
        }
        int index = id.intValue() - 1;
        if (id > lastUserID || userTable.get(index).isDeleted()) {
            return null;
        }
        if (updatedUser.getId() == null) {
            updatedUser.setId(id);
        }
        userTable.set(index, updatedUser);
        return updatedUser;
    }

    public void clean() {
        userTable.clear();
        lastUserID = FIRST_ID;
    }
}
