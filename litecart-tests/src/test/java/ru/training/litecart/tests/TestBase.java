package ru.training.litecart.tests;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import ru.training.litecart.manager.ApplicationManager;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class TestBase {

    protected static ApplicationManager app;

    Duration defaultImplicityWait = Duration.ofSeconds(10);

    @BeforeEach
    public void setUp() throws IOException {
        if (app == null) {
            var properties = new Properties();
            properties.load(new FileReader(System.getProperty("target", "local.properties")));
            app = new ApplicationManager();
            app.init(properties);
        }
    }

    protected void adminLogin(String user, String password) {
        app.driver.get(app.properties.getProperty("web.adminUrl"));
        type(By.name("username"), user);
        type(By.name("password"), password);
        click(By.name("login"));
    }

    protected void click(By locator) {
        app.driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        click(locator);
        app.driver.findElement(locator).clear();
        app.driver.findElement(locator).sendKeys(text);
    }

    boolean isElementPresent(By locator) {
        try {
            app.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return app.driver.findElements(locator).size() > 0;
        } finally {
            app.driver.manage().timeouts().implicitlyWait(defaultImplicityWait);
        }
    }

    boolean isElementNotPresent(By locator) {
        try {
            app.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return app.driver.findElements(locator).size() == 0;
        } finally {
            app.driver.manage().timeouts().implicitlyWait(defaultImplicityWait);
        }
    }
}
