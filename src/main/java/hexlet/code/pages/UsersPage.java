package hexlet.code.pages;

import org.openqa.selenium.By;
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

    public void create(String email, String firstName, String lastName) {
        openCreatePage();
        fillForm(email, firstName, lastName);
        submit();
        open("users");
    }

    public void fillForm(String email, String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(emailInput));
        emailInput.clear();
        emailInput.sendKeys(email);
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    public UsersPage openCreatePage() {
        open("users");
        click(createButton);
        return this;
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.className("MuiFormHelperText-root"))).getText();
    }

    public UsersPage openUserSettings(String email) {
        open("users");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath))));
        return this;
    }

    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(newFirstName);

        String oldLast = lastNameInput.getAttribute("value");
        lastNameInput.clear();
        lastNameInput.sendKeys(oldLast + "Updated");

        submit();
        open("users");
        return this;
    }

    public UsersPage deleteUser(String email) {
        open("users");
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        click(wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath + "//input[@type='checkbox']/.."))));

        click(deleteButton);
        String cellXpath = "//table/tbody/tr/td[contains(., '%s')]".formatted(email);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cellXpath)));
        return this;
    }

    public boolean isUserPresent(String text) {
        try {
            return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getUserCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    public UsersPage bulkDelete() {
        open("users");
        if (getUserCount() > 0) {
            click(selectAllCheckbox);
            click(deleteButton);
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table/tbody/tr[td]"), 0));
        }
        return this;
    }
}
