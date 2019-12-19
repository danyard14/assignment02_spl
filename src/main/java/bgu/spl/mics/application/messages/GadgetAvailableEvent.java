package bgu.spl.mics.application.messages;

import bgu.spl.mics.application.passiveObjects.Result;

import java.util.List;

public class GadgetAvailableEvent extends Event<Result> {
    private String gadget;

    public String getGadget() {
        return gadget;
    }

    public void setGadgets(String gadget) {
        this.gadget = gadget;
    }
}
