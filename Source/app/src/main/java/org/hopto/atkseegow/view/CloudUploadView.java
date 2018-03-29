package org.hopto.atkseegow.view;

import android.app.Activity;
import android.content.Intent;

import org.hopto.atkseegow.passwordmanager.CloudUploadActivity;

public class CloudUploadView {
    public static void RedirectTo(Activity activity, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(activity, CloudUploadActivity.class);
        activity.startActivity(intent);
        if (isFinish)
            activity.finish();
    }
}