package hexlet.code.pages;

import hexlet.code.config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement signInButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Login page")
    public void open() {
        driver.get(Config.get().baseUrl());
    }

    @Step("Login as {username}")
    public DashboardPage login(String username, String password) {
        elementHelper.sendKeys(usernameField, username);
        elementHelper.sendKeys(passwordField, password);
        elementHelper.click(signInButton);

        return new DashboardPage(driver);
    }

    @Step("Check if Login page is displayed")
    public boolean isLoginPageDisplayed() {
        return elementHelper.isVisible(usernameField)
            && elementHelper.isVisible(passwordField)
            && elementHelper.isVisible(signInButton);
    }
}
