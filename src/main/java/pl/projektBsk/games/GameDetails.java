package pl.projektBsk.games;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
public class GameDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String developer;
    @NotEmpty
    private String publisher;
    @Column(length = 2048)
    @NotEmpty
    private String ogolnyOpis;
    @Column(length = 2048)
    @NotEmpty
    private String fabula;
    @Column(length = 2048)
    @NotEmpty
    private String mechanika;
    @Column(length = 2048)
    @NotEmpty
    private String inne;

    public GameDetails(String developer, String publisher, String ogolnyOpis, String fabula, String mechanika, String inne) {
        this.developer = developer;
        this.publisher = publisher;
        this.ogolnyOpis = ogolnyOpis;
        this.fabula = fabula;
        this.mechanika = mechanika;
        this.inne = inne;
    }

    public GameDetails() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getOgolnyOpis() {
        return ogolnyOpis;
    }

    public void setOgolnyOpis(String ogolnyOpis) {
        this.ogolnyOpis = ogolnyOpis;
    }

    public String getFabula() {
        return fabula;
    }

    public void setFabula(String fabula) {
        this.fabula = fabula;
    }

    public String getMechanika() {
        return mechanika;
    }

    public void setMechanika(String mechanika) {
        this.mechanika = mechanika;
    }

    public String getInne() {
        return inne;
    }

    public void setInne(String inne) {
        this.inne = inne;
    }
}
