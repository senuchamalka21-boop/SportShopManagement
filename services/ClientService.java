package services;

import models.Client;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private List<Client> clients = new ArrayList<>();
    private int idCounter = 1001;

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    public Client getClientById(String id) {
        return clients.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void updateClient(Client updatedClient) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId().equals(updatedClient.getId())) {
                clients.set(i, updatedClient);
                return;
            }
        }
    }

    public void deleteClient(String id) {
        clients.removeIf(c -> c.getId().equals(id));
    }

    public String generateId() {
        return "C" + (idCounter++);
    }
}