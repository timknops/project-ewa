package nl.solar.app.models;


import com.fasterxml.jackson.annotation.JsonView;
import nl.solar.app.Views.ResourceView;

import java.util.Objects;

public class Warehouse {
    @JsonView(ResourceView.Complete.class)
    private long id;

    @JsonView(ResourceView.Complete.class)
    private String warehouseName;

    private String location;

    public Warehouse(long id, String name, String location){
        this.id = id;
        this.warehouseName = name;
        this.location = location;
    }

    public static Warehouse createDummyWarehouses(long id, String name, String location){
        return new Warehouse(
                id,
                name,
                location
        );
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj instanceof Warehouse warehouse){
            return this.getId() == warehouse.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
