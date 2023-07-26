import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierApi;
import org.example.CourierDelete;
import org.example.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class OrdersCourierIdTest {
    final static String COURIER =  "/api/v1/courier";
    final static String LOGIN_COURIER = "/api/v1/courier/login";
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CourierApi courierApi = new CourierApi();
        courierApi.Create();
    }



    @Test
    public void checkOrdersResponseBodyTest() {
        CourierLogin courierLogin = new CourierLogin("romanyvsky", "romanev");

        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER);

        responseLogin.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(SC_OK);

        String IdString = responseLogin.body().asString();
        Gson gson = new Gson();
        CourierDelete id = gson.fromJson(IdString, CourierDelete.class);


        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(String.format("/api/v1/orders?courierId=%s", id.getId()));

        response.then().assertThat()
                .statusCode(SC_OK);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierApi courierApi = new CourierApi();
        courierApi.DeleteCourier();

    }
}