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

package org.spreadme.commons.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Concurrent tool
 * @author shuwei.wang
 */
public abstract class Concurrents {

	private Concurrents() {

	}

	public static long startAllTaskInOnce(final int threadNums, final Runnable task, final ExecutorService executor) throws Exception {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(threadNums);
		for (int i = 0; i < threadNums; i++) {
			executor.submit(() -> {
				try {
					// 使线程在此等待，当开始门打开时，一起涌入门中
					startGate.await();
					try {
						task.run();
					}
					finally {
						// 将结束门减1，减到0时，就可以开启结束门了
						endGate.countDown();
					}
				}
				catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			});
		}
		long startTime = System.nanoTime();
		System.out.println("\n[" + Thread.currentThread() + "] All thread is ready to begin task.");
		// 因开启门只需一个开关，所以立马就开启开始门
		startGate.countDown();
		// 等等结束门开启
		endGate.await();
		long endTime = System.nanoTime();
		System.out.println("\n[" + Thread.currentThread() + "] All thread is completed.");
		return endTime - startTime;
	}
}
