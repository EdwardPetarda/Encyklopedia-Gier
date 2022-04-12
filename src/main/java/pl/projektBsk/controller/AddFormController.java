package pl.projektBsk.controller;


import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.service.AddGameService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AddFormController {

    private GameRepository gameRepository;
    private AddGameService addGameService;

    @Autowired
    public AddFormController(GameRepository gameRepository, AddGameService addGameService) {
        this.gameRepository = gameRepository;
        this.addGameService = addGameService;
    }

    @GetMapping("/addGame")
    private String getAddForm(Model model){
        Game game = new Game();
        GameDetails gameDetails = new GameDetails();
        model.addAttribute("game", game);
        model.addAttribute("gameDetails",gameDetails);
        model.addAttribute("emptyFields",false);
        model.addAttribute("wrongFormat", false);
        return "addForm";
    }


    @PostMapping("/addGame")
    private String addGame(@Valid @ModelAttribute Game game, BindingResult bindingResult, @Valid @ModelAttribute GameDetails gameDetails, BindingResult bindingResult2 ,
                           @RequestParam String[] platform, @RequestParam (name= "image",required = false) MultipartFile image,  Model model)  {

            if (bindingResult.hasErrors() || bindingResult2.hasErrors() || image.isEmpty()) {
                model.addAttribute("emptyFields", true);
                model.addAttribute("game", game);
                model.addAttribute("gameDetails",gameDetails);
                model.addAttribute("wrongFormat", false);
                return "addForm";
            }


            String igmSrc = "/img/" + image.getOriginalFilename();
            if (gameRepository.findAllByImgSrc(igmSrc).isEmpty()) {
                try {
                    addGameService.saveImg(image, game.getName());
                } catch (Exception e) {
                    model.addAttribute("emptyFields", false);
                    model.addAttribute("wrongFormat", true);
                    model.addAttribute("game", game);
                    model.addAttribute("gameDetails",gameDetails);
                    return "addForm";
                }
            }
            List<Game> gameToSave = new ArrayList<>();

            for (String s : platform) {
                if (gameRepository.findByNameAndPlatform(game.getName(), s).isEmpty()) {
                    game.setImgSrc(igmSrc);
                    game.setPlatform(s);
                    gameToSave.add(new Game(
                            game.getName(),
                            game.getShortDescription(),
                            game.getDataWydania(),
                            game.getGenre(),
                            s,
                            igmSrc,
                            gameDetails
                    ));
                }
            }

            gameRepository.saveAll(gameToSave).iterator().hasNext();
                return "succesAdd";


    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String handleMissingParams(Model model){
        Game game = new Game();
        GameDetails gameDetails = new GameDetails();
        model.addAttribute("game", game);
        model.addAttribute("gameDetails",gameDetails);
        model.addAttribute("emptyFields", true);
        model.addAttribute("wrongFormat", false);
        return "addForm";
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class,FileSizeLimitExceededException.class})
    public String wrongFormat( Model model){
        Game game = new Game();
        GameDetails gameDetails = new GameDetails();
        model.addAttribute("game", game);
        model.addAttribute("gameDetails",gameDetails);
        model.addAttribute("emptyFields", false);
        model.addAttribute("wrongFormat", true);
        return "addForm";
    }

}
