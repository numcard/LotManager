package meshok.page;

import app.model.Lot;
import app.service.CategoryService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@SuppressWarnings("ALL")
class SecondSellPage
{
	private By nameLocator = By.id("i_name");                   // Название лота
	private By descriptionLocator = By.name("descriptionn");    // Описание
	private By dynamicDescriptionLocator = By.name("off_v_editor");
	private By categoryLocator = By.name("type");               // Категория
	private By priceLocator = By.name("min_price");             // Стоимость
	private By bestOfferLocator = By.name("best_offer");        // Торг уместен
	private By iFrameLocator = By.xpath("/html/body/form/table[2]/tbody/tr[2]/td/iframe");    // iFrame изображений
	private By picsLocator = By.name("up_pic[]");               // input изображений в iFrame
	private By publishLocator = By.name("Publish");             // Выставить
	private WebDriver driver;

	SecondSellPage(WebDriver driver, Lot lot)
	{
		this.driver = driver;
		try
		{
			waitElements(nameLocator, priceLocator, bestOfferLocator, descriptionLocator);

			WebElement name = driver.findElement(nameLocator);
			WebElement description = driver.findElement(descriptionLocator);
			WebElement price = driver.findElement(priceLocator);
			WebElement bestOffer = driver.findElement(bestOfferLocator);

			name.clear();
			name.sendKeys(lot.getFullName());

			description.clear();
			description.sendKeys("<p><strong><em>" + lot.getName() + "</em></strong></p>");

			driver.switchTo().defaultContent(); // for what we do it?
			driver.switchTo().frame(driver.findElement(iFrameLocator));
			List<String> images = lot.getImages();
			for(int i = 0; i < images.size(); i++)
			{
				WebElement pics = driver.findElement(picsLocator);
				driver.findElement(picsLocator).sendKeys(images.get(i));
				new WebDriverWait(driver, 20)               // Ждем загрузки
						.until(ExpectedConditions.visibilityOfElementLocated(By.id("p" + i)));
			}
			driver.switchTo().defaultContent();

			price.clear();
			price.sendKeys(String.valueOf(lot.getPrice()));

			if(lot.isBestOffer()) bestOffer.click();

			driver.findElement(publishLocator).click();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String category2key(String category) throws Exception
	{
		CategoryService categoryService = CategoryService.getInstance();
		int id = categoryService.findMeshokCategory(category);
		if(id == 0) throw new Exception("Категория на найдена");
		return String.valueOf(id);
	}

	private void waitElements(By... elems)
	{
		for(By by : elems)
		{
			new WebDriverWait(driver, 10) // Ждем загрузку элемента
					.until(ExpectedConditions.visibilityOfElementLocated(by));
		}
	}

	private void waitElement(By by)
	{
		new WebDriverWait(driver, 3) // Ждем загрузку элемента
				.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
}
