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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.spreadme.commons.crypt.Algorithm;
import org.spreadme.commons.crypt.Hash;
import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.PrefixedLeftNumericGenerator;
import org.spreadme.commons.id.support.SnowflakeLongGenerator;
import org.spreadme.commons.id.support.TimeBasedIdentifierGenerator;
import org.spreadme.commons.lang.Console;
import org.spreadme.commons.lang.Dates;
import org.spreadme.commons.lang.ImageFormats;
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
		for (int i = 0; i < 100; i++) {
			Console.info("timebase: %s, numeric: %s, longid: %s",
					timeBasedGenerator.nextIdentifier(),
					leftNumericGenerator.nextIdentifier(),
					longIdentifierGenerator.nextIdentifier());
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
	public void testDates() {
		Console.info(Dates.getStartOfDate(new Date()));
		Console.info(Dates.getEndOfDate(new Date()));
		Console.info(Dates.getDate(new Date(), ChronoUnit.DAYS, -100));
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
		Console.info(Dates.getTimestamp());
	}

	@Test
	public void testTextToImage() throws IOException {
		BufferedImage image = ImageUtil.toImage("Test测试", new Font(Font.SANS_SERIF, Font.PLAIN, (int) (50 * 0.75)), Color.BLACK);
		ImageIO.write(image, ImageFormats.PNG.getName(),
				new File(ClassUtil.getClassPath() + SystemInfo.FILE_SEPARATOR + "water.png"));
	}

	@Test
	public void testNetUtil() {
		final String host = NetUtil.getHostByUrl("https://ci.qiyuesuo.me");
		final String hostIp = NetUtil.getIpByDomain(host);
		Console.info(host);
		Console.info(hostIp);
		Console.info(NetUtil.isReachable(hostIp, 5000));
	}
}
