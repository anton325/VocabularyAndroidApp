package es.gidm.backstack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Buscar extends AppCompatActivity {


    RadioGroup radioGroup1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_style);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Buscar");
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);


        // load all the words there are (both directions!)
        // get sharedpreferences
        SharedPreferences sharedpreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        // read out which languages we already have
        Gson gson = new Gson();
        String json = sharedpreferences.getString("languages","");
        ArrayList<String> languages = new ArrayList<String>();
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            languages = gson.fromJson(json,type);
        }

        // for all languages get all the lists
        ArrayList<String> listsOfLanguages = new ArrayList<String>();
        for(String l : languages) {
            json = sharedpreferences.getString(l,"");
            if (!json.isEmpty()) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                listsOfLanguages.addAll((Collection<? extends String>) gson.fromJson(json,type));
            }
        }

        ArrayList<String> allWords = new ArrayList<String>();
        HashMap<String,String> allTranslations = new HashMap<String,String>();
        // each word in each list needs to be added to our accumluated list
        // and each translation as well
        for(String list : listsOfLanguages) {
            json = sharedpreferences.getString(list,"");
            if (!json.isEmpty()) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                allWords.addAll(gson.fromJson(json,type));
        }



        final String[] PROVINCIAS = new String[] {
                "Hallo",
                "hi",
                "na"
        };

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.buscarTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, PROVINCIAS);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "La provincia elegida es " +
                        adapterView.getAdapter().getItem(i), Toast.LENGTH_SHORT).show();
            }
        });


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
                        in = new Intent(getBaseContext(), AprenderLanguages.class);
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