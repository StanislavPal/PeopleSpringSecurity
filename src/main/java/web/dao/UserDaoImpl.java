package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.NoResultException;
import java.util.*;

@Repository
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDAO {

    private final RoleDao roleDao;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserDaoImpl(RoleDao roleDao, PasswordEncoder bCryptPasswordEncoder) {
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<User> allUsers() {
        return (List<User>) getEntityManager()
        .createQuery("select u from User u").getResultList();
    }

    @Override
    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, "ROLE_USER"));
        //roles.add(roleDao.getRole(1));
        user.setRoles(roles);
        entityManager.persist(user);
    }

    @Override
    public void deleteUser(User user) {
        user = entityManager.find(User.class, user.getId());
        entityManager.remove(user);

    }

    @Override
    public User updateUser(User user) {

        return entityManager.merge(user);

    }

    @Override
    public User getById(int id) {
        try {
            return (User) getEntityManager()
                    .createQuery("select u from User u where u.id =:id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public User findByUsername(String login) {
        try {
            return (User) entityManager
                    .createQuery("select u from User u where lower(u.login) like :username")
                    //.createQuery("select u from User u where lower(u.login)=:login")
                    .setParameter("username", "%" + login.toLowerCase() + "%")
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
