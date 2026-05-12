package hexlet.code.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LabelsPage extends BasePage {

    @FindBy(name = "name")
    private WebElement nameInput;

    public LabelsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Labels page")
    public void open() {
        elementHelper.click(labelsMenuItem);
    }

    @Step("Create label '{name}'")
    public void create(String name) {
        openCreatePage();
        fillForm(name);
        submit();
        open();
    }

    @Step("Fill label form")
    public void fillForm(String name) {
        elementHelper.sendKeys(nameInput, name);
    }

    @Step("Open label create page")
    public LabelsPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    @Step("Check if create label form is displayed")
    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(nameInput)
            && elementHelper.isVisible(submitButton);
    }

    @Step("Open settings for label '{name}'")
    public LabelsPage openLabelSettings(String name) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        WebElement row = elementHelper.findElement(By.xpath(rowXpath));
        elementHelper.click(row);
        return this;
    }

    @Step("Edit label '{name}' to new name '{newName}'")
    public LabelsPage edit(String name, String newName) {
        openLabelSettings(name);
        elementHelper.replaceInputValue(nameInput, newName);

        submit();
        open();
        return this;
    }

    @Step("Delete label '{name}'")
    public LabelsPage deleteLabel(String name) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = elementHelper.findElement(By.xpath(checkboxXpath));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(name);
        elementHelper.waitForInvisibility(By.xpath(cellXpath));
        return this;
    }

    @Step("Check if label with text '{text}' is present")
    public boolean isLabelPresent(String text) {
        return elementHelper.isPresent(By.xpath("//td[contains(., '" + text + "')]"));
    }

    @Step("Get label count")
    public int getLabelCount() {
        return elementHelper.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }
}
