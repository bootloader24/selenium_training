package ru.training.litecart.manager;

import org.openqa.selenium.By;

public class LoginHelper extends HelperBase {

    public LoginHelper(ApplicationManager manager) {
        super(manager);
    }

    public void adminLogin(String user, String password) {
        manager.driver.get(manager.properties.getProperty("web.adminUrl"));
        type(By.name("username"), user);
        type(By.name("password"), password);
        click(By.name("login"));
    }

    public boolean isAdminLoggedIn() {
        return isElementPresent(By.xpath("//a[contains(@href,'admin/logout.php')]"));
    }
}