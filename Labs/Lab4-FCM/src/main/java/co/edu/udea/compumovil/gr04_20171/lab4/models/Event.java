package co.edu.udea.compumovil.gr04_20171.lab4.models;

/**
 * Created by daemonsoft on 10/03/17.
 */
public class Event {

    private String id;
    private String name;
    private String owner;
    private String description;
    private String picture;
    private String date;
    private Double latitude;
    private Double longitude;
    private String location;
    private Integer score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Event() {
    }

    public Event(String name, String owner, String description, String picture, String date, Double latitude, Double longitude, String location, Integer score) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.picture = picture;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.score = score;
    }
}