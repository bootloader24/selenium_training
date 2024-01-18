package ru.training.litecart.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class LoginTests {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    protected void click(By locator) {
        driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        click(locator);
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }

    protected boolean isElementPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    public void adminLogin(String user, String password) {
        driver.get("http://localhost/litecart/admin");
        type(By.name("username"), user);
        type(By.name("password"), password);
        click(By.name("login"));
    }

    public boolean isAdminLoggedIn() {
        return isElementPresent(By.xpath("//a[contains(@href,'admin/logout.php')]"));
    }

    @Test
    public void canAdminLogin() {
        adminLogin("admin", "admin");
        Assertions.assertTrue(isAdminLoggedIn());
    }

}