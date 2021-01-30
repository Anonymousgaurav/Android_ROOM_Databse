package com.gaurav.android_gisfy;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor executors = Executors.newSingleThreadExecutor();
    private Drive drive;


    public DriveServiceHelper(Drive drive) {
        this.drive = drive;
    }


    public Task<String> createPdfFile(String filePath) {
        return Tasks.call(executors, () ->
                {
                    File fileData = new File();
                    fileData.setName("My Data");

                    java.io.File file = new java.io.File(filePath);

                    FileContent fileContent = new FileContent("application/pdf", file);
                    File myFile = null;
                    try {

                        myFile = drive.files().create(fileData, fileContent).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (myFile == null)
                    {
                        throw new IOException("Null result");
                    }

                    return myFile.getName();


                }

        );
    }



}
