package web.dao;

import web.model.Role;

import java.util.List;

public interface RoleDao {
    Role getRole(int id);
    List<Role> allRoles();
    Role findRoleByName(String roleName);
}
