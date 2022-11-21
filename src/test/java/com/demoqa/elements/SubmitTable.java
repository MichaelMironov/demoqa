package com.demoqa.elements;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SubmitTable {

    private final SelenideElement table = $(".table-responsive").find(By.tagName("tbody"));
    private String row;

    public SubmitTable field(String name){
       row = table.$(byText(name)).sibling(0).innerText();
       return this;
    }

    public SubmitTable expectedValue(String expectedValue){
        Assertions.assertEquals(expectedValue, row, "Поля не равны");
        return this;
    }
}
