package hexlet.code.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UsersPage extends BasePage {

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@aria-label='Select all']/..")
    private WebElement selectAllCheckbox;

    public UsersPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open Users page")
    public void open() {
        elementHelper.click(usersMenuItem);
    }

    @Step("Create user with email {email}")
    public void create(String email, String firstName, String lastName) {
        openCreatePage();
        fillForm(email, firstName, lastName);
        submit();
        open();
    }

    @Step("Fill user form")
    public void fillForm(String email, String firstName, String lastName) {
        elementHelper.sendKeys(emailInput, email);
        elementHelper.sendKeys(firstNameInput, firstName);
        elementHelper.sendKeys(lastNameInput, lastName);
    }

    @Step("Open user create page")
    public UsersPage openCreatePage() {
        open();
        elementHelper.click(createButton);
        return this;
    }

    @Step("Check if create user form is displayed")
    public boolean isCreateFormDisplayed() {
        return elementHelper.isVisible(emailInput)
            && elementHelper.isVisible(firstNameInput)
            && elementHelper.isVisible(lastNameInput)
            && elementHelper.isVisible(submitButton);
    }

    @Step("Get error message")
    public String getErrorMessage() {
        return elementHelper.getText(By.className("MuiFormHelperText-root"));
    }

    @Step("Open settings for user {email}")
    public UsersPage openUserSettings(String email) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        WebElement row = elementHelper.findElement(By.xpath(rowXpath));
        elementHelper.click(row);
        return this;
    }

    @Step("Edit user {email} to have first name {newFirstName}")
    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        String oldLast = elementHelper.getAttribute(lastNameInput, "value");
        elementHelper.replaceInputValue(firstNameInput, newFirstName);
        elementHelper.replaceInputValue(lastNameInput, oldLast + "Updated");

        submit();
        open();
        return this;
    }

    @Step("Delete user {email}")
    public UsersPage deleteUser(String email) {
        open();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        String checkboxXpath = rowXpath + "//input[@type='checkbox']/..";
        WebElement checkbox = elementHelper.findElement(By.xpath(checkboxXpath));
        elementHelper.click(checkbox);

        elementHelper.click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(email);
        elementHelper.waitForInvisibility(By.xpath(cellXpath));
        return this;
    }

    @Step("Check if user with text '{text}' is present")
    public boolean isUserPresent(String text) {
        return elementHelper.isPresent(By.xpath("//td[contains(., '" + text + "')]"));
    }

    @Step("Get user count")
    public int getUserCount() {
        return elementHelper.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    @Step("Bulk delete users")
    public UsersPage bulkDelete() {
        open();
        if (getUserCount() > 0) {
            elementHelper.click(selectAllCheckbox);
            elementHelper.click(deleteButton);
            elementHelper.waitForNumberOfElements(By.xpath("//table/tbody/tr[td]"), 0);
        }
        return this;
    }
}
