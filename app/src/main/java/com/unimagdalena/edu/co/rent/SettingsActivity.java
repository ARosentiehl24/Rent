package com.unimagdalena.edu.co.rent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.fingerlinks.mobile.android.navigator.Navigator;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Navigator.with(this).utils().finishWithAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
