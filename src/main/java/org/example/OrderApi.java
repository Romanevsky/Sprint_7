package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class OrderApi {
    final static String ORDER = "/api/v1/orders";
    public static final String COURIER_ID_S = "/api/v1/orders?courierId=%s";


    @Step("Получение списка заказов")
    public Response courierOrder(String id) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(String.format(COURIER_ID_S, id));
    }
    @Step("Создание заказа")
    public Response newOrders(Orders orders) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(orders)
                .when()
                .post(ORDER);
    }
}
