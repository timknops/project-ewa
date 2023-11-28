package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import nl.solar.app.models.views.ResourceView;

import java.util.Objects;

@Entity
public class Warehouse {

    @Id
    @JsonView(ResourceView.Complete.class)
    @SequenceGenerator(name = "warehouse_id_generator", initialValue = 1000, allocationSize = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_generator")
    private long id;

    @JsonView(ResourceView.Complete.class)
    private String name;

    private String location;

    public Warehouse(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Warehouse(){}

    public static Warehouse createDummyWarehouses(long id, String name, String location) {
        return new Warehouse(
                id,
                name,
                location);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Warehouse warehouse) {
            return this.getId() == warehouse.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
