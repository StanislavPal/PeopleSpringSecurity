package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.dao.RoleDao;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class PeopleController {

    private final UserService userService;
    private final RoleDao roleDao;

    @Autowired
    public PeopleController(UserService userService, RoleDao roleDao) {
        this.userService = userService;
        this.roleDao = roleDao;
    }


    @RequestMapping ("/login")
    public String loginPage () {
        return "login";
    }

    @GetMapping ("/admin")
    public String adminPage (Principal principal, ModelMap modelMap) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append(" ");
        }

        modelMap.addAttribute("username", principal.getName());
        modelMap.addAttribute("roleSet", new String(sb));
        //modelMap.addAttribute("allUsers", allUsers);

        return "admin";
    }

    @GetMapping (value = "/admin/people"/*, method = {RequestMethod.GET, RequestMethod.POST}*/)
    public String allUsers (ModelMap modelMap) {
        modelMap.addAttribute("allUsers", userService.allUsers());
        modelMap.addAttribute("allRoles", roleDao.allRoles());
        return "people";
    }

    /*@RequestMapping(value = "/admin/people", method = RequestMethod.POST)
    public String people(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("allUsers", userService.allUsers());
        return "people";
    }*/


    @GetMapping ("admin/people/add")
    public String newUser (@ModelAttribute("user") User user, ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("allRoles", roleDao.allRoles());  // отображение всех ролей в чек-боксе
        return "add";
    }

    @PostMapping("/admin/people")
    public String create (@ModelAttribute("user") User user,
                          /*@RequestParam("firstName") String firstName,
                          @RequestParam("lastName") String lastName,
                          @RequestParam("age") int age,
                          @RequestParam("email") String email,
                          @RequestParam("login") String login,
                          @RequestParam("password") String password,*/
                          @RequestParam("allRoles") String [] roles) {


        Set<Role> roleSet = new HashSet<>();
        for (String roleName : roles) {
            roleSet.add(roleDao.findRoleByName(roleName));
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin/people";
    }

    @GetMapping("/admin/people/{id}")
    public String getById(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getById(id));
        modelMap.addAttribute("allRoles", roleDao.allRoles());

        return "id";
    }

    @GetMapping("/admin/people/{id}/edit")
    public String edit (@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("allRoles", roleDao.allRoles());
        modelMap.addAttribute("user", userService.getById(id));
        return "edit";
    }

    @PostMapping("/admin/people/{id}")
    public String update (@ModelAttribute("user") User user,
//                          ModelMap modelMap,
                          @RequestParam("allRoles") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String roleName : roles) {
            roleSet.add(roleDao.findRoleByName(roleName));
        }
        user.setRoles(roleSet);
        userService.updateUser(user);
//        modelMap.addAttribute("user", userService.updateUser(user));
//        modelMap.addAttribute("allRoles", roleDao.allRoles());
        return "redirect:/admin/people";
    }


    @DeleteMapping("/admin/people/{id}")
    public String delete (@PathVariable("id") int id) {
        User user = userService.getById(id);
        userService.deleteUser(user);
        return "redirect:/admin/people";
    }

    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        StringBuilder sb = new StringBuilder();
        for (String role : roles) {
            sb.append(role).append(" ");
        }

        model.addAttribute("username", principal.getName());
        model.addAttribute("roleSet", new String(sb));

        String userName = principal.getName();

        User user = userService.findByUsername(userName);

        model.addAttribute("user", user);

        return "user";
    }
}
