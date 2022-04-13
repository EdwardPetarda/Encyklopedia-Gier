package pl.projektBsk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;
import pl.projektBsk.user.UserRole;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Dodanie nowego użytkownika do bazy danych
    @Override
    public void saveUser(User user){
        userRepository.save(new User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail(),
                user.getRole()));
    }

    //Logowanie użytkownika i nadanie uprawnienień
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Invalid username");
        }
        User user1 = user.get();
        UserRole role = user1.getRole();
        return new org.springframework.security.core.userdetails.User(user1.getUsername(),user1.getPassword(), getGrantedAuthority(role));
    }
    
    //Nadanie roli użytkownikowi 
    private List<GrantedAuthority> getGrantedAuthority(UserRole role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }
    
    //Zmiana hasła użytkownika
    public boolean changePassword(String oldPassword, String password, String checkPassword){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByUsername(name).get();
        if(passwordEncoder.matches(oldPassword,user.getPassword())){
            if(password.equals(checkPassword)){
                userRepository.changePassword(passwordEncoder.encode(password),user.getID());
                return true;
            }else {
                return false;
            }

        }else {
            return false;
        }
    }

    public User getUser(String username){
        return userRepository.findByUsername(username).get();
    }
    }


