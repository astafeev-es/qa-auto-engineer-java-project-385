package hexlet.code.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//*[contains(text(), 'Lorem ipsum sic dolor amet')]")
    private WebElement dashboardText;

    public DashboardPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public void open() {
        elementHelper.click(dashboardMenuItem);
    }

    public boolean isDashboardPageDisplayed() {
        return elementHelper.isVisible(dashboardText);
    }

    public boolean isProfileVisible() {
        return elementHelper.isVisible(profileButton);
    }
}
