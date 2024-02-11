package ru.training.litecart.manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;

import java.util.Properties;

public class ApplicationManager {

    public WebDriver driver;
    public Properties properties;

    public void init(Properties properties) throws MalformedURLException {
        this.properties = properties;
        var browser = properties.getProperty("browser");
        if (driver == null) {
            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %s", browser));
            }
            Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
        }
    }
}
