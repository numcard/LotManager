package meshok.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@SuppressWarnings("ALL")
class FirstSellPage
{
    private final String SELL_URL = "https://meshok.net/post_a.php";
    private By categoryLocator = By.name("type");                       // Категория
    private By patternLocator = By.name("pattern");                     // Шаблон
    private By sellFixedLocator = By.name("sellFixed");                 // Кнопка фиксированная цена
    private By noDynamicMenuLocator = By.xpath("/html/body/form/table[2]/tbody/tr[1]/td/table/tbody/tr/td[1]/p[2]/a");
    private By switchMenuLocator = By.linkText("Переключиться");

    FirstSellPage(WebDriver driver, String pattern)
    {
        driver.get(SELL_URL);

        try
        {
            if(driver.findElements(switchMenuLocator).size() < 1)   // Открываем статичное меню если не открыто
                driver.findElement(noDynamicMenuLocator).click();

            new WebDriverWait(driver, 10) // Ждем загрузки после открытия меню
                    .until(ExpectedConditions.visibilityOfElementLocated(categoryLocator));

            new Select(driver.findElement(categoryLocator)).selectByValue("140");   // Выбрали категорию
            new Select(driver.findElement(patternLocator)).selectByVisibleText(pattern);                 // Выбрали паттерн

            driver.findElement(sellFixedLocator).click(); // Нажали выставить по фикс. цене
        }
        catch(Exception e)
        {
            System.out.println("Ошибка выставления лотов");
            e.printStackTrace();
        }
    }
}
