/*
 *    Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.spreadme.commons.io;

import java.io.InputStream;
import java.security.MessageDigest;

import org.spreadme.commons.io.observer.MessageDigestInputStreamObserver;
import org.spreadme.commons.io.observer.ObservableInputStream;

/**
 * MessageDigestInputStream
 * @author shuwei.wang
 * @since 1.0.0
 */
public class MessageDigestInputStream extends ObservableInputStream {

	private final MessageDigest digest;

	public MessageDigestInputStream(final InputStream in, final MessageDigest digest) {
		super(in);
		this.digest = digest;
		add(new MessageDigestInputStreamObserver(digest));
	}

	public MessageDigest getMessageDigest() {
		return digest;
	}
}
