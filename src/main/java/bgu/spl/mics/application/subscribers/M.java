package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
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
	private final boolean debug = true;
	private int currentTime;
	private int mId;

	public M() {
		super("M");
		currentTime = 0;
	}

	public M(int mId) {
		super("M"+mId);
		currentTime = 0;
		this.mId = mId;
	}

	@Override
	protected void initialize() { //TODO Make it thread safety
		subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent event) -> {
			if (debug)
				System.out.println(this.getName() + " Received Message");
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
			MessageBroker x = MessageBrokerImpl.getInstance();//todo: delete
			AgentAvailableResult agentsAvailableEventFutureResult = (AgentAvailableResult) agentsAvailableEventFuture.get();

			if (agentsAvailableEventFutureResult.isSuccessful())
			{
				GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent(missionInfo.getGadget());
				Future gadgetAvailableEventFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);
				GadgetAvailableResult gadgetAvailableEventFutureResult = (GadgetAvailableResult) gadgetAvailableEventFuture.get();
				if (gadgetAvailableEventFutureResult.isSuccessful() && gadgetAvailableEventFutureResult.getqTime() <= missionInfo.getTimeExpired()) {
					SendAgentsEvent sendAgentsEvent = new SendAgentsEvent(agents, missionInfo.getDuration());
					Future sendAgentsEventFuture = getSimplePublisher().sendEvent(sendAgentsEvent);
					sendAgentsEventFuture.get();
					Report report = createReport(missionInfo, agentsAvailableEventFutureResult, gadgetAvailableEventFutureResult);
					diary.addReport(report);
					result.setIsSuccessful(true);
				}
				else {
					diary.incrementTotal();
					ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(missionInfo.getSerialAgentsNumbers());
					getSimplePublisher().sendEvent(releaseAgentsEvent);
				}
			}
			else {
				diary.incrementTotal();
			}
			diary.printToFile("ss");//TODO: Delete
			complete(event, result);
		});

		subscribeBroadcast(TickBroadcast.class, (TickBroadcast broadcast) -> {
			currentTime = broadcast.getCurrentTime();
			if (broadcast.isTerminated()) {
				terminate();
			}
		});
	}

	private Report createReport(MissionInfo missionInfo, AgentAvailableResult agentsAvailableEventFutureResult, GadgetAvailableResult gadgetAvailableEventFutureResult) {
		Report report = new Report();
		report.setName(missionInfo.getMissionName());
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
