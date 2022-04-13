package pl.projektBsk.service;


import org.springframework.stereotype.Service;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameDetailsRepository;
import pl.projektBsk.games.GameRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GameService {

    private GameRepository gameRepository;
    private GameDetailsRepository gameDetailsRepository;

    private List<String> genres = List.of("RPG", "Akcji", "Strategiczne", "Przygodowe", "MMO", "Sportowe", "Bijatyki", "Symulacyjne");

    public GameService(GameRepository gameRepository, GameDetailsRepository gameDetailsRepository) {
        this.gameRepository = gameRepository;
        this.gameDetailsRepository = gameDetailsRepository;
    }

    public void deleteGame(Game game){

        gameRepository.deleteFromFavorite(game.getId());
        GameDetails gameDetails = game.getDetails();
        gameRepository.delete(game);
        if(!gameRepository.existsByDetails(gameDetails)){
            gameDetailsRepository.deleteById(gameDetails.getId());
        }

    }

    //Podliczenie gier z każdego gatunku 
    public Map<String,Integer> countGamesByGenreAndPlatform(String platform){
        Map<String,Integer> gamesByGenre = new HashMap<>();
        gamesByGenre.put("RPG",gameRepository.countByGenreAndPlatform("RPG",platform));
        gamesByGenre.put("Akcji",gameRepository.countByGenreAndPlatform("Akcji",platform));
        gamesByGenre.put("Strategiczne",gameRepository.countByGenreAndPlatform("Strategiczne",platform));
        gamesByGenre.put("Przygodowe",gameRepository.countByGenreAndPlatform("Przygodowe",platform));
        gamesByGenre.put("MMO",gameRepository.countByGenreAndPlatform("MMO",platform));
        gamesByGenre.put("Sportowe",gameRepository.countByGenreAndPlatform("Sportowe",platform));
        gamesByGenre.put("Bijatyki",gameRepository.countByGenreAndPlatform("Bijatyki",platform));
        gamesByGenre.put("Symulacyjne",gameRepository.countByGenreAndPlatform("Symulacyjne",platform));
        return gamesByGenre;

    }

    //Sprawdzenie czy dana gra instenieje w bazie danych 
    public boolean exist(Long gameId){
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent())
            return true;
        else
            return false;
    }


    //Sprawdzenie czy dany gatunek istnieje w aplikacji 
    public boolean checkGenre(String genre){

        if (genres.contains(genre))
            return true;
        else
            return false;
    }
}
