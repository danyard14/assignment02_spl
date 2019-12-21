package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int currentTime;
	private int mId;

	public M() {
		super("Change_This_Name");
		// TODO init serial
}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent event) -> {
			MissionInfo missionInfo = event.getMissionInfo();
			List<String> agents = missionInfo.getSerialAgentsNumbers();
			Diary diary = Diary.getInstance();
			Result result = new Result(false);
			AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(agents);
			Future agentsAvailableEventFuture = getSimplePublisher().sendEvent(agentsAvailableEvent);
//			while (!futureAgentsEvent.isDone())	{//TODO:synchronized?
//				try {
//					wait();
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}
//			}
			AgentAvailableResult agentsAvailableEventFutureResult = (AgentAvailableResult) agentsAvailableEventFuture.get();

			if (agentsAvailableEventFutureResult.isSuccessful())
			{
				GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(missionInfo.getGadget());
				Future gadgetAvailableEventFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);
				GadgetAvailableResult gadgetAvailableEventFutureResult = (GadgetAvailableResult) gadgetAvailableEventFuture.get();
				if (gadgetAvailableEventFutureResult.isSuccessful() && currentTime <= missionInfo.getTimeExpired()) {
					SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(agents, missionInfo.getDuration());
					Future sendAgentsEventFuture = getSimplePublisher().sendEvent(sendAgentsEvent);
					Result sendAgentsEventFutureResult = (Result) sendAgentsEventFuture.get();
					Report report = createReport(missionInfo, agentsAvailableEventFutureResult, gadgetAvailableEventFutureResult);
					diary.addReport(report);
					result.setIsSuccessful(true);
				}
				else {
					ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers());
					getSimplePublisher().sendEvent(releaseAgentsEvent);
				}
			}
			diary.incrementTotal();
			complete(event, result);
		});
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast broadcast) -> {
			currentTime = broadcast.getCurrentTime();
		});
	}

	private Report createReport(MissionInfo missionInfo, AgentAvailableResult agentsAvailableEventFutureResult, GadgetAvailableResult gadgetAvailableEventFutureResult) {
		Report report = new Report();
		report.setMissionName(missionInfo.getMissionName());
		report.setM(mId);
		report.setMoneypenny(agentsAvailableEventFutureResult.getMoneypenny());
		report.setAgentsSerialNumbers(missionInfo.getSerialAgentsNumbers());
		report.setAgentsNames(agentsAvailableEventFutureResult.getAgentsNames());
		report.setGadgetName(missionInfo.getGadget());
		report.setTimeIssued(missionInfo.getTimeIssued());
		report.setQTime(gadgetAvailableEventFutureResult.getqTime());
		report.setTimeCreated(currentTime);
		return report;
	}
}
