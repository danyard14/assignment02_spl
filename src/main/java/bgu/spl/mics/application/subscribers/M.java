package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.SendAgentsEvent;
import bgu.spl.mics.application.passiveObjects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int serial;

	public M() {
		super("Change_This_Name");
		// TODO init serial
}

	@Override
	protected void initialize() {
		subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent event) -> {
			MissionInfo missionInfo = event.getMissionInfo();
			List<String> agents = missionInfo.getSerialAgentsNumbers();
			AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent();
			agentsAvailableEvent.setAgents(agents);
			Future futureAgentsEvent = getSimplePublisher().sendEvent(agentsAvailableEvent);
			while (!futureAgentsEvent.isDone())	{
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent();
			gadgetAvailableEvent.setGadgets(missionInfo.getGadget());
			Future futureGadgetEvent = getSimplePublisher().sendEvent(gadgetAvailableEvent);

			while (!futureGadgetEvent.isDone())	{
				try {
					wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			Diary diary = Diary.getInstance();
			if(missionInfo.getTimeExpired() > 0 /* TODO: check how to make this work and check about duration */){
				diary.incrementTotal();
			}
			else { // time not expired

				Future futureSendAgents = getSimplePublisher().sendEvent(new SendAgentsEvent(missionInfo.getSerialAgentsNumbers(),missionInfo.getDuration()));

				while (!futureSendAgents.isDone()){
					try {
						wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}


				Result result = new Result();
				result.setResolved(true);
				event.setResult(result);

				Report report = createReport(missionInfo,futureAgentsEvent);;
				diary.addReport(report);
			}


//			Result result = new Result(inventory.getItem(gadget), 0 ); //TODO: deal with time
//			complete(event, result);
		});
	}
	private Report createReport(MissionInfo missionInfo, Future future){
		Report report = new Report();
		report.setAgentsSerialNumbers(missionInfo.getSerialAgentsNumbers());
		report.setGadgetName(missionInfo.getGadget());
		report.setMissionName(missionInfo.getMissionName());
		report.setMoneypenny(((ResultAgentAvailable) future.get()).getMoneypenny());
		report.setM(this.serial);
		//TODO: deal with time (set time and so on)...

		return report;
	}

}
