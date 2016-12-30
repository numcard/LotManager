package app.service;

import app.AppException;
import app.model.Lot;
import app.model.LotListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileNotFoundException;

public class LotService
{
    private static LotService instance;
    private final PreferenceService preferenceService = PreferenceService.getInstance();
    private LotService()
    {}

    public static LotService getInstance()
    {
        if(instance == null)
            instance = new LotService();
        return instance;
    }

    public ObservableList<Lot> loadLots(File file)
    {
        ObservableList<Lot> lots = FXCollections.observableArrayList();
        try
        {
            if(!file.exists())
                throw new FileNotFoundException(file.getAbsolutePath());

            JAXBContext context = JAXBContext.newInstance(LotListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            LotListWrapper wrapper = (LotListWrapper) um.unmarshal(file);

            lots.clear();
            if(wrapper.getLots() != null)
                lots.addAll(wrapper.getLots());

            // Сохраняем путь к файлу в реестре.
            preferenceService.setLotFilePath(file);
        }
        catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + file.getAbsolutePath());
        }
        catch(JAXBException e)
        {
            AppException.Throw(e);
        }
        return lots;
    }

    public void saveLots(File file, ObservableList<Lot> saveLots)
    {
        try
        {
            if(!file.exists())
                throw new FileNotFoundException(file.getAbsolutePath());
            JAXBContext context = JAXBContext.newInstance(LotListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах.
            LotListWrapper wrapper = new LotListWrapper();
            wrapper.setLots(saveLots);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            preferenceService.setLotFilePath(file);
        }
        catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + file.getAbsolutePath());
        }
        catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }
}