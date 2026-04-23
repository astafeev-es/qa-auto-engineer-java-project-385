package hexlet.code.pages.login;

import hexlet.code.pages.BasePage;
import hexlet.code.pages.DashboardPage;
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
        wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        
        passwordField.clear();
        passwordField.sendKeys(password);
        
        signInButton.click();

        return new DashboardPage(driver, wait);
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(usernameField)).isDisplayed()
            &&  wait.until(ExpectedConditions.visibilityOf(passwordField)).isDisplayed()
            && wait.until(ExpectedConditions.visibilityOf(signInButton)).isDisplayed();
    }
}
