package pl.projektBsk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministrationController {




    @GetMapping("/administration")
    public String getAdmin(Model model){
        return "administration";
    }
}
