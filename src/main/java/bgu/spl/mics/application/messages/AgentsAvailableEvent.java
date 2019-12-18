package bgu.spl.mics.application.messages;

import java.util.List;

public class AgentsAvailableEvent extends Event<Boolean> {
    private List<String> agents;

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }
}
