package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginTests extends TestBase {

    @Test
    void canAdminLogin() {
        app.session().adminLogin("admin", "admin");
        Assertions.assertTrue(app.session().isAdminLoggedIn());
    }
}