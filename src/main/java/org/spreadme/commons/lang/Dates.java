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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.spreadme.commons.cache.CacheClient;
import org.spreadme.commons.cache.support.LocalCacheClient;

/**
 * date utils
 * @author shuwei.wang
 */
public abstract class Dates {

	private static final CacheClient<String, DateTimeFormatter> FORMATTER_CACHE = new LocalCacheClient<>();

	private Dates() {

	}

	public static String format(Date date, String pattern) {
		LocalDateTime time = toLocalDataTime(date);
		DateTimeFormatter formatter = getDateFormatter(pattern);
		return time.format(formatter);
	}

	public static LocalDateTime toLocalDataTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	public static LocalDate toLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

	public static LocalTime toLocalTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalTime();
	}

	public static Calendar toCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Date getDate(Date date, int field, int amount) {
		Calendar calendar = toCalendar(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	public static Date parse(CharSequence text, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
		return Date.from(zonedDateTime.toInstant());
	}

	private static DateTimeFormatter getDateFormatter(String pattern) {
		DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern(pattern);
			final DateTimeFormatter previousFormatter = FORMATTER_CACHE.putIfAbsent(pattern, formatter);
			if (previousFormatter != null) {
				formatter = previousFormatter;
			}
		}
		return formatter;
	}

	public static final class DateFormatType {

		public static String NORM_DATE_PATTERN = "yyyy-MM-dd";

		public static String NORM_TIME_PATTERN = "HH:mm:ss";

		public static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	}

}
