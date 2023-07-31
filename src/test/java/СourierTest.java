import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class СourierTest extends CourierApi {

    CourierApi courierApi;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierApi = new CourierApi();
    }

    @Test
    public void checkCourierResponseBodyTest() {

        Courier courier = new Courier("romanyvsky", "romanev", "Roman");
       Response response = courierApi.createCourier(courier);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);
        System.out.println(response.body().asString());
    }

    @Test
    public void checkCourierDoubleResponseBodyTest() {
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");

        Response responseCreateCourier = courierApi.createCourier(courier);

        responseCreateCourier.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);

        Response responseCreateCourierDuplicate = courierApi.createCourier(courier);

        responseCreateCourierDuplicate.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

        System.out.println(responseCreateCourierDuplicate.body().asString());

    }

    @Test
    public void checkCourierResponseWithoutFieldBodyTest() {
        Courier courier = new Courier("romanyvsky", null, "Roman");

        Response response = courierApi.createCourier(courier);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");

        CourierId id = courierApi.getCourierId(courier);
        courierApi.deleteCourier(id.getId());

    }

}