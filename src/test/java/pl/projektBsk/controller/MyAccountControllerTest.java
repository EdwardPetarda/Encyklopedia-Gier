package pl.projektBsk.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.projektBsk.security.WithCustomUser;
import pl.projektBsk.service.UserServiceImpl;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;
import pl.projektBsk.user.UserRole;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MyAccountControllerTest {


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;


    //Test na usunięcie użytkownika
    @Test
    @WithCustomUser(authority = "USER")
    public void should_delete_user() throws Exception{
        User user = new User("username","old","email", UserRole.USER);
        user.setID(1L);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
        userRepository.deleteById(user.getID());

        mockMvc.perform(get("/myAccount/deleteAccount"))
                .andExpect(model().attribute("message","Udalo sie usunac konto"))
                .andExpect(view().name("index"));
    }




    //Test na zmiane hasła użytkownika
    @Test
    @WithCustomUser(authority = "USER")
    public void should_return_result_true_change_password() throws Exception{

        when(userService.changePassword("old","new","new")).thenReturn(true);

        mockMvc.perform(post("/myAccount/zmienHaslo")
                .param("oldPassword","old")
                .param("password","new")
                .param("checkPassword","new")
                .with(csrf()))
                .andExpect(model().attribute("result","Udalo sie zmienic haslo"))
                .andExpect(view().name("changePasswordForm"));
    }

    //Test na zmiane hasła użytkownika, zwraca błąd
    @Test
    @WithCustomUser(authority = "USER")
    public void should_return_invalid_credentials_true_change_password() throws Exception{

        when(userService.changePassword("old","new","new1")).thenReturn(false);

        mockMvc.perform(post("/myAccount/zmienHaslo")
        .param("oldPassword","old")
        .param("password","new")
        .param("checkPassword","new1")
        .with(csrf()))
                .andExpect(model().attribute("invalidCredentials",true))
                .andExpect(view().name("changePasswordForm"));
    }



    //Test na zwrócenie widoku panelu użytkownika
    @Test
    @WithCustomUser(authority = "USER")
    public void should_return_my_account_view() throws Exception{

        User user = new User("username","old","email", UserRole.USER);
        user.setID(1L);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/myAccount"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user",user))
                .andExpect(view().name("myAccount"));
    }
}
