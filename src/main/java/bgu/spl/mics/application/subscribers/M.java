package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
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

	public M() {
		super("Change_This_Name");
		// TODO Implement this
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
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			GadgetAvailableEvent gadgetAvailableEvent = new GadgetAvailableEvent();
			gadgetAvailableEvent.setGadgets(missionInfo.getGadget());
			Future futureGadgetEvent = getSimplePublisher().sendEvent(gadgetAvailableEvent);
			while (!futureGadgetEvent.isDone())	{
				try {
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			Diary diary = Diary.getInstance();
			if(missionInfo.getTimeExpired() > 0 /* TODO: check how to make this work and check about duration */){
				diary.incrementTotal();
			}
			else { // time not expired
				Squad.getInstance().sendAgents(agents,missionInfo.getDuration());
				Result result = new Result();
				result.setResolved(true);
				event.setResult(result);
				Report report = new Report();
				report.setAll();
				event.setResult(result);
				diary.addReport( );
			}


			Result result = new Result(inventory.getItem(gadget), 0 ); //TODO: deal with time
			complete(event, result);
		});
	}
	private Report createReport(MissionInfo missionInfo){
		Report report = new Report();

		report.setAgentsNames();



	}

}
