/*
 * Copyright [4/15/20 11:03 AM] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.message;

/**
 * Message Listener Registrar
 * @author shuwei.wang
 */
public interface MessageListenerRegistrar {

	<M extends Message> void register(MessageListener<M> listener);
	
	void destroy();
}
