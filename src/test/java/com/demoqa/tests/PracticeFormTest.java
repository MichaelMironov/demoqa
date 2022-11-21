package com.demoqa.tests;

import com.demoqa.elements.SubmitTable;
import com.demoqa.pages.RegistrationPage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PracticeFormTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeFormTest.class);
    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
    private final RegistrationPage registrationPage = new RegistrationPage();
    private final SubmitTable submitTable = new SubmitTable();

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
                .inputBirthday(birthday)
                .inputPhone(number)
                .selectHobbiesByTitle("Reading", "Sports")
                .inputSubjects("Computer Science", "Maths", "English")
                .uploadPicture("test.jpeg")
                .inputAddress(address)
                .selectState("NCR")
                .selectCity("Delhi")
                .submit();


        submitTable
                .field("Student Name").expectedValue(firstName + " " + lastName)
                .field("Gender").expectedValue(gender)
                .field("Student Email").expectedValue(email)
                .field("Mobile").expectedValue(number)
                .field("Address").expectedValue(address)
                .field("Subjects").expectedValue("Computer Science, Maths, English")
                .field("Hobbies").expectedValue("Reading, Sports")
                .field("State and City").expectedValue("NCR Delhi");

        LOGGER.info(String.join(" ", "Студент", firstName, lastName, "успешно зарегистрирован."));
    }
}
