package ru.training.litecart.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class CountryTests extends TestBase {

    record CountriesData(String countryName, int zonesCount) {
        public CountriesData() {
            this("", 0);
        }

        public CountriesData setCountryName(String countryName) {
            return new CountriesData(countryName, this.zonesCount);
        }

        public CountriesData setZonesCount(int zonesCount) {
            return new CountriesData(this.countryName, zonesCount);
        }

    }

    public List<CountriesData> getCountriesList(List<WebElement> tableRows) {
        return tableRows.stream()
                .map(tableRow -> {
                    var countryName = tableRow.findElement(By.cssSelector("td:nth-child(5) > a")).getText();
                    var zonesCount = tableRow.findElement(By.cssSelector("td:nth-child(6)")).getText();
                    return new CountriesData().setCountryName(countryName).setZonesCount(Integer.parseInt(zonesCount));
                })
                .collect(Collectors.toList());
    }
    @Test
    void checkCountriesAndGeozonesSorting() {
        app.session().adminLogin("admin", "admin");
        app.driver.get("http://localhost/litecart/admin/?app=countries&doc=countries");

        var tableRows = app.driver.findElements(By.className("row"));
        var countries = getCountriesList(tableRows);

    }
}
