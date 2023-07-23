import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierDelete;
import org.example.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class courierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }


    @Test
    public void checkCourierLoginResponseBodyTest() {
        CourierLogin courierLogin = new CourierLogin("romanyvsky", "romanev");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(200);

        System.out.println(response.body().asString());

    }

    @Test
    public void checkCourierLoginBadPasswordResponseBodyTest() {
        CourierLogin courierLogin = new CourierLogin("romanyvsky", "roman");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");

        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierLogin courierLogin = new CourierLogin("romanyvsky", "romanev");

        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");

        responseLogin.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(200);

        String IdString = responseLogin.body().asString();
        Gson gson = new Gson();
        CourierDelete id = gson.fromJson(IdString, CourierDelete.class);


        Response responseDelete = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format("/api/v1/courier/%s", id.getId()));

        responseDelete.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(200);

    }

}