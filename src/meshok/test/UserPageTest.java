package meshok.test;

import meshok.page.UserPage;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class UserPageTest
{
	public static void main(String[] args)
	{
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);

		HtmlUnitDriver driver = new HtmlUnitDriver();

		try(FileWriter writer = new FileWriter("links.txt", false))
		{
			UserPage userPage;
			int counter = 0;
			do
			{
				userPage = new UserPage(driver, "https://meshok.net/?user=666666", counter);
				for(String link : userPage.getPageLinks())
				{
					writer.write(link + "\n");
					counter++;
				}
				System.out.println(userPage.getPageInfo());
			} while(userPage.hasNextLink());
			writer.flush();
		}
		catch(IOException ex)
		{
			System.out.println(ex.getMessage());
		}
		driver.close();
	}
}
