package app.controller;

import app.AppException;
import app.MainApp;
import app.model.App;
import app.model.Lot;
import javafx.collections.FXCollections;
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
	@FXML
	private TextField username;       // Почта(в качестве логина)
	@FXML
	private PasswordField password;   // Пароль
	@FXML
	private TextField pattern;        // Название шаблона выставления
	@FXML
	private Button okButton;          // Кнопка выставить
	private MainApp mainApp;

	public void setMainApp(MainApp mainApp)
	{
		this.mainApp = mainApp;
	}

	@FXML
	private void handleSell()
	{
		okButton.setDisable(true);
		// Отключаем логирования - зачем?
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		// Подгружаем лоты
		ObservableList<Lot> lots = FXCollections.observableArrayList();
		for(Lot lot : App.getInstance().getLots())
		{
			if(lot.getMeshokLink().length() < 1) lots.add(lot);
		}

		FirefoxDriver driver = new FirefoxDriver();
		// Авторизуемся
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username.getText(), password.getText());
		boolean success = loginPage.isSuccess();
		// Авторизация успешная
		if(success)
		{
			// Счетчик выставлений
			int counter = lots.size();

			try
			{
				for(Lot lot : lots)
				{
					System.out.println("Осталось " + counter + " лотов.");
					// Выставляем на продажу
					SellPage sellPage = new SellPage(driver);
					lot.setMeshokLink(sellPage.sell(lot, pattern.getText()));
					counter--;
					Thread.sleep(1800);
				}
			}
			catch(Exception e)
			{
				AppException.Throw(e);
			}
		}
		else
		{
			System.out.println("Ошибка авторизации");
		}

		okButton.setDisable(false);
		mainApp.getRootController().handleSaveLots();
		System.out.println("try");
		driver.quit();
	}
}