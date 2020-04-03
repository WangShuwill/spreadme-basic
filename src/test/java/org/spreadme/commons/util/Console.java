/*
 * Copyright [4/3/20 12:51 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.util.Date;

import org.spreadme.commons.lang.Dates;

/**
 * Console instead of system out
 * @author shuwei.wang
 */
public abstract class Console {

	public static void info(Object object, Object... args) {
		System.out.println("INFO[" + Dates.format(new Date(), Dates.DateFormatType.NORM_DATETIME_PATTERN) +
				"] " + String.format(object.toString(), args));
	}

}
