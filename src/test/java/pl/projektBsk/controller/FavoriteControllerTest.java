package pl.projektBsk.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.security.WithCustomUser;
import pl.projektBsk.service.FavoriteService;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;
import pl.projektBsk.user.UserRole;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private UserServiceImpl userService;


    @Test
    @WithCustomUser(authority = "USER")
    public void should_return_game_details_from_favorite() throws Exception{


        Game game = getGame();
        User user = getUser();


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userService.getUser(user.getUsername())).thenReturn(user);

        mockMvc.perform(get("/ulubione/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(model().attribute("game",game))
                .andExpect(model().attributeExists("rate"))
                .andExpect(view().name("favoriteDetails"));

    }


    @Test
    @WithCustomUser(authority = "USER")
    public void should_add_game_to_favorite() throws Exception{

        Game game = getGame();
        User user = getUser();


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/ulubione/dodaj")
        .param("gameId","1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/1"));

    }

    @Test
    @WithCustomUser(authority = "USER")
    public void should_no_add_game_to_favorite() throws Exception{

        User user = getUser();


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ulubione/dodaj")
                .param("gameId","1"))
                .andExpect(status().isOk())
                .andExpect(view().name("errorView"))
                .andExpect(model().attribute("message","Podana gra nie istnieje"));

    }


    @Test
    @WithCustomUser(authority = "USER")
    public void should_delete_game_from_favorite() throws Exception{
        Game game = getGame();
        User user = getUser();


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        mockMvc.perform(get("/ulubione/delete/{id}",1L))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/ulubione"));
    }


    @Test
    @WithCustomUser(authority = "USER")
    public void should_no_delete_game_from_favorite() throws Exception{
        User user = getUser();


        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ulubione/delete/{id}",1L))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/ulubione"));
    }



    private Game getGame(){
        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);
        List<Rating> rate = new ArrayList<>();
        game.setRate(rate);

        return game;
    }

    private User getUser(){
        User user = new User("username","old","email", UserRole.USER);
        user.setID(1L);
        List<Rating> rate = new ArrayList<>();
        user.setRate(rate);

        return user;
    }

}
