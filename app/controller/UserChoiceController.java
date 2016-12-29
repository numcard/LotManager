package app.controller;

import app.AppException;
import app.MainApp;
import app.model.User;
import app.model.App;
import app.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class UserChoiceController
{
    @FXML private ChoiceBox<String> usersBox;  // Пользователи
    private final ObservableList<String> users = UserService.getUserNames();
    private Stage dialogStage;
    private MainApp mainApp;
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }
    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize()
    {
        usersBox.setItems(users);
        usersBox.getSelectionModel().select(0);
    }

    @FXML
    private void handleChoice()
    {
        try
        {
            ObservableList<User> users = UserService.findUserByName(usersBox.getSelectionModel().getSelectedItem());
            if(users.size() == 0)
                throw new Exception("Выбранный пользователь не найден!");
            User user = users.get(0);
            mainApp.getPrimaryStage().setTitle(App.APP_NAME_USER + user.getName());
            App.getUser().setName(user.getName());
            App.setLots(user.getLots());
            App.getUserMode().setValue(true);
            dialogStage.close();
        } catch(Exception e)
        {
            AppException.Throw(e);
        }
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }
}