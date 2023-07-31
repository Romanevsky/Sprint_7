import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

public class OrdersCourierIdTest {
    CourierApi courierApi = new CourierApi();
    Courier courier = new Courier("romanyvsky", "romanev", null);
    OrderApi orderApi = new OrderApi();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierApi.createCourier(courier);
    }


    @Test
    public void checkOrdersResponseBodyTest() {
        Courier courier = new Courier("romanyvsky", "romanev", null);

        Response response = courierApi.createCourierLogin(courier);

        response.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(SC_OK);

        String idString = response.body().asString();
        Gson gson = new Gson();
        CourierId courierId = gson.fromJson(idString, CourierId.class);


        Response responseOrder = orderApi.courierOrder(courierId.getId());

        responseOrder.then().assertThat()
                .statusCode(SC_OK);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierId id = courierApi.getCourierId(courier);
        courierApi.deleteCourier(id.getId());

    }
}