package com.demoqa.tests;

import com.demoqa.pages.RegistrationPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PracticeFormTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeFormTest.class);
    private final SimpleDateFormat formatDate = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
    private final RegistrationPage registrationPage = new RegistrationPage();

    private String firstName = faker.name().firstName();
    private String lastName = faker.name().lastName();
    private String email = faker.internet().safeEmailAddress();
    private String number = String.valueOf(faker.numerify("##########"));
    private String birthday = formatDate.format(faker.date().birthday());
    private String address = faker.address().fullAddress();
    private String gender = "Female";


    @Test
    void registrationTest() {
        registrationPage
                .open()
                .inputName(firstName)
                .inputLastname(lastName)
                .inputEmail(email)
                .selectGender(gender)
                .inputPhone(number)
                .selectHobbiesByTitle("Reading", "Sport")
                .inputSubjects("Computer Science", "Maths", "English")
                .uploadPicture("test.jpeg")
                .inputAddress(address)
                .selectState("NCR")
                .selectCity("Delhi")
                .inputBirthday(birthday)
                .submit();

        String expectedDate = $(".table tr", 5).getText().replace(",", "");

        Assertions.assertAll(
                () -> $$(".table tr").shouldHave(size(11)),
                () -> $(".table tr", 1).shouldHave(text(String.join(" ",firstName, lastName))),
                () -> $(".table tr", 2).shouldHave(text(email)),
                () -> $(".table tr", 3).shouldHave(text(gender)),
                () -> $(".table tr", 4).shouldHave(text(number)),
                () -> $(".table tr", 9).shouldHave(text(address)),
                () -> expectedDate.equals(birthday)
                //TODO etc
        );
        LOGGER.info("Студент {} {} зарегистрирован", firstName, lastName);
    }
}
