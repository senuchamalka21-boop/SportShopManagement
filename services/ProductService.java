package services;

import models.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private List<Product> products = new ArrayList<>();
    private int idCounter = 1001;

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product getProductById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(updatedProduct.getId())) {
                products.set(i, updatedProduct);
                return;
            }
        }
    }

    public void deleteProduct(String id) {
        products.removeIf(p -> p.getId().equals(id));
    }

    public String generateId() {
        return "P" + (idCounter++);
    }

    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<>();
        for (Product p : products) {
            if (p.getStock() <= p.getReorderLevel()) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }

    public void addStock(String productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
        }
    }
}