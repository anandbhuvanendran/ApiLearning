package com.sampleapi;

import com.files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;
import static org.hamcrest.Matchers.equalTo;

public class ApiBasics {

    public static void main(String[] args){

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body(Payload.addPlace()).when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("Server","Apache/2.4.52 (Ubuntu)").extract().response().asString();
        System.out.println(response);
        JsonPath jp = new JsonPath(response);
        String placeId = jp.getString("place_id");
        System.out.println(placeId);
        String updatedAddress = "70 Summer walk, USA";

        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+updatedAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n").when().put("/maps/api/place/update/json")
                .then().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));

        String getResponse = given().queryParam("key","qaclick123").queryParam("place_id",placeId)
                .when().get("/maps/api/place/get/json")
                .then().log().all().statusCode(200).extract().response().asString();
        JsonPath js = new JsonPath(getResponse);
        String actualAddress = js.getString("address");
        Assert.assertEquals(actualAddress,updatedAddress);

    }
}
