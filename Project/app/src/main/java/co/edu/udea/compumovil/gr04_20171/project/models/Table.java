package co.edu.udea.compumovil.gr04_20171.project.models;


public class Table {

    private String id;
    private String name;
    private Integer capacity;
    private Boolean free;
    private String currentOrder;
    private String service;

    public Table(String id, String name, Integer capacity, Boolean free) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.free = free;
    }

    public Table() {
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public Boolean getFree() {
        return this.free;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }
}
