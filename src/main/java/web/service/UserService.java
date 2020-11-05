package web.service;

import web.model.User;

import java.util.List;

public interface UserService {

    List<User> allUsers();
    void addUser (User user);
    void deleteUser (User user);
    User updateUser (User user);
    User getById (int id);
    User findByUsername(String username);
}
