package app;

import app.controller.*;
import app.model.App;
import app.model.Category;
import app.model.Lot;
import app.service.CategoryService;
import app.service.LotService;
import app.service.PreferenceService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApp extends Application
{
    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootLayoutController controller;
    private final PreferenceService preferenceServes = PreferenceService.getInstance();
    private final LotService lotService = LotService.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance();
    private final App app = App.getInstance();

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }
    public RootLayoutController getRootController()
    {
        return controller;
    }

    // Точка входа
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(App.APP_NAME);

        // Устанавливаем иконку приложения.
        this.primaryStage.getIcons().add(new Image("file:" + App.ICON_PATH));

        initRootLayout();
        showLotOverview();
    }

    @Override
    public void stop()
    {
        controller.handleSaveLots();
    }

    private void initRootLayout()
    {
        try
        {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Даём контроллеру доступ к главному приложению.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            this.controller = controller;
            primaryStage.show();

            File lotFile = preferenceServes.getLotFile();   // Загрузка лотов в приложение
            if(lotFile != null)
            {
                app.setLots(lotService.loadLots(lotFile));
                primaryStage.setTitle(App.APP_NAME_LOTS + lotFile.getAbsolutePath());
            }
        }
        catch(IOException e)
        {
            AppException.Throw(e);
        }
    }

    private void showLotOverview()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LotOverview.fxml"));
            AnchorPane lotOverviewPage = loader.load();

            rootLayout.setCenter(lotOverviewPage);

            LotOverviewController controller = loader.getController();
            controller.setMainApp(this);
        }
        catch(IOException e)
        {
            AppException.Throw(e);
        }
    }

    public boolean showLotEditDialog(Lot lot)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LotEditDialog.fxml"));
            AnchorPane lotEditDialogPage = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование лота");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(lotEditDialogPage);
            dialogStage.setScene(scene);

            LotEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLot(lot);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch(IOException e)
        {
            AppException.Throw(e);
            return false;
        }
    }

    public void showAutoloadDialog()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/LotAutoloadDialog.fxml"));
            AnchorPane autoloadDialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Автозагрузка лотов");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(autoloadDialog);
            dialogStage.setScene(scene);

            LotAutoloadController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        }
        catch(IOException e)
        {
            AppException.Throw(e);
        }
    }

    public boolean showCategoryEditDialog(Category tempCategory)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CategoryEditDialog.fxml"));
            AnchorPane autoloadDialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавление категории");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(autoloadDialog);
            dialogStage.setScene(scene);

            CategoryEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(tempCategory);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch(IOException e)
        {
            AppException.Throw(e);
            return false;
        }
    }

    public void showDeleteCategoryDialog()
    {
        if(categoryService.getCategoryNames().size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка удаления категорий");
            alert.setContentText("Категории не найдены!");
            alert.showAndWait();
        }
        else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/CategoryDeleteDialog.fxml"));
                AnchorPane autoloadDialog = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Удаление категории");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(autoloadDialog);
                dialogStage.setScene(scene);

                CategoryDeleteController controller = loader.getController();
                controller.setDialogStage(dialogStage);

                dialogStage.showAndWait();
            }
            catch(IOException e)
            {
                AppException.Throw(e);
            }
        }
    }

    public void showSellDialog()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/SellDialog.fxml"));
            AnchorPane loginDialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выставление лотов");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(loginDialog);
            dialogStage.setScene(scene);

            SellDialogController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch(IOException e)
        {
            AppException.Throw(e);
        }
    }
}