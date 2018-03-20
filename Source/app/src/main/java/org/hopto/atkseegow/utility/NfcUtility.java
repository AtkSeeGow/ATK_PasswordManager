package org.hopto.atkseegow.utility;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.widget.EditText;

public class NfcUtility {
    public static boolean IsToScan = false;

    public static void Reader(Intent intent, EditText editText)
    {
        if (NfcUtility.IsToScan && intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            editText.setText(NfcUtility.DumpTagData(tag).toString());
        }
    }

    public static String DumpTagData(Parcelable parcelable) {
        StringBuilder stringBuilder = new StringBuilder();
        Tag tag = (Tag) parcelable;
        byte[] id = tag.getId();
        stringBuilder.append(EncodeUtility.GetHexString(id));
        return stringBuilder.toString();
    }
}
