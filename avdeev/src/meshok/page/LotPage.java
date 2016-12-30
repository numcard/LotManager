package meshok.page;

import meshok.model.Lot;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LotPage
{
    private final WebDriver driver;
    private final By nameLocator = By.xpath("//span[@itemprop=\"name\"]");
    private final By availableLocator = By.cssSelector("html body#bbody div table tbody tr td div div#iInfo.a3 div#info div#gInfo table tbody tr td b");
    private final By priceLocator = By.xpath("//b[@itemprop=\"price\"]");
    private final By categoryLocator = By.id("itemTree");
    private final By viewsLocator = By.xpath("/html/body/div[5]/table[1]/tbody/tr/td/div[2]/div/div[3]/div[3]/table[2]/tbody/tr[1]/td");
    private final By lotNumberLocator = By.xpath("/html/body/div[5]/table[1]/tbody/tr/td/div[2]/div/div[3]/div[3]/table[2]/tbody/tr[2]/td");
    private final By shippingLocator = By.xpath("/html/body/div[5]/table[1]/tbody/tr/td/div[2]/div/div[3]/div[3]/table[1]/tbody/tr[5]/td");
    private final By photosLocator = By.className("prettyPhoto");
    private final By bestOfferLocator = By.xpath("/html/body/div[5]/table[1]/tbody/tr/td/div[1]/div[1]/a[2]");

    public LotPage(WebDriver driver, String link)
    {
        this.driver = driver;
        driver.get(link);
    }

    private int getLotNumber()
    {
        WebElement lotNumber = driver.findElement(lotNumberLocator);
        return Integer.parseInt(lotNumber.getText().split(":")[1].trim());
    }

    private String getFixedName()
    {
        WebElement name = driver.findElement(nameLocator);
        return name.getText().split("\\.")[0].trim();
    }

    private String getRegularName()
    {
        WebElement name = driver.findElement(nameLocator);
        return name.getText().split("\\.")[1].split("#")[0].trim();
    }

    private String getBox()
    {
        WebElement name = driver.findElement(nameLocator);
        return "#" + name.getText().split("#")[1];
    }

    private int getPrice()
    {
        WebElement price = driver.findElement(priceLocator);
        return Integer.parseInt(price.getText().split("\\.")[0]);
    }

    private int getViews()
    {
        WebElement views = driver.findElement(viewsLocator);
        return Integer.parseInt(views.getText().replaceAll("[^0-9]+", " ").trim().split(" ")[0]);
    }

    private int getAvailable()
    {
        WebElement available = driver.findElement(availableLocator);
        return Integer.parseInt(available.getText().trim());
    }

    private String getCategory()
    {
        WebElement category = driver.findElement(categoryLocator);
        List<String> categoryList = Arrays.asList(category.getText().split(","));
        return categoryList.get(categoryList.size() - 1).trim();
    }

    private String getShipping()
    {
        WebElement shipping = driver.findElement(shippingLocator);
        return shipping.getText().trim();
    }

    private boolean getBestOffer()
    {
        return driver.findElements(bestOfferLocator).get(0).getText().equals("Предложить цену");
    }

    private List<String> getImages()
    {
        List<WebElement> photos = driver.findElements(photosLocator);
        List<String> images = new ArrayList<>();
        photos.forEach(element -> images.add(element.getAttribute("rel").replace("https", "http")));
        images.forEach(image -> {
            System.out.println(Arrays.toString(image.split("(meshok.net)(.*.jpg)")));
            try (InputStream in = new URL(image).openStream()) {
                Files.copy(in, Paths.get("C:/meshok/" + image.split("(meshok.net)(.*.jpg)")[2]), StandardCopyOption.REPLACE_EXISTING);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        });
        return images;
    }

    public Lot getLot()
    {
        Lot lot = new Lot();
        try
        {
            lot.setLotNumber(getLotNumber());
            lot.setFixedName(getFixedName());
            lot.setRegularName(getRegularName());
            lot.setBox(getBox());
            lot.setPrice(getPrice());
            lot.setViews(getViews());
            lot.setAvailable(getAvailable());
            lot.setCategory(getCategory());
            lot.setShipping(getShipping());
            lot.setImages(getImages());
            lot.setBestOffer(getBestOffer());
        }
        catch(NoSuchElementException e)
        {
            e.printStackTrace();
        }
        return lot;
    }
}
