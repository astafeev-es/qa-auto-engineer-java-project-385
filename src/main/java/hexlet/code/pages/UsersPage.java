package hexlet.code.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UsersPage extends BasePage {

    public UsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void create(String email, String firstName, String lastName) {
        openCreatePage();
        fillForm(email, firstName, lastName);
        submit();
        openUsersPage();
    }

    public void fillForm(String email, String firstName, String lastName) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        emailField.clear();
        emailField.sendKeys(email);
        driver.findElement(By.name("firstName")).clear();
        driver.findElement(By.name("firstName")).sendKeys(firstName);
        driver.findElement(By.name("lastName")).clear();
        driver.findElement(By.name("lastName")).sendKeys(lastName);
    }

    public UsersPage openCreatePage() {
        openUsersPage();
        wait.until(ExpectedConditions.visibilityOf(createButton)).click();
        return this;
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.className("MuiFormHelperText-root"))).getText();
    }

    public UsersPage openUserSettings(String email) {
        openUsersPage();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath))).click();
        return this;
    }

    public UsersPage edit(String email, String newFirstName) {
        openUserSettings(email);

        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("firstName")));
        field.clear();
        field.sendKeys(newFirstName);

        WebElement last = driver.findElement(By.name("lastName"));
        String oldLast = last.getAttribute("value");
        last.clear();
        last.sendKeys(oldLast + "Updated");

        submit();
        openUsersPage();
        return this;
    }

    public UsersPage deleteUser(String email) {
        openUsersPage();
        String rowXpath = "//tr[td[contains(., '%s')]]".formatted(email);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(rowXpath)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath + "//input[@type='checkbox']/..")))
            .click();

        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
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
