package meshok.test;

import meshok.page.LotPage;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

class UserLotTest
{
	public static void main(String[] args)
	{
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);

		String link = "https://meshok.net/item/NAME_OF_ITEM";
		HtmlUnitDriver driver = new HtmlUnitDriver();
		LotPage lotPage = new LotPage(driver, link);
		System.out.println(lotPage.getLot());
		//List<Lot> lots = new ArrayList<>();
	}
}
