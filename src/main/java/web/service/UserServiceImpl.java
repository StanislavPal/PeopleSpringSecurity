package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDaoImpl;
import web.model.User;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private UserDaoImpl userDAO;

    @Autowired
    public void setUserDAO (UserDaoImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public List<User> allUsers() {
        return userDAO.allUsers();
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userDAO.addUser(user);

    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userDAO.deleteUser(user);

    }

    @Override
    @Transactional
    public User updateUser(User user) {
        userDAO.updateUser(user);
        return user;
    }

    @Override
    @Transactional
    public User getById(int id) {
        return userDAO.getById(id);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
