package ru.training.litecart.manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;

import java.time.Duration;
import java.util.Properties;

public class ApplicationManager {

    public WebDriver driver;
    public Properties properties;
    private CustomerMainPage mainPage;
    private CustomerProductPage productPage;
    private CustomerCartPage cartPage;


    public void init(Properties properties) throws MalformedURLException {
        this.properties = properties;
        var browser = properties.getProperty("browser");
        if (driver == null) {
            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                LoggingPreferences prefs = new LoggingPreferences();
//                prefs.enable("browser", Level.ALL);
                ChromeOptions options = new ChromeOptions();
                options.setCapability("goog:loggingPrefs", prefs);
                driver = new ChromeDriver(options);
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %s", browser));
            }
            Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
        }
    }

    public WebDriverWait durationWait(long seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public CustomerMainPage customerMainPage() {
        if (mainPage == null) {
            mainPage = new CustomerMainPage(this);
        }
        return mainPage;
    }

    public CustomerProductPage customerProductPage() {
        if (productPage == null) {
            productPage = new CustomerProductPage(this);
        }
        return productPage;
    }

    public CustomerCartPage customerCartPage() {
        if (cartPage == null) {
            cartPage = new CustomerCartPage(this);
        }
        return cartPage;
    }
}
