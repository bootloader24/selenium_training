package ru.training.litecart.manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;

import java.time.Duration;
import java.util.Properties;

public class ApplicationManager {

    public WebDriver driver;
    private LoginHelper session;
    protected Properties properties;

    public void init(String browser, Properties properties) throws MalformedURLException {
        this.properties = properties;
        if (driver == null) {
            if ("firefox".equals(browser)) {
                driver = new FirefoxDriver();
            } else if ("chrome".equals(browser)) {
                driver = new ChromeDriver();
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %s", browser));
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
            Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
        }
    }

    public LoginHelper session() {
        if (session == null) {
            session = new LoginHelper(this);
        }
        return session;
    }
}
