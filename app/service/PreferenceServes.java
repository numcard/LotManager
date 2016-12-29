package app.service;

import app.MainApp;

import java.io.File;
import java.util.prefs.Preferences;

public class PreferenceServes
{
    private PreferenceServes()
    {}

    public static String getLastPhotoDirectory()
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        return prefs.get("lastPhotoDirectory", "");
    }

    public static void setLastPhotoDirectory(String lastPhotoDirectory)
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String[] newDirectory = lastPhotoDirectory.split(new File(lastPhotoDirectory).getName());
        prefs.put("lastPhotoDirectory", newDirectory[0]);
    }

    public static String getLastImportDirectory()
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        return prefs.get("lastImportDirectory", "");
    }

    public static void setLastImportDirectory(String lastImportDirectory)
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String[] newDirectory = lastImportDirectory.split(new File(lastImportDirectory).getName());
        prefs.put("lastImportDirectory", newDirectory[0]);
    }

    public static String getLastOpenDirectory()
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        return prefs.get("lastOpenDirectory", "");
    }

    public static void setLastOpenDirectory(String lastImportDirectory)
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String[] newDirectory = lastImportDirectory.split(new File(lastImportDirectory).getName());
        prefs.put("lastOpenDirectory", newDirectory[0]);
    }

    public static File getLotFile()
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("lotFilePath", null);
        if(filePath != null)
        {
            File file = new File(filePath);
            if(file.exists())
                return file;
            else
                return null;
        } else
        {
            return null;
        }
    }

    public static void setLotFilePath(File file)
    {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if(file != null)
        {
            prefs.put("lotFilePath", file.getPath());
        } else
        {
            prefs.remove("lotFilePath");
        }
    }
}
