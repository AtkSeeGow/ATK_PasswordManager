package org.hopto.atkseegow.passwordmanager;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.hopto.atkseegow.domain.ContentInformation;
import org.hopto.atkseegow.domain.ContentInformationItem;
import org.hopto.atkseegow.utility.FileUtility;
import org.hopto.atkseegow.utility.IntentUtility;
import org.hopto.atkseegow.utility.NavigationItemUtility;
import org.hopto.atkseegow.utility.NfcUtility;
import org.hopto.atkseegow.view.ContentInformationView;
import org.hopto.atkseegow.view.FileListView;

public class ContentInformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;
    String[][] techList;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(R.string.title_activity_content_information);

        setContentView(R.layout.activity_content_information);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInformationView.AddItem((ConstraintLayout)findViewById(R.id.largeConstraintLayout), null, null);
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent();
        String buttonText = intent.getStringExtra(IntentUtility.FILE_PATH);
        if(buttonText != null && !buttonText.trim().isEmpty()){
            String textContent = FileUtility.ReadFile(buttonText,this);
            ContentInformation contentInformation = new Gson().fromJson(textContent, ContentInformation.class);
            ((EditText)findViewById(R.id.titleEditText)).setText(contentInformation.Title);
            for (ContentInformationItem contentInformationItem : contentInformation.ContentInformationItems)
                ContentInformationView.AddItem((ConstraintLayout)findViewById(R.id.largeConstraintLayout), contentInformationItem.Name, contentInformationItem.Value);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(nfcAdapter != null) {
            if (nfcAdapter.isEnabled()) {
                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                        getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
                intentFiltersArray = new IntentFilter[] { intentFilter };
                techList = new String[][] { new String[] { NfcA.class.getName(), MifareUltralight.class.getName()} };
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
            } else {
                Toast.makeText(this,"NFC Disabled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        NfcUtility.Reader(intent, (EditText)ContentInformationView.ContentInformationNFCMain.findViewById(R.id.tagIdEditText));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_to_local) {
            EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
            if(titleEditText.getText().toString().isEmpty()){
                Toast.makeText(titleEditText.getContext(), "Please input title", Toast.LENGTH_LONG).show();
            }
            else{
                ConstraintLayout largeConstraintLayout = (ConstraintLayout)findViewById(R.id.largeConstraintLayout);
                ContentInformationView.SaveToLocal(titleEditText, largeConstraintLayout);
                FileListView.RedirectTo(this,false);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        NavigationItemUtility.Action( item.getItemId(), this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
