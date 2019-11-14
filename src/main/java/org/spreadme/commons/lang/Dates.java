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

	private static final CacheClient<String, DateTimeFormatter> FORMATTER_CACHE = new LocalCacheClient<>(16);

	private static final Calendar CALENDAR = Calendar.getInstance();

	private Dates() {

	}

	/**
	 * 日期格式化
	 *
	 * @param date date
	 * @param pattern pattern
	 * @return format string
	 */
	public static String format(Date date, String pattern) {
		DateTimeFormatter formatter = getDateFormatter(pattern);
		return formatter.format(date.toInstant());
	}

	/**
	 * 字符串转日期
	 *
	 * @param text text
	 * @param pattern pattern
	 * @return date
	 */
	public static Date parse(CharSequence text, String pattern) {
		DateTimeFormatter formatter = getDateFormatter(pattern);
		LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = dateTime.atZone(zoneId);
		return Date.from(zonedDateTime.toInstant());
	}

	/**
	 * 获取日期
	 *
	 * @param date date
	 * @param field Field number for {@link Calendar}
	 * @param amount 差值
	 * @return date
	 */
	public static Date getDate(Date date, int field, int amount) {
		Calendar calendar = toCalendar(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * date to LocalDateTime
	 * @param date date
	 * @return LocalDateTime
	 */
	public static LocalDateTime toLocalDataTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * date to LocalDate
	 *
	 * @param date date
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDate();
	}

	/**
	 * date to LocalTime
	 *
	 * @param date date
	 * @return LocalTime
	 */
	public static LocalTime toLocalTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalTime();
	}

	/**
	 * date to Calendar
	 *
	 * @param date date
	 * @return Calendar
	 */
	public static Calendar toCalendar(Date date) {
		CALENDAR.setTime(date);
		return CALENDAR;
	}

	/**
	 * get datetime fomatter by cache
	 *
	 * @param pattern pattern
	 * @return DateTimeFormatter
	 */
	public static DateTimeFormatter getDateFormatter(String pattern) {
		DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
		if (formatter == null) {
			formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
			final DateTimeFormatter previousFormatter = FORMATTER_CACHE.putIfAbsent(pattern, formatter);
			if (previousFormatter != null) {
				formatter = previousFormatter;
			}
		}
		return formatter;
	}

	/**
	 * 获取一天的开始
	 *
	 * @param date date
	 * @return start time of date
	 */
	public static Date getStartOfDate(Date date) {
		CALENDAR.setTime(date);
		CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
		CALENDAR.set(Calendar.MINUTE, 0);
		CALENDAR.set(Calendar.SECOND, 0);
		return CALENDAR.getTime();
	}

	/**
	 * 获取一天的结束
	 *
	 * @param date date
	 * @return end time of date
	 */
	public static Date getEndOfDate(Date date) {
		CALENDAR.setTime(date);
		CALENDAR.set(Calendar.HOUR_OF_DAY, 23);
		CALENDAR.set(Calendar.MINUTE, 59);
		CALENDAR.set(Calendar.SECOND, 59);
		return CALENDAR.getTime();
	}

	public static final class DateFormatType {

		public static String NORM_DATE_PATTERN = "yyyy-MM-dd";

		public static String NORM_TIME_PATTERN = "HH:mm:ss";

		public static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	}

}
