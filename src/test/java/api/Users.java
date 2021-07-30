package api;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Random;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Users {
    // генератор имен
    public static String generateRandomName(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
    // генератор email
    public static String generateRandomEmail(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
        String email;
        String temp = RandomStringUtils.random(length, allowedChars);
        email = temp + "@testdata.com";
        return email;
    }

    String randomName = generateRandomName(9);
    public static String randomEmail = generateRandomEmail(9);

    public static String getRandomEmail() {
        return randomEmail;
    }
    // создаем пользователя и его задачу
    @Test(priority = 0)
    public void createUserAndTask() {
        step("createUserAndTask");

        String testUser;
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        array.put(new JSONObject().put("title", "Первая задача").put("description","Первая задача 11"));

        json.put("email", getRandomEmail());
        json.put("name", randomName);
        json.put("tasks", array);

        testUser = json.toString();

            given()
                    .header("Content-type", "application/json")
                    .body(testUser)
                    .when()
                    .post("http://users.bugred.ru/tasks/rest/createuserwithtasks")
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .body("name", equalTo(randomName))
                    .body("tasks.name[0]", equalTo("Первая задача"));
    }
}