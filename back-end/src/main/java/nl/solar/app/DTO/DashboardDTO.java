package nl.solar.app.DTO;

import java.time.LocalDate;

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

    public static DashboardDTO merge(DashboardDTO dto1, DashboardDTO dto2) {
        if (dto1 == null && dto2 == null) {
            return null;
        } else if (dto1 == null) {
            return dto2;
        } else if (dto2 == null) {
            return dto1;
        }

        if (dto1.getWarehouseId() == dto2.getWarehouseId()
                && dto1.getProductName() == dto2.getProductName()){

            // Combine quantities, keep other fields as is
            dto1.setQuantity(dto1.getQuantity() + dto2.getQuantity());
            return dto1;
        }

        return null;
    }
}
