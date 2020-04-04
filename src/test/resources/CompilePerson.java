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

import org.spreadme.commons.util.Console;
import org.spreadme.commons.util.StringUtil;

public class CompilePerson {

	private String name;
	private Integer age;

	public CompilePerson() {

	}

	public CompilePerson(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public void hello() {
		Console.info("My name is %s, age is %d, randome string is %s",
				this.name, this.age, StringUtil.randomString(8));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}