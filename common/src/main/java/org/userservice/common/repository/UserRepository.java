package org.userservice.common.repository;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.userservice.common.model.entity.User;

@Component
public class UserRepository {
    private final List<User> userTable = new LinkedList<>();
    private Long lastUserID = 1L;

    public User addUser(User user) {
        user.setId(lastUserID);
        userTable.add(user);
        lastUserID++;
        return user;
    }

    public User getUser(Long id) {
        int index = id.intValue() - 1;
        if (id > lastUserID || userTable.get(index).isDeleted()) {
            return null;
        }
        if (id < 1) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return userTable.get(index);
    }

    public List<User> getAllUsers() {
        return userTable.stream()
                .filter(user -> !user.isDeleted())
                .toList();
    }

    public boolean deleteUser(Long id) {
        if (id > lastUserID) {
            return false;
        }
        if (id < 1) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        int index = id.intValue() - 1;
        User user = userTable.get(index);
        user.setIsDeleted(true);
        return true;
    }

    public User updateUser(Long id, User updatedUser) {
        int index = id.intValue() - 1;
        if (id > lastUserID || userTable.get(index).isDeleted()) {
            return null;
        }
        if (id < 1) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (updatedUser.getId() == null) {
            updatedUser.setId(id);
        }
        userTable.set(index, updatedUser);
        return updatedUser;
    }

    public void clean() {
        userTable.clear();
        lastUserID = 1L;
    }
}