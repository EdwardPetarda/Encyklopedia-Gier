package pl.projektBsk.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.security.WithCustomUser;
import pl.projektBsk.service.EditGameService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EditGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private EditGameService editGameService;


    @Test
    @WithCustomUser(authority = "ADMIN")
    public void should_edit_game() throws Exception{

        Game game= getGame();

        List<Game> games = List.of(game);
        MockMultipartFile image
                = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));


        mockMvc.perform(multipart("/edit/{id}",1).file(image)
                .param("name",game.getName())
                .param("genre",game.getGenre())
                .param("date",game.getDataWydania().toString())
                .param("shortDescription",game.getShortDescription())
                .param("developer",game.getDetails().getDeveloper())
                .param("publisher",game.getDetails().getPublisher())
                .param("ogolnyOpis",game.getDetails().getOgolnyOpis())
                .param("fabula",game.getDetails().getFabula())
                .param("mechanika",game.getDetails().getMechanika())
                .param("inne",game.getDetails().getInne())
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/1"));
    }
    @Test
    @WithCustomUser(authority = "ADMIN")
    public void should_no_edit_game() throws Exception{

        Game game= getGame();

        List<Game> games = List.of(game);
        MockMultipartFile image
                = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));


        mockMvc.perform(multipart("/edit/{id}",2).file(image)
                .param("name",game.getName())
                .param("genre",game.getGenre())
                .param("date",game.getDataWydania().toString())
                .param("shortDescription",game.getShortDescription())
                .param("developer",game.getDetails().getDeveloper())
                .param("publisher",game.getDetails().getPublisher())
                .param("ogolnyOpis",game.getDetails().getOgolnyOpis())
                .param("fabula",game.getDetails().getFabula())
                .param("mechanika",game.getDetails().getMechanika())
                .param("inne",game.getDetails().getInne())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("errorView"));
    }


    private Game getGame(){
        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);
        List<Rating> rate = new ArrayList<>();
        game.setRate(rate);
        game.setComments(new ArrayList<>());

        return game;
    }
}
