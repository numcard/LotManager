package app.controller;

import app.model.Category;
import app.service.CategoryService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryEditController
{
    @FXML private TextField name;   // Название категории
    private Stage dialogStage;
    private Category category;
    private boolean okClicked = false;
    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage = dialogStage;
    }
    public void setCategory(Category category)
    {
        this.category = category;
    }
    public boolean isOkClicked()
    {
        return okClicked;
    }

    @FXML
    private void handleNewCategory()
    {
        if(isInputValid())
        {
            category.setName(name.getText());
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
        ObservableList<Category> category = CategoryService.findCategoryByName(name.getText());
        if(category.size() > 0)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Внимание");
            alert.setHeaderText("Категория с таким именем существует!");
            alert.setContentText("Придумайте новую категорию!");
            alert.showAndWait();
            return false;
        }
        return name.getText().length() > 0;
    }
}
