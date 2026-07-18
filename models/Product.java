
package models;

public class Product {
    private String id, name, category, sportType, ecoRating;
    private double price;
    private int stock, reorderLevel;

    public Product(String id, String name, String category, String sportType,
                   double price, int stock, int reorderLevel, String ecoRating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.sportType = sportType;
        this.price = price;
        this.stock = stock;
        this.reorderLevel = reorderLevel;
        this.ecoRating = ecoRating;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSportType() { return sportType; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getReorderLevel() { return reorderLevel; }
    public String getEcoRating() { return ecoRating; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setSportType(String sportType) { this.sportType = sportType; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }
    public void setEcoRating(String ecoRating) { this.ecoRating = ecoRating; }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}