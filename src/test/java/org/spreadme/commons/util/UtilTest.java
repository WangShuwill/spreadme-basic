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
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.id.IdentifierGenerator;
import org.spreadme.commons.id.support.PrefixedLeftNumericGenerator;
import org.spreadme.commons.id.support.SnowflakeLongGenerator;
import org.spreadme.commons.id.support.TimeBasedIdentifierGenerator;
import org.spreadme.commons.lang.Dates;
import org.spreadme.commons.lang.ImageFormats;
import org.spreadme.commons.lang.Randoms;
import org.spreadme.commons.lang.SizeUnit;
import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.system.sampler.JvmMemorySampler;
import org.spreadme.commons.system.sampler.ProcessorSampler;
import org.spreadme.commons.system.sampler.Sampler;
import org.spreadme.commons.system.sampler.UptimeSampler;

/**
 * @author shuwei.wang
 */
public class UtilTest {

	@Test
	public void testStringUtil() throws Exception {
		Concurrents.startAll(10, () -> {
			Console.info("%s 的随机字符串为 %s, 随机数组 %s",
					Thread.currentThread().getName(),
					StringUtil.randomString(8),
					Hex.toHexString(Randoms.nextBytes(3)));
			return null;
		});
		Console.info(StringUtil.replace("wsweiwwww//\\w/", "w", "90"));
		String plain = "TEst^&测试";
		String unicode = StringUtil.stringToUnicode(plain);
		Console.info("%s 的Unicode码: %s", plain, unicode);
		Console.info("%s 的原文为: %s", unicode, StringUtil.unicodeToString(unicode));

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
	public void testDates() throws Exception {
		final int poolSize = 8;
		final Date now = new Date();
		final String formatter = "HH:mm:ss dd.MM.yyyy";
		final String text = "19:30:55 03.05.2015";
		//final SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		ExecutorService executor = Executors.newFixedThreadPool(poolSize);

		Concurrents.startAll(poolSize, () -> {
			//Date date = sdf.parse(dateCreate);
			Date date = Dates.parse(text, formatter);
			Console.info("%s parse %s to %s", Thread.currentThread().getName(), text, date);
		}, executor);

		Console.info("今天开始时间: %s", Dates.getStartOfDate(new Date()));
		Console.info("今天结束时间: %s", Dates.getEndOfDate(new Date()));
		Console.info("100天前的时间: %s", Dates.getDate(new Date(), ChronoUnit.DAYS, -100));

		Console.info("时间戳: %s", Dates.getTimestamp());

		Date past = Dates.parse(text, formatter);
		Duration duration = Dates.getDuration(past, now);
		Console.info("%s和%s相距%d天", past, now, duration.toDays());

		Console.info("%s格式化为%s", now, Dates.format(now, formatter));
	}

	@Test
	public void testSampler() throws InterruptedException {
		Console.info("osName: %s, osType: %s, archType: %s",
				SystemInfo.OS_NAME, SystemInfo.OS_TYPE, SystemInfo.ARCH_TYPE);
		Sampler processorSampler = new ProcessorSampler();
		Sampler jvmMemorySampler = new JvmMemorySampler();
		Sampler uptimeSampler = new UptimeSampler();
		for (int i = 0; i < 10; i++) {
			TimeUnit.SECONDS.sleep(1);
			String processor = processorSampler.sample().stream()
					.map(m -> String.format("%s: %5.4f", m.name(), m.value()))
					.collect(Collectors.joining(", "));
			Console.info(processor);
			String jvmmemory = jvmMemorySampler.sample().stream().filter(m -> !m.name().contains("buffer"))
					.map(m -> String.format("%s: %s", m.name(), SizeUnit.convert(m.value().longValue())))
					.collect(Collectors.joining(", "));
			Console.info(jvmmemory);
			String uptime = uptimeSampler.sample().stream()
					.map(m -> String.format("%s: %d", m.name(), m.value().longValue()))
					.collect(Collectors.joining(", "));
			Console.info(uptime);
		}
	}

	@Test
	public void testTextToImage() throws IOException {
		BufferedImage image = ImageUtil.toImage("Test测试", new Font(Font.SANS_SERIF, Font.PLAIN, (int) (50 * 0.75)), Color.BLACK);
		ImageIO.write(image, ImageFormats.PNG.getName(),
				new File(ClassUtil.getClassPath() + SystemInfo.FILE_SEPARATOR + "water.png"));
	}

	@Test
	public void testNetUtil() {
		String url = "https://shuwei.me";
		final String host = NetUtil.getHostByUrl(url);
		final String hostIp = NetUtil.getIpByDomain(host);
		Console.info("%s的host为%s", url, host);
		Console.info("%s的host ip为%s", url, hostIp);
		Console.info("%s是否可以连接%s", url, NetUtil.isConnected(url, 5000));
	}

}
