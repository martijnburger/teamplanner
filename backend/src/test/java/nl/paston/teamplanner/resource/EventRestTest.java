package nl.paston.teamplanner.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

/**
 * Runs against PostgreSQL and Elasticsearch started by Dev Services, with the data from import.sql.
 * Tests that change data create their own events, so they do not depend on each other.
 */
@QuarkusTest
class EventRestTest {

    private static final String API = "/api/v1.0";

    private static long createEvent(String name) {
        String location = given().contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"date\":\"2026-10-04\",\"planned\":false}")
                .when().post(API + "/events")
                .then().statusCode(201)
                .extract().header("Location");
        return Long.parseLong(location.substring(location.lastIndexOf('/') + 1));
    }

    private static String createMember(long eventId, String name) {
        return given().contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"available\":\"WAITING\",\"planned\":\"WAITING\"}")
                .when().post(API + "/events/" + eventId + "/members")
                .then().statusCode(201)
                .extract().header("Location");
    }

    private static long eventCount() {
        return given().when().get(API + "/events?pageSize=100")
                .then().statusCode(200)
                .extract().jsonPath().getLong("count");
    }

    // Issue #71: PUT and PATCH

    @Test
    void putReplacesTheEventInsteadOfCreatingANewOne() {
        long id = createEvent("Put");
        long before = eventCount();

        given().contentType(ContentType.JSON)
                .body("{\"name\":\"Put renamed\",\"date\":\"2026-10-05\",\"planned\":true}")
                .when().put(API + "/events/" + id)
                .then().statusCode(200)
                .body("id", equalTo((int) id))
                .body("name", equalTo("Put renamed"));

        given().when().get(API + "/events/" + id)
                .then().statusCode(200)
                .body("name", equalTo("Put renamed"))
                .body("date", equalTo("2026-10-05"))
                .body("planned", equalTo(true));
        assertEquals(before, eventCount());
    }

    @Test
    void putKeepsTheMembersOfTheEvent() {
        long id = createEvent("Put members");
        createMember(id, "Stays");

        given().contentType(ContentType.JSON)
                .body("{\"name\":\"Put members\",\"date\":\"2026-10-04\",\"planned\":true}")
                .when().put(API + "/events/" + id)
                .then().statusCode(200);

        given().when().get(API + "/events/" + id + "/members")
                .then().statusCode(200)
                .body("name", contains("Stays"));
    }

    @Test
    void patchOnlyChangesTheGivenFields() {
        long id = createEvent("Patch");
        long before = eventCount();

        given().contentType(ContentType.JSON)
                .body("{\"planned\":true}")
                .when().patch(API + "/events/" + id)
                .then().statusCode(200);

        given().when().get(API + "/events/" + id)
                .then().statusCode(200)
                .body("name", equalTo("Patch"))
                .body("date", equalTo("2026-10-04"))
                .body("planned", equalTo(true));
        assertEquals(before, eventCount());
    }

    @Test
    void patchUpdatesAMemberEvent() {
        String member = createMember(createEvent("Member patch"), "Rower");

        given().contentType(ContentType.JSON)
                .body("{\"available\":\"NOT_AVAILABLE\",\"comment\":\"Vakantie\"}")
                .when().patch(member)
                .then().statusCode(200)
                .body("name", equalTo("Rower"))
                .body("available", equalTo("NOT_AVAILABLE"))
                .body("planned", equalTo("WAITING"))
                .body("comment", equalTo("Vakantie"));
    }

    @Test
    void patchRejectsInvalidValues() {
        long id = createEvent("Patch invalid");

        given().contentType(ContentType.JSON)
                .body("{\"date\":\"not a date\"}")
                .when().patch(API + "/events/" + id)
                .then().statusCode(400);
    }

    @Test
    void putAndPatchReturn404ForUnknownIds() {
        given().contentType(ContentType.JSON).body("{\"name\":\"x\"}")
                .when().put(API + "/events/999999").then().statusCode(404);
        given().contentType(ContentType.JSON).body("{\"name\":\"x\"}")
                .when().patch(API + "/events/999999").then().statusCode(404);
    }

    // Issue #72: POST /events/{id}/members

    @Test
    void postAddsAMemberToTheEvent() {
        long id = createEvent("Members");

        String member = createMember(id, "New Member");

        // Must point to the new member event, not to events/member-events/{id}
        org.hamcrest.MatcherAssert.assertThat(member, matchesPattern(".*/api/v1\\.0/member-events/\\d+"));
        given().when().get(member)
                .then().statusCode(200)
                .body("name", equalTo("New Member"));
        given().when().get(API + "/events/" + id + "/members")
                .then().statusCode(200)
                .body("name", contains("New Member"));
    }

    @Test
    void postMemberReturns404ForUnknownEvent() {
        given().contentType(ContentType.JSON)
                .body("{\"name\":\"Nobody\",\"available\":\"WAITING\",\"planned\":\"WAITING\"}")
                .when().post(API + "/events/999999/members")
                .then().statusCode(404);
    }

    // Issue #73: paging

    @Test
    void nextPageLinkKeepsTheSearchFilter() {
        // import.sql has four events named Training
        String next = given().when().get(API + "/events?search=Training&pageSize=2")
                .then().statusCode(200)
                .body("count", equalTo(4))
                .body("pageCount", equalTo(2))
                .body("nextPage", containsString("search=Training"))
                .extract().jsonPath().getString("nextPage");

        given().when().get(API + "/" + next)
                .then().statusCode(200)
                .body("items.name", everyItem(equalTo("Training")))
                .body("previousPage", containsString("search=Training"))
                .body("$", not(hasKey("nextPage")));
    }

    @Test
    void singlePageHasNoNextLink() {
        given().when().get(API + "/events?search=Wedstrijd")
                .then().statusCode(200)
                .body("count", equalTo(1))
                .body("pageCount", equalTo(1))
                .body("items.name", hasItem("Wedstrijd"))
                .body("$", not(hasKey("nextPage")));
    }

}
