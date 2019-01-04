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

package club.spreadme.lang.event.support;

import club.spreadme.lang.event.AbstractEvent;
import club.spreadme.lang.event.EventMulticaster;
import club.spreadme.lang.event.IEventListener;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public class CommonEventMulticaster implements EventMulticaster {

    private final ListenerStorage listenerStorage = new ListenerStorage();

    private Executor taskExecutor;

    private volatile static CommonEventMulticaster eventMulticaster;

    private CommonEventMulticaster() {

    }

    public static CommonEventMulticaster getInstance() {
        if (eventMulticaster == null) {
            synchronized (CommonEventMulticaster.class) {
                if (eventMulticaster == null) {
                    eventMulticaster = new CommonEventMulticaster();
                }
            }
        }
        return eventMulticaster;
    }

    @Override
    public void addEventListener(IEventListener<?> listener) {
        this.listenerStorage.listeners.add(listener);
    }

    @Override
    public void multicastEvent(AbstractEvent event) {
        for (final IEventListener<?> listener : getApplicationListeners(event)) {
            Executor executor = getTaskExecutor();
            if (executor != null) {
                executor.execute(() -> invokeListener(listener, event));
            } else {
                invokeListener(listener, event);
            }
        }
    }

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public Executor getTaskExecutor() {
        return taskExecutor;
    }

    private Collection<IEventListener<?>> getApplicationListeners(AbstractEvent event) {
        Set<IEventListener<?>> allListeners = new LinkedHashSet<>();
        Class<?> sourceType = event.getSource().getClass();
        for (IEventListener<?> listener : this.listenerStorage.listeners) {
            if (listener.supportsEventType(event.getClass()) && listener.supportsSourceType(sourceType)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void invokeListener(IEventListener listener, AbstractEvent event) {
        listener.onApplicationEvent(event);
    }

    private class ListenerStorage {

        public final Set<IEventListener<?>> listeners;

        public ListenerStorage() {
            this.listeners = new LinkedHashSet<>();
        }
    }
}
