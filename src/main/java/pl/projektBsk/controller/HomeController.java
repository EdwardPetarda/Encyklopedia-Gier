package pl.projektBsk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.projektBsk.user.User;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session){
        if (session.getAttribute("user") == null) {
            session.setAttribute("user",new User());
        }
        return "index";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
