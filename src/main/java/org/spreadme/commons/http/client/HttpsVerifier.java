package org.spreadme.commons.http.client;

import javax.net.ssl.HttpsURLConnection;

public interface HttpsVerifier {

	HttpsURLConnection verify(HttpsURLConnection connection);
}
