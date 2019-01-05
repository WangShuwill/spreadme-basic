/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.lang;

import club.spreadme.lang.event.Event;
import club.spreadme.lang.event.EventMulticaster;
import club.spreadme.lang.event.EventPublisher;
import club.spreadme.lang.event.IEventListener;
import club.spreadme.lang.event.support.CommonEventMulticaster;
import club.spreadme.lang.event.support.CommonEventPublisher;

import java.util.concurrent.Executor;

public class EventBus {

    private Executor executor;

    private EventMulticaster eventMulticaster;
    private EventPublisher eventPublisher;

    public EventBus() {
        this(null);
    }

    public EventBus(Executor executor) {
        this.executor = executor;
        eventMulticaster = CommonEventMulticaster.getInstance();
        eventMulticaster.setTaskExecutor(executor);
        eventPublisher = CommonEventPublisher.getInstance();
    }

    public EventBus addEventListener(IEventListener eventListener) {
        eventMulticaster.addEventListener(eventListener);
        return this;
    }

    public void publishEvent(Event event) {
        eventPublisher.publishEvent(event);
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
