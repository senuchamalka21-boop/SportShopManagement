package models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private String orderId, clientId, status, deliveryAgentId;
    private Date orderDate, dispatchDate;
    private Map<String, Integer> items;
    private double total;

    public Order(String orderId, String clientId) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.status = "PENDING";
        this.orderDate = new Date();
        this.items = new HashMap<>();
        this.total = 0.0;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getClientId() { return clientId; }
    public String getStatus() { return status; }
    public String getDeliveryAgentId() { return deliveryAgentId; }
    public Date getOrderDate() { return orderDate; }
    public Date getDispatchDate() { return dispatchDate; }
    public Map<String, Integer> getItems() { return items; }
    public double getTotal() { return total; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setDeliveryAgentId(String deliveryAgentId) { this.deliveryAgentId = deliveryAgentId; }
    public void setDispatchDate(Date dispatchDate) { this.dispatchDate = dispatchDate; }
    public void setTotal(double total) { this.total = total; }
}