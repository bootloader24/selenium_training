package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.training.litecart.common.CommonFunctions;

import java.util.List;

public class GeozonesTests extends TestBase {

    @Test
    void geozonesSortingTest() {
        app.session().adminLogin("admin", "admin");
        app.driver.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");

        // получаем элементы строк из таблицы со странами:
        var tableRows = app.driver.findElements(By.className("row"));

        // из каждой строки извлекаем ссылки на страницы зон и формируем из них список:
        List<String> zonesLinks = tableRows
                .stream()
                .map(link -> link.findElement(By.tagName("a")).getAttribute("href"))
                .toList();

        // в цикле переходим по ссылкам из списка:
        for (var link : zonesLinks) {
            app.driver.get(link);
            // после перехода на страницу с таблицей геозон извлекаем элементы выбранных в каждой строке зон:
            var selects = app.driver.findElements(By.cssSelector("#table-zones td:nth-child(3) select > [selected]"));

            // из списка элементов с выбранными зонами формируем список строк с названиями зон
            List<String> geozonesList = selects
                    .stream()
                    .map(WebElement::getText)
                    .toList();
            // проверяем, что список с названиями зон корректно отсортирован:
            Assertions.assertTrue(CommonFunctions.checkSorting(geozonesList));
        }
    }
}
