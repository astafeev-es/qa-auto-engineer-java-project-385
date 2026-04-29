package hexlet.code.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public boolean isProfileVisible() {
        return wait.withMessage("Profile button should be visible")
                .until(ExpectedConditions.visibilityOf(profileButton))
                .isDisplayed();
    }
}
