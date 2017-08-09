package meshok.page;

import app.model.Lot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SellPage
{
	private final WebDriver driver;
	private final By linkLocator = By.linkText("Посмотреть");

	public SellPage(WebDriver driver)
	{
		this.driver = driver;
	}

	/**
	 * Выставление лота на продажу
	 *
	 * @param lot     Лот
	 * @param pattern Патерн по которому выставляют
	 * @return Ссылка на лот
	 */
	public String sell(Lot lot, String pattern)
	{
		try
		{
			new FirstSellPage(driver, lot, pattern);
			new SecondSellPage(driver, lot);
			new WebDriverWait(driver, 30) // Проверка что лот выставлен
					.until(ExpectedConditions.visibilityOfElementLocated(linkLocator));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return driver.findElement(linkLocator).getAttribute("href");
	}
}
