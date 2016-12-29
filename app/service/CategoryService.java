package app.service;

import app.AppException;
import app.model.Category;
import app.model.CategoryListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.xml.bind.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CategoryService
{
    private static final String filePath = "src/app/resources/categories.xml";
    private static final File file = new File(filePath);
    private static final ObservableList<Category> categories = FXCollections.observableArrayList();
    private static final ObservableList<String> categoryNames = FXCollections.observableArrayList();
    private static final ObservableList<Category> findCategory = FXCollections.observableArrayList();
    static {
        loadCategories();
    }

    private static boolean fileNotExists()
    {
        if(!file.exists())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Загрузка файла");
            alert.setContentText("Файл с категориями не найден:\n" + file.getPath());
            alert.showAndWait();
        }

        return !file.exists();
    }

    public static void addCategory(Category category)
    {
        categories.add(category);
        saveCategories();
    }

    public static void removeAll(String categoryName)
    {
        categories.removeAll(findCategoryByName(categoryName));
        saveCategories();
    }

    public static ObservableList<Category> findCategoryByName(String categoryName)
    {
        findCategory.clear();
        categories.forEach(category -> {
            if(category.getName().equals(categoryName))
                findCategory.add(category);
        });
        return findCategory;
    }

    public static ObservableList<String> getCategoryNames()
    {
        categoryNames.clear();
        categories.forEach(category -> categoryNames.add(category.getName()));
        return categoryNames;
    }

    private static void loadCategories()
    {
        try
        {
            if(fileNotExists())
                throw new FileNotFoundException(filePath);
            JAXBContext context = JAXBContext.newInstance(CategoryListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            CategoryListWrapper wrapper = (CategoryListWrapper) um.unmarshal(file);

            categories.clear();
            if(wrapper.getCategories() != null)
                categories.addAll(wrapper.getCategories());
        } catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + filePath);
        } catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }

    private static void saveCategories()
    {
        try
        {
            if(fileNotExists())
                throw new FileNotFoundException(filePath);
            JAXBContext context = JAXBContext.newInstance(CategoryListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах.
            CategoryListWrapper wrapper = new CategoryListWrapper();
            wrapper.setCategories(categories);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);
        }
        catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + filePath);
        }
        catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }
}
