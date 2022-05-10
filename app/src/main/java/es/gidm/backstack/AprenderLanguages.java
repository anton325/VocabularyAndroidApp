package es.gidm.backstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;


public class AprenderLanguages extends AppCompatActivity {


    RadioGroup radioGroup1;
    private ArrayList<String> languages;
    private Gson gson;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_palabras);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Mis Lenguas");
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        TextView textTop = (TextView) findViewById(R.id.languageText);
        textTop.setText("Elije la lengua que quieres aprender");

        // get sharedpreferences
        sharedpreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        // read out which languages we already have
        gson = new Gson();
        String json = sharedpreferences.getString("languages","");
        languages = new ArrayList<String>();
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            languages = gson.fromJson(json,type);
        }

        // dynamically add a button for each language
        // the onClickListener
        View.OnClickListener btnclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("StartIntent","test");
                Log.i("StartIntent",languages.get(view.getId()));
                Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
                i.putExtra("language", languages.get(view.getId()));
                startActivity(i);
            }
        };
        // and save them in array
        ArrayList<Button> buttons = new ArrayList<Button>();
        int buttonID = 0;
        for (final String list : languages) {
            Button myButton = new Button(this);
            myButton.setText(list);
            myButton.setOnClickListener(btnclick);
            Log.i("list button: ",list);
            LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myButton, lp);
            myButton.setId(buttonID);
            buttonID++;
            buttons.add(myButton);
        }

        // make add language button invisitble
        Button addLanguage = (Button) findViewById(R.id.anadirlengua);
        addLanguage.setVisibility(View.INVISIBLE);



        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.home:
                        Log.i("matching", "matching inside1 matching" +  checkedId);
                        in=new Intent(getBaseContext(),HomeActivity.class);
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
                        in = new Intent(getBaseContext(), Palabras.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.ajustes:

                        Log.i("matching", "matching inside1 rate" + checkedId);
                        in = new Intent(getBaseContext(), Palabras.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}