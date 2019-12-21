package bgu.spl.mics.application.passiveObjects;

import java.util.List;

public class ResultGadgetAvailable extends Result  {
    private int qTime;

    public ResultGadgetAvailable (int qTime, boolean isSuccessful) {
        super(isSuccessful);
        this.qTime = qTime;
    }

    public int getqTime() {
        return qTime;
    }
}
