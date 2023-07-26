package org.example;

import com.google.gson.Gson;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isA;

public class CourierApi {
    public void Create() {
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_CREATED);
        System.out.println(response.body().asString());
    }

    public void DeleteCourier() {
        Courier courier = new Courier("romanyvsky", "romanev", "Roman");

        Response responseLogin = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login");

        responseLogin.then().assertThat().body("id", isA(Integer.class))
                .and()
                .statusCode(SC_OK);

        String IdString = responseLogin.body().asString();
        Gson gson = new Gson();
        CourierDelete id = gson.fromJson(IdString, CourierDelete.class);


        Response responseDelete = given()
                .header("Content-type", "application/json")
                .when()
                .delete(String.format("/api/v1/courier/%s", id.getId()));

        responseDelete.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
}
