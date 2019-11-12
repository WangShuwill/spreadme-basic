/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.io.observer;

import java.security.MessageDigest;

/**
 * MessageDigest InputSteam Observer
 * @author shuwei.wang
 * @since 1.0.0
 */
public class MessageDigestInputStreamObserver extends AbstractInputStreamObserver {

	private final MessageDigest digest;

	public MessageDigestInputStreamObserver(MessageDigest digest) {
		this.digest = digest;
	}

	@Override
	public void data(final int pByte) {
		digest.update((byte) pByte);
	}

	@Override
	public void data(final byte[] pBuffer, final int pOffset, final int pLength) {
		digest.update(pBuffer, pOffset, pLength);
	}
}
