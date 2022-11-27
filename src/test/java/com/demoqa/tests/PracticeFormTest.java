package com.demoqa.tests;

import com.demoqa.elements.SubmitTable;
import com.demoqa.pages.RegistrationPage;
import com.demoqa.utils.datagenerator.FakeUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeFormTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PracticeFormTest.class);
    private final RegistrationPage registrationPage = new RegistrationPage();
    private final SubmitTable submitTable = new SubmitTable();


    @Test
    void registrationTest() {

        registrationPage
                .open()
                .inputName(FakeUser.NAME)
                .inputLastname(FakeUser.SURNAME)
                .inputEmail(FakeUser.MAIL)
                .selectGender(FakeUser.GENDER)
                .inputBirthday(FakeUser.DATE)
                .inputPhone(FakeUser.PHONE_NUMBER)
                .selectHobbiesByTitle(FakeUser.HOBBIES)
                .uploadPicture(FakeUser.IMG_JPEG)
                .inputAddress(FakeUser.ADDRESS)
                .selectState(FakeUser.STATE)
                .selectCity(FakeUser.CITY)
                .inputSubjects(FakeUser.SUBJECTS)
                .submit();


        submitTable
                .field("Student Name").expectedValue(FakeUser.NAME + " " + FakeUser.SURNAME)
                .field("Gender").expectedValue(FakeUser.GENDER)
                .field("Student Email").expectedValue(FakeUser.MAIL)
                .field("Mobile").expectedValue(FakeUser.PHONE_NUMBER)
                .field("Address").expectedValue(FakeUser.ADDRESS)
                .field("Subjects").expectedValue(FakeUser.SUBJECTS)
                .field("Hobbies").expectedValue(FakeUser.HOBBIES)
                .field("State and City").expectedValue(FakeUser.STATE + " " + FakeUser.CITY);

        LOGGER.info(String.join(" ", "Студент", FakeUser.NAME, FakeUser.SURNAME, "успешно зарегистрирован."));
    }
}
