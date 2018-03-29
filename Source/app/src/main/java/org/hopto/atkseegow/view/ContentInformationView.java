package org.hopto.atkseegow.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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

import com.google.gson.Gson;

import org.hopto.atkseegow.domain.ContentInformation;
import org.hopto.atkseegow.domain.ContentInformationItem;
import org.hopto.atkseegow.passwordmanager.FileListActivity;
import org.hopto.atkseegow.passwordmanager.R;
import org.hopto.atkseegow.utility.AlgorithmAESUtility;
import org.hopto.atkseegow.utility.ConstraintLayoutUtility;
import org.hopto.atkseegow.utility.FileUtility;
import org.hopto.atkseegow.utility.NfcUtility;

public class ContentInformationView {
    public static View ContentInformationNFCMain;

    public static void AddItem(ConstraintLayout largeConstraintLayout, String name, String value){
        ConstraintLayout smallConstraintLayout = new ConstraintLayout(largeConstraintLayout.getContext());
        smallConstraintLayout.setId(View.generateViewId());
        smallConstraintLayout.setBackground(ContextCompat.getDrawable(largeConstraintLayout.getContext(), R.drawable.shape_rectangle));
        largeConstraintLayout.addView(smallConstraintLayout);

        TextView nameTextView = new TextView(largeConstraintLayout.getContext());
        nameTextView.setText("Name");
        nameTextView.setId(View.generateViewId());
        nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        nameTextView.setTypeface(ResourcesCompat.getFont(largeConstraintLayout.getContext(), R.font.gen_jyuu_gothic_bold));
        smallConstraintLayout.addView(nameTextView);

        EditText nameEditText = new EditText(largeConstraintLayout.getContext());
        nameEditText.setId(View.generateViewId());
        nameEditText.setTypeface(ResourcesCompat.getFont(largeConstraintLayout.getContext(), R.font.gen_jyuu_gothic_bold));
        if(name != null && !name.trim().isEmpty())
            nameEditText.setText(name);
        smallConstraintLayout.addView(nameEditText);

        TextView valueTextView = new TextView(largeConstraintLayout.getContext());
        valueTextView.setText("Value");
        valueTextView.setId(View.generateViewId());
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        valueTextView.setTypeface(ResourcesCompat.getFont(largeConstraintLayout.getContext(), R.font.gen_jyuu_gothic_bold));
        smallConstraintLayout.addView(valueTextView);

        EditText valueEditText = new EditText(largeConstraintLayout.getContext());
        valueEditText.setId(View.generateViewId());
        valueEditText.setTypeface(ResourcesCompat.getFont(largeConstraintLayout.getContext(), R.font.gen_jyuu_gothic_bold));
        if(value != null && !value.trim().isEmpty())
            valueEditText.setText(value);
        smallConstraintLayout.addView(valueEditText);

        FloatingActionButton deleteFloatingActionButton = new FloatingActionButton(largeConstraintLayout.getContext());
        deleteFloatingActionButton.setId(View.generateViewId());
        deleteFloatingActionButton.setImageResource(R.drawable.ic_delete_white);
        deleteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInformationView.RemoveItem(view);
            }
        });
        smallConstraintLayout.addView(deleteFloatingActionButton);

        FloatingActionButton encryptionFloatingActionButton = new FloatingActionButton(largeConstraintLayout.getContext());
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

        smallConstraintSet.connect(nameTextView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
        smallConstraintSet.constrainHeight(nameTextView.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(nameTextView.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
        smallConstraintSet.connect(deleteFloatingActionButton.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));

        smallConstraintSet.connect(encryptionFloatingActionButton.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
        smallConstraintSet.connect(encryptionFloatingActionButton.getId(), ConstraintSet.RIGHT, deleteFloatingActionButton.getId(), ConstraintSet.LEFT, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));

        smallConstraintSet.connect(nameEditText.getId(), ConstraintSet.TOP, nameTextView.getId(), ConstraintSet.BOTTOM, ConstraintLayoutUtility.DpToPx(0,  largeConstraintLayout.getContext()));
        smallConstraintSet.constrainHeight(nameEditText.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(nameEditText.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(valueTextView.getId(), ConstraintSet.TOP, nameEditText.getId(), ConstraintSet.BOTTOM, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
        smallConstraintSet.constrainHeight(valueTextView.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(valueTextView.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.connect(valueEditText.getId(), ConstraintSet.TOP, valueTextView.getId(), ConstraintSet.BOTTOM, ConstraintLayoutUtility.DpToPx(0, largeConstraintLayout.getContext()));
        smallConstraintSet.constrainHeight(valueEditText.getId(), ConstraintSet.WRAP_CONTENT);
        smallConstraintSet.constrainWidth(valueEditText.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);

        smallConstraintSet.applyTo(smallConstraintLayout);

        ConstraintSet largeConstraintSet = new ConstraintSet();
        largeConstraintSet.clone(largeConstraintLayout);

        View lastConstraintLayout = ConstraintLayoutUtility.GetLastView(largeConstraintLayout);

        largeConstraintSet.connect(smallConstraintLayout.getId(), ConstraintSet.TOP, lastConstraintLayout.getId(), ConstraintSet.BOTTOM, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));
        largeConstraintSet.constrainHeight(smallConstraintLayout.getId(), ConstraintSet.WRAP_CONTENT);
        largeConstraintSet.constrainWidth(smallConstraintLayout.getId(), ConstraintLayout.LayoutParams.MATCH_PARENT);
        largeConstraintSet.setMargin(smallConstraintLayout.getId(), ConstraintSet.START, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));
        largeConstraintSet.setMargin(smallConstraintLayout.getId(), ConstraintSet.END, ConstraintLayoutUtility.DpToPx(8, largeConstraintLayout.getContext()));

        largeConstraintSet.applyTo(largeConstraintLayout);
    }

    public static void RemoveItem(View view){
        ConstraintLayout smallConstraintLayout =  (ConstraintLayout)((ViewGroup)view.getParent());
        ConstraintLayout largeConstraintLayout = (ConstraintLayout)((ViewGroup)smallConstraintLayout.getParent());

        View targetTopToBottomView = ConstraintLayoutUtility.GetTargetTopToBottomView(smallConstraintLayout.getId(), largeConstraintLayout);
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
                EditText lastEditText = (EditText)ConstraintLayoutUtility.GetLastView(smallConstraintLayout);

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
                EditText lastEditText = (EditText)ConstraintLayoutUtility.GetLastView(smallConstraintLayout);

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

    public static void SaveToLocal(EditText titleEditText, ConstraintLayout largeConstraintLayout){
        ContentInformation target = new ContentInformation();

        target.Title = titleEditText.getText().toString();

        ConstraintLayout smallConstraintLayout = (ConstraintLayout) ConstraintLayoutUtility.GetTargetTopToBottomView(R.id.titleEditText,largeConstraintLayout);
        while(smallConstraintLayout != null){
            EditText nameEditText = (EditText)smallConstraintLayout.getChildAt(1);
            EditText valueEditText = (EditText)smallConstraintLayout.getChildAt(3);

            ContentInformationItem contentInformation = new ContentInformationItem();
            contentInformation.Name = nameEditText.getText().toString();
            contentInformation.Value = valueEditText.getText().toString();

            target.ContentInformationItems.add(contentInformation);

            smallConstraintLayout = (ConstraintLayout) ConstraintLayoutUtility.GetTargetTopToBottomView(smallConstraintLayout.getId(), largeConstraintLayout);
        }

        FileUtility.SaveFile(titleEditText, new Gson().toJson(target));
    }

    public static void RedirectTo(Activity activity, boolean isFinish){
        Intent intent = new Intent();
        intent.setClass(activity, FileListActivity.class);
        activity.startActivity(intent);
        if(isFinish)
            activity.finish();
    }
}
