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
	private boolean success; // Indicate the state of authorization

	public LoginPage(WebDriver driver)
	{
		this.driver = driver;
		driver.get("https://meshok.net/");
	}

	public void login(String username, String password)
	{
		try
		{
			WebElement usernameElement = driver.findElement(usernameLocator);
			WebElement formSubmit = driver.findElement(formSubmitLocator);
			usernameElement.sendKeys(username);
			WebElement passwordElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(passwordLocator));
			passwordElement.sendKeys(password);
			formSubmit.submit();
			(new WebDriverWait(driver, 10)).until((ExpectedCondition<Boolean>) driver -> {
				assert driver != null;
				return driver.findElement(welcomeLocator).getText().equals("Выйти");
			});
			success = true;
		}
		catch(Exception e)
		{
			success = false;
		}
	}

	public boolean isSuccess()
	{
		return success;
	}
}
