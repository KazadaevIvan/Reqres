import io.restassured.RestAssured;
import models.Data;
import models.Resource;
import models.User;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class ReqresTest {

    @BeforeMethod
    public void beforeMethod() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    public void postUser() {
        User user = User.builder()
                .job("leader")
                .name("morpheus")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .post("/api/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("$", hasKey("id") ,
                        "job", equalTo(user.getJob()),
                        "name", equalTo(user.getName()),
                        "$", hasKey("createdAt"));
    }

    @Test
    public void getListUsers() {
        given()
        .when()
                .get("/api/users?page=2")
        .then()
                .log().all()
                .statusCode(200)
                .body("total", equalTo(12));
    }

    @Test
    public void getSingleUser() {
        Data data = Data.builder()
                .id(2)
                .email("janet.weaver@reqres.in")
                .firstName("Janet")
                .lastName("Weaver")
                .avatar("https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg")
                .build();

        given()
        .when()
                .get("/api/users/2")
        .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(data.getId()),
                        "data.email", equalTo(data.getEmail()),
                        "data.first_name", equalTo(data.getFirstName()),
                        "data.last_name", equalTo(data.getLastName()),
                        "data.avatar", equalTo(data.getAvatar()));
    }

    @Test
    public void getSingleUserNotFound() {
        given()
        .when()
                .get("/api/users/23")
        .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    public void getListResource() {
        given()
        .when()
                .get("/api/unknown")
        .then()
                .log().all()
                .statusCode(200)
                .body("total", equalTo(12));
    }

    @Test
    public void getSingleResource() {
        Resource resource = Resource.builder()
                .id(2)
                .name("fuchsia rose")
                .year(2001)
                .color("#C74375")
                .pantoneValue("17-2031")
                .build();

        given()
        .when()
                .get("/api/unknown/2")
        .then()
                .log().all()
                .statusCode(200)
                .body("data.id", equalTo(resource.getId()),
                        "data.name", equalTo(resource.getName()),
                        "data.year", equalTo(resource.getYear()),
                        "data.color", equalTo(resource.getColor()),
                        "data.pantone_value", equalTo(resource.getPantoneValue()));
    }

    @Test
    public void getSingleResourceNotFound() {
        given()
        .when()
                .get("/api/unknow/23")
        .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("{}"));
    }

    @Test
    public void putUser() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .put("/api/users/2")
        .then()
                .log().all()
                .statusCode(200)
                .body("job", equalTo(user.getJob()),
                        "name", equalTo(user.getName()),
                        "$", hasKey("updatedAt"));
    }

    @Test
    public void patchUser() {
        User user = User.builder()
                .name("morpheus")
                .job("zion resident")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .patch("/api/users/2")
        .then()
                .log().all()
                .statusCode(200)
                .body("job", equalTo(user.getJob()),
                        "name", equalTo(user.getName()),
                        "$", hasKey("updatedAt"));
    }

    @Test
    public void deleteUser() {
        given()
        .when()
                .delete("/api/users/2")
        .then()
                .log().all()
                .statusCode(204)
                .body(equalTo(""));
    }

    @Test
    public void postRegisterSuccessful() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .post("/api/register")
        .then()
                .log().all()
                .statusCode(200)
                .body("$", hasKey("id"),
                        "$", hasKey("token"));
    }

    @Test
    public void postRegisterUnsuccessful() {
        User user = User.builder()
                .email("sydney@fife")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .post("/api/register")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void postLoginSuccessful() {
        User user = User.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .post("/api/login")
        .then()
                .log().all()
                .statusCode(200)
                .body("token", equalTo("QpwL5tke4Pnpja7X4"));
    }

    @Test
    public void postLoginUnsuccessful() {
        User user = User.builder()
                .email("peter@klaven")
                .build();

        given()
                .body(user)
                .header("Content-Type", "application/json")
        .when()
                .post("/api/login")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));
    }

    @Test
    public void getDelayedResponse() {
        given()
        .when()
                .get("/api/users?delay=3")
        .then()
                .log().all()
                .statusCode(200)
                .time(Matchers.greaterThanOrEqualTo(3L), SECONDS);
    }
}
