package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;

public class BrowserLogsTests extends TestBase {


    @Test
    @DisplayName("Тест отсутствия сообщений в логе браузера")
    public void isExistBrowserLogsOnProductPage() {
        // логинимся
        adminLogin("admin", "admin");
        // кликаем на разделе Catalog в меню
        app.driver.findElement(By.cssSelector("#app- a[href*='app=catalog']")).click();
        // в данном случае, чтобы не усложнять, принимаем допущение, что все проверяемые страницы товаров находятся в категории с id=1
        // в общем случае, конечно же нужно товары во всех имеющихся категориях проверить
        app.driver.findElement(By.cssSelector("table.dataTable td:nth-child(3) a[href*='category_id=1']")).click();
        // считаем количество товаров в категории
        var productsCount = app.driver.findElements
                (By.cssSelector("table.dataTable td:nth-child(3) a[href*='category_id=1&product_id=']")).size();
        // получаем из драйвера доступные типы лога
        var logLevels = app.driver.manage().logs().getAvailableLogTypes();
        // пробегаемся по всем товарам
        for (int i = 0; i < productsCount; i++) {
            // кликаем по i-му товару
            app.driver.findElements
                    (By.cssSelector("table.dataTable td:nth-child(3) a[href*='product_id=']")).get(i).click();
            // сохраняем URL страницы продукта, чтобы его вывести в консоль при наличии сообщений в логах
            var url = app.driver.getCurrentUrl();
            // для каждого типа логов проверяем что лог пуст
            // если проверка не прошла, выводим в консоль url, тип лога и содержимое лога
            logLevels.forEach(l -> Assertions.assertTrue(app.driver.manage().logs().get(l).getAll().isEmpty(),
                    String.format("URL: %s\nLog type: %s\nLog Strings: %s", url, l, app.driver.manage().logs().get(l).getAll().toString())));
            // возвращаемся на страницу со списком товаров
            app.driver.findElement(By.cssSelector("button[name='cancel']")).click();
        }
    }
}
