package ru.training.litecart.manager;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CustomerCartPage extends HelperBase {
    public CustomerCartPage(ApplicationManager manager) {
        super(manager);
    }

    public void removeAllProductsOneByOne() {
        // считаем количество наименований в общей таблице
        // (как вариант, можно еще посчитать количество элементов с классом "shortcut")
        var count = manager.driver.findElements(By.cssSelector("table.dataTable td.item")).size();
        // кликаем на кнопке Remove <count> раз
        for (int i = 1; i <= count; i++) {
            // сохраняем элемент общей таблицы
            var dataTable = manager.driver.findElement(By.cssSelector("table.dataTable"));
            // кликаем на Remove
            manager.driver.findElement(By.cssSelector("button[name='remove_cart_item']")).click();
            // ожидаем "устаревания" сохранённого элемента таблицы
            manager.durationWait(5).until(ExpectedConditions.stalenessOf(dataTable));
        }
        // Ожидаем появления элемента с текстом "There are no items in your cart."
        manager.durationWait(5).until(ExpectedConditions
                .textToBePresentInElementLocated(By.tagName("em"), "There are no items"));
    }

}
