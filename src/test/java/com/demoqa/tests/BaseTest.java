package com.demoqa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import java.util.Objects;

@SuppressWarnings("RedundantSlf4jDefinition")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected final Faker faker = new Faker();

    @BeforeAll
    void setUp(){
        Configuration.browserSize = "1920x1080";
    }

    @AfterEach
    void tearDown(){
        Objects.requireNonNull(WebDriverRunner.getWebDriver()).close();
    }

}
