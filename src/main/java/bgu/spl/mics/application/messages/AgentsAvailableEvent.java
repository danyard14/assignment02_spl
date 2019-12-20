package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.ArrayList;
import java.util.List;

public class AgentsAvailableEvent extends Event<Result> {
    private List<String> agents;

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }
}
