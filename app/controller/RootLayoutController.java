package app.controller;

import app.AppException;
import app.MainApp;
import app.model.App;
import app.model.Category;
import app.model.User;
import app.service.CategoryService;
import app.service.LotService;
import app.service.PreferenceServes;
import app.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

public class RootLayoutController
{

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleNewLots()
    {
        handleSaveLots();
        App.clearLots();
        App.getUserMode().setValue(false);
        PreferenceServes.setLotFilePath(null);
        // Обновление заглавия сцены.
        mainApp.getPrimaryStage().setTitle(App.APP_NAME);
    }

    @FXML
    private void handleOpenLots()
    {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File lastFolder = new File(PreferenceServes.getLastOpenDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null)
        {
            App.setLots(LotService.loadLots(file));
            App.getUserMode().setValue(false);
            PreferenceServes.setLastOpenDirectory(file.getAbsolutePath());

            mainApp.getPrimaryStage().setTitle(App.APP_NAME_LOTS + file.getAbsolutePath());
        }
    }

    @FXML
    public void handleSaveLots()
    {
        if(App.getUserMode().get())
        {
            try
            {
                User findUser = UserService.findUserByName(App.getUser().getName()).get(0);
                findUser.setLots(App.getLots());
                UserService.saveUsers();
            } catch(NullPointerException e)
            {
                AppException.Throw(e, "Пользователь - " + App.getUser().getName() + ", не найден!");
            }
        }
        else
        {
            File file = PreferenceServes.getLotFile();
            if(file != null)
                LotService.saveLots(file, App.getLots());
            else
                handleSaveLotsAs();
        }
    }

    @FXML
    private void handleSaveLotsAs()
    {
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Ищем последний путь сохранения
        File lastFolder = new File(PreferenceServes.getLastOpenDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if(file != null)
        {
            if(!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
            }
            LotService.saveLots(file, App.getLots());
            PreferenceServes.setLotFilePath(file);
            PreferenceServes.setLastOpenDirectory(file.getAbsolutePath());
        }
    }

    @FXML
    private void handleAutoloadLots()
    {
        mainApp.showAutoloadDialog();
    }

    @FXML
    private void handleExit()
    {
        handleSaveLots();
        System.exit(0);
    }

    @FXML
    private void handleChoiceUser()
    {
        handleSaveLots();
        mainApp.showChoiceUserDialog();
    }

    @FXML
    private void handleNewUser()
    {
        handleSaveLots();
        User tempUser = new User();
        boolean okClicked = mainApp.showUserEditDialog(tempUser);
        if(okClicked)
        {
            UserService.addUser(tempUser);
            App.getUser().setName(tempUser.getName());
            App.getUser().setLots(tempUser.getLots());
            App.setLots(tempUser.getLots());
            App.getUserMode().setValue(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание");
            alert.setHeaderText("Пользователь создан");
            alert.setContentText("Пользователь '" + tempUser.getName() + "', успешно создан!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleImportLots()
    {
        mainApp.showUserImportDialog();
    }

    @FXML
    private void handleDeleteUser()
    {
        mainApp.showDeleteUserDialog();
    }

    @FXML
    private void handleNewCategory()
    {
        Category tempCategory = new Category();
        boolean okClicked = mainApp.showCategoryEditDialog(tempCategory);
        if(okClicked)
        {
            CategoryService.addCategory(tempCategory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание");
            alert.setHeaderText("Категория создана");
            alert.setContentText("Категории '" + tempCategory.getName() + "', успешно создана!");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteCategory()
    {
        mainApp.showDeleteCategoryDialog();
    }

    @FXML
    private void handleAbout()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Информация");
        alert.setContentText("Автор: Авдеев А.О.");
        alert.showAndWait();
    }
}