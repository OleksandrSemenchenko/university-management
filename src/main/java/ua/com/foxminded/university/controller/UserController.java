package ua.com.foxminded.university.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.service.UserService;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController extends DefaultController {

    private UserService<UserModel> userService;

    public UserController(UserService<UserModel> userService) {
        this.userService = userService;
    }
    
    @PostMapping(value = "/delete", params = {"email"})
    public String delete(@RequestParam("email") String email) throws ServiceException {
        userService.deleteByEmail(email);
        return "redirect:/users/list";
    }
    
    @PostMapping(value = "/edit", params = {"userEmail"})
    public String edit(@RequestParam("userEmail") String userEmail, 
                       UserModel updatedUser, 
                       BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            handleBindingResultError(bindingResult);
        }
        log.error(userEmail);
        UserModel persistedUser = userService.getByEmail(userEmail);
        persistedUser.setEmail(updatedUser.getEmail());
        persistedUser.setEnabled(updatedUser.getEnabled());
        
        if (updatedUser.hasUserAuthority()) {
            if (persistedUser.hasUserAuthority()) {
                Integer userAuthorityId = persistedUser.getUserAuthority().getId();
                updatedUser.getUserAuthority().setId(userAuthorityId);
            }
            updatedUser.getUserAuthority().setUser(persistedUser);
            persistedUser.setUserAuthority(updatedUser.getUserAuthority());
        }
        userService.updateUser(persistedUser);
        return "redirect:/users/list";
    }

    @GetMapping("/list")
    public String listAllUsers(Model model) throws ServiceException {
        List<UserModel> allUsers = userService.getAllUsers();
        List<UserModel> notAuthorizedUsers = userService.getNotAuthorizedUsers();
        UserModel modelUser = new UserModel();
        model.addAttribute("notAuthorizedUsers", notAuthorizedUsers);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("userModel", modelUser);
        return "users/list";
    }

    @PostMapping(value = "/authorize", params = {"password", "passwordConfirm"})
    public String authorizeUser(@RequestParam("email") String email,
                                @RequestParam("password") String password, 
                                @RequestParam("passwordConfirm") String passwordConfirm,
                                UserModel userModel, 
                                BindingResult bindingResult) throws ServiceException {
        handleBindingResultError(bindingResult);

        if (!password.equals(passwordConfirm)) {
            return "users/noconfirm";
        }
        
        try {
            userService.getByEmail(email);
        } catch (ServiceException e) {
            return "users/notfound";
        }
        
        userModel.setPassword(password);
        userService.createUser(userModel);
        return "redirect:/users/list";
    }
    
    private String handleBindingResultError(BindingResult bindingResult) {
        bindingResult.getAllErrors()
                     .stream()
                     .forEach(error -> log.error(error.getDefaultMessage()));
        return "error";
    }
}
