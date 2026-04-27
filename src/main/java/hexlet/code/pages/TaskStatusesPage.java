package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TaskStatusesPage extends BasePage {

    @FindBy(name = "name")
    private WebElement nameInput;

    @FindBy(name = "slug")
    private WebElement slugInput;

    @FindBy(xpath = "//input[@aria-label='Select all']/..")
    private WebElement selectAllCheckbox;

    public TaskStatusesPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void create(String name, String slug) {
        openCreatePage();
        fillForm(name, slug);
        submit();
        open("task statuses");
    }

    public void fillForm(String name, String slug) {
        wait.until(ExpectedConditions.visibilityOf(nameInput)).clear();
        nameInput.sendKeys(name);
        slugInput.clear();
        slugInput.sendKeys(slug);
    }

    public TaskStatusesPage openCreatePage() {
        open("task statuses");
        click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(nameInput)).isDisplayed()
            && wait.until(ExpectedConditions.visibilityOf(slugInput)).isDisplayed()
            && wait.until(ExpectedConditions.visibilityOf(submitButton)).isDisplayed();
    }

    public TaskStatusesPage openStatusSettings(String slug) {
        open("task statuses");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath))));
        return this;
    }

    public TaskStatusesPage edit(String slug, String newName) {
        openStatusSettings(slug);
        wait.until(ExpectedConditions.visibilityOf(nameInput));
        replaceInputValue(nameInput, newName);
        click(slugInput);

        submit();
        open("task statuses");
        return this;
    }

    private void replaceInputValue(WebElement input, String value) {
        String currentValue = input.getAttribute("value");
        input.click();
        input.sendKeys(Keys.END);
        for (int index = 0; index < currentValue.length(); index++) {
            input.sendKeys(Keys.BACK_SPACE);
        }
        input.sendKeys(value);
    }

    public TaskStatusesPage deleteStatus(String slug) {
        open("task statuses");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath + "//input[@type='checkbox']/.."))));

        click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(slug);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isStatusPresent(String text) {
        try {
            return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getStatusCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    public TaskStatusesPage bulkDelete() {
        open("task statuses");
        if (getStatusCount() > 0) {
            click(selectAllCheckbox);
            click(deleteButton);
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table/tbody/tr[td]"), 0));
        }
        return this;
    }
}
