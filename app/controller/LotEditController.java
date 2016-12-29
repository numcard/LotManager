package app.controller;

import app.model.Lot;
import app.service.CategoryService;
import app.service.PreferenceServes;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class LotEditController
{
    private static final String IMG_FILE_PART = "file:";
    @FXML private TextField name;                   // Название лота
    @FXML private TextField regularName;            // Общее название лота(общая для всех лотов часть)
    @FXML private TextField boxName;                // Номер коробки
    @FXML private ChoiceBox<String> categoriesBox;  // Категорий лота
    @FXML private TextField price;                  // Стоимости
    @FXML private CheckBox bestOffer;               // Торг уместен
    @FXML private ImageView pic1;
    @FXML private ImageView pic2;
    @FXML private ImageView pic3;
    @FXML private ImageView pic4;
    private List<File> files;                       // Загружаемые изображения ввиде файлов

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
        categoriesBox.setItems(CategoryService.getCategoryNames());
    }

    // Редактирование лота
    public void setLot(Lot lot)
    {
        this.lot = lot;

        name.setText(lot.getLotName().getFixedName());
        regularName.setText(lot.getLotName().getRegularName());
        boxName.setText(lot.getLotName().getBox());
        categoriesBox.getSelectionModel().select(lot.getCategory());
        price.setText(String.valueOf(lot.getPrice()));
        bestOffer.setSelected(lot.isBestOffer());
        if(lot.getImage1().length() > 0)
        {
            pic1.setVisible(true);
            pic1.setImage(new Image(IMG_FILE_PART + lot.getImage1()));
        } else
            pic1.setVisible(false);

        if(lot.getImage2().length() > 0)
        {
            pic2.setVisible(true);
            pic2.setImage(new Image(IMG_FILE_PART + lot.getImage2()));
        } else
            pic2.setVisible(false);

        if(lot.getImage3().length() > 0)
        {
            pic3.setVisible(true);
            pic3.setImage(new Image(IMG_FILE_PART + lot.getImage3()));
        } else
            pic3.setVisible(false);

        if(lot.getImage4().length() > 0)
        {
            pic4.setVisible(true);
            pic4.setImage(new Image(IMG_FILE_PART + lot.getImage4()));
        } else
            pic4.setVisible(false);
    }

    @FXML
    private void handleUploadImages()
    {
        FileChooser fileChooser = new FileChooser();
        File lastFolder = new File(PreferenceServes.getLastPhotoDirectory());
        if(lastFolder.exists())
            fileChooser.setInitialDirectory(lastFolder);
        files = fileChooser.showOpenMultipleDialog(dialogStage);
        int numFiles = (files == null) ? 0 : files.size();

        if(files != null)
        {
            for(int i = 1; i <= 4; i++)
            {
                if(i == 1)
                {
                    if(i <= numFiles)
                    {
                        pic1.setVisible(true);
                        pic1.setImage(new Image(IMG_FILE_PART + files.get(i - 1).getAbsolutePath()));
                    } else
                        pic1.setVisible(false);
                }
                if(i == 2)
                {
                    if(i <= numFiles)
                    {
                        pic2.setVisible(true);
                        pic2.setImage(new Image(IMG_FILE_PART + files.get(i - 1).getAbsolutePath()));
                    } else
                        pic2.setVisible(false);
                }
                if(i == 3)
                {
                    if(i <= numFiles)
                    {
                        pic3.setVisible(true);
                        pic3.setImage(new Image(IMG_FILE_PART + files.get(i - 1).getAbsolutePath()));
                    } else
                        pic3.setVisible(false);
                }
                if(i == 4)
                {
                    if(i <= numFiles)
                    {
                        pic4.setVisible(true);
                        pic4.setImage(new Image(IMG_FILE_PART + files.get(i - 1).getAbsolutePath()));
                    } else
                        pic4.setVisible(false);
                }
            }
        }
    }

    @FXML
    private void handleOk()
    {
        if(isInputValid())
        {
            lot.getLotName().setFixedName(name.getText());
            lot.getLotName().setRegularName(regularName.getText());
            lot.getLotName().setBox(boxName.getText());
            lot.setCategory(categoriesBox.getSelectionModel().getSelectedItem());
            lot.setPrice(Integer.parseInt(price.getText()));
            lot.setBestOffer(bestOffer.isSelected());
            if(files != null)
            {
                PreferenceServes.setLastPhotoDirectory(files.get(0).getAbsolutePath());
                lot.setImage1("");
                lot.setImage2("");
                lot.setImage3("");
                lot.setImage4("");
                int count = 0;
                for(File file : files)
                {
                    count++;
                    if(count == 1)
                        lot.setImage1(file.getAbsolutePath());
                    if(count == 2)
                        lot.setImage2(file.getAbsolutePath());
                    if(count == 3)
                        lot.setImage3(file.getAbsolutePath());
                    if(count == 4)
                        lot.setImage4(file.getAbsolutePath());
                }
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
        } else
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
