package nl.solar.app.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class ForecastItem {
    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    private int inventoryChange;
    private String warehouseName;
    private String productName;

    public ForecastItem(){}

    public ForecastItem(Date date, int inventoryChange, String warehouseName, String productName) {
        this.date = date;
        this.inventoryChange = inventoryChange;
        this.warehouseName = warehouseName;
        this.productName = productName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
