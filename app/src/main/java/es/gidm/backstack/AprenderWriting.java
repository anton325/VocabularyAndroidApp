package es.gidm.backstack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
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


public class AprenderWriting extends AppCompatActivity {

    PlayButton myPlayButton;

    RadioGroup radioGroup1;
    private Gson gson;
    private SharedPreferences sharedpreferences;
    private ArrayList<String> accumulatedWords;
    private HashMap<String,String> translations;
    private TextView wordTV;
    private EditText entryUserET;
    private TextView solutionTV;
    private Button solutionButton;
    private Button changeDirection;
    private Random randomInstance;
    private ArrayList<Integer> learnt;
    private int learnOrSolve;
    private int direction;
    private int wordsLearnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aprender_writing_screen);
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
        entryUserET = (EditText) findViewById(R.id.entryOfUser);

        entryUserET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                Log.i("aprender writing","clicked");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("aprender writing","if");
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //Hide:
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    return true;
                }
                return false;
            }
        });

        int start = nextWordIndex();

        wordTV.setText(accumulatedWords.get(start));
        solutionTV.setText(translations.get(accumulatedWords.get(start)));
        solutionTV.setVisibility(View.INVISIBLE);

        changeDirection = (Button) findViewById(R.id.cambiaDirrecion);
        changeDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (direction == 1) {
                    direction = 0;
                }
                else {
                    direction = 1;
                }
                String word = wordTV.getText().toString();
                wordTV.setText(solutionTV.getText().toString());
                solutionTV.setText(word);
            }
        });

        learnOrSolve = 0;
        solutionButton = (Button) findViewById(R.id.buttonSolution);
        solutionButton.setText("Muestra soluccion");
        solutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (learnOrSolve == 0){
                    solutionButton.setText("Contiuna");
                    learnOrSolve = 1;
                    wordsLearnt++;
                    // we've just entered a word, so we need to check if the entry was correct or not
                    String entry = entryUserET.getText().toString().toLowerCase();
                    if (entry.equals(solutionTV.getText().toString().toLowerCase())) {
                        // correct
                        solutionTV.setText("Correcto!");
                        solutionTV.setVisibility(View.VISIBLE);

                    }
                    else {
                        String newString = solutionTV.getText().toString();
                        newString = "Incorrecto: "+newString;
                        solutionTV.setText(newString);
                        solutionTV.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    learnOrSolve = 0;
                    entryUserET.setText("");
                    solutionButton.setText("Comprobar soluccion");
                    solutionTV.setVisibility(View.INVISIBLE);
                    loadNextWordsOnScreen(direction);
                }

            }
        });

        String leftPartPath = accumulatedWords.get(start);
        String rightPartPath = translations.get(accumulatedWords.get(start));
        String path = getExternalCacheDir().getAbsolutePath() + "/" +leftPartPath + rightPartPath+".3gp";
        File mediaFile = new File(path);
        if(mediaFile.exists()){
            myPlayButton = new PlayButton(this,path);
            myPlayButton.setBackground(getDrawable(R.drawable.custom_rectangle_buttons));
            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myPlayButton, lp);
        }







        // color where we are
        RadioButton currentScreenButton =(RadioButton) findViewById(R.id.aprender);
        currentScreenButton.setTextColor(Color.RED);

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

    private int nextWordIndex() {
        if(wordsLearnt > 0.8 * learnt.size()) {
            learnt.clear();
            wordsLearnt = 0;
        }

        int n = randomInstance.nextInt(accumulatedWords.size());
        for(int i = 0; i < 100; i++) {
            if(!learnt.contains(n)) {
                learnt.add(n);
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
        else {
            //now do it the other way around
            wordTV.setText(translations.get(accumulatedWords.get(index)));
            solutionTV.setText(accumulatedWords.get(index));
        }
        String leftPartPath = accumulatedWords.get(index);
        String rightPartPath = translations.get(accumulatedWords.get(index));
        String path = getExternalCacheDir().getAbsolutePath() + "/" +leftPartPath + rightPartPath+".3gp";
        File mediaFile = new File(path);
        if (myPlayButton != null) {
            myPlayButton.setVisibility(View.GONE);
        }
        if(mediaFile.exists()){
            myPlayButton = new PlayButton(this,path);
            myPlayButton.setBackground(getDrawable(R.drawable.custom_rectangle_buttons));
            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonLayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.addView(myPlayButton, lp);
        }
    }
}