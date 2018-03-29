package org.hopto.atkseegow.utility;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.hopto.atkseegow.passwordmanager.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class CloudUtility {
    public static final int REQUEST_CODE_SIGN_IN = 0;

    public static DriveClient DriveClient;
    public static DriveResourceClient DriveResourceClient;

    public static void SignIn(Activity activity) {
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient(activity);
        activity.startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private static GoogleSignInClient buildGoogleSignInClient(Activity activity) {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(activity, signInOptions);
    }

    public static void Upload(final Activity activity){
        final Task<MetadataBuffer> metadataBufferTask = CloudUtility.getQueryPackageMetadataBufferTask();

        Tasks.whenAll(metadataBufferTask)
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        MetadataBuffer metadataBuffer = metadataBufferTask.getResult();
                        CloudUtility.deletePackageFolder(metadataBuffer);
                        CloudUtility.createPackageFolder(activity);
                        return null;
                    }
                });
    }

    public static void Download(final Activity activity){
        File[] filePaths = FileUtility.ListFiles("");
        for (File filePath: filePaths)
            filePath.delete();

        final Task<MetadataBuffer> metadataBufferTask = CloudUtility.getQueryPackageMetadataBufferTask();

        Tasks.whenAll(metadataBufferTask)
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        MetadataBuffer metadataBuffer = metadataBufferTask.getResult();
                        CloudUtility.openPackageFolder(activity, metadataBuffer);
                        return null;
                    }
                });
    }

    private static Task<MetadataBuffer> getQueryPackageMetadataBufferTask(){
        Query query = new Query.Builder().addFilter(
                Filters.and(
                        Filters.eq(SearchableField.TITLE, FileUtility.FolderName),
                        Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder"))).build();
        return CloudUtility.DriveResourceClient.query(query);
    }

    private static void deletePackageFolder(MetadataBuffer metadataBuffer){
        for(Metadata metadata: metadataBuffer){
            DriveFolder driveFolder = metadata.getDriveId().asDriveFolder();
            CloudUtility.DriveResourceClient.delete(driveFolder);
        }
    }

    private static void createPackageFolder(final Activity activity){
        CloudUtility.DriveResourceClient
                .getRootFolder()
                .continueWithTask(new Continuation<DriveFolder, Task<DriveFolder>>() {
                    @Override
                    public Task<DriveFolder> then(@NonNull Task<DriveFolder> task)
                            throws Exception {
                        DriveFolder parentFolder = task.getResult();
                        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                .setTitle(FileUtility.FolderName)
                                .setMimeType(DriveFolder.MIME_TYPE)
                                .setStarred(true)
                                .build();
                        return CloudUtility.DriveResourceClient.createFolder(parentFolder, changeSet);
                    }
                })
                .addOnSuccessListener(activity, new OnSuccessListener<DriveFolder>() {
                    @Override
                    public void onSuccess(DriveFolder driveFolder) {
                        CloudUtility.updatePackageFile(activity, driveFolder);
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private static void updatePackageFile(final Activity activity, final DriveFolder driveFolder){
        File[] filePaths = FileUtility.ListFiles("");

        for (File filePath: filePaths) {
            final String fileName = filePath.toString().replace(FileUtility.GetRootPath().toString() + "/", "");
            final String fileContent = FileUtility.ReadFile(filePath.toString(), activity);

            CloudUtility.DriveResourceClient.createContents().continueWithTask(new Continuation<DriveContents, Task<DriveContents>>() {
                @Override
                public Task<DriveContents> then(@NonNull Task<DriveContents> task) throws Exception { return task; }
            }).addOnSuccessListener(activity, new OnSuccessListener<DriveContents>() {
                @Override
                public void onSuccess(DriveContents driveContents) {
                    OutputStream outputStream = driveContents.getOutputStream();

                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(fileContent);
                    }
                    catch (Exception exception){
                        Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    MetadataChangeSet fileMetadataChangeSet = new MetadataChangeSet.Builder()
                            .setTitle(fileName)
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    CloudUtility.DriveResourceClient.createFile(driveFolder, fileMetadataChangeSet, driveContents);
                }
            });
        }
    }

    private static void openPackageFolder(final Activity activity, MetadataBuffer folderMetadataBuffer){
        for(Metadata folderMetadata: folderMetadataBuffer){
            Filter parentFilter = Filters.in(SearchableField.PARENTS, folderMetadata.getDriveId());
            Filter mimeTypeFilter = Filters.eq(SearchableField.MIME_TYPE, "text/plain");
            Query query = new Query.Builder().addFilter(Filters.and(parentFilter,mimeTypeFilter)).build();

            CloudUtility.DriveResourceClient.query(query).addOnSuccessListener(activity, new OnSuccessListener<MetadataBuffer>() {
                @Override
                public void onSuccess(MetadataBuffer metadataBuffer) {
                    for(final Metadata metadata: metadataBuffer){
                        CloudUtility.DriveResourceClient.openFile(metadata.getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY).addOnSuccessListener(activity, new OnSuccessListener<DriveContents>() {
                            @Override
                            public void onSuccess(DriveContents driveContents) {
                                try (BufferedReader reader = new BufferedReader(new InputStreamReader(driveContents.getInputStream()))) {
                                    StringBuilder fileContent = new StringBuilder();

                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        fileContent.append(line).append("\n");
                                    }

                                    FileUtility.SaveFile(metadata.getTitle().toString(), fileContent.toString());
                                } catch (Exception exception) {
                                    Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}