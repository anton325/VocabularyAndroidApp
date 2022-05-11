package es.gidm.backstack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class AprenderSelectLanguages extends AppCompatActivity implements MyRecyclerViewAdapter_languages.ItemClickListener {
    MyRecyclerViewAdapter_languages adapter;
    RadioGroup radioGroup1;
    private SharedPreferences sharedpreferences;
    private Gson gson;
    private ArrayList<String> selectedLists;
    private HashMap<String, Integer> colorsOfLists;
    private ArrayList<String> keys;
    private String nameTranslationList;
    private String nameSelectedList;

    private int scrollToPosition;
    private LinearLayoutManager myllm;

    private String selectedLanguage;
    private ArrayList<String> listsOfLanguage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("aprenderSelectLan","started aprender Lang selection");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palabras_style);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Las listas");
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        // set string of top text
        TextView topText = (TextView) findViewById(R.id.languageText);
        topText.setText("Elige todas las listas que quieres aprender");

        // get intent
        Intent i = getIntent();
        selectedLanguage = i.getStringExtra("language");
        Log.i("SelectedLang: ",selectedLanguage);

        // get sharedpreferences
        sharedpreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

        // read out which lists we already have for this language
        gson = new Gson();
        String json = sharedpreferences.getString(selectedLanguage,"");
        listsOfLanguage = new ArrayList<String>();
        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            listsOfLanguage = gson.fromJson(json,type);
        }

        // at the beginning no list is marked
        selectedLists = new ArrayList<String>();
        colorsOfLists = new HashMap<String, Integer>();
        for(String listName : listsOfLanguage) {
            colorsOfLists.put(listName,0);
        }



        Log.i("aprenderSelectLang","reached recycler");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerWords);
        myllm = new LinearLayoutManager(this);
        myllm.scrollToPositionWithOffset(scrollToPosition, 0);
        recyclerView.setLayoutManager(myllm);
        adapter = new MyRecyclerViewAdapter_languages(this, listsOfLanguage,colorsOfLists);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);



        // setup add word button
        Button addList = (Button) findViewById(R.id.anadirPalabra);
        addList.setText("Empezar aprender");
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), Aprender.class);
                Bundle args = new Bundle();
                // tell the next class which lists have been selected
                args.putSerializable("ARRAYLIST",(Serializable) selectedLists);
                i.putExtra("BUNDLE",args);
//                i.putExtra("selectedLang",selectedLanguage);
                startActivity(i);
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
    @Override
    public void onItemClick(View view, int position) {
    Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    String selectedList = listsOfLanguage.get(position);
    // save in selectedLists all the lists that have been selected
    if(colorsOfLists.get(selectedList) == 1) {
        // deselect this list
        selectedLists.remove(selectedList);
        colorsOfLists.put(selectedList,0);
    }
    else {
        // select this list
        selectedLists.add(selectedList);
        colorsOfLists.put(selectedList,1);
    }
    adapter.notifyDataSetChanged();
    }
}
