package io.rapid;


import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.rapid.converter.RapidJsonConverter;


class InMemoryCollectionConnection<T> implements CollectionConnection<T> {
	private final RapidJsonConverter mJsonConverter;
	private final Class<T> mType;
	Map<String, String> mDb = new HashMap<>();
	Set<RapidSubscription<T>> mSubscriptions = new HashSet<>();


	public InMemoryCollectionConnection(Class<T> type, RapidJsonConverter jsonConverter) {
		mJsonConverter = jsonConverter;
		mType = type;
	}


	@Override
	public RapidFuture<T> mutate(String id, T value) {
		RapidFuture<T> future = new RapidFuture<>();
		delayOperation(() -> {
			mDb.put(id, toJson(value));
			future.invokeSuccess();
			notifyChange();
		});
		return future;
	}


	@Override
	public RapidSubscription subscribeDocument(RapidDocumentCallback<T> callback) {
		//TODO
		return new RapidSubscription(null);
	}


	@Override
	public void onValue(MessageVal valMessage) {

	}


	@Override
	public void onUpdate(MessageUpd updMessage) {

	}


	@Override
	public RapidSubscription<T> subscribe(RapidCollectionCallback<T> callback) {
		RapidSubscription<T> subscription = new RapidSubscription<>(callback);
		mSubscriptions.add(subscription);
		subscription.setOnUnsubscribeCallback(() -> mSubscriptions.remove(subscription));
		return subscription;
	}


	private String toJson(T object) {
		try {
			return mJsonConverter.toJson(object);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	private T fromJson(String json) {
		try {
			return mJsonConverter.fromJson(json, mType);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	private void notifyChange() {
		for(RapidSubscription<T> subscription : mSubscriptions) {
			List<RapidDocument<T>> objects = new ArrayList<>();
			for(Map.Entry<String, String> entry : mDb.entrySet()) {
				objects.add(new RapidDocument<T>(entry.getKey(), fromJson(entry.getValue())));
			}
			subscription.invokeChange(objects);
		}
	}


	private String getNewKey() {
		return UUID.randomUUID().toString();
	}


	private void delayOperation(Runnable runnable) {
		new Handler().postDelayed(runnable, 200);
	}
}