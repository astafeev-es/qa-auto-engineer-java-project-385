package hexlet.code.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsersPage extends BasePage {

    @FindBy(xpath = "//a[contains(., 'Create')]")
    private WebElement createButton;

    @FindBy(xpath = "//table/tbody/tr")
    private List<WebElement> users;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "firstName")
    private WebElement firstNameField;

    @FindBy(name = "lastName")
    private WebElement lastNameField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[@aria-label='Delete']")
    private WebElement deleteButton;

    @FindBy(xpath = "//input[@aria-label='Select all']")
    private WebElement selectAllCheckbox;

    public UsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void create(String email, String firstName, String lastName) {
        wait.until(ExpectedConditions.visibilityOf(createButton)).click();
        wait.until(ExpectedConditions.visibilityOf(emailField)).sendKeys(email);
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        openUsersPage();
    }

    public UsersPage openUserSettings(String email) {
        openUsersPage();
        WebElement userToEdit = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath(String.format("//tr[td[contains(., '%s')]]", email))));
        wait.until(ExpectedConditions.elementToBeClickable(userToEdit)).click();
        return this;
    }

    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.clear();
        firstNameField.sendKeys(newFirstName);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        openUsersPage();
        return this;
    }

    public UsersPage deleteUser(String email) {
        openUsersPage();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        // Кликаем по родителю, так как input может быть скрыт или перекрыт MUI
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath + "//input[@type='checkbox']/.."))).click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//table/tbody/tr[td[contains(., '%s')]]"
            .formatted(email))));
        return this;
    }

    public boolean isUserPresent(String text) {
        try {
            // Убедимся, что мы на странице списка
            return driver.findElements(By.xpath("//td[contains(., '" + text + "')]")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getUserCount() {
        return driver.findElements(By.xpath("//table/tbody/tr[td]")).size();
    }

    public UsersPage bulkDelete() {
        openUsersPage();
        if (getUserCount() > 0) {
            WebElement selectAll = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@aria-label='Select all']/..")));
            selectAll.click();
            wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
            wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//table/tbody/tr[td]"), 0));
        }
        return this;
    }
}
