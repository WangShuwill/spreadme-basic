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

package org.spreadme.commons.crypt;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.spreadme.commons.lang.Randoms;

/**
 * KeyGen
 * @author shuwei.wang
 */
public abstract class KeyGen {

	public static byte[] generateKey(int size, Algorithm algorithm) throws NoSuchAlgorithmException {
		KeyGenerator kegGen = KeyGenerator.getInstance(algorithm.getValue());
		kegGen.init(size, Randoms.getSecureRandom());
		SecretKey secretKey = kegGen.generateKey();
		return secretKey.getEncoded();
	}

	public static byte[] extendKey(byte[] key) {
		key = Digest.toHexString(key, null, 0, Algorithm.MD5).getBytes();
		key = Arrays.copyOf(key, key.length / 2);
		return key;
	}
}
