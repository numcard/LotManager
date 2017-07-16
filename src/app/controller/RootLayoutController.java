package app.controller;

import app.MainApp;
import app.model.App;
import app.model.Category;
import app.service.CategoryService;
import app.service.LotService;
import app.service.PreferenceService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class RootLayoutController
{
    private MainApp mainApp;
    private final PreferenceService preferenceService = PreferenceService.getInstance();
    private final LotService lotService = LotService.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance();
    private final App app = App.getInstance();

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleNewLots()
    {
        handleSaveLots();
        app.clearLots();
        preferenceService.setLotFilePath(null);
        // Обновление заглавия сцены.
        mainApp.getPrimaryStage().setTitle(App.APP_NAME);
    }

    @FXML
    private void handleOpenLots()
    {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File lastFolder = new File(preferenceService.getLastOpenDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null)
        {
            app.setLots(lotService.loadLots(file));
            preferenceService.setLastOpenDirectory(file.getAbsolutePath());

            mainApp.getPrimaryStage().setTitle(App.APP_NAME_LOTS + file.getAbsolutePath());
        }
    }

    @FXML
    public void handleSaveLots()
    {
        File file = preferenceService.getLotFile();
        if(file != null)
            lotService.saveLots(file, app.getLots());
        else
            handleSaveLotsAs();
    }

    @FXML
    private void handleSaveLotsAs()
    {
        FileChooser fileChooser = new FileChooser();

        // Задаём фильтр расширений
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Ищем последний путь сохранения
        File lastFolder = new File(preferenceService.getLastOpenDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        // Показываем диалог сохранения файла
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        if(file != null)
        {
            if(!file.getPath().endsWith(".xml"))
            {
                file = new File(file.getPath() + ".xml");
            }
            lotService.saveLots(file, app.getLots());
            preferenceService.setLotFilePath(file);
            preferenceService.setLastOpenDirectory(file.getAbsolutePath());
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
    private void handleNewCategory()
    {
        Category tempCategory = new Category();
        boolean okClicked = mainApp.showCategoryEditDialog(tempCategory);
        if(okClicked)
        {
            categoryService.addCategory(tempCategory);
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
    private void handleSell()
    {
        mainApp.showSellDialog();
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