package ru.training.litecart.tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import ru.training.litecart.common.CommonFunctions;

public class UserRegistrationTests extends TestBase {

    private record UserAccount(String email, String password) {
    }

    // метод заполнения и отправки формы регистрации пользователя
    // (здесь можно было бы использовать генератор тестовых данных,
    // но в контексте данного курса наверное нет смысла с этим заморачиваться)
    private UserAccount fillForm(WebElement regForm) {
        // генерируем случайные email и пароль
        var email = String.format("%s@localhost", CommonFunctions.randomString(10));
        var password = CommonFunctions.randomString(10);

        // заполняем все поля формы регистрации
        regForm.findElement(By.name("tax_id")).sendKeys("123456789");
        regForm.findElement(By.name("company")).sendKeys("Some company");
        regForm.findElement(By.name("firstname")).sendKeys(CommonFunctions.randomString(10));
        regForm.findElement(By.name("lastname")).sendKeys(CommonFunctions.randomString(10));
        regForm.findElement(By.name("address1")).sendKeys(CommonFunctions.randomString(10));
        regForm.findElement(By.name("address2")).sendKeys(CommonFunctions.randomString(10));
        regForm.findElement(By.name("postcode")).sendKeys("54321");
        regForm.findElement(By.name("city")).sendKeys("Dallas");
        Select countrySelect = new Select(regForm.findElement(By.cssSelector("select[name=country_code]")));
        countrySelect.selectByVisibleText("United States");
        Select zoneSelect = new Select(regForm.findElement(By.cssSelector("select[name=zone_code]")));
        zoneSelect.selectByVisibleText("Texas");
        regForm.findElement(By.name("email")).sendKeys(email);
        regForm.findElement(By.name("phone")).sendKeys("0-800-0292-047");
        regForm.findElement(By.cssSelector("input[name=newsletter]")).click();
        regForm.findElement(By.name("password")).sendKeys(password);
        regForm.findElement(By.name("confirmed_password")).sendKeys(password);
        // нажимаем кнопку создания аккаунта
        regForm.findElement(By.cssSelector("button[name=create_account]")).click();
        // возвращаем запись с парой е-мейла и пароля
        return new UserAccount(email, password);
    }

    // метод логина, в качестве аргумента принимает запись аккаунта
    private void login(UserAccount userAccount) {
        // находим форму логина
        var loginForm = app.driver.findElement(By.name("login_form"));
        // вводим в соответствующие поля е-мейл и пароль
        loginForm.findElement(By.name("email")).sendKeys(userAccount.email());
        loginForm.findElement(By.name("password")).sendKeys(userAccount.password());
        // нажимаем кнопку логина
        loginForm.findElement(By.cssSelector("button[name=login]")).click();
        // ищем признак успешности логина, например блок box-account
        app.driver.findElement(By.id("box-account"));
    }

    // метод логаута
    private void logout() {
        // находим блок аккаунта пользователя
        var accountBox = app.driver.findElement(By.id("box-account"));
        // нажимаем ссылку "Logout"
        accountBox.findElement(By.xpath("//a[contains(text(), 'Logout')]")).click();
        // ищем признак успешности логаута, например форму логина
        app.driver.findElement(By.name("login_form"));
    }

    @Test
    public void canRegisterUser() {
        app.driver.get("http://localhost/litecart");
        // находим форму логина
        var loginForm = app.driver.findElement(By.name("login_form"));
        // нажимаем ссылку "New customers click here"
        loginForm.findElement(By.xpath("//a[contains(text(), 'New customers')]")).click();
        // находим форму регистрации
        var regForm = app.driver.findElement(By.name("customer_form"));
        // заполняем и отправляем форму регистрации
        var userAccount = fillForm(regForm);
        // делаем логаут, логин и еще раз логаут
        logout();
        login(userAccount);
        logout();
    }
}