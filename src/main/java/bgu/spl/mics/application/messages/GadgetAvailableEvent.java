package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.List;

public class GadgetAvailableEvent extends Event<Result> {
    private List<String> gadgets;

    public List<String> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<String> gadgets) {
        this.gadgets = gadgets;
    }
}
