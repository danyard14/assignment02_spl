package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.ResultAgentAvailable;

import java.util.ArrayList;
import java.util.List;

public class AgentsAvailableEvent extends Event<ResultAgentAvailable> {
    private List<String> agents;
    private int moneypenny;//TODO TALK ABOUT IT
    private List<String> agentsNames;

    public AgentsAvailableEvent(List<String> agents) {
        this.agents = agents;
    }

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
