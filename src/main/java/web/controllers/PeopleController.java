package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import web.service.UserServiceImpl;

@Controller
@RequestMapping (value = "/people")
public class PeopleController {

    private final UserService userService;

    @Autowired
    public PeopleController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping ()
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
