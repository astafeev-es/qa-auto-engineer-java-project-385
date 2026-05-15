package hexlet.code.pages;

import hexlet.code.utils.ElementHelper;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {
    protected WebDriver driver;
    protected ElementHelper elementHelper;

    @FindBy(xpath = "//a[contains(text(), 'Dashboard')]")
    protected WebElement dashboardMenuItem;

    @FindBy(xpath = "//a[contains(text(), 'Tasks')]")
    protected WebElement tasksMenuItem;

    @FindBy(xpath = "//a[contains(text(), 'Users')]")
    protected WebElement usersMenuItem;

    @FindBy(xpath = "//a[contains(text(), 'Labels')]")
    protected WebElement labelsMenuItem;

    @FindBy(xpath = "//a[contains(text(), 'Task statuses')]")
    protected WebElement taskStatusesMenuItem;

    @FindBy(xpath = "//button[contains(., 'Jane Doe')]")
    protected WebElement profileButton;

    @FindBy(xpath = "//*[@role='menuitem' and contains(., 'Logout')]")
    protected WebElement logoutButton;

    @FindBy(xpath = "//button[@type='submit']")
    protected WebElement submitButton;

    @FindBy(xpath = "//a[contains(., 'Create')]")
    protected WebElement createButton;

    @FindBy(xpath = "//button[@aria-label='Delete']")
    protected WebElement deleteButton;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.elementHelper = new ElementHelper(driver);
        PageFactory.initElements(driver, this);
    }

    public abstract void open();

    @Step("Submit form")
    public void submit() {
        elementHelper.click(submitButton);
    }

    @Step("Get field value: {fieldName}")
    public String getFieldValue(String fieldName) {
        By locator = By.name(fieldName);
        return elementHelper.getAttribute(locator, "value");
    }

    @Step("Check if column '{columnName}' is visible")
    public boolean isColumnVisible(String columnName) {
        return elementHelper.isPresent(By.xpath("//th[contains(., '" + columnName + "')]"));
    }

    private int getColumnIndex(String columnName) {
        List<WebElement> headers = elementHelper.findElements(By.xpath("//th"));
        int index = 0;
        for (WebElement header : headers) {
            if (header.getText().contains(columnName)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Step("Get cell value for row with text '{rowSearchText}' and column '{columnName}'")
    public String getCellValue(String rowSearchText, String columnName) {
        int colIndex = getColumnIndex(columnName);
        if (colIndex == -1) {
            return null;
        }

        String cellXpath = "//tr[td[contains(., '%s')]]/td[%d]".formatted(rowSearchText, colIndex + 1);
        return elementHelper.getText(By.xpath(cellXpath));
    }

    @Step("Select '{value}' from combobox '{label}'")
    public void selectFromCombobox(String label, String value) {
        String comboXp = "//div[label[contains(., '%s')]]//div[@role='combobox']".formatted(label);
        WebElement combo = elementHelper.findElement(By.xpath(comboXp));
        elementHelper.click(combo);

        String optXp = "//li[@role='option' and contains(., '%s')]".formatted(value);
        WebElement option = elementHelper.findElement(By.xpath(optXp));
        elementHelper.click(option);
    }

    @Step("Press Escape key")
    public void pressEscape() {
        elementHelper.pressEscape();
    }

    @Step("Logout")
    public LoginPage logout() {
        elementHelper.click(profileButton);
        elementHelper.click(logoutButton);

        return new LoginPage(driver);
    }
}
