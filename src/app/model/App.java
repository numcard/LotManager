package app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Модель приложения(Текущии данные приложения, лоты, пользователи и т.п.)
 */
public class App
{
	private static App instance;
	private final ObservableList<Lot> lots = FXCollections.observableArrayList();   // Текущие лоты

	public final static String APP_NAME = "Менеджер лотов";
	public final static String APP_NAME_LOTS = "Лоты - ";
	public final static String ICON_PATH = "src/app/resources/newspaper.png";

	private App()
	{}

	public static App getInstance()
	{
		if(instance == null) instance = new App();
		return instance;
	}

	public ObservableList<Lot> getLots()
	{
		return lots;
	}

	public void setLots(ObservableList<Lot> setLots)
	{
		clearLots();
		lots.addAll(setLots);
	}

	public void clearLots()
	{
		lots.clear();
	}
}