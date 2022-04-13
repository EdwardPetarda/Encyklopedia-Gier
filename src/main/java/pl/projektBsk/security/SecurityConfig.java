package pl.projektBsk.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.projektBsk.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Autowired
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }




    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        
        //Zezwolenie wszystkim użytkownikom do poniższych zapytań
        http
                .authorizeRequests().antMatchers(
                        "/register**",
                "/js/**",
                "/style/**",
                "/img/**",
                "/",
                "images/**",
                "/gamePC/**",
                "/gameXbox/**",
                "/gamePS/**",
                "/gameSwitch/**",
                "/game/**",
                "/search"
        ).permitAll()
            //Zezwolnie tylko zalogowanym użytkownikom do poniższych zapytań
                .antMatchers("/myAccount/**",
                        "/ratings/**",
                        "/comment/**").hasAuthority("USER")
            
            //Zezwolnie tylko dla administratora do poniższych zapytań
                .antMatchers("/administration/**",
                        "/addGame/**",
                        "/game/delete/**",
                       "/edit/**" ).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").permitAll();
    }
}
