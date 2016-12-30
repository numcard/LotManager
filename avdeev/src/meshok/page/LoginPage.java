package meshok.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage
{
    private final WebDriver driver;
    private final By usernameLocator = By.name("LOGIN");
    private final By passwordLocator = By.name("password");
    private final By welcomeLocator = By.xpath("/html/body/ul/li[2]/a");
    private final By formSubmitLocator = By.className("autoreg");

    public LoginPage(WebDriver driver)
    {
        this.driver = driver;
        driver.get("https://meshok.net/");
    }

    public boolean login(String username, String password)
    {
        try
        {
            WebElement usernameElement = driver.findElement(usernameLocator);
            WebElement formSubmit = driver.findElement(formSubmitLocator);
            usernameElement.sendKeys(username);
            formSubmit.submit();
            WebElement passwordElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(passwordLocator));
            passwordElement.sendKeys(password);
            formSubmit.submit();
            (new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) driver ->
            {
                assert driver != null;
                return driver.findElement(welcomeLocator).getText().equals("Выйти");
            });
        }
        catch(Exception e)
        {
            System.out.println("Ошибка авторизации");
            return false;
        }
        return true;
    }
}
