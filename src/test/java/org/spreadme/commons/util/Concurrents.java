/*
 * Copyright [4/2/20 2:35 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Concurrent tool
 * @author shuwei.wang
 */
public abstract class Concurrents {

	private Concurrents() {

	}

	public static long startAll(final int threadNums, final Runnable task, final ExecutorService executor) throws Exception {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(threadNums);
		for (int i = 0; i < threadNums; i++) {
			executor.submit(() -> {
				try {
					startGate.await();
					try {
						task.run();
					}
					finally {
						endGate.countDown();
					}
				}
				catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			});
		}
		long startTime = System.nanoTime();
		Console.info("[" + Thread.currentThread() + "] All thread is ready to begin task.");
		startGate.countDown();
		endGate.await();
		long endTime = System.nanoTime();
		Console.info("[" + Thread.currentThread() + "] All thread is completed.");
		return endTime - startTime;
	}

	public static <T> void startAll(int size, Callable<T> callable) throws InterruptedException {
		ExecutorService executorService = Executors.newWorkStealingPool();
		List<Callable<T>> callables = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			callables.add(callable);
		}
		executorService.invokeAll(callables);
	}
}
