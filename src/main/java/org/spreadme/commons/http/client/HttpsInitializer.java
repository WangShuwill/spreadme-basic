package org.spreadme.commons.http.client;

import javax.net.ssl.HttpsURLConnection;

public interface HttpsInitializer {

	HttpsURLConnection init(HttpsURLConnection connection);
}
