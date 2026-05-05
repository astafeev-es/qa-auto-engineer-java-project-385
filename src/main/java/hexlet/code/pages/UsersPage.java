package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsersPage extends BasePage {

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@aria-label='Select all']/..")
    private WebElement selectAllCheckbox;

    public UsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public void open() {
        elementHelper.click(usersMenuItem);
    }

    public void create(String email, String firstName, String lastName) {
        openCreatePage();
        fillForm(email, firstName, lastName);
        submit();
        open();
    }

    public void fillForm(String email, String firstName, String lastName) {
        elementHelper.sendKeys(emailInput, email);
        elementHelper.sendKeys(firstNameInput, firstName);
        elementHelper.sendKeys(lastNameInput, lastName);
    }

    public UsersPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(emailInput)
            && elementHelper.isVisible(firstNameInput)
            && elementHelper.isVisible(lastNameInput)
            && elementHelper.isVisible(submitButton);
    }

    public String getErrorMessage() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.className("MuiFormHelperText-root")));
        return error.getText();
    }

    public UsersPage openUserSettings(String email) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        WebElement row = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath)));
        elementHelper.click(row);
        return this;
    }

    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        String oldLast = lastNameInput.getAttribute("value");
        replaceInputValue(firstNameInput, newFirstName);
        replaceInputValue(lastNameInput, oldLast + "Updated");

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

    public UsersPage deleteUser(String email) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(checkboxXpath)));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(email);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isUserPresent(String text) {
        return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
    }

    public int getUserCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    public UsersPage bulkDelete() {
        open();
        if (getUserCount() > 0) {
            elementHelper.click(selectAllCheckbox);
            elementHelper.click(deleteButton);
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table/tbody/tr[td]"), 0));
        }
        return this;
    }
}
