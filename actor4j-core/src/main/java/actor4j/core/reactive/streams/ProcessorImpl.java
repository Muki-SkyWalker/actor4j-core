/*
 * Copyright (c) 2015-2017, David A. Bauer
 */
package actor4j.core.reactive.streams;

import java.util.UUID;
import java.util.function.Consumer;

import actor4j.core.actors.Actor;
import actor4j.core.messages.ActorMessage;
import tools4j.function.Procedure;

public class ProcessorImpl extends PublisherImpl {
	protected SubscriberImpl subscriberImpl;
	
	public ProcessorImpl(Actor actor) {
		super(actor);
		subscriberImpl = new SubscriberImpl(actor);
	}
	
	@Override
	public void receive(ActorMessage<?> message) {
		super.receive(message);
		subscriberImpl.receive(message);
	}
	
	public void subscribe(UUID dest, Consumer<Object> onNext, Consumer<String> onError, Procedure onComplete) {
		subscriberImpl.subscribe(dest, onNext, onError, onComplete);
	}
	
	public void unsubscribe(UUID dest) {
		subscriberImpl.unsubscribe(dest);
	}
	
	public void request(long n, UUID dest) {
		subscriberImpl.request(n, dest);
	}
	
	public void requestReset(long n, UUID dest) {
		subscriberImpl.requestReset(n, dest);
	}
	
	public void bulk(UUID dest) {
		subscriberImpl.bulk(dest);
	}
	
	public void cancelBulk(UUID dest) {
		subscriberImpl.cancelBulk(dest);
	}
}
