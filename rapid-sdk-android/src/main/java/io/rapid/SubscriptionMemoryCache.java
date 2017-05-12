package io.rapid;


import android.util.LruCache;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


class SubscriptionMemoryCache<T> {

	private LruCache<String, List<RapidDocument<T>>> mCache;
	private boolean mEnabled = true;


	SubscriptionMemoryCache(int maxEntries) {
		mCache = new LruCache<>(maxEntries);
	}


	public void setMaxSize(int maxEntries) {
		mCache.resize(maxEntries);
	}


	public synchronized List<RapidDocument<T>> get(Subscription subscription) throws IOException, JSONException, NoSuchAlgorithmException {
		if(!mEnabled)
			return null;
		String fingerprint = subscription.getFingerprint();
		Logcat.d("Reading from in-memory subscription cache. key: %s", fingerprint);
		return mCache.get(fingerprint);
	}


	public synchronized void put(Subscription subscription, List<RapidDocument<T>> value) throws IOException, JSONException, NoSuchAlgorithmException {
		if(!mEnabled)
			return;
		String fingerprint = subscription.getFingerprint();
		Logcat.d("Saving to in-memory subscription cache. key: %s", fingerprint);
		mCache.put(fingerprint, value);
	}


	public synchronized void clear() throws IOException {
		mCache.evictAll();
	}


	synchronized void remove(Subscription subscription) throws IOException, NoSuchAlgorithmException, JSONException {
		if(!mEnabled)
			return;
		String fingerprint = subscription.getFingerprint();
		Logcat.d("Removing from in-memory subscription cache. key: %s", fingerprint);
		mCache.remove(fingerprint);
	}


	public void setEnabled(boolean cachingEnabled) {
		mEnabled = cachingEnabled;
	}
}