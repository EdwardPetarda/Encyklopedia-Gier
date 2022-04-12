package pl.projektBsk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("invalidCredentials", false);
        return "login";
    }
    @PostMapping("login")
    public String login(@RequestParam String username, @RequestParam String password,Model model,HttpSession session){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent() && user.get().getPassword().equals(password)) {
            model.addAttribute("invalidCredentials", false);
            model.addAttribute("user",user);
            session.setAttribute("user",user);
            return "index";
        }
        else{
            model.addAttribute("invalidCredentials", true);
            return "login";
        }



    }


    @GetMapping("/logout")
    public String logut(HttpSession session){
        session.setAttribute("user",new User());
        return "index";
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String handleMissingParams(Model model){
        model.addAttribute("invalidCredentials", true);
        return "login";
    }

}
