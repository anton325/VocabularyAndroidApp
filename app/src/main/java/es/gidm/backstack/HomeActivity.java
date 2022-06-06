package es.gidm.backstack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {


    RadioGroup radioGroup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        Log.i("home",Integer.toString(android.os.Build.VERSION.SDK_INT));
        // check if we have the permission to record audio
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i("home", "request");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        10);
            }
        }
            // set up buttons in menu
            Button toMisPalabras = (Button) findViewById(R.id.homeToMisPalabras);
            toMisPalabras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getBaseContext(), MisPalabras.class);
                    startActivity(i);
                }
            });

            Button toAprender = (Button) findViewById(R.id.home_aprender);
            toAprender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "to to aprender", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), AprenderLanguages.class);
                    startActivity(i);
                }
            });

            Button toBuscar = (Button) findViewById(R.id.home_buscar);
            toBuscar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "to buscar", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getBaseContext(), Buscar.class);
                    startActivity(i);
                }
            });


            // color where we are
            RadioButton currentScreenButton = (RadioButton) findViewById(R.id.home);
            currentScreenButton.setTextColor(Color.RED);

            // setup button nav bar -
            radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Intent in;
                    Log.i("matching", "matching inside1 bro" + checkedId);
                    switch (checkedId) {
                        case R.id.home:
                            Log.i("matching", "matching inside1 matching" + checkedId);
                            in = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                            break;
                        case R.id.palabras:
                            Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                            in = new Intent(getBaseContext(), MisPalabras.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                            break;
                        case R.id.aprender:
                            Log.i("matching", "matching inside1 rate" + checkedId);
                            in = new Intent(getBaseContext(), AprenderLanguages.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                            break;
                        case R.id.buscar:
                            Log.i("matching", "matching inside1 rate" + checkedId);
                            in = new Intent(getBaseContext(), Buscar.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                            break;
                        default:
                            break;
                    }
                }
            });
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        10);
            }
        }
    }
}