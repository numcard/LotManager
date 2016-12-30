package meshok;

import app.model.Lot;
import meshok.page.LoginPage;
import meshok.page.SellPage;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

class MainApp
{
    public static void main(String[] args)
    {
        String username = "login";
        String password = "password";
        String pattern = "pattern";

        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        Lot lot = new Lot();

        FirefoxDriver driver = new FirefoxDriver();
        LoginPage loginPage = new LoginPage(driver);
        boolean success = loginPage.login(username, password);
        if(success)
        {
            SellPage sellPage = new SellPage(driver);
            lot.setMeshokLink(sellPage.sell(lot, pattern));
        }
    }
}
