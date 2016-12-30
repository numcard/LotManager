package app.controller;

import app.MainApp;
import app.model.App;
import app.model.Lot;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import meshok.page.LoginPage;
import meshok.page.SellPage;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SellDialogController
{
    @FXML private TextField username;       // Почта(в качестве логина)
    @FXML private PasswordField password;   // Пароль
    @FXML private TextField pattern;        // Название шаблона выставления
    @FXML private Button okButton;          // Кнопка выставить
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleSell()
    {
        okButton.setDisable(true);
        // Отключаем логирования
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.OFF);
        // Подгружаем лоты
        ObservableList<Lot> lots = App.getInstance().getLots();

        FirefoxDriver driver = new FirefoxDriver();
        // Авторизуемся
        LoginPage loginPage = new LoginPage(driver);
        boolean success = loginPage.login(username.getText(), password.getText());
        if(success)
        {
            // Счетчик выставлений
            int counter = lots.size();
            for(Lot lot: lots)
            {
                System.out.println("Осталось " + counter + " лотов.");
                // Выставляем на продажу
                SellPage sellPage = new SellPage(driver);
                lot.setMeshokLink(sellPage.sell(lot, pattern.getText()));
                counter--;
            }
        }
        okButton.setDisable(false);
        mainApp.getRootController().handleSaveLots();
    }
}