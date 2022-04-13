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
import org.springframework.web.multipart.MultipartFile;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.security.WithCustomUser;
import pl.projektBsk.service.AddGameService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AddFormControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRepository gameRepository;
    @MockBean
    private AddGameService addGameService;


    //Sprawdzenie czy zwracany jest widok z formularzem na dodanie gry 
    @Test
    @WithCustomUser(authority = "ADMIN")
    public void should_get_add_game_view() throws Exception{

        mockMvc.perform(get("/addGame"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("game","gameDetails","emptyFields","wrongFormat"));
    }

//Test na dodanie gry do bazy danych
    @Test
    @WithCustomUser(authority = "ADMIN")
    public void should_add_game() throws Exception{

        Game game= getGame();

        List<Game> games = List.of(game);
        MockMultipartFile image
                = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(gameRepository.findAllByImgSrc("img")).thenReturn(new ArrayList<Game>());
        when(gameRepository.saveAll(games)).thenReturn(games);
        when(gameRepository.findByNameAndPlatform(game.getName(),game.getPlatform())).thenReturn(Optional.empty());

        mockMvc.perform(multipart("/addGame").file(image)
        .param("name",game.getName())
        .param("genre",game.getGenre())
        .param("dataWydania",game.getDataWydania().toString())
        .param("shortDescription",game.getShortDescription())
        .param("developer",game.getDetails().getDeveloper())
        .param("publisher",game.getDetails().getPublisher())
        .param("ogolnyOpis",game.getDetails().getOgolnyOpis())
        .param("fabula",game.getDetails().getFabula())
        .param("mechanika",game.getDetails().getMechanika())
        .param("inne",game.getDetails().getInne())
        .param("platform",game.getPlatform())
        .with(csrf()))
                .andExpect(view().name("succesAdd"));
    }

    //Test na dodanie gry do bazy danych, zwraca błąd 
    @Test
    @WithCustomUser(authority = "ADMIN")
    public void should_no_add_game() throws Exception{

        Game game= getGame();

        List<Game> games = List.of(game);
        MockMultipartFile image = new MockMultipartFile("image", (byte[]) null);

        when(gameRepository.findAllByImgSrc("img")).thenReturn(new ArrayList<Game>());
        when(gameRepository.saveAll(games)).thenReturn(games);
        when(gameRepository.findByNameAndPlatform(game.getName(),game.getPlatform())).thenReturn(Optional.empty());

        mockMvc.perform(multipart("/addGame").file(image)
                .param("name",game.getName())
                .param("genre",game.getGenre())
                .param("dataWydania",game.getDataWydania().toString())
                .param("shortDescription",game.getShortDescription())
                .param("developer",game.getDetails().getDeveloper())
                .param("publisher",game.getDetails().getPublisher())
                .param("ogolnyOpis",game.getDetails().getOgolnyOpis())
                .param("fabula",game.getDetails().getFabula())
                .param("mechanika",game.getDetails().getMechanika())
                .param("inne",game.getDetails().getInne())
                .param("platform",game.getPlatform())
                .with(csrf()))
                .andExpect(model().attributeExists("emptyFields","game","gameDetails","wrongFormat"))
                .andExpect(view().name("addForm"));
    }



    //Funkcja zwracająca nowy obiekt Game
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
