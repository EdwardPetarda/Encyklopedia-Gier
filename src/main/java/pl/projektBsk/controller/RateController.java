package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.service.GameService;
import pl.projektBsk.service.RateService;

@Controller
public class RateController {

    private RateService rateService;
    private GameService gameService;

    @Autowired
    public RateController(RateService rateService, GameService gameService) {
        this.rateService = rateService;
        this.gameService = gameService;
    }

    //Dodanie oceny, jeżeli już użytkownik dodał ocene do danej gry to następuje edycja oceny, wykorzystywane w podstawowym widoku szczegółów gry 
    @PostMapping("/rate/{id}")
    public String addRate(@PathVariable Long id, @RequestParam double userRate, Model model) {
        if (gameService.exist(id)) {
            if (rateService.rateIsExist(id)) {
                rateService.editRate(userRate,id);
            } else {
                if (rateService.addRate(userRate, id)) { //dodanie informacji o bledzie
                    return "redirect:/game/{id}";
                }
            }
            return "redirect:/game/{id}";

        }
            model.addAttribute("message","Gra, ktora chciales ocenic nie istnieje");
            return "errorView";

    }
    //Wykorzystywane w panelu użytkownika "ocenione" przy wejściu na stronę szczegółów gry, edytuje ocenę gry.
    @PostMapping("/ratings/{id}")
    public String editRate(@PathVariable Long id, @RequestParam double userRate, Model model) {
        if (gameService.exist(id)) {
            if (rateService.rateIsExist(id)) {
                rateService.editRate(userRate,id);
            } else {
                if (rateService.addRate(userRate, id)) { //dodanie informacji o bledzie
                    return "redirect:/ratings/{id}";
                }
            }
            return "redirect:/ratings/{id}";

        }
        model.addAttribute("message","Gra, ktora chciales ocenic nie istnieje");
        return "errorView";

    }
}
