package ru.training.litecart.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class CountryExternalLinkTests extends TestBase {

    // метод, ожидающий появление нового окна
    private String waitForNewWindow(WebDriverWait wait, Set<String> oldWindows) {
        String newWindowHandle = wait.until((ExpectedCondition<String>) webDriver -> {
            Set<String> handles = webDriver.getWindowHandles();
            handles.removeAll(oldWindows);
            return handles.size() > 0 ? handles.iterator().next() : null;
        });
        return newWindowHandle;
    }

    @Test
    @DisplayName("Тест открытия внешних ссылок в новом окне")
    public void isOpenInNewWindowTest() {
        // задаём порог явного ожидания
        WebDriverWait wait = new WebDriverWait(app.driver, Duration.ofSeconds(5));
        // логинимся, переходим в раздел "Countries" и кликаем по кнопке "Add New Country"
        adminLogin("admin", "admin");
        app.driver.findElement(By.cssSelector("#box-apps-menu a[href*='app=countries&doc=countries']")).click();
        app.driver.findElement(By.cssSelector("#content a.button[href*='app=countries&doc=edit_country']")).click();
        // собираем из формы все элементы со ссылками на внешние ресурсы
        var externalLinks = app.driver.findElements(By.cssSelector("form table i.fa-external-link"));
        // получаем дескриптор текущего окна с открытой админкой litecart
        String mainWindow = app.driver.getWindowHandle();
        // получаем дескрипторы всех открытых окон
        Set<String> oldWindows = app.driver.getWindowHandles();
        // пробегаемся по всем элементам со ссылками
        for (var externalLink : externalLinks) {
            // кликаем на элемент
            externalLink.click();
            // ожидаем появления нового окна и получаем его дескриптор
            String newWindow = waitForNewWindow(wait, oldWindows);
            // переключаем контекст драйвера на новое окно
            app.driver.switchTo().window(newWindow);
            // дополнительно можем проверить заголовок страницы в новом окне
            wait.until(ExpectedConditions.or
                    (ExpectedConditions.titleContains("- Wikipedia"), ExpectedConditions.titleContains("| Informatica")));
            // закрываем новое окно
            app.driver.close();
            // переключаем контекст драйвера на окно с открытой админкой litecart
            app.driver.switchTo().window(mainWindow);
        }
    }
}
