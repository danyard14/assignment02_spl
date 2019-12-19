package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.List;

public class AgentsAvailableEvent extends Event<Result> {
    private List<Result> agents;

    public List<Result> getAgents() {
        return agents;
    }

    public void setAgents(List<Result> agents) {
        this.agents = agents;
    }
}
