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

public class СourierTest extends CourierApi {
    final static String COURIER =  "/api/v1/courier";
    final static String LOGIN_COURIER = "/api/v1/courier/login";
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkCourierResponseBodyTest() {
        CourierApi courierApi = new CourierApi();
        courierApi.Create();
    }

    @Test
    public void checkCourierDoubleResponseBodyTest() {
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");

        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);

        response1.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);

        response2.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

        System.out.println(response2.body().asString());

    }

    @Test
    public void checkCourierResponseWithoutFieldBodyTest() {
        CourierWithoutPassword courierWithoutPassword = new CourierWithoutPassword("romanyvsky", "Roman");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierWithoutPassword)
                .when()
                .post(COURIER);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierApi courierApi = new CourierApi();
        courierApi.DeleteCourier();

    }

}