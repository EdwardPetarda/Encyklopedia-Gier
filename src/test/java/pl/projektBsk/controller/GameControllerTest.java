package pl.projektBsk.controller;


import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameDetails;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.service.GameService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameRepository gameRepository;


    //Test na zwrócenie widoku z grami 
    @Test
    public void should_return_view_with_data() throws Exception {
        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);

        List<Game> games = new ArrayList<>();
        games.add(game);
        Page<Game> page =  new PageImpl<>(games);


      when(gameRepository.findAllByPlatform("PC",PageRequest.of(0,20))).thenReturn(page);

        this.mockMvc
                .perform(get("/game/PC"))
                .andExpect(status().isOk())
                .andExpect(view().name("games"))
                .andExpect(model().attribute("games",page))
                .andExpect(model().attributeExists("pages","showGenreFilter","gamesByGenre"));
    }


    //Test na zwrócenie widoku ze szczegółami gry 
    @Test
    public void should_return_view_with_game_details() throws Exception{
        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);
        game.setRate(new ArrayList<>());

        Optional<Game> g = Optional.of(game);

        when(gameRepository.findById(1L)).thenReturn(g);

        mockMvc.perform(get("/game/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(view().name("gameDetails"))
                .andExpect(model().attribute("game",g.get()))
                .andExpect(model().attributeExists("rate"));

    }




    //Test na zwrócenie widoku z grami wyszykanymi po danej frazie 
    @Test
    public void should_return_view_with_game_found_by_name() throws Exception{
        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);

        List<Game> games = new ArrayList<>();
        games.add(game);
        Page<Game> page =  new PageImpl<>(games);

        when(gameRepository.findByNameContains("name",PageRequest.of(0,20))).thenReturn(page);

        this.mockMvc
                .perform(get("/search")
                .param("name","name"))
                .andExpect(status().isOk())
                .andExpect(view().name("games"))
                .andExpect(model().attribute("games",page))
                .andExpect(model().attribute("gamesByGenre",Matchers.nullValue()))
                .andExpect(model().attributeExists("pages","showGenreFilter"));

    }

    //Test na usunięcie gry
    @Test
    public void should_delete_game_and_redirect_to_home_page() throws Exception{

        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);
        game.setRate(new ArrayList<>());

        Optional<Game> g = Optional.of(game);

        when(gameRepository.findById(1L)).thenReturn(g);

        mockMvc.perform(get("/game/delete/{id}",1L))
                .andExpect(view().name("redirect:/"));

    }




    //Test na usunięcie gry z bazdy danych, zwraca błąd
    @Test
    public void should_return_error_game_delete() throws Exception {

        GameDetails gameDetails = new GameDetails("developer","publisher","ogolnyOpis","fabula","mechanika","inne");
        gameDetails.setId(1L);
        LocalDate localDate = LocalDate.of(2021,12,30);
        Game game = new Game("name","shortDescription", localDate,"RPG","PC","imgSrc",gameDetails);
        game.setId(1L);
        game.setRate(new ArrayList<>());

        Optional<Game> g = Optional.of(game);

        when(gameRepository.findById(1L)).thenReturn(g);

        mockMvc.perform(get("/game/delete/{id}",2L))
                .andExpect(model().attribute("message","Gra, ktora chciales usunac nie istnieje"))
                .andExpect(view().name("errorView"));

    }

    //Test na zwrócenie błędu przy błędnym zapytaniu
    @Test
    public void should_return_error() throws Exception {


        this.mockMvc
                .perform(get("/game/PR"))
                .andExpect(status().isBadRequest());
    }


}
