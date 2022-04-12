package pl.projektBsk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameMapper;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.service.GameService;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;

import java.util.Map;
import java.util.Optional;

@Controller
public class GameController {

    private GameRepository gameRepository;
    private GameMapper gameMapper;
    private GameService gameService;
    private UserServiceImpl userService;


    @Autowired
    public GameController(GameRepository gameRepository, GameMapper gameMapper, GameService gameService, UserServiceImpl userService) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;

        this.gameService = gameService;
        this.userService = userService;
    }
    
    //Wyświetlenie gier na PC
    @GetMapping("/game/PC")
    public String getGamePc(Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = getPageable(sort,pageNr);

        return getByPlatform
                (model,page,"PC");
    }

    //Wyświetlenie gier na PC odpowiedniego gatunku 
    @GetMapping("/game/PC/genre/{genre}")
    public String getGamePc(@PathVariable String genre, Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        if(gameService.checkGenre(genre)) {
            Pageable page = getPageable(sort, pageNr);

            return getByGenreAndPlatform(model, page, "PC", genre);
        }else {
            model.addAttribute("message","Bledny gatunek gry");
            return "errorView";
        }
    }

//Wyświetlenie gier na PS
    @GetMapping("/game/PS")
    public String getGamePs(Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = getPageable(sort, pageNr);

        return getByPlatform
                (model, page,"PS");
    }
    @GetMapping("/game/PS/genre/{genre}")
    public String getGamePs(@PathVariable String genre, Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        if(gameService.checkGenre(genre)) {
            Pageable page = getPageable(sort, pageNr);

            return getByGenreAndPlatform(model, page, "PS", genre);
        }else {
            model.addAttribute("message","Bledny gatunek gry");
            return "errorView";
        }
    }

//Wyświetlenie gier na XBOX
    @GetMapping("/game/Xbox")
    public String getGameXbox(Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = getPageable(sort, pageNr);

        return getByPlatform
                (model, page,"XBOX");
    }

    @GetMapping("/game/Xbox/genre/{genre}")
    public String getGameXbox(@PathVariable String genre, Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        if(gameService.checkGenre(genre)) {
            Pageable page = getPageable(sort, pageNr);

            return getByGenreAndPlatform(model, page, "XBOX", genre);
        }else {
            model.addAttribute("message","Bledny gatunek gry");
            return "errorView";
        }
    }
//Wyświetlenie gier na SWITCH
    @GetMapping("/game/Switch")
    public String getGameSwitch(Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = getPageable(sort, pageNr);

        return getByPlatform
                (model, page,"SWITCH");
    }


    @GetMapping("/game/Switch/genre/{genre}")
    public String getGameSwitch(@PathVariable String genre, Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = null;
        if(gameService.checkGenre(genre)) {
            page = getPageable(sort, pageNr);

            return getByGenreAndPlatform(model, page, "SWITCH", genre);
        }else {
            model.addAttribute("message","Bledny gatunek gry");
            return "errorView";
        }
    }


    //Wyszukanie gier po podanej frazie
    @GetMapping("/search")
    public String getSearch(@RequestParam String name, Model model, @RequestParam(required = false) String sort,@RequestParam(required = false,defaultValue = "0") int pageNr){
        Pageable page = getPageable(sort, pageNr);

        return getByName(model,page,name);

    }
    
    //Wyświetlenie strony szczegółowej gry, zapytanie o wszystkie dane z bazy danych o grze z danym id
    @GetMapping("/game/{id}")
    public String getDetailsGame(@PathVariable long id,Model model){
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
            return "gameDetails";
        }else {
            model.addAttribute("message","Gra, ktora chciales wyswietlic nie istnieje");
            return "errorView";
        }
    }

    //Usunięcie gry z bazy danych 
    @GetMapping("/game/delete/{id}")
    public String getDeleteGame(@PathVariable long id,Model model){
        Optional<Game> game = gameRepository.findById(id);
        if(game.isPresent()){
            gameService.deleteGame(game.get() );
            return "redirect:/";
        }else {
            model.addAttribute("message","Gra, ktora chciales usunac nie istnieje");
            return "errorView";
        }
    }

    
    //Pobranie wszystkich gier z bazy danych na daną platrofme.
    private String getByPlatform
            (Model model, Pageable page,String platform) {

        Page<Game> games = gameRepository.findAllByPlatform(platform,page);
        Map<String,Integer> gamesByGenre = gameService.countGamesByGenreAndPlatform(platform);
        boolean showGenreFilter = true;
        if(page.getPageNumber()!=0){
            showGenreFilter=false;
        }

        if(!games.isEmpty()) {
            games.get().map(gameMapper::toDto);
            long ileStron = 0;
            long iloscRekordow = gameRepository.countByPlatform(platform);
            if (iloscRekordow != 0) {
                if (iloscRekordow % 20 == 0) {
                    ileStron = iloscRekordow / 20;
                } else {
                    ileStron = iloscRekordow / 20 + 1;
                }
            }
            model.addAttribute("showGenreFilter",showGenreFilter);
            model.addAttribute("pages",ileStron);
            model.addAttribute("games",games);
            model.addAttribute("gamesByGenre",gamesByGenre);
        }
        else {
            model.addAttribute("showGenreFilter",showGenreFilter);
            model.addAttribute("pages",0);
            model.addAttribute("games",null);
            model.addAttribute("gamesByGenre",null);
        }
        return "games";
    }

    
    //Pobranie wszystkich gier z bazy danych na daną platformę oraz o podanym gatunku.
    private String getByGenreAndPlatform
            (Model model, Pageable page,String platform, String genre) {

        Page<Game> games = gameRepository.findAllByGenreAndPlatform(genre,platform,page);
        boolean showGenreFilter = true;
        Map<String,Integer> gamesByGenre = gameService.countGamesByGenreAndPlatform(platform);
        if(page.getPageNumber()!=0){
            showGenreFilter=false;
        }

        if(!games.isEmpty()) {
            games.get().map(gameMapper::toDto);
            long ileStron = 0;
            long iloscRekordow = gameRepository.countByGenreAndPlatform(genre,platform);
            if (iloscRekordow != 0) {
                if (iloscRekordow % 20 == 0) {
                    ileStron = iloscRekordow / 20;
                } else {
                    ileStron = iloscRekordow / 20 + 1;
                }
            }
            model.addAttribute("showGenreFilter",showGenreFilter);
            model.addAttribute("pages",ileStron);
            model.addAttribute("games",games);
            model.addAttribute("gamesByGenre",gamesByGenre);

        }
        else {
            model.addAttribute("showGenreFilter",showGenreFilter);
            model.addAttribute("pages",0);
            model.addAttribute("games",null);
            model.addAttribute("gamesByGenre",null);
        }
        return "games";
    }


    //Pobranie gier z bazy danych, których nazwa zaczyna się od podanej frazy 
    private String getByName
            (Model model, Pageable page,String name) {

        Page<Game> games = gameRepository.findByNameContains(name,page);
        if(!games.isEmpty()) {
            games.get().map(gameMapper::toDto);
            long ileStron = 0;
            long iloscRekordow = gameRepository.countByNameContaining(name);
            if (iloscRekordow != 0) {
                if (iloscRekordow % 20 == 0) {
                    ileStron = iloscRekordow / 20;
                } else {
                    ileStron = iloscRekordow / 20 + 1;
                }
            }
            model.addAttribute("showGenreFilter",false);
            model.addAttribute("pages",ileStron);
            model.addAttribute("games",games);
            model.addAttribute("gamesByGenre",null);
        }
        else {
            model.addAttribute("showGenreFilter",false);
            model.addAttribute("pages",0);
            model.addAttribute("games",null);
            model.addAttribute("gamesByGenre",null);
        }
        return "search";
    }

    //Funkcja służąca do tworzenia stron potrzebnych do pobierania gier, można przekazać także argument do sortowania
    private Pageable getPageable( String sort,  int pageNr) {
        Pageable page;
        pageNr--;
        if(sort==null && pageNr<=0){
            page = PageRequest.of(0,20);}
        else if(sort==null) {
            page = PageRequest.of(pageNr,20);
        }
        else if(pageNr<=0){
            page=PageRequest.of(0,20,Sort.by(sort));
        }
        else {
            page = PageRequest.of(pageNr,20, Sort.by(sort));
        }
        return page;
    }


}
