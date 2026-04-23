package hexlet.code.pages;

import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsersPage extends BasePage {

    @FindBy(xpath = "//a[contains(text(), 'Create')]")
    private WebElement createButton;

    @FindBy(xpath = "//tr[contains(@class, 'RaDatagrid-clickableRow')]")
    private List<WebElement> users;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "firstName")
    private WebElement firstNameField;

    @FindBy(name = "lastName")
    private WebElement lastNameField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    @FindBy(xpath = "//button[contains(text(), 'Delete')]")
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
        wait.until(ExpectedConditions.urlContains("/users"));
    }

    public UsersPage openUserSettings(String email) {
        WebElement userToEdit = users.stream()
            .filter(user ->
                user.findElement(By.xpath(String.format(".//td[contains(test(), '%s')]", email))).isDisplayed())
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("Failed to find user with email: " + email));

        wait.until(ExpectedConditions.visibilityOf(userToEdit)).click();

        return this;
    }

    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        wait.until(ExpectedConditions.visibilityOf(firstNameField));
        firstNameField.clear();
        firstNameField.sendKeys(newFirstName);
        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
        wait.until(ExpectedConditions.urlContains("/users"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), '" + newFirstName + "')]")));

        return this;
    }

    public UsersPage deleteUser(String email) {
        openUserSettings(email);
        
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[contains(text(), '" + email + "')]")));

        return this;
    }

    public boolean isUserPresent(String text) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), '" + text + "')]")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getUserCount() {
        return users.size();
    }
    
    public UsersPage bulkDelete() {
        selectAllCheckbox.click();
        deleteButton.click();

        return this;
    }
}
