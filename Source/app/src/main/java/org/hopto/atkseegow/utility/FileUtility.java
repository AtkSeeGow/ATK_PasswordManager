package org.hopto.atkseegow.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileUtility {
    public static String FolderName = "PasswordManager";

    public static void GetPermission(Activity activity){
        int permissionCheck1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
        }
    }

    public static File GetRootPath(){
        File result = new File(Environment.getExternalStorageDirectory(),FileUtility.FolderName);

        if(!result.exists())
            result.mkdirs();

        return result;
    }

    public static File[] ListFiles(String queryValue){
        File rootPath = FileUtility.GetRootPath();

        final String filterValue = queryValue == null ? "" : queryValue;

        File[] result = rootPath.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                    return name.matches(".*" + filterValue + ".*") && name.toLowerCase().endsWith(".txt");
            }
        });

        if(result == null)
            result = new File[0];

        return result;
    }

    public static void SaveFile(EditText titleEditText, String fileContent){
        try{
            FileUtility.SaveFile(titleEditText.getText().toString() + ".txt", fileContent);
        }catch(IOException ioException){
            Toast.makeText(titleEditText.getContext(), ioException.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public  static void SaveFile(String title, String fileContent) throws  IOException{
        File filePath = new File(FileUtility.GetRootPath(), title.toString());
        FileWriter fileWriter = new FileWriter(filePath, false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(fileContent);
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public static String ReadFile(String filePath, Activity activity){
        StringBuilder result = new StringBuilder();

        File file = new File(filePath);

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            String lineContent;
            while ((lineContent = bufferedReader.readLine()) != null)
                result.append(lineContent);

            bufferedReader.close();
        }
        catch (IOException ioException) {
            Toast.makeText(activity, ioException.getMessage(), Toast.LENGTH_LONG).show();
        }

        return result.toString();
    }

    public static void DeleteFile(String filePath){
        File file = new File(filePath);
        file.delete();
    }
}
