package io.rapid;


public class RapidCollection<T> {

	private final String mCollectionName;
	private CollectionConnection<T> mConnection;


	public RapidCollection(Rapid rapid, String collectionName, Class<T> type) {
		mCollectionName = collectionName;
		mConnection = new RapidCollectionConnection<>(rapid, collectionName, type);
	}


	// Query methods
	public RapidCollection<T> equalTo(String property, String value) {
		return this;
	}


	public RapidCollection<T> not(String property, String value) {
		return this;
	}


	public RapidCollection<T> lessThan(String property, String value) {
		return this;
	}


	public RapidCollection<T> greaterThan(String property, String value) {
		return this;
	}


	public RapidCollection<T> beginGroup() {
		return this;
	}


	public RapidCollection<T> endGroup() {
		return this;
	}


	public RapidCollection<T> or() {
		return this;
	}


	public RapidCollection<T> between(String property, int from, int to) {
		return this;
	}


	public RapidCollection<T> limit(int limit) {
		return this;
	}


	public RapidCollection<T> skip(int skip) {
		return this;
	}


	public RapidCollection<T> first() {
		return limit(1);
	}


	public RapidCollection<T> orderBy(String property, Sorting sorting) {
		return this;
	}


	public RapidCollection<T> orderBy(String property) {
		return orderBy(property, Sorting.ASC);
	}


	public RapidSubscription subscribe(RapidCollectionCallback<T> callback) {
		return mConnection.subscribe(callback);
	}


	public RapidDocument<T> newDocument() {
		return new RapidDocument<>(mCollectionName, mConnection);
	}


	public RapidDocument<T> document(String documentId) {
		return new RapidDocument<>(mCollectionName, mConnection, documentId);
	}


	void onValue(MessageVal valMessage) {
		mConnection.onValue(valMessage);
	}


	void onUpdate(MessageUpd updMessage) {
		mConnection.onUpdate(updMessage);
	}
}
