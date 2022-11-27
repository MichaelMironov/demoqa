package com.demoqa.elements;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;

import java.util.Arrays;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class SubmitTable {

    private final SelenideElement table = $(".table-responsive").find(By.tagName("tbody"));
    private SelenideElement row;

    public SubmitTable field(String name){
       row = table.$(byText(name));
       return this;
    }

    public SubmitTable expectedValue(String expectedValue){
        Assertions.assertEquals(expectedValue, row.sibling(0).innerText(), "Поле: " + row.innerText());
        return this;
    }

    public SubmitTable expectedValue(String[] expectedValue){
        String expectedResult = Arrays.toString(expectedValue).replaceAll("^\\[|\\]$", "");
        Assertions.assertEquals(expectedResult, row.sibling(0).innerText(), "Поле: " + row.innerText());
        return this;
    }
}
