package hexlet.code.pages;

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

    @FindBy(xpath = "//button[@type='submit']")
    protected WebElement submitButton;

    @FindBy(xpath = "//a[contains(., 'Create')]")
    protected WebElement createButton;

    @FindBy(xpath = "//button[@aria-label='Delete']")
    protected WebElement deleteButton;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                return;
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                // Wait for any MUI Snackbars (notifications) to disappear
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("MuiSnackbar-root")));
                } catch (Exception ignored) {
                    // Fallback: short wait if snackbar not found or already gone
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                attempts++;
            }
        }
        element.click(); // Final attempt
    }

    public void submit() {
        click(submitButton);
    }

    public String getFieldValue(String fieldName) {
        By locator = By.name(fieldName);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute("value");
    }

    public boolean isColumnVisible(String columnName) {
        return driver.findElements(By.xpath("//th[contains(., '" + columnName + "')]")).size() > 0;
    }

    public String getCellValue(String rowSearchText, String columnName) {
        List<WebElement> headers = driver.findElements(By.xpath("//th"));
        int index = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).getText().contains(columnName)) {
                index = i + 1;
                break;
            }
        }
        if (index == -1) {
            return null;
        }
        String cellXpath = "//tr[td[contains(., '%s')]]/td[%d]".formatted(rowSearchText, index);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cellXpath))).getText();
    }

    public void selectFromCombobox(String label, String value) {
        String comboXp = "//div[label[contains(., '%s')]]//div[@role='combobox']".formatted(label);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(comboXp))));

        String optXp = "//li[@role='option' and contains(., '%s')]".formatted(value);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optXp))));
    }

    public DashboardPage openDashboardPage() {
        click(dashboardMenuItem);
        return new DashboardPage(driver, wait);
    }

    public TasksPage openTasksPage() {
        click(tasksMenuItem);
        return new TasksPage(driver, wait);
    }

    public UsersPage openUsersPage() {
        click(usersMenuItem);
        return new UsersPage(driver, wait);
    }

    public LabelsPage openLabelsPage() {
        click(labelsMenuItem);
        return new LabelsPage(driver, wait);
    }

    public TaskStatusesPage openTaskStatusesPage() {
        click(taskStatusesMenuItem);
        return new TaskStatusesPage(driver, wait);
    }
}
