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

package org.spreadme.commons.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spreadme.commons.codec.Hex;
import org.spreadme.commons.lang.Nullable;
import org.spreadme.commons.lang.Randoms;
import org.spreadme.commons.util.CollectionUtil;

/**
 * 各种Hash算法
 * 性能优化，使用ThreadLocal的MessageDigest(from ElasticSearch)
 * 支持带salt并且进行迭代达到更高的安全性.
 * MD5的安全性较低, 只在文件Checksum时支持
 *
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class Digest {

	private Digest() {
	}

	private static final int BUFFER_LENGTH = 8 * 1024;

	// the support message digest algorithm
	private static final List<Algorithm> ALGORITHM;

	private static final Map<Algorithm, ThreadLocal<MessageDigest>> MESSAGEDIGEST;

	static {
		ALGORITHM = Collections.unmodifiableList(CollectionUtil.toList(
				Algorithm.SHA, Algorithm.SHA1, Algorithm.SHA224,
				Algorithm.SHA256, Algorithm.SHA384, Algorithm.SHA512,
				Algorithm.MD5
		));
		MESSAGEDIGEST = new HashMap<>(ALGORITHM.size());
		for (Algorithm algorithm : ALGORITHM) {
			MESSAGEDIGEST.put(algorithm, createMessageDigest(algorithm));
		}
	}

	/**
	 * 创建ThreadLocal的MessageDigest
	 *
	 * @param algorithm hash算法 {@link Algorithm}
	 * @return ThreadLocal MessageDIgest
	 */
	private static ThreadLocal<MessageDigest> createMessageDigest(Algorithm algorithm) {
		return ThreadLocal.withInitial(() -> {
			try {
				return MessageDigest.getInstance(algorithm.getValue());
			}
			catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	/**
	 * 判断该算法是否支持
	 *
	 * @param algorithm hash算法
	 * @return is support
	 */
	public static boolean isSupport(Algorithm algorithm) {
		return ALGORITHM.contains(algorithm);
	}

	/**
	 * 输入流进行Hash运算
	 *
	 * @param in InputStream {@link InputStream}
	 * @param algorithm hash算法 {@link Algorithm}
	 * @return hash值
	 * @throws IOException IOException
	 */
	public static byte[] get(InputStream in, Algorithm algorithm) throws IOException {
		byte[] buffer = new byte[BUFFER_LENGTH];
		int readed = in.read(buffer);
		MessageDigest digest = getMessageDigest(algorithm);
		while (readed > -1) {
			digest.update(buffer, 0, readed);
			readed = in.read(buffer, 0, BUFFER_LENGTH);
		}
		return digest.digest();
	}

	public static String toHexString(InputStream in, Algorithm algorithm) throws IOException {
		byte[] result = get(in, algorithm);
		return Hex.toHexString(result);
	}

	/**
	 * 对输入的字符串进行hash运算
	 *
	 * @param data data
	 * @param salt 盐值
	 * @param iterations 迭代次数
	 * @param algorithm hash算法
	 * @return hash值
	 */
	public static byte[] get(@Nullable byte[] data, byte[] salt, int iterations, Algorithm algorithm) {
		MessageDigest digest = getMessageDigest(algorithm);
		// 带盐
		if (salt != null) {
			digest.update(salt);
		}
		// 第一次散列
		byte[] result = digest.digest(data);
		// 如果迭代次数>1，进一步迭代散列
		for (int i = 1; i < iterations; i++) {
			digest.reset();
			result = digest.digest(result);
		}
		return result;
	}

	public static String toHexString(@Nullable byte[] data, byte[] salt, int iterations, Algorithm algorithm) {
		byte[] result = get(data, salt, iterations, algorithm);
		return Hex.toHexString(result);
	}

	/**
	 * 生成盐值
	 *
	 * @param length 长度
	 * @return 盐值
	 */
	public static byte[] generateSalt(int length) {
		byte[] bytes = new byte[length];
		Randoms.getSecureRandom().nextBytes(bytes);
		return bytes;
	}

	/**
	 * 获取MessageDigest
	 *
	 * @param algorithm 算法 {@link Algorithm}
	 * @return MessageDigest {@link MessageDigest}
	 */
	public static MessageDigest getMessageDigest(Algorithm algorithm) {
		if (!isSupport(algorithm)) {
			throw new IllegalArgumentException(String.format("can not support the algorithm %s to digest.", algorithm.name()));
		}
		ThreadLocal<MessageDigest> digestThreadLocal = MESSAGEDIGEST.get(algorithm);
		MessageDigest messageDigest = digestThreadLocal.get();
		messageDigest.reset();
		return messageDigest;
	}
}
