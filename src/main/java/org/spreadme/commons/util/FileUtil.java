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

import org.spreadme.commons.system.SystemInfo;

/**
 * file util
 * @author shuwei.wang
 */
public abstract class FileUtil {

	private static final int NOT_FOUND = -1;

	public static final char EXTENSION_SEPARATOR = '.';

	public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);

	public static final String UNIX_SEPARATOR = "/";

	public static final String WINDOWS_SEPARATOR = "\\";

	private static final String OTHER_SEPARATOR;

	static {
		if (isSystemWindows()) {
			OTHER_SEPARATOR = UNIX_SEPARATOR;
		}
		else {
			OTHER_SEPARATOR = WINDOWS_SEPARATOR;
		}
	}

	static boolean isSystemWindows() {
		return SystemInfo.FILE_SEPARATOR.equals(WINDOWS_SEPARATOR);
	}

	/**
	 * 创建文件或者文件夹
	 *
	 * @param path 路径
	 * @param isFile 是否为文件
	 * @return File
	 * @throws IOException IOException
	 */
	public static File createFile(String path, boolean isFile) throws IOException {
		Path filePath = Paths.get(path);
		filePath = filePath.normalize();
		if (!Files.exists(filePath)) {
			if (isFile) {
				Path dirPath = filePath.getParent();
				Files.createDirectories(dirPath);
				Files.createFile(filePath);
				return filePath.toFile();
			}
			Files.createDirectories(filePath);
		}
		return filePath.toFile();
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
	 * Returns the index of the last extension separator character, which is a dot.
	 *
	 * @param fileName fileName
	 * @return index
	 */
	public static int indexOfExtension(String fileName) {
		if (StringUtil.isBlank(fileName)) {
			return NOT_FOUND;
		}
		if (isSystemWindows()) {
			// Special handling for NTFS ADS: Don't accept colon in the fileName.
			final int offset = fileName.indexOf(':', getAdsCriticalOffset(fileName));
			if (offset != -1) {
				throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
			}
		}
		final int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
		final int lastUnixPos = fileName.lastIndexOf(UNIX_SEPARATOR);
		final int lastWindowsPos = fileName.lastIndexOf(WINDOWS_SEPARATOR);
		final int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
		return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
	}

	/**
	 * get file extension
	 *
	 * @param fileName fileName
	 * @return fileName extension
	 */
	public static String getExtension(String fileName) {
		if (StringUtil.isBlank(fileName)) {
			return null;
		}
		final int index = indexOfExtension(fileName);
		if (index == NOT_FOUND) {
			return StringUtil.EMPTY;
		}
		return fileName.substring(index + 1);
	}

	/**
	 * Special handling for NTFS ADS: Don't accept colon in the fileName.
	 *  from apache-commons-io
	 *
	 * @param fileName a file name
	 * @return ADS offsets.
	 */
	private static int getAdsCriticalOffset(final String fileName) {
		// Step 1: Remove leading path segments.
		final int offset1 = fileName.lastIndexOf(SystemInfo.FILE_SEPARATOR);
		final int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
		if (offset1 == -1) {
			if (offset2 == -1) {
				return 0;
			}
			return offset2 + 1;
		}
		if (offset2 == -1) {
			return offset1 + 1;
		}
		return Math.max(offset1, offset2) + 1;
	}

	/**
	 * normalize path
	 *
	 * @param path path
	 * @return normalize path
	 */
	public static String normalize(String path) {
		Path filePath = Paths.get(path);
		filePath = filePath.normalize();
		return filePath.toString();
	}
}
