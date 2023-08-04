import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
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
    CourierApi courierApi = new CourierApi();
    Courier courier = new Courier("romanyvsky", "romanev", null);
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierApi.createCourier(courier);
    }


    @Test
    @DisplayName("check Courier Login Response Body Test")
    @Description("Применение метода /api/v1/courier/login")
    public void checkCourierLoginResponseBodyTest() {
        Response response = courierApi.createCourierLogin(courier);

        response.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(SC_OK);

        System.out.println(response.body().asString());

    }

    @Test
    @DisplayName("check Courier Login Bad Password Response Body Test")
    @Description("Проверка авторизации с неверным паролем с неверным")
    public void checkCourierLoginBadPasswordResponseBodyTest() {
        Courier courier = new Courier("romanyvsky", "romanevBug", null);
        Response response = courierApi.createCourierLogin(courier);

        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);

        System.out.println(response.body().asString());

    }

    @After
    public void tearDown() {
        CourierId id = courierApi.getCourierId(courier);
        courierApi.deleteCourier(id.getId());

    }

}