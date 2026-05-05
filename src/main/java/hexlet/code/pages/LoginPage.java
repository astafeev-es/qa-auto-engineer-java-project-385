package hexlet.code.pages;

import hexlet.code.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement signInButton;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public void open() {
        driver.get(Config.get().baseUrl());
    }

    public DashboardPage login(String username, String password) {
        elementHelper.sendKeys(usernameField, username);
        elementHelper.sendKeys(passwordField, password);
        elementHelper.click(signInButton);

        return new DashboardPage(driver, wait);
    }

    public boolean isLoginPageDisplayed() {
        return elementHelper.isVisible(usernameField)
            && elementHelper.isVisible(passwordField)
            && elementHelper.isVisible(signInButton);
    }
}
