package pl.projektBsk.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    private GameRepository gameRepository;

    @Autowired
    public GameMapper(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameDto toDto(Game game){
        return new GameDto(game.getId(),
                game.getName(),
                game.getShortDescription(),
                game.getDataWydania(),
                game.getGenre(),
                game.getPlatform(),
                game.getImgSrc());
    }

    public  Game toEntity(GameDto gameDto){
        Game game = new Game();
        game.setId(gameDto.getId());
        game.setPlatform(gameDto.getPlatform());
        game.setImgSrc(gameDto.getImgSrc());
        game.setGenre(gameDto.getGenre());
        game.setDetails(gameRepository.findById(game.getId()).get().getDetails());
        game.setDataWydania(gameDto.getDataWydania());
        game.setShortDescription(gameDto.getShortDescription());
        game.setName(gameDto.getName());
        return game;
    }
}
