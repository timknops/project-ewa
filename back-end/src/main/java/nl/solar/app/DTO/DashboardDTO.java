package nl.solar.app.DTO;

import java.time.LocalDate;

public class DashboardDTO {

    private Long warehouseId;
    private String warehouseName;
    private String productName;
    private int quantity;
    private int inventoryQuantity;

    private LocalDate deliverDate;
    private LocalDate dueDate;
    private Long projectId;
    private String projectName;
    private Long productId;
    private int amountOfProduct;


    public DashboardDTO(Long warehouseId, String warehouseName, String productName, Integer quantity, Integer inventoryQuantity, LocalDate deliverDate) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.quantity = quantity;
        this.inventoryQuantity = inventoryQuantity;
//        this.orderId = orderId;
//        this.orderWarehouseId = orderWarehouseId;
        this.deliverDate = deliverDate;
//        this.productId = productId;
    }

    public DashboardDTO(Long warehouseId, String warehouseName, String productName, int inventoryQuantity, Long productId) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.productName = productName;
        this.inventoryQuantity = inventoryQuantity;
        this.productId = productId;
    }

    public DashboardDTO(){

    }
    public DashboardDTO(LocalDate dueDate, Long projectId, String projectName, Long warehouseId, String warehouseName, Long productId, String productName, int amountOfProduct) {
        this.dueDate = dueDate;
        this.projectId = projectId;
        this.projectName = projectName;
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.productId = productId;
        this.productName = productName;
        this.amountOfProduct = amountOfProduct;
    }


    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
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

    public int getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public LocalDate getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(LocalDate deliverDate) {
        this.deliverDate = deliverDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getAmountOfProduct() {
        return amountOfProduct;
    }

    public void setAmountOfProduct(int amountOfProduct) {
        this.amountOfProduct = amountOfProduct;
    }
}
