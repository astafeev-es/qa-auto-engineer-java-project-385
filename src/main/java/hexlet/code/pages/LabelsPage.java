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

    public void create(String name) {
        openCreatePage();
        fillForm(name);
        submit();
        open("labels");
    }

    public void fillForm(String name) {
        wait.until(ExpectedConditions.visibilityOf(nameInput)).clear();
        nameInput.sendKeys(name);
    }

    public LabelsPage openCreatePage() {
        open("labels");
        click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(nameInput)).isDisplayed()
            && wait.until(ExpectedConditions.visibilityOf(submitButton)).isDisplayed();
    }

    public LabelsPage openLabelSettings(String name) {
        open("labels");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath))));
        return this;
    }

    public LabelsPage edit(String name, String newName) {
        openLabelSettings(name);
        wait.until(ExpectedConditions.visibilityOf(nameInput)).clear();
        nameInput.sendKeys(newName);

        submit();
        open("labels");
        return this;
    }

    public LabelsPage deleteLabel(String name) {
        open("labels");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(name);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath + "//input[@type='checkbox']/.."))));

        click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(name);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isLabelPresent(String text) {
        try {
            return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getLabelCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }
}
