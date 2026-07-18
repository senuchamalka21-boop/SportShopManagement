package models;

public class DeliveryAgent {
    private String id, name, phone, vehicleType, vehiclePlate, deliveryZone;

    public DeliveryAgent(String id, String name, String phone, String vehicleType,
                         String vehiclePlate, String deliveryZone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
        this.deliveryZone = deliveryZone;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getVehicleType() { return vehicleType; }
    public String getVehiclePlate() { return vehiclePlate; }
    public String getDeliveryZone() { return deliveryZone; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public void setVehiclePlate(String vehiclePlate) { this.vehiclePlate = vehiclePlate; }
    public void setDeliveryZone(String deliveryZone) { this.deliveryZone = deliveryZone; }

    @Override
    public String toString() {
        return name + " (" + vehiclePlate + ")";
    }
}