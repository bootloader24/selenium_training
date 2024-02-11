package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class LoginTests extends TestBase {

    public boolean isAdminLoggedIn() {
        return isElementPresent(By.xpath("//a[contains(@href,'admin/logout.php')]"));
    }

    @Test
    void canAdminLogin() {
        adminLogin("admin", "admin");
        Assertions.assertTrue(isAdminLoggedIn());
    }
}