package app.controller;

import app.MainApp;
import app.model.App;
import app.model.Lot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class LotOverviewController
{
    private static final String IMG_FILE_PART = "file:";
    private static final ObservableList<Lot> selectedLots = FXCollections.observableArrayList();
    @FXML private TableView<Lot> lotTable;
    @FXML private TableColumn<Lot, String> nameColumn;
    @FXML private TableColumn<Lot, String> categoryColumn;
    @FXML private TableColumn<Lot, String> boxColumn;
    @FXML private Label nameLabel;
    @FXML private Label boxLabel;
    @FXML private Label priceLabel;
    @FXML private CheckBox bestOfferBox;
    @FXML private Label category;
    @FXML private ImageView pic1;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp)
    {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize()
    {
        lotTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lotTable.setItems(App.getLots());

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getLotName().fixedNameProperty());
        boxColumn.setCellValueFactory(cellData -> cellData.getValue().getLotName().boxProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());

        showLotDetails(null);

        lotTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showLotDetails(newValue));

        lotTable.setOnKeyPressed(keyEvent ->
        {
            KeyCode keyCode = keyEvent.getCode();
            if(keyCode == KeyCode.DELETE)
            {
                handleDeleteLot();
            }
            if(keyCode == KeyCode.Q)
            {
                handleNewLotAs();
            }
            if(keyCode == KeyCode.E)
            {
                handleEditLot();
            }
        });
    }

    private static void showLotAlert()
    {
        if(selectedLots.size() > 1)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Выбрано слишком много лотов!");
            alert.setContentText("Выберите один лот для редактирования.");
            alert.showAndWait();
        }
        else if(selectedLots.size() < 1)
        {
            // Ничего не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Лот не выбран");
            alert.setContentText("Выберите лот для копирования.");
            alert.showAndWait();
        }
    }

    private void showLotDetails(Lot lot)
    {
        if(lot != null)
        {
            nameLabel.setText(lot.getLotName().getFullName());
            boxLabel.setText(lot.getLotName().getBox());
            priceLabel.setText(String.valueOf(lot.getPrice()));
            bestOfferBox.setVisible(true);
            bestOfferBox.setSelected(lot.isBestOffer());
            category.setText(lot.getCategory());
            if(lot.getImage1().length() > 0)
            {
                pic1.setVisible(true);
                pic1.setImage(new Image(IMG_FILE_PART + lot.getImage1()));
            }
            else
            {
                pic1.setVisible(false);
            }
        }
        else
        {
            nameLabel.setText("");
            boxLabel.setText("");
            priceLabel.setText("");
            bestOfferBox.setVisible(false);
            bestOfferBox.setSelected(false);
            category.setText("");
            pic1.setVisible(false);
        }
    }

    @FXML
    private void handleNewLot()
    {
        Lot tempLot = new Lot();
        boolean okClicked = mainApp.showLotEditDialog(tempLot);
        if(okClicked)
            App.getLots().add(tempLot);
    }

    @FXML
    private void handleNewLotAs()
    {
        selectedLots.clear();
        selectedLots.addAll(lotTable.getSelectionModel().getSelectedItems());
        if(selectedLots.size() == 1)
        {
            Lot selectedLot = selectedLots.get(0);
            Lot lot = new Lot();
            lot.getLotName().setRegularName(selectedLot.getLotName().getRegularName());
            lot.getLotName().setBox(selectedLot.getLotName().getBox());
            lot.setBestOffer(selectedLot.isBestOffer());
            lot.setCategory(selectedLot.getCategory());
            lot.setPrice(selectedLot.getPrice());
            boolean okClicked = mainApp.showLotEditDialog(lot);
            if(okClicked)
                App.getLots().add(lot);
        } else
        {
            showLotAlert();
        }
    }

    @FXML
    private void handleEditLot()
    {
        selectedLots.clear();
        selectedLots.addAll(lotTable.getSelectionModel().getSelectedItems());
        if(selectedLots.size() == 1)
        {
            boolean okClicked = mainApp.showLotEditDialog(selectedLots.get(0));
            if(okClicked)
            {
                showLotDetails(selectedLots.get(0));
            }
        }
        else
        {
            showLotAlert();
        }
    }

    @FXML
    private void handleDeleteLot()
    {
        selectedLots.clear();
        selectedLots.addAll(lotTable.getSelectionModel().getSelectedItems());
        if(selectedLots.size() > 1)
        {
            App.getLots().removeAll(selectedLots);
        }
        else if(selectedLots.size() == 1)
        {
            App.getLots().remove(selectedLots.get(0));
        }
        else if(selectedLots.size() < 1)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Лот не выбран");
            alert.setContentText("Выберите лот для удаления.");

            alert.showAndWait();
        }
    }
}
