/*
 * Copyright [4/1/20 10:43 PM] [shuwei.wang (c) wswill@foxmail.com]
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

/**
 * Compile Exception
 * @author shuwei.wang
 */
public class CompileException extends RuntimeException {

	private static final long serialVersionUID = 3824980821083838187L;

	public CompileException(String message) {
		super(message);
	}

	public CompileException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompileException() {
		super();
	}

	public CompileException(Throwable cause) {
		super(cause);
	}
}
