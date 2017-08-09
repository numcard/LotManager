package meshok.page;

import app.model.Lot;
import app.service.CategoryService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

class FirstSellPage
{
	FirstSellPage(WebDriver driver, Lot lot, String pattern)
	{
		String SELL_URL = "https://meshok.net/post_a.php";
		driver.get(SELL_URL + "?good=" + CategoryService.getInstance().findMeshokCategory(lot.getCategory()));

		try
		{
			By patternLocator = By.name("pattern");
			new Select(driver.findElement(patternLocator)).selectByVisibleText(pattern);                 // Выбрали паттерн

			By sellFixedLocator = By.name("sellFixed");
			driver.findElement(sellFixedLocator).click(); // Нажали выставить по фикс. цене
		}
		catch(Exception e)
		{
			System.out.println("Ошибка выставления лотов");
			e.printStackTrace();
		}
	}
}
