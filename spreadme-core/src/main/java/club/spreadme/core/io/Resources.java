/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core.io;

import java.io.File;

public interface Resources {

	String TEMP_PATH = System.getProperty("java.io.tmpdir");
	String USER_PATH = System.getProperty("user.home");

	boolean exists();

	ResourceMetadata getMetadate();

	static String getTempPath() {
		return TEMP_PATH;
	}

	static File getTempFile(){
		return new File(getTempPath());
	}

	static  String getUserPath(){
		return USER_PATH;
	}

	static File getUserFile(){
		return new File(getUserPath());
	}
}
