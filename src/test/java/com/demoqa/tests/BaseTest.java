package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import java.util.Objects;

public class BaseTest {

    protected final Faker faker = new Faker();

    @BeforeAll
    static void setUp(){
        Configuration.browserSize = "1920x1080";
    }

    @AfterEach
    void tearDown(){
        Objects.requireNonNull(WebDriverRunner.getWebDriver()).close();
    }

}
