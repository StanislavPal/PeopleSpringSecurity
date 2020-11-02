package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.dao.RoleDao;
import web.model.User;
import web.service.UserService;

import java.security.Principal;
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


    @GetMapping ("/login")
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

        String userName = principal.getName();
        User user = userService.findByUsername(userName);

        modelMap.addAttribute("user", user);

        return "admin";
    }

    @GetMapping (value = "/people")
    public String allUsers (ModelMap modelMap) {
        modelMap.addAttribute("allUsers", userService.allUsers());
        return "people";
    }

    @GetMapping ("/add")
    public String newUser (@ModelAttribute ("user") User user) {
        return "add";
    }

    @PostMapping()
    public String create (@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getById(id));
        return "id";
    }

    @GetMapping("/{id}/edit")
    public String edit (@PathVariable("id") int id, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.getById(id));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("user") User user, ModelMap modelMap) {
        modelMap.addAttribute("user", userService.updateUser(user));
        return "redirect:/people";
    }


    @DeleteMapping("/{id}")
    public String delete (@PathVariable("id") int id) {
        User user = userService.getById(id);
        userService.deleteUser(user);
        return "redirect:/people";
    }
}
