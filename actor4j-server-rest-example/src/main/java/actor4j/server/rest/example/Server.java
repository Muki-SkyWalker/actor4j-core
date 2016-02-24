/*
 * Copyright (c) 2015, David A. Bauer
 */
package actor4j.server.rest.example;

import java.util.ArrayList;
import java.util.List;

import actor4j.core.actors.Actor;
import actor4j.core.messages.ActorMessage;
import actor4j.core.messages.RemoteActorMessage;

public class Server extends Actor {
	public Server() {
		super();
	}

	protected static class Payload {
		public List<String> data;
		
		public Payload() {
			data = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "Payload [data=" + data + "]";
		}
	}
	
	@Override
	public void receive(ActorMessage<?> message) {
		RemoteActorMessage.optionalConvertValue(message, Payload.class);
		
		System.out.println(message);
	}
}
