package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class StickerTests extends TestBase {

    private boolean findOneSticker(WebElement product) { // метод проверки наличия ровно одного стикера у товара
        return product.findElements(By.className("sticker")).size() == 1;
    }

    @Test
    void stickerCheck() {
        app.driver.get("http://localhost/litecart");

        var products = app.driver.findElements(By.className("product")); // ищем все карточки товаров на странице
        boolean stickersOk = true; // для хранения итогового результата проверки
        for (var product : products) {
            stickersOk = findOneSticker(product) && stickersOk;
        }
        // если есть хоть один товар с некорректным количеством стикеров - тест провален
        Assertions.assertTrue(stickersOk);
    }
}
