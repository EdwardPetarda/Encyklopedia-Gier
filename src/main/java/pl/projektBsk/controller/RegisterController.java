package pl.projektBsk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.projektBsk.service.UserService;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;
import pl.projektBsk.user.UserRole;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public RegisterController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("/register")
    public String getRegister(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }


    //Rejestracja użytkownika
    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "register";
        }

        if(user.getUsername().equals(userRepository.findByUsername(user.getUsername()))){
            return "register";
        }else{
            user.setRole(UserRole.USER);
            userService.saveUser(user);
            model.addAttribute("user",user);
            return "succesReg";
        }


    }


}
