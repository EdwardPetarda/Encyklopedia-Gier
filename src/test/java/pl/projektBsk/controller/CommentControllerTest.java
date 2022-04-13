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
import pl.projektBsk.service.CommentService;
import pl.projektBsk.service.GameService;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CommentControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GameService gameService;


    //Test na dodanie komenatrza
    @Test
    @WithCustomUser(authority = "USER")
    public void should_add_comment() throws Exception{
        when(commentService.addComment("content",1L)).thenReturn(true);

        mockMvc.perform(post("/comment/add/1")
        .with(csrf())
        .param("content","content"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/{id}"));
    }


    //Test na dodanie komentarza, zwraca błąd
    @Test
    @WithCustomUser(authority = "USER")
    public void should_no_add_comment() throws Exception{
        when(commentService.addComment("content",1L)).thenReturn(false);

        mockMvc.perform(post("/comment/add/1")
                .with(csrf())
                .param("content","content"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message","Probowales dodac komentarz do gry, ktora nie istnieje"))
                .andExpect(view().name("errorView"));
    }

    //Test na usunięcie komentarza
    @Test
    @WithCustomUser(authority = "USER")
    public void should_delete_comment() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(commentService.deleteComment(1L,1L)).thenReturn(true);

        mockMvc.perform(get("/comment/delete")
        .param("gameId","1")
        .param("commentId","1"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/1"));
    }


    //Test na usunięcie komentarza, zwraca błąd
    @Test
    @WithCustomUser(authority = "USER")
    public void should_no_delete_comment() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(commentService.deleteComment(1L,1L)).thenReturn(false);

        mockMvc.perform(get("/comment/delete")
                .param("gameId","1")
                .param("commentId","1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message","Probowales usunac komentarz, ktory nie istnieje"))
                .andExpect(view().name("errorView"));
    }

    //Test na edycję komentarza
    @Test
    @WithCustomUser(authority = "USER")
    public void should_edit_comment() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(commentService.editComment(1L,1L,"content")).thenReturn(true);

        mockMvc.perform(post("/comment/edit/1/1")
        .param("content","content")
        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/game/1"));

    }

    //Test na edycję komentarza, zwraca błąd 
    @Test
    @WithCustomUser(authority = "USER")
    public void should__no_edit_comment() throws Exception{
        when(gameService.exist(1L)).thenReturn(true);
        when(commentService.editComment(1L,1L,"content")).thenReturn(false);

        mockMvc.perform(post("/comment/edit/1/1")
                .param("content","content")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Komentarz, ktory chciales edytowac nie istnieje"))
                .andExpect(view().name("errorView"));

    }







}
