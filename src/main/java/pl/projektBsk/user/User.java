package pl.projektBsk.user;

import pl.projektBsk.comment.Comment;
import pl.projektBsk.games.Game;
import pl.projektBsk.rating.Rating;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @NotEmpty
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    @NotEmpty
    private String password;
    @Email
    @NotEmpty
    @Column(unique = true,nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private UserRole role;


    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Rating> rate;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name ="ulubione",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "game_id"))
    List<Game> ulubione= new ArrayList<>();

    public User() {
    }
    public User(String username, String password, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public List<Game> getUlubione() {
        return ulubione;
    }

    public void setUlubione(List<Game> ulubione) {
        this.ulubione = ulubione;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Rating> getRate() {
        return rate;
    }

    public void setRate(List<Rating> rate) {
        this.rate = rate;
    }
}
