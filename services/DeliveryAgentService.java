package services;

import models.DeliveryAgent;
import java.util.ArrayList;
import java.util.List;

public class DeliveryAgentService {
    private List<DeliveryAgent> agents = new ArrayList<>();
    private int idCounter = 1001;

    public List<DeliveryAgent> getAllAgents() {
        return new ArrayList<>(agents);
    }

    public DeliveryAgent getAgentById(String id) {
        return agents.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addAgent(DeliveryAgent agent) {
        agents.add(agent);
    }

    public void updateAgent(DeliveryAgent updatedAgent) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).getId().equals(updatedAgent.getId())) {
                agents.set(i, updatedAgent);
                return;
            }
        }
    }

    public void deleteAgent(String id) {
        agents.removeIf(a -> a.getId().equals(id));
    }

    public String generateId() {
        return "A" + (idCounter++);
    }
}