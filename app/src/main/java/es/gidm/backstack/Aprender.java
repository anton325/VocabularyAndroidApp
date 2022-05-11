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
import java.util.Random;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class Aprender extends AppCompatActivity {


    RadioGroup radioGroup1;
    private Gson gson;
    private SharedPreferences sharedpreferences;
    private ArrayList<String> accumulatedWords;
    private HashMap<String,String> translations;
    private TextView wordTV;
    private TextView solutionTV;
    private Button solutionButton;
    private Random randomInstance;
    private ArrayList<Integer> learnt;
    private int learnOrSolve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aprender_screen);
        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Aprendiendo");
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        randomInstance = new Random();
        learnt = new ArrayList<Integer>();


        // get intent
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<String> selectedLists = (ArrayList<String>) args.getSerializable("ARRAYLIST");

        // get sharedpreferences
        sharedpreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        gson = new Gson();
        // for each list, load all the words and all the translations
        accumulatedWords = new ArrayList<String>();
        translations = new HashMap<String,String>();
        String nameTranslationList;
        String json;
        for(int i = 0; i < selectedLists.size(); i++){
            json = sharedpreferences.getString(selectedLists.get(i),"");
            if (!json.isEmpty()) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                accumulatedWords.addAll((Collection<? extends String>) gson.fromJson(json,type));
            }

            nameTranslationList = selectedLists.get(i)+"translation";
            json = sharedpreferences.getString(nameTranslationList,"");
            if (!json.isEmpty()) {
                Type type = new TypeToken<HashMap<String,String>>(){}.getType();
                translations.putAll((Map<? extends String, ? extends String>) gson.fromJson(json,type));
            }
        }

        wordTV = (TextView) findViewById(R.id.palabraAprender);
        solutionTV = (TextView) findViewById(R.id.palabraSoluccion);

        int start = nextWordIndex();

        wordTV.setText(accumulatedWords.get(start));
        solutionTV.setText(translations.get(accumulatedWords.get(start)));
        solutionTV.setVisibility(View.INVISIBLE);

        learnOrSolve = 0;
        solutionButton = (Button) findViewById(R.id.buttonSolution);
        solutionButton.setText("Muestra soluccion");
        solutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (learnOrSolve == 0){
                    solutionTV.setVisibility(View.VISIBLE);
                    solutionButton.setText("Contiuna");
                    learnOrSolve = 1;
                }
                else {
                    learnOrSolve = 0;
                    solutionButton.setText("Muestra soluccion");
                    solutionTV.setVisibility(View.INVISIBLE);
                    loadNextWordsOnScreen(0);
                }

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

    private int nextWordIndex() {
        int n = randomInstance.nextInt(accumulatedWords.size());;
        for(int i = 0; i < 100; i++) {
            if(!learnt.contains(n)) {
                return n;
            }
            n = randomInstance.nextInt(accumulatedWords.size());
        }
        return n;
    }

    private void loadNextWordsOnScreen(int direction) {
        int index = nextWordIndex();
        if (direction == 0) {
            wordTV.setText(accumulatedWords.get(index));
            solutionTV.setText(translations.get(accumulatedWords.get(index)));
        }
    }
}