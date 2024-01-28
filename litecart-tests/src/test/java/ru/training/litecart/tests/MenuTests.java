package ru.training.litecart.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MenuTests extends TestBase {

    public WebElement getMenuWrapper() {  // метод поиска враппера меню
        return app.driver.findElement(By.id("box-apps-menu-wrapper"));
    }

    public List<WebElement> getMenuItems() { // метод поиска пунктов меню
        return getMenuWrapper().findElements(By.id("app-"));
    }

    public List<WebElement> getSubmenuItems(int menuItemNum) { // метод поиска вложенных пунктов меню
        return getMenuItems().get(menuItemNum).findElements(By.cssSelector("[id^=doc-]"));
    }

    public void findHead() { // метод поиска заголовка в блоке контента
        app.driver.findElement(By.cssSelector("#content > h1"));
    }

    @Test
    public void menuTest() {
        app.session().adminLogin("admin", "admin");

        List<WebElement> menuItems = getMenuItems(); // получаем список пунктов меню
        for (int menuItemNum = 0; menuItemNum < menuItems.size(); menuItemNum++) { // цикл прокликивания пунктов меню
            menuItems = getMenuItems(); // получаем список пунктов меню
            menuItems.get(menuItemNum).click(); // кликаем на пункте меню
            findHead(); // ищем заголовок h1
            List<WebElement> submenuItems = getSubmenuItems(menuItemNum); // получаем список вложенных пунктов
            if (submenuItems.size() > 0) { // если имеются вложенные пункты, запускаем цикл их прокликивания
                for (int submenuItemNum = 0; submenuItemNum < submenuItems.size(); submenuItemNum++) {
                    submenuItems = getSubmenuItems(menuItemNum); // получаем список вложенных пунктов
                    submenuItems.get(submenuItemNum).click(); // кликаем на пункте подменю
                    findHead(); // ищем заголовок h1
                }
            }
        }
    }

}
