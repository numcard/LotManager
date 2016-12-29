package app.controller;

import app.model.User;
import app.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditController
{
    @FXML private TextField name;   // Имя пользователя
    private Stage dialogStage;
    private User user;
    private boolean okClicked = false;
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }
    public void setCategory(User user)
    {
        this.user = user;
    }

    @FXML
    private void handleNewUser()
    {
        if(isInputValid())
        {
            user.setName(name.getText());
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }

    public boolean isOkClicked()
    {
        return okClicked;
    }

    private boolean isInputValid()
    {
        ObservableList<User> users = UserService.findUserByName(name.getText());
        if(users.size() > 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание");
            alert.setHeaderText("Пользователь с таким именем существует!");
            alert.setContentText("Придумайте новое имя пользователя!");
            alert.showAndWait();
            return false;
        }
        return name.getText().length() > 0;
    }

}
