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
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("MuiSnackbar-root")));
                } catch (Exception ignored) {
                    // do nothing
                }
                attempts++;
            }
        }
        element.click();
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
        int currentIndex = 0;
        for (WebElement header : headers) {
            if (header.getText().contains(columnName)) {
                index = currentIndex;
                break;
            }
            currentIndex++;
        }

        if (index == -1) {
            return null;
        }

        String cellXpath = "//tr[td[contains(., '%s')]]/td[%d]".formatted(rowSearchText, index + 1);
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(cellXpath))).getText();
    }

    public void selectFromCombobox(String label, String value) {
        String comboXp = "//div[label[contains(., '%s')]]//div[@role='combobox']".formatted(label);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(comboXp))));

        String optXp = "//li[@role='option' and contains(., '%s')]".formatted(value);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optXp))));
    }

    public LoginPage logout() {
        click(profileButton);
        click(logoutButton);

        return new LoginPage(driver, wait);
    }

    @SuppressWarnings("unchecked")
    public <T extends BasePage> T open(String pageName) {
        switch (pageName.toLowerCase()) {
            case "dashboard" -> {
                click(dashboardMenuItem);
                return (T) new DashboardPage(driver, wait);
            }
            case "tasks" -> {
                click(tasksMenuItem);
                return (T) new TasksPage(driver, wait);
            }
            case "users" -> {
                click(usersMenuItem);
                return (T) new UsersPage(driver, wait);
            }
            case "labels" -> {
                click(labelsMenuItem);
                return (T) new LabelsPage(driver, wait);
            }
            case "task statuses" -> {
                click(taskStatusesMenuItem);
                return (T) new TaskStatusesPage(driver, wait);
            }
            default -> throw new IllegalArgumentException("Unknown page: " + pageName);
        }
    }
}
