package ru.training.selenium;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.automation.remarks.junit5.Video;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;

public class MyFirstTest {

    public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void start() throws IOException {
        var properties = new Properties();
        properties.load(new FileReader(System.getProperty("target", "local.properties")));
        var browser = properties.getProperty("browser");
        var seleniumServer = properties.getProperty("seleniumServer");
        var cloudSeleniumServer = properties.getProperty("cloudSeleniumServer");

        if (tlDriver.get() != null) {
            driver = tlDriver.get();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return;
        }

        if (cloudSeleniumServer != null) {
            FirefoxOptions options = new FirefoxOptions();
            FirefoxProfile profile = new FirefoxProfile();
            profile.setPreference("intl.accept_languages", "en-GB");
            options.setProfile(profile);
            options.setBrowserVersion("latest");
            options.setPlatformName("WINDOWS11");
            options.addArguments("--lang=ru");
            driver = new RemoteWebDriver(new URL(cloudSeleniumServer), options);
        } else {
            if ("firefox".equals(browser)) {
                FirefoxOptions options = new FirefoxOptions();
                //options.addArguments("-devtools");
                //options.setCapability("unexpectedAlertBehaviour", "dismiss");
                if (seleniumServer != null) {
                    driver = new RemoteWebDriver(new URL(seleniumServer), options);
                } else {
                    driver = new FirefoxDriver(options);
                }
            } else if ("firefox-nightly".equals(browser)) {
                FirefoxOptions options = new FirefoxOptions();
                options.setBinary("firefox-trunk"); // путь к исполняемому файлу
                if (seleniumServer != null) {
                    driver = new RemoteWebDriver(new URL(seleniumServer), options);
                } else {
                    driver = new FirefoxDriver(options);
                }
            } else if ("chrome".equals(browser)) {
                ChromeOptions options = new ChromeOptions();
                //options.addArguments("--start-fullscreen");  // запуск в полноэкранном режиме
                //options.setPageLoadStrategy(PageLoadStrategy.EAGER);  // установка стратегии загрузки страниц
                options.setBinary("/opt/browsers/chrome/1250748/chrome-linux/chrome"); // путь к исполняемому файлу
                if (seleniumServer != null) {
                    driver = new RemoteWebDriver(new URL(seleniumServer), options);
                } else {
                    driver = new ChromeDriver(options);
                }
            } else if ("edge".equals(browser)) {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--start-maximized");
                if (seleniumServer != null) {
                    driver = new RemoteWebDriver(new URL(seleniumServer), options);
                } else {
                    driver = new EdgeDriver(options);
                }
            } else if ("ie".equals(browser)) {
                InternetExplorerOptions options = new InternetExplorerOptions();
                if (seleniumServer != null) {
                    driver = new RemoteWebDriver(new URL(seleniumServer), options);
                } else {
                    driver = new InternetExplorerDriver(options);
                }
            } else {
                throw new IllegalArgumentException(String.format("Unknown browser: %s", browser));
            }
        }
        tlDriver.set(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println(((HasCapabilities) driver).getCapabilities());
        Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
    }

    @Test
    public void myFirstTest() {
        driver.get("https://bing.com");

        driver.manage().addCookie(new Cookie("test", "test"));
        Cookie testCookie = driver.manage().getCookieNamed("test");
        System.out.println("Print only testCookie: " + testCookie);
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("Print cookies: " + cookies);
        driver.manage().deleteCookieNamed("test");
        cookies = driver.manage().getCookies();
        System.out.println("Print cookies after delete testCookie: " + cookies);
        driver.manage().deleteAllCookies();
        cookies = driver.manage().getCookies();
        System.out.println("Print cookies after delete all cookies: " + cookies);

        driver.findElement(By.name("q")).sendKeys("selenium");
        driver.findElement(By.id("search_icon")).click();
        wait.until(titleContains("selenium"));
    }

    @Test
    public void mySecondTest() {
        driver.get("https://bing.com");
        driver.findElement(By.name("q")).sendKeys("selenium");
        driver.findElement(By.id("search_icon")).click();
        wait.until(titleContains("selenium"));
    }

    @Test
    @Video
    public void myThirdTest() {
        driver.get("https://google.ru");
        driver.findElement(By.name("q")).sendKeys("selenium");
        driver.findElement(By.name("btnK")).click();
        wait.until(titleContains("selenium"));
    }
}
