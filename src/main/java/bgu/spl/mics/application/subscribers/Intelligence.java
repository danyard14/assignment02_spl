package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.List;


/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private final boolean debug = true;
    private List<MissionInfo> missionInfoList;

    public Intelligence(int id, List<MissionInfo> missionInfoList) {
        super("Intelligence_" + id);
        this.missionInfoList = missionInfoList;
        if (debug)
            System.out.println(this.getName() + " is created");
    }

    @Override
    protected void initialize() {
        if (debug)
            System.out.println(this.getName() + " is initialize");
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast broadcast) -> {
            for (MissionInfo missionInfo: missionInfoList) {
                if(missionInfo.getTimeIssued() == broadcast.getCurrentTime()) {
                    if (debug)
                        System.out.println("CurrentTime: " + missionInfo.getTimeIssued() + ", Intelligence: " + this.getName() + ", Sent MissionReceivedEvent");
                    MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(missionInfo);
                    getSimplePublisher().sendEvent(missionReceivedEvent);
                }
            }
        });
    }
}
