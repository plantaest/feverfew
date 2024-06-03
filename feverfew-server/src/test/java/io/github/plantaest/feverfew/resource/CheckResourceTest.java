package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.dto.request.CreateCheckRequestBuilder;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class CheckResourceTest {

    @Test
    void testCreateCheckApi() {
        given()
                .contentType(ContentType.JSON)
                .body(CreateCheckRequestBuilder.builder()
                        .wikiId("viwiki")
                        .pageTitle("A")
                        .build())
                .when().post("/api/v1/checks")
                .then()
                .statusCode(201)
                .body("status", equalTo(201));
    }

}
