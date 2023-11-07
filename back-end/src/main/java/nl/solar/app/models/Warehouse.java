package nl.solar.app.models;

import java.util.Objects;

/**
 * temporary
 */
public class Warehouse {
    private long id;
    private String WarehouseName;

    public Warehouse(long id, String warehouseName) {
        this.id = id;
        WarehouseName = warehouseName;
    }

    public Warehouse() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Warehouse warehouse) {
            return warehouse.id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
