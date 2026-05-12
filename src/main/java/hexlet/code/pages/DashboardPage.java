package hexlet.code.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//*[contains(text(), 'Lorem ipsum sic dolor amet')]")
    private WebElement dashboardText;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Dashboard page")
    public void open() {
        elementHelper.click(dashboardMenuItem);
    }

    @Step("Check if Dashboard page is displayed")
    public boolean isDashboardPageDisplayed() {
        return elementHelper.isVisible(dashboardText);
    }
}
