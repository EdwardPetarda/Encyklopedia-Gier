package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class MyAccountController {

    private UserRepository userRepository;
    private UserServiceImpl userService;

    @Autowired
    public MyAccountController(UserRepository userRepository, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }



    @GetMapping("/myAccount")
    public String getUserDate(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name).get();
        model.addAttribute("user",user);
        return "myAccount";
    }

    //Pobranie formularza do zmiany hasła
    @GetMapping("/myAccount/zmienHaslo")
    private String getChangePassword(Model model){
        model.addAttribute("invalidCredentials",false);
        return "changePasswordForm";
    }

    //Zmiana hasła
    @PostMapping("/myAccount/zmienHaslo")
    private String changePassword(@RequestParam String oldPassword, @RequestParam String password, @RequestParam String checkPassword, Model model){

        if(userService.changePassword(oldPassword,password,checkPassword)){
            model.addAttribute("result","Udalo sie zmienic haslo");
            return "changePasswordForm";
        }
        else {
            model.addAttribute("invalidCredentials",true);
            return "changePasswordForm";
        }

    }

    //Usunięcie konta
    @GetMapping("/myAccount/deleteAccount")
    private String deleteAccount(HttpSession session, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Optional<User> user = userRepository.findByUsername(auth.getName());
        userRepository.deleteById(user.get().getID());
        session.setAttribute("user",new User());
        model.addAttribute("message","Udalo sie usunac konto");
        return "index";
    }




}
