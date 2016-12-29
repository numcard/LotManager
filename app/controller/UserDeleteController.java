package app.controller;

import app.AppException;
import app.MainApp;
import app.model.App;
import app.model.User;
import app.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class UserDeleteController
{
    @FXML private ChoiceBox<String> usersBox;  // Пользователи
    private final static ObservableList<String> userNames = UserService.getUserNames();
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
        usersBox.setItems(userNames);
        usersBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleDeleteUser()
    {
        try
        {
            ObservableList<User> findUsers = UserService.findUserByName(usersBox.getSelectionModel().getSelectedItem());
            if(findUsers.size() < 1)
                throw new Exception("Удаляемый пользователь не найден!");
            if(findUsers.size() > 1)
                throw  new Exception("Удаляемых пользователей слишком много!");
            User user = findUsers.get(0);
            if(App.getUser().equals(user))
            {
                mainApp.getRootController().handleNewLots();
                App.getUserMode().setValue(false);
            }

            UserService.removeAll(usersBox.getSelectionModel().getSelectedItem());
            userNames.removeAll(usersBox.getSelectionModel().getSelectedItem());
        } catch(Exception e)
        {
            AppException.Throw(e);
        }

        if(userNames.size() < 1)
            dialogStage.close();
        else
            usersBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }
}
