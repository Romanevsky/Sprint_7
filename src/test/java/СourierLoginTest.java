import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class СourierLoginTest extends CourierApi {
    final static String LOGIN_COURIER = "/api/v1/courier/login";
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CourierApi courierApi = new CourierApi();
        courierApi.Create();
    }


    @Test
    public void checkCourierLoginResponseBodyTest() {

        CourierLogin courierLogin = new CourierLogin("romanyvsky", "romanev");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER);

        response.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(SC_OK);

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
                .post(LOGIN_COURIER);

        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierApi courierApi = new CourierApi();
        courierApi.DeleteCourier();

    }

}