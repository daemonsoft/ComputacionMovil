package co.edu.udea.compumovil.gr04_20171.project.models;

import java.util.List;

/**
 * Created by daemonsoft on 8/06/17.
 */

public class OrderList {

    String id;
    List<Order> products;
    String status;
    String table;
    Long totalOrder;

    public OrderList() {
        totalOrder = 0L;
    }

    public Long getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(Long totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
