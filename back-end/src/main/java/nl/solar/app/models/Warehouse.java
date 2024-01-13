package nl.solar.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import nl.solar.app.models.views.ResourceView;
import nl.solar.app.models.views.UserView;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @JsonView({ResourceView.Complete.class, UserView.userFull.class})
    @SequenceGenerator(name = "warehouse_id_generator", initialValue = 1000, allocationSize = 3)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_generator")
    private long id;
    @JsonView({ResourceView.Complete.class, UserView.userFull.class})
    private String name;

    private String location;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Inventory> inventory = new HashSet<>();

    @OneToMany(mappedBy = "warehouse")
    @JsonIgnore
    Set<Order> orders = new HashSet<>();


    @OneToMany(mappedBy = "warehouse")
    @JsonIgnore
    private Set<Team> teams;

    public Warehouse(long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Warehouse() {
    }

    public static Warehouse createDummyWarehouse(String name, String location) {
        Warehouse warehouse = new Warehouse();

        warehouse.setName(name);
        warehouse.setLocation(location);

        return warehouse;
    }

    public Set<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(Set<Inventory> inventory) {
        this.inventory = inventory;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> team) {
        this.teams = team;
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
