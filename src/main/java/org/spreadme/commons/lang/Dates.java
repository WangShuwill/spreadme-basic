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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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
		TemporalAccessor accessor = formatter.parse(text);
		LocalTime time = accessor.query(TemporalQueries.localTime());
		LocalDate date = accessor.query(TemporalQueries.localDate());
		LocalDateTime dateTime = Objects.isNull(time) ? date.atStartOfDay() : LocalDateTime.of(date, time);
		ZoneId zoneId = ZoneId.systemDefault();
		return Date.from(dateTime.atZone(zoneId).toInstant());
	}

	/**
	 * 获取日期
	 *
	 * @param date date
	 * @param unit A unit of date-time, such as Days or Hours. {@link TemporalUnit}
	 * @param amount 差值
	 * @return date
	 */
	public static Date getDate(Date date, ChronoUnit unit, long amount) {
		LocalDateTime dateTime = toLocalDataTime(date);
		dateTime = dateTime.plus(amount, unit);
		ZoneId zoneId = ZoneId.systemDefault();
		return Date.from(dateTime.atZone(zoneId).toInstant());
	}

	/**
	 * date to LocalDateTime
	 *
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
	@Deprecated
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
		return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
	}

	/**
	 * 获取一天的开始
	 *
	 * @param date date
	 * @return start time of date
	 */
	public static Date getStartOfDate(Date date) {
		LocalDate localDate = toLocalDate(date);
		LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN);
		return toDate(dateTime);
	}

	/**
	 * 获取一天的结束
	 *
	 * @param date date
	 * @return end time of date
	 */
	public static Date getEndOfDate(Date date) {
		LocalDate localDate = toLocalDate(date);
		LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MAX);
		return toDate(dateTime);
	}

	public static Date toDate(LocalDateTime dateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		return Date.from(dateTime.atZone(zoneId).toInstant());
	}

	/**
	 * get amount of time
	 *
	 * @param d1 from date
	 * @param d2 to date
	 * @return Duration {@link Duration}
	 */
	public static Duration getDuration(Date d1, Date d2) {
		LocalDateTime dt1 = toLocalDataTime(d1);
		LocalDateTime dt2 = toLocalDataTime(d2);
		return Duration.between(dt1, dt2);
	}

	public static Duration getDuration(LocalDateTime d1, LocalDateTime d2){
		return Duration.between(d1, d2);
	}

	/**
	 * 获取当前时间戳
	 *
	 * @return 时间戳
	 */
	public static int getTimestamp() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static final class DateFormatType {

		public static String NORM_DATE_PATTERN = "yyyy-MM-dd";

		public static String NORM_TIME_PATTERN = "HH:mm:ss";

		public static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	}

}
