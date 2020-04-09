package com.easyapps.coronatracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.easyapps.coronatracker.fragments.GlobalFragment;
import com.easyapps.coronatracker.fragments.IndiaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bot_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bot_nav = findViewById(R.id.bot_nav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GlobalFragment()).commit();
        }

        open_fragments_bottom_nav();
    }

    private void open_fragments_bottom_nav() {
        bot_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_global:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new GlobalFragment()).commit();
                        break;
                    case R.id.bottom_india:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new IndiaFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.main_help) {
            //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("COVID-19 Database")
                    .setMessage("Corona Virus status all over world.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
