package app.controller;

import app.model.Lot;
import app.service.CategoryService;
import app.service.PreferenceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class LotEditController
{
    private final String IMG_FILE_PART = "file:";
    private List<File> files;                       // Загружаемые изображения ввиде файлов
    @FXML private TextField name;                   // Название лота
    @FXML private TextField regularName;            // Общее название лота(общая для всех лотов часть)
    @FXML private TextField boxName;                // Номер коробки
    @FXML private ChoiceBox<String> categoriesBox;  // Категорий лота
    @FXML private TextField price;                  // Стоимости
    @FXML private CheckBox bestOffer;               // Торг уместен
    @FXML private ObservableList<ImageView> pics;
    @FXML private ImageView pic1;
    @FXML private ImageView pic2;
    private final CategoryService categoryService = CategoryService.getInstance();
    private final PreferenceService preferenceService = PreferenceService.getInstance();
    private Stage dialogStage;
    private Lot lot;
    private boolean okClicked = false;

    public boolean isOkClicked()
    {
        return okClicked;
    }
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize()
    {
        categoriesBox.setItems(categoryService.getCategoryNames());
        pics = FXCollections.observableArrayList();
        pics.add(pic1);
        pics.add(pic2);
    }

    public void setLot(Lot lot)
    {
        this.lot = lot;

        name.setText(lot.getFixedName());
        regularName.setText(lot.getRegularName());
        boxName.setText(lot.getBox());
        categoriesBox.getSelectionModel().select(lot.getCategory());
        price.setText(String.valueOf(lot.getPrice()));
        bestOffer.setSelected(lot.isBestOffer());
        ObservableList<String> lotImages = lot.getImages();
        for(int i = 0; i < pics.size(); i++)
        {
            if(lotImages.size() > i)
            {
                pics.get(i).setVisible(true);
                pics.get(i).setImage(new javafx.scene.image.Image(IMG_FILE_PART + lotImages.get(i), 280, 228, true, false));
            }
            else
            {
                pics.get(i).setVisible(false);
            }
        }
    }

    @FXML
    private void handleUploadImages()
    {
        FileChooser fileChooser = new FileChooser();

        File lastFolder = new File(preferenceService.getLastPhotoDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);

        files = fileChooser.showOpenMultipleDialog(dialogStage);

        for(int i = 0; i < pics.size(); i++)
        {
            if(i < files.size())
            {
                pics.get(i).setVisible(true);
                pics.get(i).setImage(new javafx.scene.image.Image(IMG_FILE_PART + files.get(i).getAbsolutePath(), 280, 228, true, false));
            }
            else
                pics.get(i).setVisible(false);
        }
    }

    @FXML
    private void handleOk()
    {
        if(isInputValid())
        {
            lot.setFixedName(name.getText());
            lot.setRegularName(regularName.getText());
            lot.setBox(boxName.getText());
            lot.setCategory(categoriesBox.getSelectionModel().getSelectedItem());
            lot.setPrice(Integer.parseInt(price.getText()));
            lot.setBestOffer(bestOffer.isSelected());
            if(files != null)
            {
                preferenceService.setLastPhotoDirectory(files.get(0).getAbsolutePath());
                lot.getImages().clear();
                for(File file : files)
                    lot.getImages().add(file.getAbsolutePath());
            }
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }

    private boolean isInputValid()
    {
        String errorMessage = "";

        if(name.getText() == null || name.getText().length() == 0)
        {
            errorMessage += "Название лота не валидно!\n";
        }
        if(boxName.getText() == null || boxName.getText().length() == 0)
        {
            errorMessage += "Нет коробочки!\n";
        }
        if(price.getText() == null || Integer.parseInt(price.getText()) < 5)
        {
            errorMessage += "Стоимость лота не валидно!\n";
        }
        String selectedCategory = categoriesBox.getSelectionModel().getSelectedItem();
        if(selectedCategory == null || selectedCategory.length() == 0)
        {
            errorMessage += "Категория не выбрана!\n";
        }
        if(pic1.getImage() == null)
        {
            errorMessage += "Фото не выбрано!\n";
        }
        if(errorMessage.length() == 0)
        {
            return true;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Невалидные поля");
            alert.setHeaderText("Исправьте невалидные поля");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }
}
