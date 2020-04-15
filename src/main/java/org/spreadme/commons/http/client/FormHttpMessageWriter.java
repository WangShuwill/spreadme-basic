/*
 * Copyright [3/26/20 3:14 PM] [shuwei.wang (c) wswill@foxmail.com]
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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.spreadme.commons.http.HttpParam;
import org.spreadme.commons.lang.AbstractResource;
import org.spreadme.commons.util.IOUtil;

/**
 * Form Http Message Writer
 * @author shuwei.wang
 */
public class FormHttpMessageWriter implements HttpMessageWriter {

	private HttpParam param;
	private Charset charset;
	private byte[] boundary;

	public FormHttpMessageWriter(HttpParam param, Charset charset, String boundary) {
		this.param = param;
		this.charset = charset;
		this.boundary = boundary.getBytes(charset);
	}

	@Override
	public void write(OutputStream out) throws IOException {
		this.wirteParam(this.param.getParams(), this.boundary, out, this.charset);
		this.writeMultipart(this.param.getMultipart(), this.boundary, out, this.charset);
		this.writeNewLine(out);
		this.writeEnd(out, this.boundary);
	}

	private void wirteParam(Map<String, String> params, byte[] boundary, OutputStream out, Charset charset) throws IOException {
		boolean firstParam = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			byte[] paramBytes = this.getText(entry.getKey(), entry.getValue(), this.charset);
			if (firstParam) {
				this.writeBoundary(out, boundary);
				firstParam = false;
			}
			else {
				this.writeNewLine(out);
				this.writeBoundary(out, boundary);
			}
			out.write(paramBytes);
		}
	}

	private void writeMultipart(Map<String, AbstractResource> multipat, byte[] boundary, OutputStream out, Charset charset) throws IOException {
		for (Map.Entry<String, AbstractResource> entry : multipat.entrySet()) {
			this.writeNewLine(out);
			this.writeBoundary(out, boundary);
			byte[] multipartBytes = this.getMultipart(entry.getKey(), entry.getValue(), this.charset);
			out.write(multipartBytes);
			IOUtil.copy(entry.getValue().getInputStream(), out);
		}
	}

	private byte[] getText(String key, String value, Charset charset) {
		StringBuilder builder = new StringBuilder();
		builder.append("Content-Disposition:form-data;name=\"");
		builder.append(key);
		builder.append("\"\r\nContent-Type:text/plain\r\n\r\n");
		builder.append(value);
		return builder.toString().getBytes(charset);
	}

	private byte[] getMultipart(String key, AbstractResource resource, Charset charset) {
		StringBuilder builder = new StringBuilder();
		builder.append("Content-Disposition:form-data;name=\"");
		builder.append(key);
		builder.append("\";filename=\"");
		builder.append(resource.getName());
		builder.append("\"\r\nContent-Type:");
		builder.append(resource.getContentType().getType());
		builder.append("\r\n\r\n");
		return builder.toString().getBytes(charset);
	}

	private void writeBoundary(OutputStream os, byte[] boundary) throws IOException {
		os.write('-');
		os.write('-');
		os.write(boundary);
		writeNewLine(os);
	}

	private void writeEnd(OutputStream os, byte[] boundary) throws IOException {
		os.write('-');
		os.write('-');
		os.write(boundary);
		os.write('-');
		os.write('-');
		writeNewLine(os);
	}

	private void writeNewLine(OutputStream os) throws IOException {
		os.write('\r');
		os.write('\n');
	}
}
