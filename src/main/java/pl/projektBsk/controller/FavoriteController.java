package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.service.FavoriteService;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class FavoriteController {


    private UserRepository userRepository;
    private GameRepository gameRepository;
    private FavoriteService favoriteService;
    private UserServiceImpl userService;

    @Autowired
    public FavoriteController(UserRepository userRepository, GameRepository gameRepository, FavoriteService favoriteService, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @GetMapping("/ulubione")
    public String getUlubione(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        List<Game> favorite = user.getUlubione();
        model.addAttribute("favorite",favorite);
        return "favorite";
    }
    @GetMapping("/ulubione/{id}")
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
            return "favoriteDetails";
        }else return "redirect:/ulubione";
    }

    @GetMapping("/ulubione/dodaj")
    private String addToFavorite(@RequestParam Long gameId,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
         Optional<Game> game = gameRepository.findById(gameId);
         if(game.isPresent()){
             favoriteService.update(game.get(),user.getID());
             return "redirect:/game/"+gameId;
         }
         else {
             model.addAttribute("message","Podana gra nie istnieje");
             return "errorView";
         }
    }

    @GetMapping("/ulubione/delete/{id}")
    public String deleteFromFavorite(@PathVariable Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).get();
        Optional<Game> game =gameRepository.findById(id);
        if(game.isPresent() && user.getUlubione().contains(game.get())){
            favoriteService.delete(game.get(),user.getID());
            return "redirect:/ulubione";
        }
        return "redirect:/ulubione";
    }
}
