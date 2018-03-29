package org.hopto.atkseegow.view;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.hopto.atkseegow.passwordmanager.ContentInformationActivity;
import org.hopto.atkseegow.passwordmanager.FileListActivity;
import org.hopto.atkseegow.passwordmanager.R;
import org.hopto.atkseegow.utility.ConstraintLayoutUtility;
import org.hopto.atkseegow.utility.FileUtility;
import org.hopto.atkseegow.utility.IntentUtility;

import java.io.File;

public class FileListView {
    public static void ListItem(ConstraintLayout largeConstraintLayout, String queryValue){
        File[] filePaths = FileUtility.ListFiles(queryValue);

        ConstraintSet largeConstraintSet = new ConstraintSet();
        largeConstraintSet.clone(largeConstraintLayout);

        int previousId = 0;
        for (final File filePath: filePaths) {
            ConstraintLayout smallConstraintLayout = new ConstraintLayout(largeConstraintLayout.getContext());
            smallConstraintLayout.setId(View.generateViewId());
            smallConstraintLayout.setBackground(ContextCompat.getDrawable(largeConstraintLayout.getContext(), R.drawable.shape_rectangle));
            largeConstraintLayout.addView(smallConstraintLayout);

            TextView titleTextView = new TextView(largeConstraintLayout.getContext());
            titleTextView.setText(filePath.toString().replace(FileUtility.GetRootPath().toString() + "/", "").replace(".txt",""));
            titleTextView.setId(View.generateViewId());
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            titleTextView.setTypeface(ResourcesCompat.getFont(largeConstraintLayout.getContext(), R.font.gen_jyuu_gothic_bold));
            smallConstraintLayout.addView(titleTextView);

            FloatingActionButton viewFloatingActionButton = new FloatingActionButton(largeConstraintLayout.getContext());
            viewFloatingActionButton.setId(View.generateViewId());
            viewFloatingActionButton.setImageResource(R.drawable.ic_assignment_white);
            viewFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(IntentUtility.FILE_PATH, filePath.toString());
                    intent.setClass(view.getContext(), ContentInformationActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
            smallConstraintLayout.addView(viewFloatingActionButton);

            FloatingActionButton deleteFloatingActionButton = new FloatingActionButton(largeConstraintLayout.getContext());
            deleteFloatingActionButton.setId(View.generateViewId());
            deleteFloatingActionButton.setImageResource(R.drawable.ic_delete_white);
            deleteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileUtility.DeleteFile(filePath.toString());

                    Activity activity = (Activity)view.getContext();

                    Intent intent = new Intent();
                    intent.setClass(activity, FileListActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
            smallConstraintLayout.addView(deleteFloatingActionButton);

            ConstraintSet smallConstraintSet = new ConstraintSet();
            smallConstraintSet.clone(smallConstraintLayout);

            smallConstraintSet.connect(titleTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
            smallConstraintSet.constrainHeight(titleTextView.getId(), ConstraintSet.WRAP_CONTENT);
            smallConstraintSet.constrainWidth(titleTextView.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

            smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
            smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));

            smallConstraintSet.connect(viewFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
            smallConstraintSet.connect(viewFloatingActionButton.getId(), ConstraintSet.RIGHT, deleteFloatingActionButton.getId(), ConstraintSet.LEFT, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));

            smallConstraintSet.applyTo(smallConstraintLayout);

            largeConstraintSet.connect(smallConstraintLayout.getId(), ConstraintSet.TOP, previousId, ConstraintSet.BOTTOM, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));
            largeConstraintSet.constrainWidth(smallConstraintLayout.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);
            largeConstraintSet.constrainHeight(smallConstraintLayout.getId(), ConstraintSet.WRAP_CONTENT);

            previousId = smallConstraintLayout.getId();
        }

        largeConstraintSet.applyTo(largeConstraintLayout);
    }

    public static void RedirectTo(Activity activity, boolean isFinish){
        Intent intent = new Intent();
        intent.setClass(activity, FileListActivity.class);
        activity.startActivity(intent);
        if(isFinish)
            activity.finish();
    }
}
