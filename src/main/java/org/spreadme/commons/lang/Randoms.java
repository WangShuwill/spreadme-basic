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

package org.spreadme.commons.lang;

import java.util.concurrent.ThreadLocalRandom;

/**
 * threadlocal random
 * @author shuwei.wang
 */
public abstract class Randoms {

	private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

	private Randoms() {

	}

	public static boolean nextBoolean() {
		return RANDOM.nextBoolean();
	}

	public static byte[] nextBytes(final int count) {
		final byte[] bytes = new byte[count];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

	public static int nextInt(final int start, final int end) {
		return RANDOM.nextInt(start, end);
	}

	public static int nextInt(final int end) {
		return RANDOM.nextInt(end);
	}

	public static int nextInt() {
		return RANDOM.nextInt();
	}

	public static long nextLong(final long start, final long end) {
		return RANDOM.nextLong(start, end);
	}

	public static long nextLong(final long end) {
		return RANDOM.nextLong(end);
	}

	public static long nextLong() {
		return RANDOM.nextLong();
	}

	public static double nextDouble(final double start, final double end) {
		return RANDOM.nextDouble(start, end);
	}

	public static double nextDouble(final double end) {
		return RANDOM.nextDouble(end);
	}

	public static double nextDouble() {
		return RANDOM.nextDouble();
	}

	public static float nextFloat() {
		return RANDOM.nextFloat();
	}
}
