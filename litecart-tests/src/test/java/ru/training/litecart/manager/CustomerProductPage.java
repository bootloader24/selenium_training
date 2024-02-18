package ru.training.litecart.manager;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class CustomerProductPage extends HelperBase {
    public CustomerProductPage(ApplicationManager manager) {
        super(manager);
    }

    public void addProductToCart(String size) {
        // сохраняем текущее значение количества товаров в корзине
        var oldCartQuantity = Integer.parseInt(manager.driver.findElement(By.cssSelector("#cart-wrapper .quantity"))
                .getText());
        // для товаров у которых есть выбор размера, нужно его обязательно выбрать, иначе кнопка добавления неактивна
        if (isElementPresent(By.name("options[Size]"))) {
            Select sizeSelect = new Select(manager.driver.findElement(By.name("options[Size]")));
            sizeSelect.selectByValue(size);
        }
        // кликаем на кнопке добавления товара в корзину
        manager.driver.findElement(By.name("add_cart_product")).click();
        // ждём обновления счётчика товара в корзине
        manager.durationWait(5)
                .until(textToBe(By.cssSelector("#cart-wrapper .quantity"), Integer.toString(oldCartQuantity + 1)));
    }
}
