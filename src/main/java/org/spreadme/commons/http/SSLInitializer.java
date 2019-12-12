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

package org.spreadme.commons.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSL Initializer
 * @author shuwei.wang
 */
public class SSLInitializer {

	private SSLContext sslcontext;

	private HostnameVerifier allHostsValid;

	public SSLInitializer() {
		initContext();
	}

	private void initContext() {
		TrustManager trustAllManager = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] cert, String arg1) {
			}

			public void checkServerTrusted(X509Certificate[] cert, String arg1) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] {trustAllManager}, null);
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		// Create all-trusting host name verifier
		allHostsValid = (hostname, session) -> true;
	}

	public HttpsURLConnection init(HttpsURLConnection connection) {
		connection.setSSLSocketFactory(sslcontext.getSocketFactory());
		connection.setHostnameVerifier(allHostsValid);
		return connection;
	}
}
