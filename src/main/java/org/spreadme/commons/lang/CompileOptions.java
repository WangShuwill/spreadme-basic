/*
 * Copyright [4/1/20 9:51 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.lang;

import java.util.Collections;
import java.util.List;

import javax.annotation.processing.Processor;

import org.spreadme.commons.util.CollectionUtil;

/**
 * Compile Options
 * @author shuwei.wang
 */
public final class CompileOptions {

	private final List<? extends Processor> processors;
	private final List<String> options;

	public CompileOptions() {
		this(Collections.emptyList(), Collections.emptyList());
	}

	private CompileOptions(List<? extends Processor> processors, List<String> options) {
		this.processors = processors;
		this.options = options;
	}

	public final CompileOptions processors(List<? extends Processor> processors) {
		return new CompileOptions(processors, options);
	}

	public final CompileOptions processors(Processor... processors) {
		return processors(CollectionUtil.toList(processors));
	}

	public final CompileOptions options(List<String> options) {
		return new CompileOptions(processors, options);
	}

	public final CompileOptions options(String... options) {
		return options(CollectionUtil.toList(options));
	}

	public List<? extends Processor> getProcessors() {
		return processors;
	}

	public List<String> getOptions() {
		return options;
	}
}
