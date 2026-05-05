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

    @Override
    public void open() {
        elementHelper.click(taskStatusesMenuItem);
    }

    public void create(String name, String slug) {
        openCreatePage();
        fillForm(name, slug);
        submit();
        open();
    }

    public void fillForm(String name, String slug) {
        elementHelper.sendKeys(nameInput, name);
        elementHelper.sendKeys(slugInput, slug);
    }

    public TaskStatusesPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(nameInput)
            && elementHelper.isVisible(slugInput)
            && elementHelper.isVisible(submitButton);
    }

    public TaskStatusesPage openStatusSettings(String slug) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        WebElement row = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath)));
        elementHelper.click(row);
        return this;
    }

    public TaskStatusesPage edit(String slug, String newName) {
        openStatusSettings(slug);
        replaceInputValue(nameInput, newName);
        elementHelper.click(slugInput);

        submit();
        open();
        return this;
    }

    private void replaceInputValue(WebElement input, String value) {
        String currentValue = input.getAttribute("value");
        elementHelper.click(input);
        input.sendKeys(Keys.END);
        for (int index = 0; index < currentValue.length(); index++) {
            input.sendKeys(Keys.BACK_SPACE);
        }
        input.sendKeys(value);
    }

    public TaskStatusesPage deleteStatus(String slug) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXpath)));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(slug);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isStatusPresent(String text) {
        return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
    }

    public int getStatusCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    public TaskStatusesPage bulkDelete() {
        open();
        if (getStatusCount() > 0) {
            elementHelper.click(selectAllCheckbox);
            elementHelper.click(deleteButton);
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table/tbody/tr[td]"), 0));
        }
        return this;
    }
}
