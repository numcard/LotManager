package app.service;

import app.MainApp;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.util.prefs.Preferences;

public class PreferenceService
{
    private static PreferenceService instance;
    private final Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

    private PreferenceService()
    {}

    private String getDirectory(String filePath)
    {
        return filePath.split(new File(filePath).getName())[0];
    }

    public static PreferenceService getInstance()
    {
        if(instance == null)
            instance = new PreferenceService();
        return instance;
    }

    public String getLastPhotoDirectory()
    {
        return prefs.get("lastPhotoDirectory", "");
    }
    public void setLastPhotoDirectory(String filePath)
    {
        prefs.put("lastPhotoDirectory", getDirectory(filePath));
    }
    public String getLastAutoloadDirectory()
    {
        return prefs.get("lastAutoloadDirectory", "");
    }
    public void setLastAutoloadDirectory(String filePath)
    {
        prefs.put("lastAutoloadDirectory", filePath);
    }
    public String getLastOpenDirectory()
    {
        return prefs.get("lastOpenDirectory", "");
    }
    public void setLastOpenDirectory(String filePath)
    {
        prefs.put("lastOpenDirectory", getDirectory(filePath));
    }

    @Nullable
    public File getLotFile()
    {
        String filePath = prefs.get("lotFilePath", null);
        if(filePath != null)
        {
            File file = new File(filePath);
            if(file.exists())
                return file;
            else
                return null;
        }
        else
            return null;
    }

    public void setLotFilePath(File file)
    {
        if(file != null)
            prefs.put("lotFilePath", file.getPath());
        else
            prefs.remove("lotFilePath");
    }
}
