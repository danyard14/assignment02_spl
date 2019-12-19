package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.ArrayList;
import java.util.List;

public class AgentsAvailableEvent extends Event<Result> {
    private ArrayList<String> agents;

    public ArrayList<String> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<String> agents) {
        this.agents = agents;
    }
}
