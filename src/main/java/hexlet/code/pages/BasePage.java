package hexlet.code.pages;

import org.openqa.selenium.JavascriptExecutor;
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

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public DashboardPage openDashboardPage() {
        wait.until(ExpectedConditions.visibilityOf(dashboardMenuItem)).click();
        return new DashboardPage(driver, wait);
    }

    public TasksPage openTasksPage() {
        wait.until(ExpectedConditions.visibilityOf(tasksMenuItem)).click();
        return new TasksPage(driver, wait);
    }

    public UsersPage openUsersPage() {
        wait.until(ExpectedConditions.visibilityOf(usersMenuItem)).click();
        return new UsersPage(driver, wait);
    }

    public LabelsPage openLabelsPage() {
        wait.until(ExpectedConditions.visibilityOf(labelsMenuItem)).click();
        return new LabelsPage(driver, wait);
    }

    public TaskStatusesPage openTaskStatusesPage() {
        wait.until(ExpectedConditions.visibilityOf(taskStatusesMenuItem)).click();
        return new TaskStatusesPage(driver, wait);
    }
}
