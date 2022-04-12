package pl.projektBsk.games;

import java.time.LocalDate;

public class GameDto {

    private Long id;
    private String name;
    private String shortDescription;
    private LocalDate dataWydania;
    private String genre;
    private String platform;
    private String imgSrc;

    public GameDto(Long id, String name, String shortDescription, LocalDate dataWydania, String genre, String platform, String imgSrc) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.dataWydania = dataWydania;
        this.genre = genre;
        this.platform = platform;
        this.imgSrc = imgSrc;
    }

    public GameDto() {
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
}
