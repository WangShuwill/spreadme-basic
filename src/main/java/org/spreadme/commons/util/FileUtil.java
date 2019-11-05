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

package org.spreadme.commons.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * files
 * @author shuwei.wang
 */
public abstract class FileUtil {

	public static List<File> getFiles(String path, FileFilter filter) {
		List<File> files = new ArrayList<>();
		try {
			Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
					File file = path.toFile();
					if (filter == null) {
						files.add(file);
					}
					if (filter != null && filter.accept(file)) {
						files.add(file);
					}
					return FileVisitResult.CONTINUE;
				}
			});
		}
		catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return files;
	}

	public static List<File> getFiles(String path) {
		return getFiles(path, null);
	}

	public static String getRelativePath(String rootPath, File file) {
		String filepath = file.getPath().replace("\\", "/");
		if (filepath.startsWith(rootPath)) {
			return filepath.substring(rootPath.length());
		}

		return null; //should not get here
	}

}
