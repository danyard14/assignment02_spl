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
    private List<MissionInfo> missionInfoList;

    public Intelligence(int id, List<MissionInfo> missionInfoList) {
        super("Intelligence_" + id);
        this.missionInfoList = missionInfoList;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast event) -> {
            for (MissionInfo mission: missionInfoList) {
                if(event.getCurrentTime() == mission.getTimeIssued()) {
                    MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(mission);
                    MessageBrokerImpl.getInstance().sendEvent(missionReceivedEvent);
                }
            }
        });
    }
}
