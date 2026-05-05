package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LabelsPage extends BasePage {

    @FindBy(name = "name")
    private WebElement nameInput;

    public LabelsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public void open() {
        elementHelper.click(labelsMenuItem);
    }

    public void create(String name) {
        openCreatePage();
        fillForm(name);
        submit();
        open();
    }

    public void fillForm(String name) {
        elementHelper.sendKeys(nameInput, name);
    }

    public LabelsPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(nameInput)
            && elementHelper.isVisible(submitButton);
    }

    public LabelsPage openLabelSettings(String name) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        WebElement row = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath)));
        elementHelper.click(row);
        return this;
    }

    public LabelsPage edit(String name, String newName) {
        openLabelSettings(name);
        elementHelper.sendKeys(nameInput, newName);

        submit();
        open();
        return this;
    }

    public LabelsPage deleteLabel(String name) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXpath)));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(name);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isLabelPresent(String text) {
        return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
    }

    public int getLabelCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }
}
