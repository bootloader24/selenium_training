package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.training.litecart.model.CountriesData;

import java.util.List;
import java.util.stream.Collectors;

public class CountryTests extends TestBase {

    // метод для получения данных о странах из строк таблицы со страницы "Countries".
    // данные сохраняются в списке записей со структурой, описанной в модельном классе CountriesData
    private List<CountriesData> getCountriesList(List<WebElement> tableRows) {
        return tableRows.stream()
                .map(tableRow -> {
                    var countryName = tableRow.findElement(By.cssSelector("td:nth-child(5) > a")).getText();
                    var countryLink = tableRow.findElement(By.cssSelector("td:nth-child(5) > a")).getAttribute("href");
                    var zonesCount = tableRow.findElement(By.cssSelector("td:nth-child(6)")).getText();
                    return new CountriesData()
                            .setCountryName(countryName)
                            .setCountryLink(countryLink)
                            .setZonesCount(Integer.parseInt(zonesCount));
                })
                .collect(Collectors.toList());
    }

    // метод проверки правильности сортировки списка строк
    // возвращает true, если сортировка корректна
    private boolean checkSorting(List<String> strings) {
        String previous = "";
        for (var current : strings) {
            if (current.compareTo(previous) < 0)
                return false;
            previous = current;
        }
        return true;
    }

    // метод проверки правильности сортировки геозон для каждой страны, где количество геозон > 0
    // возвращает true, если сортировка списков геозон всех стран корректна
    private boolean checkGeozonesSorting(List<CountriesData> countriesList) {
        boolean result = true;
        for (var country : countriesList) {
            if (country.getZonesCount() > 0) {
                app.driver.get(country.getCountryLink());
                List<WebElement> geozones = app.driver.findElements(By.
                        cssSelector("#table-zones td:nth-child(3) [type=hidden]"));
                List<String> geozonesNames = geozones.stream()
                        .map(geozone -> geozone.getAttribute("value"))
                        .toList();
                result = checkSorting(geozonesNames) && result;
            }
        }
        return result;
    }

    @Test
    void CountriesAndGeozonesSortingTest() {
        app.session().adminLogin("admin", "admin");
        app.driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");

        var tableRows = app.driver.findElements(By.className("row"));
        var countriesList = getCountriesList(tableRows);
        List<String> countriesNamesList = countriesList
                .stream()
                .map(CountriesData::getCountryName)
                .toList();

        // проверка, что страны расположены в алфавитном порядке:
        Assertions.assertTrue(checkSorting(countriesNamesList));
        // проверка, что геозоны расположены в алфавитном порядке, для стран, у которых количество зон отлично от нуля:
        Assertions.assertTrue(checkGeozonesSorting(countriesList));
    }
}
