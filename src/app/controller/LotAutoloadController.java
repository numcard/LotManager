package app.controller;

import app.model.App;
import app.model.Lot;
import app.service.CategoryService;
import app.service.PreferenceService;
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
	@FXML
	private TextField name;                       // Название лота
	@FXML
	private TextField regularName;                // Общее название лота(общая для всех лотов часть)
	@FXML
	private TextField boxName;                    // Номер коробки
	@FXML
	private ChoiceBox<String> categoriesBox;      // Категории лотов
	@FXML
	private TextField price;                      // Стоимости
	@FXML
	private CheckBox bestOffer;                   // Торг уместен
	private List<String> photoLinks;                    // Ссылки на файлы изображений
	private final App app = App.getInstance();
	private final int photoSetCounter = 2;              // Кол-во фото в автозагрузке
	private final CategoryService categoryService = CategoryService.getInstance();
	private final PreferenceService preferenceServes = PreferenceService.getInstance();
	private Stage dialogStage;

	public void setDialogStage(Stage dialogStage)
	{
		this.dialogStage = dialogStage;
	}

	@FXML
	private void initialize()
	{
		categoriesBox.setItems(categoryService.getCategoryNames());
	}

	@FXML
	private void handleUploadImages()
	{
		photoLinks = new ArrayList<>();
		DirectoryChooser directoryChooser = new DirectoryChooser();

		File lastFile = new File(preferenceServes.getLastAutoloadDirectory());
		if(lastFile.exists()) directoryChooser.setInitialDirectory(lastFile);

		File directoryFile = directoryChooser.showDialog(dialogStage);
		if(directoryFile != null)
		{
			try(Stream<Path> paths = Files.walk(Paths.get(directoryFile.getAbsolutePath())))
			{
				paths.forEach((Path filePath) -> {
					if(Files.isRegularFile(filePath))
					{
						if(directoryFile.getAbsolutePath().equals(filePath.getParent().toString()))
							photoLinks.add(filePath.toString());
					}
				});
			}
			catch(IOException e)
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Ошибка");
				alert.setHeaderText("Ошибка загрузки папки");
				alert.setContentText("Папка с файлами не найдена!");
				alert.showAndWait();
			}
			preferenceServes.setLastAutoloadDirectory(directoryFile.getAbsolutePath());
		}
	}

	@FXML
	private void handleCreateLots()
	{
		if(photoLinks == null)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Ошибка выбора папки");
			alert.setContentText("Папка не содержит фото!");
			alert.showAndWait();
		}
		else if(photoLinks.isEmpty())
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Ошибка выбора папки");
			alert.setContentText("Папка с фотографиями не выбрана!");
			alert.showAndWait();
		}
		else if(photoLinks.size() % photoSetCounter != 0)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ошибка");
			alert.setHeaderText("Ошибка загрузки фото");
			alert.setContentText("Требуется четность:" + photoSetCounter);
			alert.showAndWait();
		}
		else
		{
			for(int i = 0; i < photoLinks.size(); i += photoSetCounter)
			{
				Lot lot = new Lot();
				builder(lot, photoLinks, i);
				app.getLots().add(lot);
			}
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel()
	{
		dialogStage.close();
	}

	private void builder(Lot lot, List<String> photoLinks, int startIndex)
	{
		lot.setFixedName(name.getText());
		lot.setRegularName(regularName.getText());
		lot.setBox(boxName.getText());
		lot.setCategory(categoriesBox.getSelectionModel().getSelectedItem());
		lot.setPrice(Integer.parseInt(price.getText()));
		lot.setBestOffer(bestOffer.isSelected());
		for(int i = startIndex; i < startIndex + photoSetCounter; i++)
		{
			lot.getImages().add(photoLinks.get(i));
		}

		validationName(lot); // Валидация имени
	}

	private void validationName(Lot lot) // Удаление пробелов и точки
	{
		String fixedName = lot.getFixedName().trim();
		if(fixedName.length() > 0 && fixedName.charAt(fixedName.length() - 1) == '.')
		{
			fixedName = fixedName.substring(0, fixedName.length() - 2);
		}
		lot.setFixedName(fixedName);
	}
}
