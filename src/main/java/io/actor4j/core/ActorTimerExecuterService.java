/*
 * Copyright (c) 2015-2018, David A. Bauer. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.actor4j.core;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import io.actor4j.core.messages.ActorMessage;
import io.actor4j.core.utils.ActorGroup;

public class ActorTimerExecuterService implements ActorTimer {
	protected final ActorSystemImpl system;
	
	protected final ScheduledExecutorService timerExecuterService;
	
	public ActorTimerExecuterService(ActorSystemImpl system, int corePoolSize, String threadName) {
		super();
		
		this.system = system;
		this.timerExecuterService = new ScheduledThreadPoolExecutor(corePoolSize, new DefaultThreadFactory(threadName));
	}
	
	public ActorTimerExecuterService(ActorSystemImpl system, int corePoolSize) {
		this(system, corePoolSize, "actor4j-timer-thread");
	}
		
	public ScheduledFuture<?> scheduleOnce(Runnable command, long delay, TimeUnit unit) {
		return timerExecuterService.schedule(command, delay, unit);
	}
	
	public ScheduledFuture<?> schedule(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return timerExecuterService.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	
	@Override
	public ScheduledFuture<?> scheduleOnce(final Supplier<ActorMessage<?>> supplier, final UUID dest, long delay, TimeUnit unit) {
		return timerExecuterService.schedule(new Runnable() {
			@Override
			public void run() {
				ActorMessage<?> message = supplier.get();
				message.dest = dest;
				system.send(message);
			}
		}, delay, unit); 
	}
	
	@Override
	public ScheduledFuture<?> scheduleOnce(final ActorMessage<?> message, final UUID dest, long delay, TimeUnit unit) {
		return scheduleOnce(new Supplier<ActorMessage<?>>() {
			@Override
			public ActorMessage<?> get() {
				return message;
			}
		}, dest, delay, unit);
	}
	
	@Override
	public ScheduledFuture<?> scheduleOnce(final Supplier<ActorMessage<?>> supplier, final ActorGroup group, long delay, TimeUnit unit) {
		return timerExecuterService.schedule(new Runnable() {
			@Override
			public void run() {
				ActorMessage<?> message = supplier.get();
				for (UUID id : group) {
					message.dest = id;
					system.send(message);
				}
			}
		}, delay, unit); 
	}
	
	@Override
	public ScheduledFuture<?> scheduleOnce(final ActorMessage<?> message, final ActorGroup group, long delay, TimeUnit unit) {
		return scheduleOnce(new Supplier<ActorMessage<?>>() {
			@Override
			public ActorMessage<?> get() {
				return message;
			}
		}, group, delay, unit);
	}
	
	@Override
	public ScheduledFuture<?> schedule(final Supplier<ActorMessage<?>> supplier, final UUID dest, long initalDelay, long period, TimeUnit unit) {
		return timerExecuterService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				ActorMessage<?> message = supplier.get();
				message.dest = dest;
				system.send(message);
			}
		}, initalDelay, period, unit); 
	}
	
	@Override
	public ScheduledFuture<?> schedule(final ActorMessage<?> message, final UUID dest, long initalDelay, long period, TimeUnit unit) {
		return schedule(new Supplier<ActorMessage<?>>() {
			@Override
			public ActorMessage<?> get() {
				return message;
			}
		}, dest, initalDelay, period, unit);
	}
	
	@Override
	public ScheduledFuture<?> schedule(final Supplier<ActorMessage<?>> supplier, final ActorGroup group, long initalDelay, long period, TimeUnit unit) {
		return timerExecuterService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				ActorMessage<?> message = supplier.get();
				for (UUID id : group) {
					message.dest = id;
					system.send(message);
				}
			}
		}, initalDelay, period, unit); 
	}
	
	@Override
	public ScheduledFuture<?> schedule(final ActorMessage<?> message, final ActorGroup group, long initalDelay, long period, TimeUnit unit) {
		return schedule(new Supplier<ActorMessage<?>>() {
			@Override
			public ActorMessage<?> get() {
				return message;
			}
		}, group, initalDelay, period, unit);
	}
	
	public void shutdown() {
		timerExecuterService.shutdown();
	}
}
