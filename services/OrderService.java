package services;

import models.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<Order> orders = new ArrayList<>();
    private int orderCounter = 1001;

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order getOrderById(String id) {
        return orders.stream()
                .filter(o -> o.getOrderId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void updateOrder(Order updatedOrder) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(updatedOrder.getOrderId())) {
                orders.set(i, updatedOrder);
                return;
            }
        }
    }

    public void deleteOrder(String id) {
        orders.removeIf(o -> o.getOrderId().equals(id));
    }

    public String generateId() {
        return "ORD" + (orderCounter++);
    }

    public List<Order> getPendingOrders() {
        List<Order> pending = new ArrayList<>();
        for (Order o : orders) {
            if ("PENDING".equals(o.getStatus())) {
                pending.add(o);
            }
        }
        return pending;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStatus().equals(status)) {
                result.add(o);
            }
        }
        return result;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Order o : orders) {
            total += o.getTotal();
        }
        return total;
    }
}