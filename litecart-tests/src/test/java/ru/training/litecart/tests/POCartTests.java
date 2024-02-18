package ru.training.litecart.tests;

import org.junit.jupiter.api.Test;

public class POCartTests extends TestBase {

    // Объект "app" класса ApplicationManager создаётся в классе TestBase c помощью метода setUp() с аннотацией @BeforeEach.
    // Далее, для объекта "app" из класса ApplicationManager вызывается метод init(), который инициализирует драйвер.
    // Для непосредственной работы с соответствующими страницами определен класс HelperBase и дочерние ему классы
    // CustomerMainPage, CustomerProductPage и CustomerCartPage, содержащие в себе методы-помощники. Объекты этих классов
    // создаются с помощью "ленивой" инициализации.

    @Test
    void canWorkWithCart() {
        app.customerMainPage().openMainPage();
        for (int i = 1; i <= 3; i++) {
            app.customerMainPage().clickOnFirstProduct();
            app.customerProductPage().addProductToCart("Small");
            app.customerMainPage().goToHomepage();
        }
        app.customerMainPage().goToCart();
        app.customerCartPage().removeAllProductsOneByOne();
        app.customerMainPage().goToHomepage();
    }
}
