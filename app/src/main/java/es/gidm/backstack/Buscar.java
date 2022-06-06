package es.gidm.backstack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
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
import java.util.Map;

public class Buscar extends AppCompatActivity {


    RadioGroup radioGroup1;
    public RecordButton myRecordButton;

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

        // for all languages get all the lists and read all the words out of the list to add
        ArrayList<String> listsOfLanguages;
        ArrayList<String> wordsInList;
        ArrayList<String> wordsShownInSearch = new ArrayList<String>(); // accumulates all words
        String nameTranslationList;
        HashMap<String,String> allTranslations;
        final HashMap<String,String> wordsTranslationSearch = new HashMap<String,String>();

        for(String l : languages) {
            json = sharedpreferences.getString(l,"");
            listsOfLanguages = new ArrayList<String>();
            if (!json.isEmpty()) {
                Type type = new TypeToken<ArrayList<String>>(){}.getType();
                listsOfLanguages.addAll((Collection<? extends String>) gson.fromJson(json,type));
            }
            for (String list : listsOfLanguages) {
                json = sharedpreferences.getString(list,"");
                wordsInList = new ArrayList<String>();
                if (!json.isEmpty()) {
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    wordsInList.addAll((Collection<? extends String>) gson.fromJson(json,type));
                    for(String w : wordsInList) {
                        Log.i("b2",w);
                    }
                }
                nameTranslationList = list+"translation";
                json = sharedpreferences.getString(nameTranslationList,"");
                allTranslations = new HashMap<String,String>();
                if (!json.isEmpty()) {
                    Type type = new TypeToken<HashMap<String,String>>(){}.getType();
                    allTranslations.putAll((Map<? extends String, ? extends String>) gson.fromJson(json,type));
                }
                for (String word : wordsInList) {
                    String newWord = word + " - " + l;
                    String itsTranslation = allTranslations.get(word);
                    Log.i("b",newWord);
                    wordsShownInSearch.add(newWord);
                    wordsTranslationSearch.put(newWord,itsTranslation);

                    // we also want to be able to search in the other direction:
                    String newTranslation = itsTranslation + " - " + l;
                    wordsShownInSearch.add(newTranslation);
                    wordsTranslationSearch.put(newTranslation,word);
                }
            }
        }


//        ArrayList<String> allWords = new ArrayList<String>();
//        // each word in each list needs to be added to our accumluated list
//        // and each translation as well
//        for(String list : listsOfLanguages) {
//            json = sharedpreferences.getString(list,"");
//            if (!json.isEmpty()) {
//                Type type = new TypeToken<ArrayList<String>>() {
//                }.getType();
//                allWords.addAll((Collection<? extends String>) gson.fromJson(json, type));
//            }
//            nameTranslationList = list+"translation";
//            json = sharedpreferences.getString(nameTranslationList,"");
//            if (!json.isEmpty()) {
//                Type type = new TypeToken<HashMap<String,String>>(){}.getType();
//                allTranslations.putAll((Map<? extends String, ? extends String>) gson.fromJson(json,type));
//            }
//        }
//
//        // now we have all the words and all the word + translation pairs
//        // i also want to be able to search through the translations
//        ArrayList<String> additionalWords = new ArrayList<String>();
//        for(String word : allWords) {
//            String translation = allTranslations.get(word);
//            allTranslations.put(translation,word);
//            additionalWords.add(translation);
//        }
//        allWords.addAll(additionalWords);


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.buscarTextView);
        // put all the words we just created in this adapter, which goes into the
        // autocompletetextview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, wordsShownInSearch);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String word = (String) adapterView.getAdapter().getItem(i);
                // edit the word a little bit, right now it's in the format: "word - language"
                String wordClean = word.split("-")[0];
                String translation = wordsTranslationSearch.get(word);

                // hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Hide:
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                // display word + translation with a popup window
                onButtonShowPopupWindowClick(view, wordClean, translation);
            }
        });

        // color where we are
        RadioButton currentScreenButton =(RadioButton) findViewById(R.id.buscar);
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

    public void onButtonShowPopupWindowClick(final View view, String word, String translation) {
        Log.i("buscar","popup");
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_palabra, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // make it close on touch outside of popup window
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // show the popup window

        // which view you pass in doesn't matter, it is only used for the window tolken
        Log.i("buscar","before");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        }, 100); //Delay one second

        Log.i("buscar","after");
        TextView popupText = (TextView) popupView.findViewById(R.id.popupText);
        popupText.setText("El resultado de la busqueda:");
        final EditText textfieldWord = (EditText) popupView.findViewById(R.id.word);
        textfieldWord.setText(word);

        textfieldWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                Log.i("mis palabras","clicked");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("mis palabras","if");
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //Hide:
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    return true;
                }
                return false;
            }
        });

        final EditText textfieldTranslation = (EditText) popupView.findViewById(R.id.translation);
        textfieldTranslation.setText(translation);
        textfieldTranslation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int keyCode, KeyEvent event) {
                Log.i("mis palabras","clicked");
                if ((event.getAction() == KeyEvent.ACTION_DOWN) || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("mis palabras","if");
                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    //Hide:
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    return true;
                }
                return false;
            }
        });

        Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);
        Button addList = (Button) popupView.findViewById(R.id.afirmarPalabra);
        addList.setVisibility(View.INVISIBLE);

        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if the recorder is still we running, we have to stop it, else program breaks
                try {
                    Log.i("palabras","recorder stopped");
                    myRecordButton.stopRecording();
                }
                catch (Exception e) {
                    Log.i("palabras","recorder wasnt running");
                }
                popupWindow.dismiss();
            }
        });
        Button deleteWord = (Button) popupView.findViewById(R.id.borrarPalabra);
        deleteWord.setVisibility(View.INVISIBLE);

        // load and show the buttons
        // word has a space at the end - get rid of
        word = word.substring(0,word.length()-1);
        String path = getExternalCacheDir().getAbsolutePath() + "/" +word + translation+".3gp";
        Log.i("buscar path",path);
        PlayButton myPlayButton = new PlayButton(this,path);
        myRecordButton = new RecordButton(this,path,popupWindow);
        myRecordButton.setBackground(getDrawable(R.drawable.custom_rectangle_buttons));
        myPlayButton.setBackground(getDrawable(R.drawable.custom_rectangle_buttons));
        LinearLayout ll = (LinearLayout) popupView.findViewById(R.id.recordAndPlay);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,0,10,0);
        ll.addView(myRecordButton,lp);
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpp.setMargins(10,0,20,0);
        ll.addView(myPlayButton, lpp);
    }

}