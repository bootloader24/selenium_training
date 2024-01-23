package ru.training.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;

public class MyFirstTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void start() throws IOException {
        var properties = new Properties();
        properties.load(new FileReader(System.getProperty("target", "local.properties")));
        var browser = properties.getProperty("browser");

        if (driver == null) {
            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else if ("edge".equals(browser)) {
                driver = new EdgeDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %s", browser));
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }
    }

    @Test
    public void myFirstTest() {
        driver.get("https://bing.com");
        driver.findElement(By.name("q")).sendKeys("selenium");
        driver.findElement(By.id("search_icon")).click();
        wait.until(titleContains("selenium"));
    }

    @AfterEach
    public void stop() {
        driver.quit();
        driver = null;
    }
}
