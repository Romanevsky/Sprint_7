package org.example;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    final static String COURIER = "/api/v1/courier";
    public final static String LOGIN_COURIER = "/api/v1/courier/login";
    static final String COURIER_S = "/api/v1/courier/%s";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);
    }
    @Step("Авторизация курьера методом /api/v1/courier/login")
    public Response createCourierLogin(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(LOGIN_COURIER);
    }
    @Step("Удаление курьера")
    public void deleteCourier(String id) {

        Response responseDelete = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format(COURIER_S, id));

    }

    @Step("Вернуть id курьера")
    public CourierId getCourierId(Courier courier) {

        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(CourierApi.LOGIN_COURIER);
        String IdString = responseLogin.body().asString();
        Gson gson = new Gson();
        CourierId id = gson.fromJson(IdString, CourierId.class);
        return id;

    }
}
