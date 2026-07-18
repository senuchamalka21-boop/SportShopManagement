package models;

public class Client {
    private String id, name, email, phone, address, teamName;

    public Client(String id, String name, String email, String phone, String address, String teamName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.teamName = teamName;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getTeamName() { return teamName; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    @Override
    public String toString() {
        return name + (teamName != null ? " (" + teamName + ")" : "");
    }
}