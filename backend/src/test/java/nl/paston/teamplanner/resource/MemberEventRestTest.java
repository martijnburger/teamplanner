package nl.paston.teamplanner.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest @QuarkusTestResource(H2DatabaseTestResource.class)
public class MemberEventRestTest {

    @Test
    public void dummyTest() {
        given().when().get("/member-events?pageSize=5")
            .then().statusCode(200)
                .body(
                    "items", hasSize(5),
                    "count", is(25), 
                    "pageSize", is(5),
                    "pageCount", is(5)
                );
    }

}