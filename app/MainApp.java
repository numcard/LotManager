package app;

import app.controller.*;
import app.model.App;
import app.model.Category;
import app.model.Lot;
import app.model.User;
import app.service.CategoryService;
import app.service.LotService;
import app.service.PreferenceServes;
import app.service.UserService;
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
    private Stage primaryStage;                                                         // Сцена приложения
    private BorderPane rootLayout;                                                      // Корневой макет
    private RootLayoutController controller;                                            // Ссылка на контроллер меню

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

            File lotFile = PreferenceServes.getLotFile();   // Загрузка лотов в приложение
            if(lotFile != null)
            {
                App.setLots(LotService.loadLots(lotFile));
                primaryStage.setTitle(App.APP_NAME_LOTS + lotFile.getAbsolutePath());
            }
        } catch(IOException e)
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
        } catch(IOException e)
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
        } catch(IOException e)
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
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch(IOException e)
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
        } catch(IOException e)
        {
            AppException.Throw(e);
            return false;
        }
    }

    public void showDeleteCategoryDialog()
    {
        if(CategoryService.getCategoryNames().size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка удаления категорий");
            alert.setContentText("Категории не найдены!");
            alert.showAndWait();
        } else
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
            } catch(IOException e)
            {
                AppException.Throw(e);
            }
        }
    }

    public boolean showUserEditDialog(User tempUser)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/UserEditDialog.fxml"));
            AnchorPane autoloadDialog = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Создание пользователя");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(autoloadDialog);
            dialogStage.setScene(scene);

            UserEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(tempUser);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch(IOException e)
        {
            AppException.Throw(e);
            return false;
        }
    }

    public void showDeleteUserDialog()
    {
        if(UserService.getUserNames().size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка удаления пользователей");
            alert.setContentText("Пользователи не найдены!");
            alert.showAndWait();
        } else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/UserDeleteDialog.fxml"));
                AnchorPane autoloadDialog = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Удаление пользователей");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(autoloadDialog);
                dialogStage.setScene(scene);

                UserDeleteController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setMainApp(this);

                dialogStage.showAndWait();
            } catch(IOException e)
            {
                AppException.Throw(e);
            }
        }
    }

    public void showChoiceUserDialog()
    {
        if(UserService.getUserNames().size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка выбора пользователей");
            alert.setContentText("Пользователи не найдены!");
            alert.showAndWait();
        } else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/UserChoiceDialog.fxml"));
                AnchorPane autoloadDialog = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Выбор пользователей");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(autoloadDialog);
                dialogStage.setScene(scene);

                UserChoiceController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setMainApp(this);

                dialogStage.showAndWait();
            } catch(IOException e)
            {
                AppException.Throw(e);
            }
        }
    }

    public void showUserImportDialog()
    {
        if(UserService.getUserNames().size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка импортирования");
            alert.setContentText("Пользователи не найдены!");
            alert.showAndWait();
        } else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/UserImportDialog.fxml"));
                AnchorPane autoloadDialog = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Импортирование лотов");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(autoloadDialog);
                dialogStage.setScene(scene);

                UserImportController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setMainApp(this);

                dialogStage.showAndWait();
            } catch(IOException e)
            {
                AppException.Throw(e);
            }
        }
    }
}