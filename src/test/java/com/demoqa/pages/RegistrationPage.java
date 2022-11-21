package com.demoqa.pages;

import com.codeborne.selenide.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byTagName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class RegistrationPage {

    private final SelenideElement firstname = $("#firstName"),
            lastname = $("#lastName"),
            email = $("#userEmail"),
            phone = $("#userNumber"),
            subjects = $("#subjectsContainer"),
            currentAddress = $("#currentAddress");

    private final ElementsCollection hobbies = $$x("//input[contains(@id, 'hobbies-checkbox')]/following-sibling::label");

    public RegistrationPage inputName(String name) {
        firstname.sendKeys(name);
        return this;
    }

    public RegistrationPage inputLastname(String surname) {
        lastname.sendKeys(surname);
        return this;
    }

    public RegistrationPage inputEmail(String mail) {
        email.sendKeys(mail);
        return this;
    }

    public RegistrationPage inputPhone(String number) {
        phone.sendKeys(number);
        return this;
    }

    public RegistrationPage selectHobbiesByTitle(String... titles) {
        for (String title : titles) {
            hobbies.filter(text(title)).first().click();
        }
        return this;
    }

    public RegistrationPage inputSubjects(String... subject) {
        subjects.click();
        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        for (String s : subject) {
            actions.sendKeys(s).perform();
            actions.sendKeys(Keys.ENTER).perform();
        }
        return this;
    }

    public RegistrationPage uploadPicture(String filename) {
        $("#uploadPicture").uploadFromClasspath(filename);
        return this;
    }

    public RegistrationPage inputAddress(String address) {
        currentAddress.setValue(address);
        return this;
    }

    public RegistrationPage selectState(String stateName) {
        $("#state").find(byTagName("input")).setValue(stateName).pressEnter();
        return this;
    }

    public RegistrationPage selectCity(String cityName) {
        if ($("#city").find(byTagName("input")).is(disabled))
            throw new NoSuchElementException("Указан несуществующий штат!");
        $("#city").find(byTagName("input")).setValue(cityName).pressEnter();
        return this;
    }

    public RegistrationPage selectGender(String gender) {
        $(byText(gender)).click();
        return this;
    }

    public RegistrationPage open() {
        Selenide.open("https://demoqa.com/automation-practice-form");
        return this;
    }

    public RegistrationPage submit() {
        $("#submit").click();
        return this;
    }

    public RegistrationPage inputBirthday(String date) {
        String[] arr = date.split(" ");
//        executeJavaScript(String.format("$('[id=\"dateOfBirthInput\"]').val('%s')", date)); // автозаполнение
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(arr[1]);
        $(".react-datepicker__year-select").selectOption(arr[2]);
        $(byText(arr[0])).click();
        return this;
    }
}

