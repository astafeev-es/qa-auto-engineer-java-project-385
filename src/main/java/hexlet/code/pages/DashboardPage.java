package hexlet.code.pages;

import hexlet.code.pages.login.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//button[contains(., 'Jane Doe')]")
    private WebElement profileButton;

    @FindBy(xpath = "//*[@role='menuitem' and contains(., 'Logout')]")
    private WebElement logoutButton;

    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(profileButton)).click();
        wait.until(ExpectedConditions.visibilityOf(logoutButton)).click();

        return new LoginPage(driver, wait);
    }

    public boolean isProfileVisible() {
        return wait.until(ExpectedConditions.visibilityOf(profileButton)).isDisplayed();
    }
}
