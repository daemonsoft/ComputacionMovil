package co.edu.udea.compumovil.gr04_20171.project.models;

public class Drink {

    private String id;
    private String name;
    private Integer price;
    private String description;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Drink(String name, Integer price,
                 String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Drink() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return this.price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
