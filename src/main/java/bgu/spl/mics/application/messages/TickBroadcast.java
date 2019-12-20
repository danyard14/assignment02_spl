package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private long currentTime;

    //Constructors
    public TickBroadcast() {
        currentTime = 0;
    }
    public TickBroadcast(long currentTime) {
        this.currentTime = currentTime;
    }

    //Getters
    public long getCurrentTime() {
        return currentTime;
    }
}
