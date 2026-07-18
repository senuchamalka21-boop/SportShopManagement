package utils;

import models.*;
import services.*;
import java.util.Map;

public class DataGenerator {
    public static void generateSampleData(ProductService productService, ClientService clientService,
                                          DeliveryAgentService agentService, OrderService orderService) {
        // Products
        productService.addProduct(new Product(productService.generateId(), "Cricket Bat", "Bats", "Cricket", 2500.00, 15, 5, "⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Tennis Racket", "Rackets", "Tennis", 3800.00, 8, 3, "⭐⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Football", "Balls", "Football", 1200.00, 25, 10, "⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Running Shoes", "Footwear", "Athletics", 4500.00, 12, 4, "⭐⭐⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Basketball", "Balls", "Basketball", 1800.00, 10, 5, "⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Swim Goggles", "Accessories", "Swimming", 850.00, 20, 8, "⭐⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Yoga Mat", "Fitness", "Yoga", 2200.00, 6, 3, "⭐⭐⭐⭐⭐"));
        productService.addProduct(new Product(productService.generateId(), "Cricket Helmet", "Protective", "Cricket", 3200.00, 5, 2, "⭐⭐⭐⭐"));

        // Clients
        clientService.addClient(new Client(clientService.generateId(), "Kandy Sports Club", "kandy@sports.lk", "081-2223344", "Kandy", "Kandy Lions"));
        clientService.addClient(new Client(clientService.generateId(), "Colombo Athletic", "colombo@athletic.lk", "011-2345678", "Colombo", "Colombo Strikers"));
        clientService.addClient(new Client(clientService.generateId(), "Galle Cricket Academy", "galle@cricket.lk", "091-4567890", "Galle", "Galle Titans"));
        clientService.addClient(new Client(clientService.generateId(), "Negombo Water Sports", "negombo@watersports.lk", "031-1234567", "Negombo", "Negombo Waves"));

        // Delivery Agents
        agentService.addAgent(new DeliveryAgent(agentService.generateId(), "Nuwan Perera", "077-1234567", "Van", "NP-1234", "Kandy"));
        agentService.addAgent(new DeliveryAgent(agentService.generateId(), "Chamara Silva", "076-7890123", "Motorcycle", "CS-5678", "Colombo"));
        agentService.addAgent(new DeliveryAgent(agentService.generateId(), "Dilani Fernando", "075-4567890", "Truck", "DF-9012", "Galle"));
        agentService.addAgent(new DeliveryAgent(agentService.generateId(), "Kasun Jayasinghe", "078-3456789", "Van", "KJ-3456", "Negombo"));

        // Sample Orders
        Order o1 = new Order(orderService.generateId(), "C1001");
        o1.getItems().put("P1001", 3);
        o1.getItems().put("P1003", 2);
        o1.setTotal(2500*3 + 1200*2);
        o1.setStatus("DELIVERED");
        o1.setDeliveryAgentId("A1001");
        orderService.addOrder(o1);

        Order o2 = new Order(orderService.generateId(), "C1002");
        o2.getItems().put("P1002", 2);
        o2.getItems().put("P1004", 1);
        o2.setTotal(3800*2 + 4500*1);
        o2.setStatus("DISPATCHED");
        o2.setDeliveryAgentId("A1002");
        orderService.addOrder(o2);

        Order o3 = new Order(orderService.generateId(), "C1003");
        o3.getItems().put("P1008", 2);
        o3.getItems().put("P1001", 1);
        o3.setTotal(3200*2 + 2500*1);
        o3.setStatus("PENDING");
        orderService.addOrder(o3);

        Order o4 = new Order(orderService.generateId(), "C1004");
        o4.getItems().put("P1006", 4);
        o4.setTotal(850*4);
        o4.setStatus("PENDING");
        orderService.addOrder(o4);

        // Update stock based on orders
        for (Order o : orderService.getAllOrders()) {
            for (Map.Entry<String, Integer> entry : o.getItems().entrySet()) {
                Product p = productService.getProductById(entry.getKey());
                if (p != null) {
                    p.setStock(p.getStock() - entry.getValue());
                }
            }
        }
    }
}