package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDaoImpl extends AbstractDao<Integer, User> implements UserDAO {


    @Override
    @SuppressWarnings("unchecked")
    public List<User> allUsers() {
        List<User> users = getEntityManager()
        .createQuery("select u from User u").getResultList();

        return users;
    }

    @Override
    public void addUser(User user) {
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
}
