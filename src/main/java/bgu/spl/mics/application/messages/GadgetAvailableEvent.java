package bgu.spl.mics.application.messages;

import java.util.List;

public class GadgetAvailableEvent extends Event<Boolean> {
    private List<String> gadgets;

    public List<String> getGadgets() {
        return gadgets;
    }

    public void setGadgets(List<String> gadgets) {
        this.gadgets = gadgets;
    }
}
