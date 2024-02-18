package ru.training.litecart.manager;

import org.openqa.selenium.By;

import java.time.Duration;

public class HelperBase {

    protected final ApplicationManager manager;

    public HelperBase(ApplicationManager manager) {
        this.manager = manager;
    }

    static Duration defaultImplicityWait = Duration.ofSeconds(10);

    protected void click(By locator) {
        manager.driver.findElement(locator).click();
    }

    protected void type(By locator, String text) {
        click(locator);
        manager.driver.findElement(locator).clear();
        manager.driver.findElement(locator).sendKeys(text);
    }


    protected boolean isElementPresent(By locator) {
        try {
            manager.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return manager.driver.findElements(locator).size() > 0;
        } finally {
            manager.driver.manage().timeouts().implicitlyWait(defaultImplicityWait);
        }
    }

    protected boolean isElementNotPresent(By locator) {
        try {
            manager.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            return manager.driver.findElements(locator).size() == 0;
        } finally {
            manager.driver.manage().timeouts().implicitlyWait(defaultImplicityWait);
        }
    }
}
