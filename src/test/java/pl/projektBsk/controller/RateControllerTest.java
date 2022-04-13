package pl.projektBsk.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.projektBsk.security.WithCustomUser;
import pl.projektBsk.service.GameService;
import pl.projektBsk.service.RateService;


import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateService rateService;
    @MockBean
    private GameService gameService;




    //Test na dodanie oceny do gry 
    @Test
    @WithCustomUser(authority = "USER")
    public void should_add_rate() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(rateService.rateIsExist(1L)).thenReturn(false);


        mockMvc.perform(post("/rate/{id}",1L)
                .param("userRate","2.5")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/{id}"));
    }

    //Test na edycje oceny gry
    @Test
    @WithCustomUser(authority = "USER")
    public void should_edit_rate() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(rateService.rateIsExist(1L)).thenReturn(true);


        mockMvc.perform(post("/rate/{id}",1L)
                .param("userRate","2.5")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/{id}"));
    }

    //Test na ocenę gry, zwraca błąd 
    @Test
    @WithCustomUser(authority = "USER")
    public void should_return_view_with_error() throws Exception{
        when(gameService.exist(1L)).thenReturn(false);


        mockMvc.perform(post("/rate/{id}",1L)
        .param("userRate","2.5")
                .with(csrf()))
                .andExpect(model().attribute("message","Gra, ktora chciales ocenic nie istnieje"))
                .andExpect(view().name("errorView"));
    }

}
