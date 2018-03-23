package co.edu.udea.compumovil.gr04_20171.project.models;

import java.util.List;

public class Menu {

    private String id;
    private List<Dish> dishes;
    private List<Drink> drinks;

    public Menu(String id) {
        this.id = id;
    }


    public Menu() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
