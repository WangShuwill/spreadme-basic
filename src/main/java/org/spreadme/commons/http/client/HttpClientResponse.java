/*
 * Copyright [2/28/20 2:54 PM] [shuwei.wang (c) wswill@foxmail.com]
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

package org.spreadme.commons.http.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Http Client Response
 * @author shuwei.wang
 */
public interface HttpClientResponse extends HttpMessage, Closeable {

	/**
	 * Return the body of the message as an input stream.
	 * @return the input stream body (never {@code null})
	 * @throws IOException in case of I/O errors
	 */
	InputStream getBody() throws IOException;

	/**
	 * Return the HTTP status code (potentially non-standard and not
	 * resolvable through the as an integer.
	 * @return the HTTP status as an integer
	 * @throws IOException in case of I/O errors
	 */
	int getStatusCode() throws IOException;

	String getResponseMessage() throws IOException;
}
