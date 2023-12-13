package nl.solar.app.DTO;

import java.time.LocalDate;

public class DashboardDTO {

    private Long warehouseId;
    private String warehouseName;
    private String itemName;
    private int quantity;
//    private Long orderId;
//    private Long orderWarehouseId;
    private LocalDate deliverDate;

    public DashboardDTO(Long warehouseId, String warehouseName, String itemName, int quantity, LocalDate deliverDate) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.itemName = itemName;
        this.quantity = quantity;
//        this.orderId = orderId;
//        this.orderWarehouseId = orderWarehouseId;
        this.deliverDate = deliverDate;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

}
