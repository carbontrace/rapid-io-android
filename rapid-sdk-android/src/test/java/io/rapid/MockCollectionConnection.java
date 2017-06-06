package io.rapid;


class MockCollectionConnection<T> implements CollectionConnection<T> {

	@Override
	public RapidFuture mutate(String id, T value, RapidMutateOptions options) {
		return null;
	}


	@Override
	public void subscribe(Subscription<T> subscription) {

	}


	@Override
	public void onValue(String subscriptionId, String documents) {

	}


	@Override
	public void onFetchResult(String fetchId, String documentsJson) {

	}


	@Override
	public void onUpdate(String subscriptionId, String document) {

	}


	@Override
	public void onError(String subscriptionId, RapidError error) {

	}


	@Override
	public void onTimedOut() {

	}


	@Override
	public boolean hasActiveSubscription() {
		return false;
	}


	@Override
	public void resubscribe() {

	}


	@Override
	public void onRemove(String subscriptionId, String document) {

	}


	@Override
	public void fetch(Subscription<T> subscription) {

	}
}
