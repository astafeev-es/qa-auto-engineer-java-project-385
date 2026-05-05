package hexlet.code.pages;

import hexlet.code.utils.ElementHelper;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
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

    protected BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementHelper = new ElementHelper(wait);
        PageFactory.initElements(driver, this);
    }

    public abstract void open();

    public void submit() {
        elementHelper.click(submitButton);
    }

    public String getFieldValue(String fieldName) {
        By locator = By.name(fieldName);
        WebElement element = wait.withMessage("Field should be visible: " + fieldName)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getAttribute("value");
    }

    public boolean isColumnVisible(String columnName) {
        return driver.findElements(By.xpath("//th[contains(., '" + columnName + "')]")).size() > 0;
    }

    private int getColumnIndex(String columnName) {
        List<WebElement> headers = driver.findElements(By.xpath("//th"));
        int index = 0;
        for (WebElement header : headers) {
            if (header.getText().contains(columnName)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public String getCellValue(int rowIndex, String columnName) {
        int colIndex = getColumnIndex(columnName);
        if (colIndex == -1) {
            return null;
        }

        // rowIndex is 1-based for XPath
        String cellXpath = "//tr[%d]/td[%d]".formatted(rowIndex, colIndex + 1);
        return wait.withMessage("Cell in row " + rowIndex + " and column '" + columnName + "' should be present")
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(cellXpath))).getText();
    }

    // Overload for backward compatibility or searching by text if needed
    public String getCellValue(String rowSearchText, String columnName) {
        int colIndex = getColumnIndex(columnName);
        if (colIndex == -1) {
            return null;
        }

        String cellXpath = "//tr[td[contains(., '%s')]]/td[%d]".formatted(rowSearchText, colIndex + 1);
        return wait.withMessage("Cell with value '" + rowSearchText
                        + "' and column '" + columnName + "' should be present")
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(cellXpath))).getText();
    }

    public void selectFromCombobox(String label, String value) {
        String comboXp = "//div[label[contains(., '%s')]]//div[@role='combobox']".formatted(label);
        WebElement combo = wait.withMessage("Combobox with label '" + label + "' should be clickable")
                .until(ExpectedConditions.elementToBeClickable(By.xpath(comboXp)));
        elementHelper.click(combo);

        String optXp = "//li[@role='option' and contains(., '%s')]".formatted(value);
        WebElement option = wait.withMessage("Option with value '" + value + "' should be clickable")
                .until(ExpectedConditions.elementToBeClickable(By.xpath(optXp)));
        elementHelper.click(option);
    }

    public LoginPage logout() {
        elementHelper.click(profileButton);
        elementHelper.click(logoutButton);

        return new LoginPage(driver, wait);
    }
}
