package hexlet.code.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TaskStatusesPage extends BasePage {

    @FindBy(name = "name")
    private WebElement nameInput;

    @FindBy(name = "slug")
    private WebElement slugInput;

    @FindBy(xpath = "//input[@aria-label='Select all']/..")
    private WebElement selectAllCheckbox;

    public TaskStatusesPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Task Statuses page")
    public void open() {
        elementHelper.click(taskStatusesMenuItem);
    }

    @Step("Create task status with name {name} and slug {slug}")
    public void create(String name, String slug) {
        openCreatePage();
        fillForm(name, slug);
        submit();
        open();
    }

    @Step("Fill task status form")
    public void fillForm(String name, String slug) {
        elementHelper.sendKeys(nameInput, name);
        elementHelper.sendKeys(slugInput, slug);
    }

    @Step("Open task status create page")
    public TaskStatusesPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    @Step("Check if create task status form is displayed")
    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(nameInput)
            && elementHelper.isVisible(slugInput)
            && elementHelper.isVisible(submitButton);
    }

    @Step("Open settings for status {slug}")
    public TaskStatusesPage openStatusSettings(String slug) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        WebElement row = elementHelper.findElement(By.xpath(rowXpath));
        elementHelper.click(row);
        return this;
    }

    @Step("Edit status {slug} to have name {newName}")
    public TaskStatusesPage edit(String slug, String newName) {
        openStatusSettings(slug);
        elementHelper.replaceInputValue(nameInput, newName);
        elementHelper.click(slugInput);

        submit();
        open();
        return this;
    }

    @Step("Delete status {slug}")
    public TaskStatusesPage deleteStatus(String slug) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(slug);
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = elementHelper.findElement(By.xpath(checkboxXpath));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(slug);
        elementHelper.waitForInvisibility(By.xpath(cellXpath));
        return this;
    }

    @Step("Check if status with text '{text}' is present")
    public boolean isStatusPresent(String text) {
        return elementHelper.isPresent(By.xpath("//td[contains(., '" + text + "')]"));
    }

    @Step("Get status count")
    public int getStatusCount() {
        return elementHelper.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    @Step("Bulk delete statuses")
    public TaskStatusesPage bulkDelete() {
        open();
        if (getStatusCount() > 0) {
            elementHelper.click(selectAllCheckbox);
            elementHelper.click(deleteButton);
            elementHelper.waitForNumberOfElements(By.xpath("//table/tbody/tr[td]"), 0);
        }
        return this;
    }
}
