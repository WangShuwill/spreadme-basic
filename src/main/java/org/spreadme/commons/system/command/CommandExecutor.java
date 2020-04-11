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

import java.io.InputStream;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.spreadme.commons.util.StringUtil;


/**
 * Process execute
 * @author shuwei.wang
 * @since 1.0.0
 */
public class CommandExecutor {

	public static Runtime runtime = Runtime.getRuntime();

	private String program;

	private String command;

	private Stack<CommandParam> params;

	private StringBuilder plainCommand;

	private CommandExecutor(String program, String command, Stack<CommandParam> params) {
		this.program = program;
		this.command = command;
		this.params = params;
	}

	public static class ProcessExeBuilder {
		private String program = StringUtil.EMPTY;

		private String command = StringUtil.EMPTY;

		private Stack<CommandParam> params = new Stack<>();

		public ProcessExeBuilder() {
		}

		public ProcessExeBuilder program(String program) {
			this.program = program;
			return this;
		}

		public ProcessExeBuilder command(String command) {
			this.command = command;
			return this;
		}

		public ProcessExeBuilder addParam(CommandParam param) {
			params.add(param);
			return this;
		}

		public CommandExecutor build() {
			CommandExecutor processExe = new CommandExecutor(program, command, params);
			processExe.plainCommand = new StringBuilder(program).append(command);
			for (CommandParam param : processExe.params) {
				processExe.plainCommand.append(param.toString());
			}
			return processExe;
		}
	}

	public InputStream execute(long timeout, TimeUnit timeUnit) {
		Process process = null;
		try {
			process = runtime.exec(this.plainCommand.toString());
			process.waitFor(timeout, timeUnit);
			return process.getInputStream();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	public String getProgram() {
		return program;
	}

	public String getCommand() {
		return command;
	}

	public Stack<CommandParam> getParams() {
		return params;
	}

	public static ProcessExeBuilder builder() {
		return new ProcessExeBuilder();
	}
}
