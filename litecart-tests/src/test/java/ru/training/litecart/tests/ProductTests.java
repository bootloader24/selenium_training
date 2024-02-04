package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.Color;


public class ProductTests extends TestBase {

    // запись для свойств описания товара
    record ProductData(String name,                    // название товара
                       String regularPriceValue,       // значение обычной цены
                       String regularPriceColor,       // цвет обычной цены
                       String regularPriceDecoration,  // оформление текста обычной цены
                       String regularPriceSize,        // размер текста обычной цены
                       String campaignPriceValue,      // значение акционной цены
                       String campaignPriceColor,      // цвет акционной цены
                       String campaignPriceWeight,     // толщина текста акционной цены
                       String campaignPriceSize)       // размер текста акционной цены
    {
    };

    // Метод проверки серого цвета
    private boolean colorIsGray(String color) {
        var colorHex = Color.fromString(color).asHex();
        var r = colorHex.substring(1, 3);
        var g = colorHex.substring(3, 5);
        var b = colorHex.substring(5, 7);
        return r.equals(g) && r.equals(b);
    }

    // Метод проверки красного цвета
    private boolean colorIsRed(String color) {
        var colorHex = Color.fromString(color).asHex();
        var g = colorHex.substring(3, 5);
        var b = colorHex.substring(5, 7);
        return g.equals("00") && b.equals("00");
    }

    // Метод проверки размера текста
    private boolean isLarger(String fontSize1, String fontSize2) {
        float size1 = Float.parseFloat(fontSize1.replace("px", ""));
        float size2 = Float.parseFloat(fontSize2.replace("px", ""));
        return size1 > size2;
    }

    // Метод проверки, что текст зачёркнут
    private boolean isStrikethrough(String textDecoration) {
        return textDecoration.contains("line-through");
    }

    // Метод проверки, что текст жирный
    public boolean isBold(String fontWeight) {
        int weight = Integer.parseInt(fontWeight);
        return weight >= 700;
    }

    @Test
    void productPageTest() {
        app.driver.get("http://localhost/litecart");
        // находим элемент первого товара в блоке Campaigns на главной странице
        var productCard = app.driver.findElement(By.cssSelector("#box-campaigns .product"));
        // получаем все нужные для проверок значения с карточки товара
        var dataOnMainPage = new ProductData(
                productCard.findElement(By.cssSelector(".name")).getText(),
                productCard.findElement(By.cssSelector(".regular-price")).getText(),
                productCard.findElement(By.cssSelector(".regular-price")).getCssValue("color"),
                productCard.findElement(By.cssSelector(".regular-price")).getCssValue("text-decoration"),
                productCard.findElement(By.cssSelector(".regular-price")).getCssValue("font-size"),
                productCard.findElement(By.cssSelector(".campaign-price")).getText(),
                productCard.findElement(By.cssSelector(".campaign-price")).getCssValue("color"),
                productCard.findElement(By.cssSelector(".campaign-price")).getCssValue("font-weight"),
                productCard.findElement(By.cssSelector(".campaign-price")).getCssValue("font-size")
        );
        // кликаем по карточке товара для перехода на его страницу
        productCard.click();
        // находим на странице блок, содержащий описание товара
        var productBox = app.driver.findElement(By.cssSelector("#box-product"));
        // получаем все нужные для проверок значения из блока описания товара
        var dataOnProductPage = new ProductData(
                productBox.findElement(By.cssSelector("h1.title")).getText(),
                productBox.findElement(By.cssSelector(".regular-price")).getText(),
                productBox.findElement(By.cssSelector(".regular-price")).getCssValue("color"),
                productBox.findElement(By.cssSelector(".regular-price")).getCssValue("text-decoration"),
                productBox.findElement(By.cssSelector(".regular-price")).getCssValue("font-size"),
                productBox.findElement(By.cssSelector(".campaign-price")).getText(),
                productBox.findElement(By.cssSelector(".campaign-price")).getCssValue("color"),
                productBox.findElement(By.cssSelector(".campaign-price")).getCssValue("font-weight"),
                productBox.findElement(By.cssSelector(".campaign-price")).getCssValue("font-size")
        );

        // проверяем, что на главной странице и на странице товара совпадает текст названия товара
        Assertions.assertEquals(dataOnMainPage.name(), dataOnProductPage.name());

        // проверяем, что на главной странице и на странице товара совпадают цены (обычная и акционная)
        Assertions.assertEquals(dataOnMainPage.regularPriceValue(), dataOnProductPage.regularPriceValue());
        Assertions.assertEquals(dataOnMainPage.campaignPriceValue(), dataOnProductPage.campaignPriceValue());

        // проверяем, что обычная цена зачёркнутая и серая
        Assertions.assertTrue(isStrikethrough(dataOnMainPage.regularPriceDecoration()));
        Assertions.assertTrue(colorIsGray(dataOnMainPage.regularPriceColor()));
        Assertions.assertTrue(isStrikethrough(dataOnProductPage.regularPriceDecoration()));
        Assertions.assertTrue(colorIsGray(dataOnProductPage.regularPriceColor()));

        // проверяем, что акционная цена жирная и красная
        Assertions.assertTrue(isBold(dataOnMainPage.campaignPriceWeight()));
        Assertions.assertTrue(colorIsRed(dataOnMainPage.campaignPriceColor()));
        Assertions.assertTrue(isBold(dataOnProductPage.campaignPriceWeight()));
        Assertions.assertTrue(colorIsRed(dataOnProductPage.campaignPriceColor()));

        // проверяем, что акционная цена крупнее, чем обычная
        Assertions.assertTrue(isLarger(dataOnMainPage.campaignPriceSize(), dataOnMainPage.regularPriceSize()));
        Assertions.assertTrue(isLarger(dataOnProductPage.campaignPriceSize(), dataOnProductPage.regularPriceSize()));
    }
}

