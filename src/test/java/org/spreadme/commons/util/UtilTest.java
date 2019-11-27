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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.spreadme.commons.crypt.Algorithm;
import org.spreadme.commons.crypt.Hash;
import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.PrefixedLeftNumericGenerator;
import org.spreadme.commons.id.support.SnowflakeLongGenerator;
import org.spreadme.commons.id.support.TimeBasedIdentifierGenerator;
import org.spreadme.commons.lang.Console;
import org.spreadme.commons.lang.Dates;
import org.spreadme.commons.lang.Randoms;
import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.system.SystemMonitor;

/**
 * @author shuwei.wang
 */
public class UtilTest {

	@Test
	public void testStringUtil() throws Exception {
		System.out.println(Arrays.toString(Randoms.nextBytes(3)));
		Concurrents.startAllTaskInOnce(10, () -> {
			String result = StringUtil.randomString(8) + " :: " + StringUtil.randomString("-+*", 1);
			Console.info(result + " :: " + Hash.toHexString(new ByteArrayInputStream(result.getBytes()), Algorithm.MD5));
			return result;
		});
		Console.info(StringUtil.replace("wsweiwwww//\\w/", "w", "90"));
		String unicode = StringUtil.stringToUnicode("TEst^&测试");
		Console.info(unicode);
		Console.info(StringUtil.unicodeToString(unicode));
	}

	@Test
	public void testId() {
		IdentifierGenerator<String> timeBasedGenerator = new TimeBasedIdentifierGenerator();
		IdentifierGenerator<String> leftNumericGenerator = new PrefixedLeftNumericGenerator(StringUtil.randomString(1), true, 3);
		IdentifierGenerator<Long> longIdentifierGenerator = new SnowflakeLongGenerator(1, 1);
		for (int i = 0; i < 101; i++) {
			Console.info(timeBasedGenerator.nextIdentifier());
			Console.info(leftNumericGenerator.nextIdentifier());
			Console.info(longIdentifierGenerator.nextIdentifier());
		}
	}

	@Test
	public void testDateFomatter() throws Exception {
		final int THREAD_POOL_SIZE = 10;
		final String FORMATTER = "HH:mm:ss dd.MM.yyyy";
		final SimpleDateFormat sdf = new SimpleDateFormat(FORMATTER);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

		Concurrents.startAllTaskInOnce(THREAD_POOL_SIZE, () -> {
			try {
				String dateCreate = "19:30:55 03.05.2015";
				sdf.parse(dateCreate);
				//Dates.parse(dateCreate, FORMATTER);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
			Console.info("OK ");
		}, executor);
	}

	@Test
	public void testDates() throws IOException {
		Console.info(Dates.getStartOfDate(new Date()));
		Console.info(Dates.getEndOfDate(new Date()));
		Console.info(Dates.getDate(new Date(), ChronoUnit.DAYS, -100));
		Console.info(1 << 28 ^ 80);
		final String path = ClassUtil.getClassPath() + SystemInfo.FILE_SEPARATOR + "/testpath/123/test.txt";
		FileUtil.createFile(path, true);
	}

	@Test
	public void testSystemInfo() {
		SystemMonitor monitor = new SystemMonitor();
		Console.info(monitor.getSystemInfo());
	}

	@Test
	public void testDateParse() {
		Date date = Dates.parse("1993-12-12 12:00:09", "yyyy-MM-dd HH:mm:ss");
		Console.info(date);
	}
}
