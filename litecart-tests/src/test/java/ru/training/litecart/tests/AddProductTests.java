package ru.training.litecart.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.training.litecart.common.CommonFunctions;

import java.nio.file.Paths;
import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AddProductTests extends TestBase {

    // определяем запись для хранения параметров товара
    record Product(
            String name,
            String code,
            String quantity,
            String image,
            String dateValidFrom,
            String dateValidTo,
            String manufacturer,
            String keywords,
            String shortDescription,
            String description,
            String headTitle,
            String metaDescription,
            String purchasePrice,
            String purchaseCurrency,
            String grossPriceUsd,
            String grossPriceEur) {
    }

    // задаём время ожидания загрузки вкладки
    Duration tabDuration = Duration.ofSeconds(2);

    // метод-помощник для заполнения предзаполненных полей
    protected void typeToMaskedField(WebElement element, String text) {
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    // метод-помощник для заполнения полей выбора файла
    protected void attach(WebElement element, String file) {
        if (!file.isEmpty()) {
            element.sendKeys(Paths.get(file).toAbsolutePath().toString());
        }
    }

    // метод-помощник для ввода даты в DatePicker'е
    private static void setDatePicker(WebElement datePicker, String date) {
        JavascriptExecutor js = (JavascriptExecutor) app.driver;
        js.executeScript(
                "arguments[0].setAttribute('value', '" + date + "'); arguments[0].dispatchEvent(new Event('change'))",
                datePicker);
    }

    // метод, заполняющий вкладку General
    private void fillGeneralTab(Product product) {
        app.driver.findElement(By.cssSelector(".tabs a[href='#tab-general']")).click();
        // в задании указано, что нужно просто добавить паузу, но всё-таки решил сделать неявное ожидание
        WebDriverWait wait = new WebDriverWait(app.driver, tabDuration);
        var tabGeneral = wait.until(visibilityOfElementLocated(By.id("tab-general")));
        tabGeneral.findElement(By.cssSelector("input[name='status'][value='1']")).click();
        tabGeneral.findElement(By.cssSelector("input[name='name[en]']")).sendKeys(product.name());
        tabGeneral.findElement(By.cssSelector("input[name='code']")).sendKeys(product.code());
        tabGeneral.findElement(By.cssSelector("input[name='product_groups[]'][value='1-3']")).click();
        typeToMaskedField(tabGeneral.findElement(By.cssSelector("input[name='quantity']")), product.quantity());
        attach(tabGeneral.findElement(By.name("new_images[]")), product.image());
        setDatePicker(tabGeneral.findElement(By.name("date_valid_from")), product.dateValidFrom());
        setDatePicker(tabGeneral.findElement(By.name("date_valid_to")), product.dateValidTo());
    }

    // метод, заполняющий вкладку Information
    private void fillInformationTab(Product product) {
        app.driver.findElement(By.cssSelector(".tabs a[href='#tab-information']")).click();
        WebDriverWait wait = new WebDriverWait(app.driver, tabDuration);
        var tabInformation = wait.until(visibilityOfElementLocated(By.id("tab-information")));
        Select manufacturerId = new Select(tabInformation.findElement(By.name("manufacturer_id")));
        manufacturerId.selectByVisibleText(product.manufacturer());
        tabInformation.findElement(By.name("keywords")).sendKeys(product.keywords());
        tabInformation.findElement(By.name("short_description[en]")).sendKeys(product.shortDescription());
        tabInformation.findElement(By.className("trumbowyg-editor")).sendKeys(product.description());
        tabInformation.findElement(By.name("head_title[en]")).sendKeys(product.headTitle());
        tabInformation.findElement(By.name("meta_description[en]")).sendKeys(product.metaDescription());
    }

    // метод, заполняющий вкладку Prices
    private void fillPricesTab(Product product) {
        app.driver.findElement(By.cssSelector(".tabs a[href='#tab-prices']")).click();
        WebDriverWait wait = new WebDriverWait(app.driver, tabDuration);
        var tabPrices = wait.until(visibilityOfElementLocated(By.id("tab-prices")));
        typeToMaskedField(tabPrices.findElement(By.name("purchase_price")), product.purchasePrice());
        Select purchasePrice = new Select(tabPrices.findElement(By.name("purchase_price_currency_code")));
        purchasePrice.selectByVisibleText(product.purchaseCurrency());
        typeToMaskedField(tabPrices.findElement(By.name("gross_prices[USD]")), product.grossPriceUsd());
        typeToMaskedField(tabPrices.findElement(By.name("gross_prices[EUR]")), product.grossPriceEur());
    }


    @Test
    @DisplayName("Тест добавления продукта в админке")
    public void canAddProduct() {

        // создаём продукт с некими параметрами
        var product = new Product(
                "Big Teddy bear " + CommonFunctions.randomString(5),
                "tb_ " + CommonFunctions.randomString(3),
                "123",
                CommonFunctions.randomFile("src/test/resources/images"),
                "2024-01-01",
                "2024-12-31",
                "ACME Corp.",
                "soft toy, teddy, bear, vodka, balalaika",
                "toy teddy bear",
                "The soft toy \"Teddy Bear\" will not leave either an adult or a child indifferent",
                "Big Teddy bear",
                "brown toy teddy bear",
                "10",
                "Euros",
                "20",
                "18");

        // логинимся
        app.session().adminLogin("admin", "admin");
        // кликаем на разделе Catalog в меню
        app.driver.findElement(By.cssSelector("#app- a[href*='app=catalog']")).click();
        // кликаем на кнопке Add New Product
        app.driver.findElement(By.cssSelector("a.button[href*='category_id=0&app=catalog&doc=edit_product']")).click();
        // заполняем требуемые вкладки по порядку
        fillGeneralTab(product);
        fillInformationTab(product);
        fillPricesTab(product);
        // кликаем на кнопке Save
        app.driver.findElement(By.cssSelector(".button-set button[name=save]")).click();
        // Ищем таблицу с товарами каталога
        var dataTable = app.driver.findElement(By.className("dataTable"));
        // в таблице ищем добавленный товар по его имени
        dataTable.findElement(By.
                xpath(String.format("//tr[@class='row']//a[text()='%s']", product.name())));
    }
}
