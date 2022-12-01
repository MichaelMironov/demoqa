package com.globalsqa;

import com.globalsqa.pages.ManagerPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class BankManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankManagerTest.class);

    //Csv
    @ParameterizedTest(name = "Добавление покупателя: {0} {1}")
    @CsvFileSource(resources = "/customers.csv")
    void addCustomerCsvTest(String firstname, String lastname, int postcode) {

        new ManagerPage().open()
                .clickToAddCustomer()
                .inputFirstname(firstname)
                .inputLastname(lastname)
                .inputPostcode(postcode)
                .submitCustomer()
                .verifyAdding();

        LOGGER.info(String.join(" ", "Покупатель:", firstname, lastname, "успешно добавлен!"));
    }


    //Values
    @ParameterizedTest(name = "Проверка аккаунта Hermoine по номеру: {0}")
    @ValueSource(strings = {"1001", "1002", "1003"})
    void accountNumberTest(String accountId) {
        new ManagerPage().open()
                .goToCustomers()
                .verifyAccountNumberByName("Hermoine", accountId);

    }


    //Arguments
    @ParameterizedTest
    @MethodSource("customersDataProvider")
    void existingCustomersTest(String firstname, String lastname, String postcode, String number) {
        new ManagerPage().open()
                .goToCustomers()
                .fullCustomerVerifying(firstname, lastname, postcode, number);
    }

    static Stream<Arguments> customersDataProvider() {
        return Stream.of(
                Arguments.arguments("Harry", "Potter", "E725JB", "1004 1005 1006"),
                Arguments.arguments("Ron", "Weasly", "E55555", "1007 1008 1009"),
                Arguments.arguments("Albus", "Dumbledore", "E55656", "1010 1011 1012"),
                Arguments.arguments("Neville", "Longbottom", "E89898", "1013 1014 1015")
        );
    }
}
