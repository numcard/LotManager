package app.controller;

import app.AppException;
import app.MainApp;
import app.model.App;
import app.model.Lot;
import app.model.User;
import app.service.LotService;
import app.service.PreferenceServes;
import app.service.UserService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UserImportController
{
    @FXML private ChoiceBox<String> usersBox;  // Пользователи
    private final ObservableList<String> users = UserService.getUserNames();
    private Stage dialogStage;
    private MainApp mainApp;
    private File importFile;
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
        usersBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleChoiceFile()
    {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File lastFolder = new File(PreferenceServes.getLastImportDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null)
        {
            importFile = file;
            PreferenceServes.setLastImportDirectory(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleImportLots()
    {
        if(isInputValid())
        {
            try
            {
                ObservableList<User> users = UserService.findUserByName(usersBox.getSelectionModel().getSelectedItem());
                if(users.size() < 1)
                    throw new Exception("Выбранный пользователь не найден!");
                if(users.size() > 1)
                    throw new Exception("Количество выбранных пользователей больше одного!");

                User user = users.get(0);
                ObservableList<Lot> importLots = LotService.loadLots(importFile);
                user.getLots().addAll(importLots);
                UserService.saveUsers();

                if(user.equals(App.getUser())) // Обновляем данные активного пользователя
                {
                    App.setLots(user.getLots());
                }

                // Показываем сообщение о добавлении.
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(dialogStage);
                alert.setTitle("Импортирование лотов - " + user.getName());
                alert.setHeaderText("Лоты успешно импортированы.");
                alert.setContentText("Импортировано: " + importLots.size() + " лотов.");
                alert.showAndWait();

                dialogStage.close();
            } catch(Exception e)
            {
                AppException.Throw(e);
            }
        }
    }

    private boolean isInputValid()
    {
        String errorMessage = "";

        if(importFile == null || !importFile.exists())
        {
            errorMessage += "Не выбран файл для импорта!\n";
        }
        if(errorMessage.length() == 0)
        {
            return true;
        } else
        {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Невалидное поле");
            alert.setHeaderText("Исправьте невалидное поле");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
}
