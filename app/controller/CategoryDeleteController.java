package app.controller;

import app.service.CategoryService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class CategoryDeleteController
{
    @FXML private ChoiceBox<String> categoriesBox;  // Список категорий
    private static final ObservableList<String> categoryNames = CategoryService.getCategoryNames();
    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void initialize()
    {
        categoriesBox.setItems(categoryNames);
        categoriesBox.getSelectionModel().select(0);
    }

    @FXML
    private void handleDeleteCategory()
    {
        CategoryService.removeAll(categoriesBox.getSelectionModel().getSelectedItem());
        categoryNames.removeAll(categoriesBox.getSelectionModel().getSelectedItem());
        if(categoryNames.size() < 1)
            dialogStage.close();
        else
            categoriesBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleCancel()
    {
        dialogStage.close();
    }
}
