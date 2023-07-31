package org.example;

import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierApi {

    final static String COURIER = "/api/v1/courier";
    public final static String LOGIN_COURIER = "/api/v1/courier/login";
    static final String COURIER_S = "/api/v1/courier/%s";

    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);
    }

    public Response createCourierLogin(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(LOGIN_COURIER);
    }

    public void deleteCourier(String id) {

        Response responseDelete = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format(COURIER_S, id));

    }

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
