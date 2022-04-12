package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class UserRatingsController {


    private UserRepository userRepository;
    private GameRepository gameRepository;
    private UserServiceImpl userService;

    @Autowired
    public UserRatingsController(UserRepository userRepository, GameRepository gameRepository, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @GetMapping("/ratings")
    public String getRatings(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        List<Rating> ratings = user.get().getRate();
        model.addAttribute("ratings",ratings);
        return "userRatings";
    }


    //Pobranie widoku ze szcegółami gry, dostępne w panelu użytkownika "ocenione"
    @GetMapping("/ratings/{id}")
    public String getDetailsGame(@PathVariable long id, Model model){
        Optional<Game> game = gameRepository.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(game.isPresent()){
            if (!auth.getName().equals("anonymousUser")) {
                User user = userService.getUser(auth.getName());
                model.addAttribute("userComments",user.getComments());
            }
            double rate =0;
            if (!game.get().getRate().isEmpty()) {
                rate = game.get().getRate().stream().map(Rating::getRate).mapToDouble(Double::doubleValue).sum()/game.get().getRate().size();
            }
            model.addAttribute("rate",rate);
            model.addAttribute("game",game.get());
            return "ratingsDetails";
        }else return "redirect:/ratings";
    }
}
