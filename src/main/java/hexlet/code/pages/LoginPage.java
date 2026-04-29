package hexlet.code.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement signInButton;

    @FindBy(name = "email")
    private WebElement emailField;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public DashboardPage login(String username, String password) {
        wait.withMessage("Username field should be clickable")
            .until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        signInButton.click();

        return new DashboardPage(driver, wait);
    }

    public boolean isLoginPageDisplayed() {
        return wait.withMessage("Username field should be visible")
                .until(ExpectedConditions.visibilityOf(usernameField))
                .isDisplayed()
            && wait.withMessage("Password field should be visible")
                .until(ExpectedConditions.visibilityOf(passwordField))
                .isDisplayed()
            && wait.withMessage("Sign in button should be visible")
                .until(ExpectedConditions.visibilityOf(signInButton))
                .isDisplayed();
    }
}
