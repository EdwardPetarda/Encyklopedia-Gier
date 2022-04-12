package pl.projektBsk.games;


import org.springframework.format.annotation.DateTimeFormat;
import pl.projektBsk.comment.Comment;
import pl.projektBsk.rating.Rating;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Column(length = 512)
    private String shortDescription;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataWydania;
    @NotEmpty
    private String genre;
    private String platform;


    private String imgSrc;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_details")
    private GameDetails details;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "game")
    private List<Rating> rate;

    public Game(String name, String shortDescription, LocalDate dataWydania, String genre, String platform, String imgSrc, GameDetails details) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.dataWydania = dataWydania;
        this.genre = genre;
        this.platform = platform;
        this.imgSrc = imgSrc;
        this.details = details;
    }

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public LocalDate getDataWydania() {
        return dataWydania;
    }

    public void setDataWydania(LocalDate dataWydania) {
        this.dataWydania = dataWydania;
    }



    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platforma) {
        this.platform = platforma;
    }

    public GameDetails getDetails() {
        return details;
    }

    public void setDetails(GameDetails details) {
        this.details = details;
    }
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
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
