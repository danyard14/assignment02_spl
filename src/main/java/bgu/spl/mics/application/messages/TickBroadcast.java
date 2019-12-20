package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private long currentTime;

    public TickBroadcast() {
        currentTime = 0;
    }

    public TickBroadcast(long currentTime) {
        this.currentTime = currentTime;
    }
}
