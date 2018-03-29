package org.hopto.atkseegow.view;

import android.app.Activity;
import android.content.Intent;

import org.hopto.atkseegow.passwordmanager.CloudDownloadActivity;

public class CloudDownloadView {
    public static void RedirectTo(Activity activity, boolean isFinish){
        Intent intent = new Intent();
        intent.setClass(activity, CloudDownloadActivity.class);
        activity.startActivity(intent);
        if(isFinish)
            activity.finish();
    }
}
