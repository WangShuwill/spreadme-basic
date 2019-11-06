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

package org.spreadme.commons.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.spreadme.commons.util.StringUtil;

/**
 * Base64
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class Base64 {

	private static final Encoder encoder = new Base64Encoder();

	public static String toBase64String(byte[] data) {
		return toBase64String(data, 0, data.length);
	}

	public static String toBase64String(byte[] data, int off, int length) {
		byte[] encoded = encode(data, off, length);
		return StringUtil.fromByteArray(encoded);
	}

	/**
	 * encode the input data producing a base 64 encoded byte array.
	 *
	 * @param data data
	 * @return a byte array containing the base 64 encoded data.
	 */
	public static byte[] encode(byte[] data) {
		return encode(data, 0, data.length);
	}

	/**
	 * encode the input data producing a base 64 encoded byte array.
	 *
	 * @param data data
	 * @param off off
	 * @param length length
	 * @return a byte array containing the base 64 encoded data.
	 */
	public static byte[] encode(byte[] data, int off, int length) {
		int len = (length + 2) / 3 * 4;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
		try {
			encoder.encode(data, off, length, bOut);
		}
		catch (Exception e) {
			throw new EncoderException("exception encoding base64 string: " + e.getMessage(), e);
		}
		return bOut.toByteArray();
	}

	/**
	 * Encode the byte data to base 64 writing it to the given output stream.
	 *
	 * @param data data
	 * @param out result
	 * @return the number of bytes produced.
	 * @throws IOException IOException
	 */
	public static int encode(byte[] data, OutputStream out) throws IOException {
		return encoder.encode(data, 0, data.length, out);
	}

	/**
	 * Encode the byte data to base 64 writing it to the given output stream.
	 *
	 * @param data data
	 * @param off off
	 * @param length length
	 * @param out result
	 * @return the number of bytes produced.
	 * @throws IOException IOException
	 */
	public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
		return encoder.encode(data, off, length, out);
	}

	/**
	 * decode the base 64 encoded input data. It is assumed the input data is valid.
	 *
	 * @param data data
	 * @return a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data) {
		int len = data.length / 4 * 3;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
		try {
			encoder.decode(data, 0, data.length, bOut);
		}
		catch (Exception e) {
			throw new DecoderException("unable to decode base64 data: " + e.getMessage(), e);
		}
		return bOut.toByteArray();
	}

	/**
	 * decode the base 64 encoded String data - whitespace will be ignored.
	 *
	 * @param data data
	 * @return a byte array representing the decoded data.
	 */
	public static byte[] decode(String data) {
		int len = data.length() / 4 * 3;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);
		try {
			encoder.decode(data, bOut);
		}
		catch (Exception e) {
			throw new DecoderException("unable to decode base64 string: " + e.getMessage(), e);
		}
		return bOut.toByteArray();
	}

	/**
	 * decode the base 64 encoded String data writing it to the given output stream,
	 * whitespace characters will be ignored.
	 *
	 * @param data data
	 * @param out result
	 * @return the number of bytes produced.
	 * @throws IOException IOException
	 */
	public static int decode(String data, OutputStream out) throws IOException {
		return encoder.decode(data, out);
	}

	/**
	 * Decode to an output stream;
	 *
	 * @param base64Data       The source data.
	 * @param start            Start position.
	 * @param length           the length.
	 * @param out The output stream to write to.
	 * @return the number of bytes produced.
	 */
	public static int decode(byte[] base64Data, int start, int length, OutputStream out) {
		try {
			return encoder.decode(base64Data, start, length, out);
		}
		catch (Exception e) {
			throw new DecoderException("unable to decode base64 data: " + e.getMessage(), e);
		}

	}
}
