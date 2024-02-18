package ru.training.litecart.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.training.litecart.manager.HelperBase;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class CartTests extends TestBase {

    @Test
    void canWorkWithCart() {
        // задаём порог явного ожидания
        WebDriverWait wait = new WebDriverWait(app.driver, Duration.ofSeconds(5));
        TestBase.app.driver.get("http://localhost/litecart");
        // в цикле трижды повторяем добавление товара в корзину
        for (int i = 1; i <= 3; i++) {
            // кликаем на элементе первого товара на главной странице
            app.driver.findElement(By.cssSelector(".product")).click();
            // для товаров у которых есть выбор размера, нужно его обязательно выбрать, иначе кнопка добавления неактивна
            if (isElementPresent(By.name("options[Size]"))) {
                Select sizeSelect = new Select(app.driver.findElement(By.name("options[Size]")));
                sizeSelect.selectByVisibleText("Small");
            }
            // кликаем на кнопке добавления товара в корзину
            app.driver.findElement(By.name("add_cart_product")).click();
            // ждём обновления счётчика товара в корзине
            wait.until(textToBe(By.cssSelector("#cart-wrapper .quantity"), Integer.toString(i)));
            // возвращаемся на главную страницу
            app.driver.findElement(By.cssSelector("#site-menu li.general-0")).click();
        }
        // после добавления 3-х товаров переходим в корзину
        app.driver.findElement(By.cssSelector("#cart a.link")).click();
        // считаем количество наименований в общей таблице
        // (как вариант, можно еще посчитать количество элементов с классом "shortcut")
        var count = app.driver.findElements(By.cssSelector("table.dataTable td.item")).size();
        // кликаем на кнопке Remove <count> раз
        for (int i = 1; i <= count; i++) {
            // сохраняем элемент общей таблицы
            var dataTable = app.driver.findElement(By.cssSelector("table.dataTable"));
            // кликаем на Remove
            app.driver.findElement(By.cssSelector("button[name='remove_cart_item']")).click();
            // ожидаем "устаревания" сохранённого элемента таблицы
            wait.until(ExpectedConditions.stalenessOf(dataTable));
        }
        // Ожидаем появления элемента с текстом "There are no items in your cart."
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("em"), "There are no items"));
        // Возвращаемся на домашнюю страницу
        app.driver.findElement(By.cssSelector("#site-menu li.general-0")).click();
    }

}
