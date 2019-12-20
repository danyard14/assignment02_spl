package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.ArrayList;
import java.util.List;

public class AgentsAvailableEvent extends Event<Result> {
    private List<String> agents;
    private int moneypenny;
    private List<String> agentsNames;

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    public int getMoneypenny() {
        return moneypenny;
    }

    public void setMoneypenny(int moneypenny) {
        this.moneypenny = moneypenny;
    }

    public List<String> getAgentsNames() {
        return agentsNames;
    }

    public void setAgentsNames(List<String> agentsNames) {
        this.agentsNames = agentsNames;
    }
}
