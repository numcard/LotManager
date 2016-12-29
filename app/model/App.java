package app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Модель приложения(Текущии данные приложения, лоты, пользователи и т.п.)
 */
public class App
{
    private final static ObservableList<Lot> lots = FXCollections.observableArrayList();       // Текущие лоты
    private final static User user = new User();                                               // Текущий пользователь
    private final static BooleanProperty userMode = new SimpleBooleanProperty(false);       // Режим работы с пользователем/файлом

    public final static String APP_NAME = "Менеджер лотов";
    public final static String APP_NAME_LOTS = "Лоты - ";
    public final static String APP_NAME_USER = "Пользователь - ";
    public final static String ICON_PATH = "src/app/resources/newspaper.png";

    public static ObservableList<Lot> getLots()
    {
        return lots;
    }
    public static void setLots(ObservableList<Lot> setLots)
    {
        clearLots();
        lots.addAll(setLots);
    }
    public static void clearLots()
    {
        lots.clear();
    }
    public static User getUser()
    {
        return user;
    }
    public static BooleanProperty getUserMode()
    {
        return userMode;
    }

    private App()
    {}
}