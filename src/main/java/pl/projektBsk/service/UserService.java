package pl.projektBsk.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.projektBsk.user.User;

public interface UserService extends UserDetailsService {

    public void saveUser(User user);
}
