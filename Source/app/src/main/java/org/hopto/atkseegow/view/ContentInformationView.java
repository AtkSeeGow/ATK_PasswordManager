package org.hopto.atkseegow.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.hopto.atkseegow.passwordmanager.R;
import org.hopto.atkseegow.utility.AlgorithmAESUtility;
import org.hopto.atkseegow.utility.NfcUtility;

public class ContentInformationView {
    public static View ContentInformationNFCMain;

    public static void AddItem(Context context, ConstraintLayout largeConstraintLayout){
        ConstraintLayout smallConstraintLayout = new ConstraintLayout(context);
        smallConstraintLayout.setId(View.generateViewId());
        smallConstraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rectangle));
        largeConstraintLayout.addView(smallConstraintLayout);

        TextView nameTextView = new TextView(context);
        nameTextView.setText("Name");
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        nameTextView.setId(View.generateViewId());
        smallConstraintLayout.addView(nameTextView);

        EditText nameEditText = new EditText(context);
        nameEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        nameEditText.setId(View.generateViewId());
        smallConstraintLayout.addView(nameEditText);

        TextView valueTextView = new TextView(context);
        valueTextView.setText("Value");
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        valueTextView.setId(View.generateViewId());
        smallConstraintLayout.addView(valueTextView);

        EditText valueEditText = new EditText(context);
        valueEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        valueEditText.setId(View.generateViewId());
        smallConstraintLayout.addView(valueEditText);

        FloatingActionButton deleteFloatingActionButton = new FloatingActionButton(context);
        deleteFloatingActionButton.setId(View.generateViewId());
        deleteFloatingActionButton.setImageResource(R.drawable.ic_delete_white);
        deleteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInformationView.RemoveItem(view);
            }
        });
        smallConstraintLayout.addView(deleteFloatingActionButton);

        FloatingActionButton encryptionFloatingActionButton = new FloatingActionButton(context);
        encryptionFloatingActionButton.setId(View.generateViewId());
        encryptionFloatingActionButton.setImageResource(R.drawable.ic_nfc_white);
        encryptionFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInformationView.PopupScanTagWindow(view);
            }
        });
        smallConstraintLayout.addView(encryptionFloatingActionButton);

        ConstraintSet smallConstraintSet = new ConstraintSet();
        smallConstraintSet.clone(smallConstraintLayout);

        smallConstraintSet.connect(nameTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.constrainHeight(nameTextView.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(nameTextView.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ContentInformationView.DpToPx(8, context));

        smallConstraintSet.connect(encryptionFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.connect(encryptionFloatingActionButton.getId(), ConstraintSet.RIGHT, deleteFloatingActionButton.getId(), ConstraintSet.LEFT, ContentInformationView.DpToPx(8, context));

        smallConstraintSet.connect(nameEditText.getId(), ConstraintSet.TOP, nameTextView.getId(), ConstraintSet.BOTTOM, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.constrainHeight(nameEditText.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(nameEditText.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(valueTextView.getId(), ConstraintSet.TOP, nameEditText.getId(), ConstraintSet.BOTTOM, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.constrainHeight(valueTextView.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(valueTextView.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(valueEditText.getId(), ConstraintSet.TOP, valueTextView.getId(), ConstraintSet.BOTTOM, ContentInformationView.DpToPx(8, context));
        smallConstraintSet.constrainHeight(valueEditText.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(valueEditText.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.applyTo(smallConstraintLayout);

        ConstraintSet largeConstraintSet = new ConstraintSet();
        largeConstraintSet.clone(largeConstraintLayout);

        View lastConstraintLayout = ContentInformationView.GetLastView(largeConstraintLayout);

        largeConstraintSet.connect(smallConstraintLayout.getId(), ConstraintSet.TOP, lastConstraintLayout.getId(), ConstraintSet.BOTTOM, ContentInformationView.DpToPx(8, context));
        largeConstraintSet.constrainHeight(smallConstraintLayout.getId(), ConstraintSet.WRAP_CONTENT);
        largeConstraintSet.constrainWidth(smallConstraintLayout.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);
        largeConstraintSet.setMargin(smallConstraintLayout.getId(), ConstraintSet.START, ContentInformationView.DpToPx(8, context));
        largeConstraintSet.setMargin(smallConstraintLayout.getId(), ConstraintSet.END, ContentInformationView.DpToPx(8, context));

        largeConstraintSet.applyTo(largeConstraintLayout);
    }

    public static void RemoveItem(View view){
        ConstraintLayout smallConstraintLayout =  (ConstraintLayout)((ViewGroup)view.getParent());
        ConstraintLayout largeConstraintLayout = (ConstraintLayout)((ViewGroup)smallConstraintLayout.getParent());

        View targetTopToBottomView = ContentInformationView.GetTargetTopToBottomView(smallConstraintLayout.getId(), largeConstraintLayout);
        if(targetTopToBottomView != null)
        {
            ConstraintSet largeConstraintSet = new ConstraintSet();
            largeConstraintSet.clone(largeConstraintLayout);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)smallConstraintLayout.getLayoutParams();
            largeConstraintSet.connect(targetTopToBottomView.getId(), ConstraintSet.TOP, layoutParams.topToBottom, ConstraintSet.BOTTOM, layoutParams.topMargin);
            largeConstraintSet.applyTo(largeConstraintLayout);
        }

        largeConstraintLayout.removeView(smallConstraintLayout);
    }

    public static void PopupScanTagWindow(View view){
        NfcUtility.IsToScan = true;

        ContentInformationView.ContentInformationNFCMain = LayoutInflater.from(view.getContext()).inflate(R.layout.content_information_nfc_main, null, false);

        final ConstraintLayout smallConstraintLayout =  (ConstraintLayout)((ViewGroup)view.getParent());
        final PopupWindow popWindow = new PopupWindow(ContentInformationNFCMain, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                NfcUtility.IsToScan = false;
                return false;
            }
        });

        Button encryptionButton = (Button) ContentInformationNFCMain.findViewById(R.id.encryptionButton);
        encryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NfcUtility.IsToScan = false;
                EditText tagIdEditText = ContentInformationView.ContentInformationNFCMain.findViewById(R.id.tagIdEditText);
                EditText lastEditText = (EditText)ContentInformationView.GetLastView(smallConstraintLayout);

                try {
                    lastEditText.setText(AlgorithmAESUtility.Encryption(tagIdEditText.getText().toString(), lastEditText.getText().toString()));
                }
                catch (Exception exception)
                {
                    Toast.makeText(view.getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                popWindow.dismiss();
            }
        });

        Button decryptionButton = (Button) ContentInformationNFCMain.findViewById(R.id.decryptionButton);
        decryptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NfcUtility.IsToScan = false;
                EditText tagIdEditText = ContentInformationView.ContentInformationNFCMain.findViewById(R.id.tagIdEditText);
                EditText lastEditText = (EditText)ContentInformationView.GetLastView(smallConstraintLayout);

                try {
                    lastEditText.setText(AlgorithmAESUtility.Decryption(tagIdEditText.getText().toString(), lastEditText.getText().toString()));
                }
                catch (Exception exception)
                {
                    Toast.makeText(view.getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                popWindow.dismiss();
            }
        });

        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.shape_rectangle);
        popWindow.setBackgroundDrawable(drawable);

        popWindow.showAsDropDown(view, 0, 0);
    }

    public static View GetFirstView(ConstraintLayout largeConstraintLayout){
        View result = null;
        for (int i = 0; i < largeConstraintLayout.getChildCount(); i ++) {
            View view = largeConstraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            if(layoutParams.topToBottom == -1 && view.getId() != 1)
            {
                result = view;
                break;
            }
        }
        return result;
    }

    public static View GetLastView(ConstraintLayout largeConstraintLayout) {
        View previousView = ContentInformationView.GetFirstView(largeConstraintLayout);
        View lastView = null;

        do{
            View targetTopToBottomView = ContentInformationView.GetTargetTopToBottomView(previousView.getId(), largeConstraintLayout);
            if(targetTopToBottomView == null)
                lastView = previousView;
            else
                previousView = targetTopToBottomView;
        }while(lastView == null);

        return lastView;
    }
    
    public static View GetTargetTopToBottomView(int topToBottomId, ConstraintLayout largeConstraintLayout){
        View result = null;
        for (int i = 0; i < largeConstraintLayout.getChildCount(); i ++) {
            View view = largeConstraintLayout.getChildAt(i);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)view.getLayoutParams();
            if(layoutParams.topToBottom == topToBottomId)
                result = view;
        }
        return result;
    }

    public static int DpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
