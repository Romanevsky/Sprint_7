import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderApi;
import org.example.Orders;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import static org.apache.http.HttpStatus.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isA;

@RunWith(Parameterized.class)

public class OrdersTest extends OrderApi {
    OrderApi orderApi = new OrderApi();
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;


    public OrdersTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тест: {8}")
    public static Object[][] params() {
        return new Object[][]{
                {"Roman", "Romanevsky", "Владимиров, 14 apt.", 3, "+7 800 555 00 00", 5, "2020-06-07", "По раньше", List.of("BLACK")},
                {"Roman", "Romanevsky", "Владимиров, 12 apt.", 4, "+7 800 555 00 00", 5, "2020-06-06", "По раньше", List.of("GREY")},
                {"Roman", "Romanevsky", "Владимиров, 42 apt.", 5, "+7 800 555 00 00", 5, "2020-06-08", "По раньше", List.of("BLACK", "GREY")},
                {"Roman", "Romanevsky", "Владимиров, 13 apt.", 6, "+7 800 555 00 00", 5, "2020-06-09", "По раньше", List.of()},
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkOrdersResponseBodyTest() {
        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);

        Response response = orderApi.newOrders(orders);

        response.then().assertThat().body("track", isA(Integer.class))
                .and()
                .statusCode(SC_CREATED);

        System.out.println(response.body().asString());

    }
}