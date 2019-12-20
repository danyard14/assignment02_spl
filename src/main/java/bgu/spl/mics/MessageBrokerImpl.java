package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Result;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;
/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
	private Map<Subscriber, Queue<Message>> subscribersMap; //TODO:change the message type
	private Map<Object, ArrayList<Subscriber>> eventsMap;	//TODO:change field name
	private Map<Message, Future> eventFutureMap;

	private static class MessageBrokerHolder{
		private static MessageBroker instance = new MessageBrokerImpl();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return MessageBrokerHolder.instance;
	}

	private MessageBrokerImpl(){
		subscribersMap = new ConcurrentHashMap<>();
		eventsMap = new ConcurrentHashMap<>();
		eventsMap.put(MissionReceivedEvent.class, new ArrayList<>());
		eventsMap.put(AgentsAvailableEvent.class, new ArrayList<>());
		eventsMap.put(GadgetAvailableEvent.class, new ArrayList<>());
		eventsMap.put(TickBroadcast.class, new ArrayList<>()); //TODO:maybe delete
		eventFutureMap = new ConcurrentHashMap<>();
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		synchronized (eventsMap.get(type.getClass())) { //TODO: need notifyAll?
			if (!eventsMap.get(type.getClass()).contains(m)) //TODO:check if sucsriber can subscribe twice
				eventsMap.get(type.getClass()).add(m);
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future future = eventFutureMap.get(e);
		future.resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		ArrayList<Subscriber> list = eventsMap.get(b.getClass());
		for (Subscriber subscriber : list) {
			subscribersMap.get(b.getClass()).add(b);
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		if(eventsMap.get(e.getClass()).isEmpty()) {
			return null;
		}
		ArrayList<Subscriber> list = eventsMap.get(e.getClass());
		Subscriber subscriber = list.remove(0);
		list.add(subscriber);
		subscribersMap.get(subscriber).add(e);
		Future<T> future = new Future<>();
		eventFutureMap.put(e, future);
		return future;
	}

	@Override
	public void register(Subscriber m) {
		subscribersMap.put(m, new ConcurrentLinkedQueue<>());	//TODO:maybe another stracture
	}

	@Override
	public void unregister(Subscriber m) {	//TODO:maybe have to transfer all the subsriber event in the queue to the other subscribers
		if(subscribersMap.containsKey(m)) {
			subscribersMap.remove(m);
			for(Map.Entry<Object, ArrayList<Subscriber>> eventType : eventsMap.entrySet()) {
				synchronized (eventType) {
					if (eventType.getValue().contains(m)) {
						eventType.getValue().remove(m);
					}
				}
			}
		}
	}

	@Override
	public Message awaitMessage(Subscriber m) throws InterruptedException { //TODO: check the try N catch of this method
		try {
			while (subscribersMap.get(m).isEmpty()){
				try {
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch(IllegalStateException e) { }
		return subscribersMap.get(m).poll();
	}
}
