/*
 * Copyright [4/1/20 10:59 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.test;

import java.util.Collections;
import java.util.List;

import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.StringUtil;

public class TestCompileMain{

	public void hello(){
		Console.info("Hello Compile");
		Console.info(StringUtil.randomString(10));
	}
}