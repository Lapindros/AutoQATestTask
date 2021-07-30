package api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import static io.qameta.allure.Allure.step;

public class Pets {
    // фильтруем животных по статусу sold
    public static Response filterBySoldPets() {
        return
                given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                        when().get("https://petstore.swagger.io/v2/pet/findByStatus?status=sold")
                        .then().contentType(ContentType.JSON).extract().response();
    }

    // создаем животных из json
   @Test(priority = 0)
    public void createPets() throws FileNotFoundException {
       step("createPets");
       FileInputStream reader = new FileInputStream("src/test/resources/data.json");
       JSONArray array = new JSONArray (new JSONTokener(reader));
       for (Object object : array) {
           given()
           .header("Content-type", "application/json")
           .body(object.toString())
           .when()
           .post("https://petstore.swagger.io/v2/pet")
           .then().statusCode(200);
       }
    }

    // удаляем животных со статусом sold
    @Test(priority = 1)
    public void deleteFilteredBySoldPets() {
        step("deleteFilteredBySoldPets");
        Response response = filterBySoldPets();
        List<Object> jsonResponse = response.jsonPath().getList("id");
        for (Object id : jsonResponse) {
            given()
                    .header("Content-type", "application/json")
                    .when()
                    .delete("https://petstore.swagger.io/v2/pet/" + id)
                    .then();
    }
}
    // проверка имен всех животных
    @Test(priority = 2)
    public void checkAllPetsName() {

        String regex = "0-9\\.\\-\\s+\\/()]+";


        List<String> statuses = Arrays.asList("sold", "available", "pending");

        for (String status : statuses) {
            Response response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                    when().get("https://petstore.swagger.io/v2/pet/findByStatus?status=" + status)
                    .then().contentType(ContentType.JSON).extract().response();

            List<String> jsonResponse = response.jsonPath().getList("name");
            for (String name : jsonResponse) {
                Assert.assertFalse(name.matches(regex));
            }
        }
    }

    // удаляем всех животных
    @Test(priority = 3)
    public void deleteAllPets() {

        List<String> statuses = Arrays.asList("sold", "available", "pending");

        for (String status : statuses) {
            Response response = given().headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON).
                    when().get("https://petstore.swagger.io/v2/pet/findByStatus?status=" + status)
                    .then().contentType(ContentType.JSON).extract().response();

            List<Object> jsonResponse = response.jsonPath().getList("id");
            for (Object id : jsonResponse) {
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .delete("https://petstore.swagger.io/v2/pet/" + id);
            }
        }
    }
}