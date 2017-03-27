package io.rapid;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import io.rapid.base.BaseTest;

import static junit.framework.Assert.assertEquals;


public class FilterJsonTest extends BaseTest {
	@Test
	public void test_model2json_1() throws Exception {
		String json = new FilterAnd(
				new FilterOr(
						new FilterValue("age", new FilterValue.IntComparePropertyValue(FilterValue.PropertyValue.TYPE_LESS_THAN, 18)),
						new FilterValue("age", new FilterValue.IntComparePropertyValue(FilterValue.PropertyValue.TYPE_GREATER_THAN, 60))
				),
				new FilterValue("name", new FilterValue.StringComparePropertyValue(FilterValue.PropertyValue.TYPE_EQUAL, "John"))
		).toJson();

		JSONAssert.assertEquals(json, "{\"and\":[{\"or\":[{\"age\":{\"lt\":18}},{\"age\":{\"gt\":60}}]},{\"name\":\"John\"}]}", false);
	}


	@Test
	public void test_query2json_1() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.equalTo("type", "SUV")
				.beginOr()
				.greaterOrEqualThan("size", 3)
				.lessThan("size", 2)
				.beginAnd()
				.beginOr()
				.equalTo("open", "24/7")
				.lessOrEqualThan("time", 3)
				.endOr()
				.equalTo("transmission", "automatic")
				.endAnd()
				.equalTo("enabled", true)
				.endOr()
				.skip(10)
				.limit(50)
				.orderBy("type")
				.orderBy("price", Sorting.DESC)
				.lessOrEqualThan("test", 123);


		String json = "{\"and\":[{\"type\":\"SUV\"},{\"or\":[{\"size\":{\"gte\":3}},{\"size\":{\"lt\":2}},{\"and\":[{\"or\":[{\"open\":\"24/7\"},{\"time\":{\"lte\":3}}]},{\"transmission\":\"automatic\"}]},{\"enabled\":true}]},{\"test\":{\"lte\":123}}]}";
		JSONAssert.assertEquals(
				collection.getFilter().toJson(),
				json,
				false
		);
		assertEquals(collection.getLimit(), 50);
		assertEquals(collection.getSkip(), 10);
		JSONAssert.assertEquals(collection.getOrder().toJson().toString(), "[{\"type\":\"asc\"},{\"price\":\"desc\"}]", false);
	}


	@Test
	public void test_query2json_2() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.lessOrEqualThan("mileage", 34.4)
				.beginAnd()
				.between("price", 321212.3, 1213123.2)
				.equalTo("size", 121)
				.endAnd();

		String json = "{\"and\":[{\"mileage\":{\"lte\":34.4}},{\"and\":[{\"and\":[{\"price\":{\"gte\":321212.3}},{\"price\":{\"lte\":1213123.2}}]},{\"size\":\"121\"}]}]}";
		JSONAssert.assertEquals(collection.getFilter().toJson(), json, false);
	}


	@Test(expected = IllegalArgumentException.class)
	public void test_query2json_3() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.beginAnd()
				.beginAnd()
				.beginAnd()
				.beginAnd()
				.endAnd();
		collection.getFilter();
	}


	@Test(expected = IllegalArgumentException.class)
	public void test_query2json_4() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.beginAnd()
				.endOr();
		collection.getFilter();
	}


	@Test
	public void test_query2json_7() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.beginAnd()
				.between("price", 3, 5.9)
				.endAnd();

		String json = "{\"and\":[{\"and\":[{\"and\":[{\"price\":{\"gte\":3}},{\"price\":{\"lte\":5.9}}]}]}]}";
		JSONAssert.assertEquals(collection.getFilter().toJson(), json, false);
	}


	@Test
	public void test_query2json_8() throws Exception {
		RapidCollectionReference<Object> collection = new RapidCollectionReference<>(new MockCollectionConnection<>(), "collection");
		collection
				.beginOr()
				.equalTo("model", "A5")
				.equalTo("model", "A7")
				.endOr()
				.beginAnd()
				.lessOrEqualThan("price", 10000)
				.lessOrEqualThan("hp", 400)
				.endAnd();

		String json = "{\"and\":[{\"or\":[{\"model\":\"A5\"},{\"model\":\"A7\"}]},{\"and\":[{\"price\":{\"lte\":10000}},{\"hp\":{\"lte\":400}}]}]}";
		JSONAssert.assertEquals(collection.getFilter().toJson(), json, false);
	}


}