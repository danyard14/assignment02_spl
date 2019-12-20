package bgu.spl.mics.application.passiveObjects;

import java.util.List;

public class ResultAgentAvailable extends Result {
    private List<String> serials;
    private List<String> names;
    private int moneypenny;

    public void setMoneypenny(int moneypenny) {
        this.moneypenny = moneypenny;
    }

    public int getMoneypenny() {
        return moneypenny;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getSerials() {
        return serials;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
    public void setSerials(List<String> serials){
        this.serials = serials;
    }
}
