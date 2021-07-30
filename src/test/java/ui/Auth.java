package ui;

import api.Users;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

public class Auth {

    // поиск пользователя проверка его профиля
    @Test
    public void findUserandCheckProfile() {
        open("http://users.bugred.ru/");
        $(By.xpath("//input[@placeholder='Введите email или имя']")).shouldBe().setValue(Users.getRandomEmail());
        $(By.xpath("//button[contains(text(),'Найти')]")).shouldBe().click();
        $(By.xpath("//a[contains(text(),'Посмотреть')]")).shouldBe().click();

        $(By.xpath("//td[normalize-space()=" + Users.getRandomEmail())).shouldBe();
        $(By.xpath("//a[contains(text(),'Первая задача')]")).shouldBe();
    }
}
