package org.spreadme.commons.http;

import java.io.Serializable;
import java.net.URL;

import org.spreadme.commons.util.StringUtil;

public class HttpUrl implements Serializable{

	private static final long serialVersionUID = 1L;

	private final String protocol;
	private final String host;
	private final int port;
	private final String path;
	
	private HttpUrl(String protocol, String host, int port, String path) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	public static HttpUrl toHttpUrl(URL url) {
		final String protocol = url.getProtocol();
		final String host = url.getHost();
		final int port = url.getPort();
		final String path = url.getPath();
		return new HttpUrl(protocol, host, port, path);
	}
	
	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		String url = this.protocol + "://" + this.host;
		if(this.port != -1) {
			url = url + ":" + this.port;
		}
		if(StringUtil.isNotBlank(this.path)) {
			url = url + this.path;
		}
		return url;
	}
}
