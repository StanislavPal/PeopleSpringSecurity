package web.service;

import web.model.Role;

import java.util.List;

public interface RoleService {

    Role getRole(int id);
    List<Role> allRoles();
    Role findRoleByName(String roleName);
}
