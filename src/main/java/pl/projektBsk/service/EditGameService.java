package pl.projektBsk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class EditGameService {

    private AddGameService addGameService;
    private GameRepository gameRepository;

    @Autowired
    public EditGameService(AddGameService addGameService, GameRepository gameRepository) {
        this.addGameService = addGameService;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public boolean editGame(Game game, String name, String genre, LocalDate date, String shortDescription, String developer,
                            String publisher, String ogolnyOpis, String fabula, String mechanika, String inne,
                            MultipartFile image){
        game.setName(name);
        game.setGenre(genre);
        game.setDataWydania(date);
        game.setShortDescription(shortDescription);
        GameDetails gameDetails = game.getDetails();
        gameDetails.setDeveloper(developer);
        gameDetails.setPublisher(publisher);
        gameDetails.setOgolnyOpis(ogolnyOpis);
        gameDetails.setFabula(fabula);
        gameDetails.setMechanika(mechanika);
        gameDetails.setInne(inne);
        game.setDetails(gameDetails);
        String igmSrc = "/img/"+image.getOriginalFilename();
        if(gameRepository.findAllByImgSrc(igmSrc).isEmpty()) {
            if(addGameService.saveImg(image, game.getName())){
                return true;
            }
            else {
                return false;
            }
        }
        game.setImgSrc(igmSrc);
        return true;
    }
}
