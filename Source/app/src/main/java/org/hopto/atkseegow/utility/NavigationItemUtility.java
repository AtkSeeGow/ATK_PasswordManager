package org.hopto.atkseegow.utility;

import android.app.Activity;
import android.widget.Toast;

import org.hopto.atkseegow.passwordmanager.R;
import org.hopto.atkseegow.view.*;

public class NavigationItemUtility {
    public static void Action(int id, Activity activity){
        if (id == R.id.nav_file_list)
            FileListView.RedirectTo(activity, false);
        else if (id == R.id.nav_cloud_upload) {
            CloudUploadView.RedirectTo(activity, false);
        }
        else if (id == R.id.nav_cloud_download) {
            CloudDownloadView.RedirectTo(activity, false);
        }
    }
}
