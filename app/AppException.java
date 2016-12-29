package app;

import java.io.*;

public final class AppException extends Exception
{
    private static final String filePath = "src/app/log/log.txt";

    private AppException()
    {}

    public static void Throw(Exception exception)
    {
        Throw(exception, null);
    }

    public static void Throw(Exception exception, String message)
    {
        try
        {
            FileWriter fw = new FileWriter(filePath, true);
            PrintWriter pw = new PrintWriter(fw);

            if(message != null)
                pw.write(message + "\n");
            exception.printStackTrace(pw);
            exception.printStackTrace();
            pw.close();
            fw.close();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}