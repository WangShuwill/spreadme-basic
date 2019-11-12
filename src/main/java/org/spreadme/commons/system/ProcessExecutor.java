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

package org.spreadme.commons.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.spreadme.commons.util.StringUtil;


/**
 * Process execute
 * @author shuwei.wang
 * @since 1.0.0
 */
public class ProcessExecutor {

	public static Runtime runtime = Runtime.getRuntime();

	private static final int CORE_SIZE = runtime.availableProcessors();

	private static ExecutorService executorService = new ThreadPoolExecutor(CORE_SIZE, 4 * CORE_SIZE,
			60, TimeUnit.SECONDS,
			new SynchronousQueue<>());

	private String program;

	private String command;

	private Stack<CommandParam> params;

	private StringBuilder plainCommand;

	private ProcessExecutor(String program, String command, Stack<CommandParam> params) {
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

		public ProcessExecutor build() {
			ProcessExecutor processExe = new ProcessExecutor(program, command, params);
			processExe.plainCommand = new StringBuilder(program).append(command);
			for (CommandParam param : processExe.params) {
				processExe.plainCommand.append(param.toString());
			}
			return processExe;
		}
	}

	public String execute() {
		try {
			Process process = runtime.exec(this.plainCommand.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			reader.close();
			return result.toString();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String execute(long timeout, TimeUnit timeUnit) {
		try {
			CompletableFuture<String> future = CompletableFuture.supplyAsync(this::execute, executorService);
			return future.get(timeout, timeUnit);
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
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
