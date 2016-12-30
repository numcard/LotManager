package meshok.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserPage
{
    private final By linksLocator = By.className("clk");        // Ссылка на лот
    private final By infoLocator = By.className("p_l_count");   // Ссылка на информацию о лотах
    private final WebDriver driver;

    public UserPage(WebDriver driver, String userLink, int lotStart)
    {
        this.driver = driver;
        userLink += "&pp=100"; // По 100 лотов
        userLink = (lotStart > 0) ? userLink + "&pN=" + lotStart : userLink; // С какого стартуем
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(userLink);
    }

    public List<String> getPageLinks()
    {
        List<String> strLinks = new ArrayList<>();
        List<WebElement> links = driver.findElements(linksLocator);
        links.forEach(link -> strLinks.add(link.getAttribute("href")));
        return strLinks;
    }
    public String getPageInfo()
    {
        return getInfoElement().getText().split("\n")[1];
    }

    private WebElement getInfoElement()
    {
        return driver.findElement(infoLocator);
    }
    private String getCurrentPage()
    {
        return getPageInfo().replaceAll("[^0-9]+", " ").trim().split(" ")[0];
    }
    private String getAllPages()
    {
        return getPageInfo().replaceAll("[^0-9]+", " ").trim().split(" ")[1];
    }
    public boolean hasNextLink()
    {
        return !getCurrentPage().equals(getAllPages());
    }
}
