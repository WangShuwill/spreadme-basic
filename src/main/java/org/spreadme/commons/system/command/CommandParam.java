/*
 * Copyright [4/11/20 10:10 AM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.system.command;

import java.util.Objects;

import org.spreadme.commons.util.StringUtil;


/**
 * command param
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CommandParam {

	private String name = StringUtil.EMPTY;

	private String param = StringUtil.EMPTY;

	private String symbol = StringUtil.EMPTY;

	public CommandParam() {
	}

	public CommandParam(String name, String param, String symbol) {
		this.name = name;
		this.param = param;
		this.symbol = symbol;
	}

	public CommandParam(String param) {
		this.param = param;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CommandParam that = (CommandParam) o;
		return Objects.equals(name, that.name) &&
				Objects.equals(param, that.param) &&
				Objects.equals(symbol, that.symbol);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, param, symbol);
	}

	@Override
	public String toString() {
		return this.symbol + this.name + StringUtil.SPACE + this.param;
	}
}
