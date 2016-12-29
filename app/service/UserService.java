package app.service;

import app.AppException;
import app.model.User;
import app.model.UserListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;

public final class UserService
{
    private static final String filePath = "src/app/resources/users.xml";
    private static final File file = new File(filePath);
    private static final ObservableList<User> users = FXCollections.observableArrayList();
    private final static ObservableList<String> userNames = FXCollections.observableArrayList();
    private final static ObservableList<User> findUsers = FXCollections.observableArrayList();
    static
    {
        loadUsers();
    }

    private static boolean fileNotExists()
    {
        if(!file.exists())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Загрузка файла");
            alert.setContentText("Пользовательский файл не найден:\n" + file.getPath());
        }
        return !file.exists();
    }

    public static void addUser(User user)
    {
        users.add(user);
        saveUsers();
    }

    public static void removeAll(String userName)
    {
        findUsers.clear();
        users.forEach(user ->
        {
            if(user.getName().equals(userName))
                findUsers.add(user);
        });
        users.removeAll(findUsers);
        saveUsers();
    }

    public static ObservableList<User> findUserByName(String userName)
    {
        findUsers.clear();
        users.forEach(user ->
        {
            if(user.getName().equals(userName))
                findUsers.add(user);
        });
        return findUsers;
    }

    public static ObservableList<String> getUserNames()
    {
        userNames.clear();
        users.forEach(user -> userNames.add(user.getName()));
        return userNames;
    }

    private static void loadUsers()
    {
        try
        {
            if(fileNotExists())
                throw new FileNotFoundException(filePath);
            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Чтение XML из файла и демаршализация.
            UserListWrapper wrapper = (UserListWrapper) um.unmarshal(file);

            users.clear();
            if(wrapper.getUsers() != null)
                users.addAll(wrapper.getUsers());
        } catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + filePath);
        } catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }

    public static void saveUsers()
    {
        try
        {
            if(fileNotExists())
                throw new FileNotFoundException(filePath);
            JAXBContext context = JAXBContext.newInstance(UserListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Обёртываем наши данные об адресатах.
            UserListWrapper wrapper = new UserListWrapper();
            wrapper.setUsers(users);

            // Маршаллируем и сохраняем XML в файл.
            m.marshal(wrapper, file);
        } catch(FileNotFoundException e)
        {
            AppException.Throw(e, "Файл не найден: " + filePath);
        } catch(JAXBException e)
        {
            AppException.Throw(e);
        }
    }
}
