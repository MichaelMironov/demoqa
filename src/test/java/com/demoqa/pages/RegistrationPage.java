package com.demoqa.pages;

import com.codeborne.selenide.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

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
            hobbies.filter(Condition.text(title)).first().click();
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
        $("#react-select-3-input").setValue(stateName).pressEnter();
        checkExisting(stateName);
        return this;
    }

    public RegistrationPage selectCity(String cityName) {
        $("#react-select-4-input").setValue(cityName).pressEnter();
        checkExisting(cityName);
        return this;
    }

    private void checkExisting(String name) {
        if ($(".css-26l3qy-menu").isDisplayed())
            throw new NoSuchElementException(String.format("%s не существует", name));
    }

    public RegistrationPage selectGender(String gender) {
        $(byText(gender)).click();
        return this;
    }

    public RegistrationPage open(){
        Selenide.open("https://demoqa.com/automation-practice-form");
        return this;
    }

    public RegistrationPage submit(){
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

