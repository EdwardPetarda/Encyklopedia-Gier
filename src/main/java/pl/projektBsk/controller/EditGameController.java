package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameMapper;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.service.EditGameService;
import pl.projektBsk.service.GameService;
import pl.projektBsk.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class EditGameController {

    private GameRepository gameRepository;

    private EditGameService editGameService;


    @Autowired
    public EditGameController(GameRepository gameRepository,  EditGameService editGameService) {
        this.gameRepository = gameRepository;

        this.editGameService = editGameService;
    }

    //Pobranie widoku z formularzem do edycji danych gry
   @GetMapping("/edit/{id}")
   public String getEditForm(@PathVariable Long id, Model model){
       Optional<Game> game = gameRepository.findById(id);

       if (game.isPresent()){
           model.addAttribute("gameToEdit",game.get());
           model.addAttribute("emptyFields",false);
           model.addAttribute("wrongFormat", false);
           return "editForm";
       }else {
           model.addAttribute("message","Podana gra nie istnieje");
           return "errorView";
       }
   }

    //Edycja informacji na temat gry w bazie danych 
   @PostMapping("/edit/{id}")
    public String editGame(@PathVariable Long id, @RequestParam String name, @RequestParam String genre, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                           @RequestParam String shortDescription, @RequestParam String developer, @RequestParam String publisher,
                           @RequestParam String ogolnyOpis, @RequestParam String fabula, @RequestParam String mechanika,
                           @RequestParam String inne, @RequestParam ("image") MultipartFile image, Model model) {
        Optional<Game> game = gameRepository.findById(id);
        if(image.isEmpty() && game.isPresent()){
           model.addAttribute("emptyFields", true);
           return "redirect:/edit/"+game.get().getId();
       }
        else if (game.isPresent() && !image.isEmpty()){
            if(editGameService.editGame(game.get(),name,genre,date,shortDescription,developer,publisher,ogolnyOpis,fabula
            ,mechanika,inne,image)){
            return "redirect:/game/"+game.get().getId();}
            else {
                model.addAttribute("message","Nie udało sie zedytowac");
                return "errorView";
            }
        }
        else {
            model.addAttribute("message","Podana gra nie istnieje");
            return "errorView";
        }
   }

    
    //Przechwycenie wyjątku o brakujących parametrach w zapytaniu. 
    @ExceptionHandler({MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public String handlMissingParams(Model model, HttpServletRequest request){
        Game game = new Game();
        GameDetails gameDetails = new GameDetails();
        model.addAttribute("game", game);
        model.addAttribute("gameDetails",gameDetails);
        model.addAttribute("emptyFields", true);
        model.addAttribute("wrongFormat", false);
        String view = request.getRequestURI();
        return "redirect:"+view;
    }


}
