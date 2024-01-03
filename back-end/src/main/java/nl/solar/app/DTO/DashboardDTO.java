package nl.solar.app.DTO;

import java.time.LocalDate;
import java.util.Objects;

public class DashboardDTO {

    private Long warehouseId;
    private String warehouseName;
    private String productName;
    private int quantity;
    private int inventoryQuantity;
//    private Long orderId;
//    private Long orderWarehouseId;
    private LocalDate deliverDate;

    public DashboardDTO(Long warehouseId, String warehouseName, String productName, int quantity, int inventoryQuantity, LocalDate deliverDate) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.quantity = quantity;
        this.inventoryQuantity = inventoryQuantity;
//        this.orderId = orderId;
//        this.orderWarehouseId = orderWarehouseId;
        this.deliverDate = deliverDate;
    }

    public DashboardDTO(DashboardDTO other) {
        this.warehouseId = other.warehouseId;
        this.warehouseName = other.warehouseName;
        this.productName = other.productName;
        this.quantity = other.quantity;
        this.inventoryQuantity = other.inventoryQuantity;
        this.deliverDate = other.deliverDate;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }
    //    public Long getOrderId() {
//        return orderId;
//    }
//
//    public Long getOrderWarehouseId() {
//        return orderWarehouseId;
//    }

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    @Override
    public String toString() {
        return "DashboardDTO{" +
                "warehouseId=" + warehouseId +
                ", warehouseName='" + warehouseName + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", inventoryQuantity=" + inventoryQuantity +
                ", deliverDate=" + deliverDate +
                "}\n";
    }
}
