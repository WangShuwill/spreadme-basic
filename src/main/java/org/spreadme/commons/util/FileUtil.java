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
 * file util
 * @author shuwei.wang
 */
public abstract class FileUtil {

	/**
	 * 创建文件或者文件夹
	 *
	 * @param path 路径
	 * @param isFile 是否为文件
	 * @return File
	 * @throws IOException IOException
	 */
	public static File createFile(String path, boolean isFile) throws IOException {
		path = path.replaceAll("\\*", "/");
		File file = new File(path);
		if (!file.exists()) {
			if (isFile) {
				file.getParentFile().mkdirs();
				file.createNewFile();
				return file;
			}
			else if (file.mkdirs()) {
				return file;
			}
		}
		return file;
	}

	/**
	 * 遍历文件
	 *
	 * @param path 路径
	 * @param filter 过滤器 {@link FileFilter}
	 * @return 文件列表
	 * @throws IOException IOException
	 */
	public static List<File> getFiles(String path, FileFilter filter) throws IOException {
		List<File> files = new ArrayList<>();
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
		return files;
	}

	/**
	 * 遍历文件
	 *
	 * @param path 路径
	 * @return 文件列表
	 * @throws IOException IOException
	 */
	public static List<File> getFiles(String path) throws IOException {
		return getFiles(path, null);
	}

	/**
	 * 获取相对路径
	 *
	 * @param rootPath 跟路径
	 * @param file 文件
	 * @return 相对路径
	 */
	public static String getRelativePath(String rootPath, File file) {
		String filepath = file.getPath().replace("\\", "/");
		if (filepath.startsWith(rootPath)) {
			return filepath.substring(rootPath.length());
		}

		return null;
	}

	/**
	 * 获取临时文件目录
	 *
	 * @return temp dir
	 */
	public static File getTempDir() {
		return new File(System.getProperty("java.io.tmpdir"));
	}
}
