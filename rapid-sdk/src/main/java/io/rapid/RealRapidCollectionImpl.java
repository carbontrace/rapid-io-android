package io.rapid;


import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rapid.converter.RapidJsonConverter;


class RealRapidCollectionImpl<T> implements RapidCollectionImpl<T> {

	String mCollectionName;
	Rapid mRapid;
	RapidJsonConverter mRapidJsonConverter;
	Class<T> mType;
	Set<RapidSubscription<T>> mSubscriptions = new HashSet<>();

	List<RapidWrapper<T>> mCollection = new ArrayList<>();


	public RealRapidCollectionImpl(String collectionName, Rapid rapid, RapidJsonConverter rapidJsonConverter, Class<T> type) {
		mCollectionName = collectionName;
		mRapid = rapid;
		mRapidJsonConverter = rapidJsonConverter;
		mType = type;
	}


	@Override
	public RapidFuture<T> set(String key, T value) {
		RapidFuture<T> future = new RapidFuture<>();
		RapidWrapper<T> wrapper = new RapidWrapper<T>(key, value);

		mRapid.sendMessage(new MessageMut(IdProvider.getNewEventId(), mCollectionName, toJson(wrapper)));

		return future;
	}


	@Override
	public RapidSubscription subscribe(RapidCollectionCallback<T> callback) {
		mRapid.sendMessage(new MessageSub(IdProvider.getNewEventId(), mCollectionName, IdProvider.getNewSubscriptionId()));


		RapidSubscription<T> subscription = new RapidSubscription<>(callback);
		mSubscriptions.add(subscription);
		subscription.setOnUnsubscribeCallback(() -> mSubscriptions.remove(subscription));
		return subscription;
	}


	@Override
	public RapidSubscription subscribeDocument(RapidDocumentCallback<T> callback) {
		// TODO
		return null;
	}


	@Override
	public void onValue(MessageVal valMessage) {
		mCollection = parseList(valMessage.getDocuments());
		notifyChange();
	}


	@Override
	public void onUpdate(MessageUpd updMessage) {
		RapidWrapper<T> doc = parseDocument(updMessage.getDocument());
		boolean modified = false;
		for(int i = 0; i < mCollection.size(); i++) {
			if(mCollection.get(i).getId().equals(doc.getId())) {
				mCollection.set(i, doc);
				modified = true;
				break;
			}
		}
		if(!modified) {
			mCollection.add(doc);
		}
		notifyChange();
	}


	private String toJson(RapidWrapper<T> wrapper) {
		try {
			return mRapidJsonConverter.toJson(wrapper);
		} catch(IOException e) {
			e.printStackTrace();
			// TODO: handle
			return null;
		}
	}


	private void notifyChange() {
		for(RapidSubscription<T> subscription : mSubscriptions) {
			subscription.invokeChange(new ArrayList<>(mCollection));
		}
	}


	private List<RapidWrapper<T>> parseList(String documents) {
		Type t = new TypeToken<List<RapidWrapper<T>>>() {}.getType();
		try {
			return mRapidJsonConverter.fromJson(documents, t);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	private RapidWrapper<T> parseDocument(String document) {
		Type t = new TypeToken<RapidWrapper<T>>() {}.getType();
		try {
			return mRapidJsonConverter.fromJson(document, t);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
