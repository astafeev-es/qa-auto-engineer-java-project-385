package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TasksPage extends BasePage {

    @FindBy(name = "title")
    private WebElement titleInput;

    @FindBy(name = "content")
    private WebElement contentInput;

    public TasksPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void create(String title, String status, String assignee) {
        openCreatePage();
        fillForm(title, status, assignee);
        submit();
        openTasksPage();
    }

    public void fillForm(String title, String status, String assignee) {
        wait.until(ExpectedConditions.visibilityOf(titleInput)).clear();
        titleInput.sendKeys(title);

        selectFromCombobox("Status", status);
        selectFromCombobox("Assignee", assignee);
    }

    public TasksPage openCreatePage() {
        openTasksPage();
        click(createButton);
        return this;
    }

    public boolean isTaskInColumn(String title, String columnName) {
        try {
            String xpath = "//div[h6[contains(., '%s')]]//*[@role='button' and contains(., '%s')]"
                .formatted(columnName, title);
            return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public TasksPage edit(String title, String newTitle) {
        openTaskEdit(title);
        wait.until(ExpectedConditions.visibilityOf(titleInput)).clear();
        titleInput.sendKeys(newTitle);
        // Trigger blur
        click(contentInput);

        submit();
        openTasksPage();
        return this;
    }

    public TasksPage moveTask(String title, String newStatus) {
        openTaskEdit(title);
        selectFromCombobox("Status", newStatus);
        submit();
        openTasksPage();
        return this;
    }

    public TasksPage openTaskEdit(String title) {
        openTasksPage();
        String xpath = "//*[@role='button' and contains(., '%s')]//a[contains(., 'Edit')]".formatted(title);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
        return this;
    }

    public TasksPage deleteTask(String title) {
        openTaskEdit(title);
        click(deleteButton);
        openTasksPage();
        String xpath = "//*[@role='button' and contains(., '%s')]".formatted(title);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        return this;
    }

    public int getTaskCount() {
        String xpath = "//div[@role='main']//*[@role='button' and contains(@data-rfd-draggable-id, '')]";
        return driver.findElements(By.xpath(xpath)).size();
    }
}
