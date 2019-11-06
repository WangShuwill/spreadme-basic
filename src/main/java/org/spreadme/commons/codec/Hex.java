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
 * hex
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class Hex {

	private static final Encoder encoder = new HexEncoder();

	/**
	 * hex to string
	 *
	 * @param data data
	 * @return hex string
	 */
	public static String toHexString(byte[] data) {
		return toHexString(data, 0, data.length);
	}

	/**
	 * hex to string
	 *
	 * @param data data
	 * @param off off
	 * @param length length
	 * @return hex string
	 */
	public static String toHexString(byte[] data, int off, int length) {
		byte[] encoded = encode(data, off, length);
		return StringUtil.fromByteArray(encoded);
	}

	/**
	 * encode the input data producing a Hex encoded byte array.
	 *
	 * @param data data
	 * @return a byte array containing the Hex encoded data.
	 */
	public static byte[] encode(byte[] data) {
		return encode(data, 0, data.length);
	}

	/**
	 * encode the input data producing a Hex encoded byte array.
	 *
	 * @param data data
	 * @param off off
	 * @param length length
	 * @return a byte array containing the Hex encoded data.
	 */
	public static byte[] encode(byte[] data, int off, int length) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			encoder.encode(data, off, length, out);
		}
		catch (IOException e) {
			throw new EncoderException("exception encoding Hex string: " + e.getMessage(), e);
		}
		return out.toByteArray();
	}

	/**
	 * Hex encode the byte data writing it to the given output stream.
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
	 * Hex encode the byte data writing it to the given output stream.
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
	 * decode the Hex encoded input data. It is assumed the input data is valid.
	 *
	 * @param data data
	 * @return a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			encoder.decode(data, 0, data.length, out);
		}
		catch (Exception e) {
			throw new DecoderException("exception decoding Hex data: " + e.getMessage(), e);
		}
		return out.toByteArray();
	}

	/**
	 * decode the Hex encoded String data - whitespace will be ignored.
	 *
	 * @param data data
	 * @return a byte array representing the decoded data.
	 */
	public static byte[] decode(String data) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try {
			encoder.decode(data, bOut);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("exception decoding Hex string: " + e.getMessage(), e);
		}
		return bOut.toByteArray();
	}

	/**
	 * decode the Hex encoded String data writing it to the given output stream,
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
}
