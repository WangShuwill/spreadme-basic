/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.natives;

import java.io.File;
import java.io.IOException;

/**
 * JniExtractor
 * @author shuwei.wang
 */
public interface JniExtractor {

	/**
	 * Extract a JNI library from the classpath to a temporary file.
	 *
	 * @param libPath library path
	 * @param libname System.loadLibrary() compatible library name
	 * @return the extracted file
	 * @throws IOException when extracting the desired file failed
	 */
	File extractJni(String libPath, String libname) throws IOException;

	/**
	 * Extract all libraries which are registered for auto-extraction to files in
	 * the temporary directory.
	 *
	 * @throws IOException when extracting the desired file failed
	 */
	void extractRegistered() throws IOException;
}
