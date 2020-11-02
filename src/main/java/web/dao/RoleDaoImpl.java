package web.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;

import java.util.List;

@Repository
public class RoleDaoImpl extends AbstractDao<Integer, Role> implements RoleDao {

    @Override
    @Transactional
    public Role getRole(int id) {
        return getEntityManager().find(Role.class, id);
    }

    @Override
    @Transactional
    public List<Role> allRoles() {
        return entityManager.createQuery("select r from Role r").getResultList();
    }

    @Override
    @Transactional
    public Role findRoleByName(String roleName) {
        return (Role) entityManager
                .createQuery("select r from Role r where lower(r.roleName) like :roleName")
                .setParameter("roleName", "%" + roleName.toLowerCase() + "%")
                .getSingleResult();
    }
}
