package com.globalsqa.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.url;

public class ManagerPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerPage.class);

    @Step("Открытие страницы менеджера")
    public ManagerPage open() {
        Selenide.open("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager");
        webdriver().shouldHave(url("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/manager"), Duration.ofSeconds(15));
        return this;
    }

    @Step("Кликнуть на кнопку [добавить клиента]")
    public ManagerPage clickToAddCustomer() {
        $("button[ng-click='addCust()']").click();
        return this;
    }

    @Step("Ввести имя {firstname}")
    public ManagerPage inputFirstname(String firstname) {
        $("input[placeholder='First Name']").setValue(firstname);
        return this;
    }

    @Step("Ввести фамилию {lastname}")
    public ManagerPage inputLastname(String lastname) {
        $("input[placeholder='Last Name']").setValue(lastname);
        return this;
    }

    @Step("Ввести фамилию {postcode}")
    public ManagerPage inputPostcode(int postcode) {
        $("input[placeholder='Post Code']").setValue(String.valueOf(postcode));
        return this;
    }

    public ManagerPage submitCustomer(){
        $("button[type='submit']").click();
        return this;
    }

    public ManagerPage goToCustomers(){
        $("button[ng-click='showCust()']").click();
        return this;
    }

    public void verifyAccountNumberByName(String name, String number){
        $(".ng-binding").shouldHave(text(name)).sibling(2).shouldHave(text(number));
    }

    public void fullCustomerVerifying(String name, String surname, String postcode, String number){
       $(byText(name)).sibling(0)
               .shouldHave(text(surname)).sibling(0)
               .shouldHave(text(postcode)).sibling(0)
               .shouldHave(text(number));

        LOGGER.info("Клиент: {} {} существует!", name, surname);
    }



    public void verifyAdding() {
        Assertions.assertTrue(switchTo().alert().getText().contains("Customer added successfully"));
    }
}
