package io.rapid;

import android.text.format.DateUtils;


class Config {
	public static final int DEFAULT_LIMIT = 100;
	public static final String ID_IDENTIFIER = "$id";
	public static final long CONNECTION_TIMEOUT = 30 * DateUtils.SECOND_IN_MILLIS;
	public static final long CONNECTION_RETRY_PERIOD = 5 * DateUtils.SECOND_IN_MILLIS;
	public static final long MESSAGE_TIMEOUT = 30 * DateUtils.SECOND_IN_MILLIS;
	public static final long HB_PERIOD = 10 * DateUtils.SECOND_IN_MILLIS;
	public static final int CHECKER_HANDLER_PERIOD = 10 * 1000;
	public static final String API_KEY_METADATA = "io.rapid.apikey";
	public static final int CACHE_DEFAULT_SIZE_MB = 50;
	public static final long WEBSOCKET_DISCONNECT_TIMEOUT = 30 * DateUtils.SECOND_IN_MILLIS;
}
