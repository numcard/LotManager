package app.service;

import app.AppException;
import app.model.Lot;
import app.model.LotListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileNotFoundException;

public class LotService
{
    private static final ObservableList<Lot> lots = FXCollections.observableArrayList();

    private static boolean fileNotExists(File file)
    {
        if(!file.exists())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Загрузка файла");
            alert.setContentText("Файл с лотами не найден:\n" + file.getPath());
            alert.showAndWait();
        }

        return !file.exists();
    }

    public static ObservableList<Lot> loadLots(File file)
    {
        try
        {
            if(fileNotExists(file))
                throw new FileNotFoundException(file.getAbsolutePath());
            JAXBContext context = JAXBContext.newInstance(LotListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            LotListWrapper wrapper = (LotListWrapper) um.unmarshal(file);

            lots.clear();
            if(wrapper.getLots() != null)
                lots.addAll(wrapper.getLots());
            // Сохраняем путь к файлу в реестре.
            PreferenceServes.setLotFilePath(file);
        } catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + file.getAbsolutePath());
        } catch(JAXBException e)
        {
            AppException.Throw(e);
        }
        return lots;
    }

    public static void saveLots(File file, ObservableList<Lot> saveLots)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(LotListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах.
            LotListWrapper wrapper = new LotListWrapper();
            wrapper.setLots(saveLots);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            PreferenceServes.setLotFilePath(file);
        }
        catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }
}
