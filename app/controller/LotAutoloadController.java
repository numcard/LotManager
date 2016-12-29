package app.controller;

import app.MainApp;
import app.model.App;
import app.model.Lot;
import app.service.CategoryService;
import app.service.PreferenceServes;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LotAutoloadController
{
    @FXML private TextField name;                   // Название лота
    @FXML private TextField regularName;            // Общее название лота(общая для всех лотов часть)
    @FXML private TextField boxName;                // Номер коробки
    @FXML private ChoiceBox<String> categoriesBox;  // Категории лотов
    @FXML private TextField price;                  // Стоимости
    @FXML private CheckBox bestOffer;               // Торг уместен
    private List<String> fileNames;                 // Ссылки на файлы изображений
    private MainApp mainApp;
    private Stage dialogStage;

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    private void builder(Lot lot, String pic1, String pic2)
    {
        lot.getLotName().setFixedName(name.getText());
        lot.getLotName().setRegularName(regularName.getText());
        lot.getLotName().setBox(boxName.getText());
        lot.setCategory(categoriesBox.getSelectionModel().getSelectedItem());
        lot.setPrice(Integer.parseInt(price.getText()));
        lot.setBestOffer(bestOffer.isSelected());
        lot.setImage1(pic1);
        lot.setImage2(pic2);
        validationName(lot); // Валидация имени
    }

    private void validationName(Lot lot) // Удаление пробелов и точки
    {
        String fixedName = lot.getLotName().getFixedName().trim();
        if(fixedName.length() > 0 && fixedName.charAt(fixedName.length() - 1) == '.')
        {
            fixedName = fixedName.substring(0, fixedName.length() - 2);
        }
        lot.getLotName().setFixedName(fixedName);
    }

    @FXML
    private void initialize()
    {
        categoriesBox.setItems(CategoryService.getCategoryNames());
    }

    @FXML
    private void handleUploadImages()
    {
        fileNames = new ArrayList<>(); // Загружаем ссылки на изображения из папки
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File lastFile = new File(PreferenceServes.getLastPhotoDirectory());
        if(lastFile.exists())
            directoryChooser.setInitialDirectory(lastFile);
        File directoryFile = directoryChooser.showDialog(dialogStage);
        if(directoryFile != null)
        {
            try(Stream<Path> paths = Files.walk(Paths.get(directoryFile.getAbsolutePath())))
            {
                paths.forEach(filePath ->
                {
                    if(Files.isRegularFile(filePath))
                    {
                        fileNames.add(filePath.toString());
                    }
                });
            } catch(IOException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка загрузки папки");
                alert.setContentText("Папка с файлами не найдена!");

                alert.showAndWait();
            }
        }
    }

    @FXML
    private void handleCreateLots()
    {
        if(fileNames == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка выбора папки");
            alert.setContentText("Папка не содержит файлов!");

            alert.showAndWait();
        }
        else if(fileNames.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка выбора папки");
            alert.setContentText("Папка с фотографиями не выбрана!");

            alert.showAndWait();
        }
        else if(fileNames.size() % 2 != 0)
        {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка загрузки фото");
            alert.setContentText("Папка содержит нечетное кол-во изображений!");

            alert.showAndWait();
        }
        else
        {
            for(int i = 0; i < fileNames.size(); i += 2)
            {
                Lot lot = new Lot();
                builder(lot, fileNames.get(i), fileNames.get(i+1));
                App.getLots().add(lot);
            }
            dialogStage.close();
            mainApp.getRootController().handleSaveLots();
        }
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }
}
