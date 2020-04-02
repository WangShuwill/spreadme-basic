/*
 * Copyright [4/1/20 9:49 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.spreadme.commons.system.SystemInfo;
import org.spreadme.commons.util.CollectionUtil;
import org.spreadme.commons.util.StringUtil;

/**
 * Compile
 * @author shuwei.wang
 */
public class Compile {

	public static final String CLASSPATH_OPTION = "-classpath";

	public static final String DEFINCLASS_METHOD_NAME = "defineClass";

	public static Class<?> compile(final String className, final String content, final CompileOptions compileOptions) {
		Lookup lookup = MethodHandles.lookup();
		ClassLoader classLoader = lookup.lookupClass().getClassLoader();

		try {
			return classLoader.loadClass(className);
		}
		catch (ClassNotFoundException ignore) {
			return doCompile(classLoader, className, content, compileOptions);
		}
	}

	private static Class<?> doCompile(final ClassLoader classLoader, final String className, final String content, final CompileOptions compileOptions) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try {
			ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));

			List<CharSequenceJavaFileObject> files = new ArrayList<>();
			files.add(new CharSequenceJavaFileObject(className, content));
			StringWriter out = new StringWriter();

			List<String> options = new ArrayList<>(compileOptions.getOptions());
			if (!options.contains(CLASSPATH_OPTION)) {
				options.addAll(CollectionUtil.toList(CLASSPATH_OPTION, getClassPath(classLoader)));
			}

			CompilationTask task = compiler.getTask(out, fileManager, null, options, null, files);
			if (CollectionUtil.isNotEmpty(compileOptions.getProcessors())) {
				task.setProcessors(compileOptions.getProcessors());
			}
			task.call();

			if (fileManager.isEmpty()) {
				throw new CompileException(String.format("Compilation error: %s", out));
			}

			return fileManager.loadAndReturnMainClass(className,
					(name, bytes) -> Reflect.of(classLoader).invoke(DEFINCLASS_METHOD_NAME, name, bytes, 0, bytes.length).get());

		}
		catch (Exception ex) {
			throw new CompileException("Error while compiling " + className, ex);
		}
	}

	private static String getClassPath(final ClassLoader classLoader) throws URISyntaxException {
		StringBuilder classPath = new StringBuilder();
		String separator = SystemInfo.FILE_SEPARATOR;
		String prop = SystemInfo.CLASS_PATH;
		if (StringUtil.isNotBlank(prop)) {
			classPath.append(prop);
		}
		if (classLoader instanceof URLClassLoader) {
			for (URL url : ((URLClassLoader) classLoader).getURLs()) {
				if (classPath.length() > 0) {
					classPath.append(separator);
				}
				if (Protocol.FILE.equals(url.getProtocol())) {
					classPath.append(new File(url.toURI()));
				}
			}
		}
		return classPath.toString();
	}

	static final class DefaultJavaFileObject extends SimpleJavaFileObject {

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		/**
		 * Construct a SimpleJavaFileObject of the given kind and with the
		 * given URI.
		 * @param name  the name for this file object
		 * @param kind the kind of this file object
		 */
		DefaultJavaFileObject(String name, Kind kind) {
			super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
		}

		byte[] getBytes() {
			return os.toByteArray();
		}

		@Override
		public OutputStream openOutputStream() {
			return os;
		}
	}

	static final class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

		private final Map<String, DefaultJavaFileObject> fileObjectMap;
		private Map<String, byte[]> classes;

		/**
		 * Creates a new instance of ForwardingJavaFileObject.
		 * @param fileObject delegate to this file object
		 */
		protected ClassFileManager(StandardJavaFileManager fileObject) {
			super(fileObject);
			fileObjectMap = new HashMap<>();
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
			DefaultJavaFileObject result = new DefaultJavaFileObject(className, kind);
			fileObjectMap.put(className, result);
			return result;
		}

		boolean isEmpty() {
			return fileObjectMap.isEmpty();
		}

		Map<String, byte[]> classes() {
			if (classes == null) {
				classes = new HashMap<>();
				fileObjectMap.forEach((key, value) -> classes.put(key, value.getBytes()));
			}
			return classes;
		}

		Class<?> loadAndReturnMainClass(String mainClassName, ThrowableBiFunction<String, byte[], Class<?>> definer) throws Exception {
			Class<?> result = null;
			for (Map.Entry<String, byte[]> entry : classes().entrySet()) {
				Class<?> clazz = definer.apply(entry.getKey(), entry.getValue());
				if (mainClassName.equals(entry.getKey())) {
					result = clazz;
				}
			}
			return result;
		}
	}

	static final class CharSequenceJavaFileObject extends SimpleJavaFileObject {

		final CharSequence content;

		/**
		 * Construct a SimpleJavaFileObject of the given kind and with the
		 * given URI.
		 *  @param className  the content for this file object
		 * @param content the content this file object
		 */
		protected CharSequenceJavaFileObject(String className, CharSequence content) {
			super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
			this.content = content;
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return content;
		}

	}

}
