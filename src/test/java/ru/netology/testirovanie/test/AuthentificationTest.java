package ru.netology.testirovanie.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.testirovanie.data.DataGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testirovanie.data.DataGenerator.Registration.getRegisteredUser;

public class
AuthentificationTest {

    @BeforeAll
    static void setUpAll()
    {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyLoginWithActiveRegisteredUser() {

        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("h2").shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWithUnregisteredActiveUser() {
        var unregisteredUser = DataGenerator.Registration.getUser("active");

        $("[data-test-id=login] input").setValue(unregisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(unregisteredUser.getPassword());
        $("[data-test-id=action-login]");

        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWithBlockedRegisteredUser() {

        var blockedUser = getRegisteredUser("blocked");

        // Вводим логин и пароль
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWithWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        // Вводим неправильный логин
        $("[data-test-id=login] input").setValue(DataGenerator.getRandomLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldShowErrorWithWrongPassword () {
        var registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(DataGenerator.getRandomPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }


}
