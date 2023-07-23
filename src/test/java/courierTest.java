import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierDelete;
import org.example.CourierLogin;
import org.example.CourierWithoutPassword;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class courierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkCourierResponseBodyTest() {
        Courier courier = new Courier("romanevsky", "romanev", "Roman");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        System.out.println(response.body().asString());

        CourierLogin courierLogin = new CourierLogin("romanevsky", "romanev");

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

    @Test
    public void checkCourierDoubleResponseBodyTest() {
        Courier courier = new Courier("romanevsky", "romanev", "Roman");

        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response1.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response2.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);

        System.out.println(response2.body().asString());

    }

    @Test
    public void checkCourierResponseWithoutFieldBodyTest() {
        CourierWithoutPassword courierWithoutPassword = new CourierWithoutPassword("romanevsky", "Roman");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierWithoutPassword)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);

        System.out.println(response.body().asString());

    }

}