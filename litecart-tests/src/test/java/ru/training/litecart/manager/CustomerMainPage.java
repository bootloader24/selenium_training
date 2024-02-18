package ru.training.litecart.manager;

import org.openqa.selenium.By;

public class CustomerMainPage extends HelperBase {

    public CustomerMainPage(ApplicationManager manager) {
        super(manager);
    }

    public void openMainPage() {
        manager.driver.get("http://localhost/litecart");
    }

    public void clickOnFirstProduct() {
        manager.driver.findElement(By.cssSelector(".product")).click();
    }

    public void goToHomepage() {
        manager.driver.findElement(By.cssSelector("#site-menu li.general-0")).click();
    }

    public void goToCart() {
        manager.driver.findElement(By.cssSelector("#cart a.link")).click();
    }
}
