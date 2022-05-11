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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Palabras extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
  MyRecyclerViewAdapter adapter;
  RadioGroup radioGroup1;
  private SharedPreferences sharedpreferences;
  private Gson gson;
  private HashMap<String,String> wordsAndTranslations;
  private ArrayList<String> keys;
  private String nameTranslationList;
  private String nameSelectedList;

  private int scrollToPosition;
  private LinearLayoutManager myllm;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i("palabras","started palabras");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.palabras_style);

    // assigning ID of the toolbar to a variable
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Mis Palabras");
    // using toolbar as ActionBar
    setSupportActionBar(toolbar);

    // get intent
    Intent i = getIntent();
    nameSelectedList = i.getStringExtra("list");

//    try {
//      scrollToPosition = i.getIntExtra("scrollToPosition",0);
//    }
//    catch (Exception e){
//      scrollToPosition = 0;
//    }
    Log.i("SelectedList: ",nameSelectedList);

    // get sharedpreferences
    sharedpreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

    // read out which the keys (left side of translation)
    gson = new Gson();
    String json;
    json = sharedpreferences.getString(nameSelectedList,"");
    keys = new ArrayList<String>();
    if (!json.isEmpty()) {
      Type type = new TypeToken<ArrayList<String>>(){}.getType();
      keys = gson.fromJson(json,type);
      for (String k : keys) {
        Log.i("read palabras",k);
      }
    }
    else {
      Log.i("read palabras","empty :(");
    }

    // read out the hashmap where key is the word and value is its translation
    wordsAndTranslations = new HashMap<String,String>();
    nameTranslationList = nameSelectedList+"translation";
    json = sharedpreferences.getString(nameTranslationList,"");
    if (!json.isEmpty()) {
      Type type = new TypeToken<HashMap<String,String>>(){}.getType();
      wordsAndTranslations = gson.fromJson(json,type);
    }

    Log.i("palabras","reached recycler");

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.recyclerWords);
    myllm = new LinearLayoutManager(this);
    myllm.scrollToPositionWithOffset(scrollToPosition, 0);
    recyclerView.setLayoutManager(myllm);
    adapter = new MyRecyclerViewAdapter(this, keys,wordsAndTranslations);
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);



    // setup add word button
    Button addList = (Button) findViewById(R.id.anadirPalabra);
    addList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getApplicationContext(),"show popup",Toast.LENGTH_SHORT).show();
        onButtonShowPopupWindowClick(view,"","",Boolean.FALSE,0);
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
            in = new Intent(getBaseContext(), Aprender.class);
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
//    Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    String key = keys.get(position);
    String value = wordsAndTranslations.get(key);
    // delete the old values from memory to make space for the new ones
    wordsAndTranslations.remove(key);
    keys.remove(key);
    onButtonShowPopupWindowClick(view,key,value,Boolean.TRUE,position);
  }

  public void onButtonShowPopupWindowClick(View view, String key, String value, Boolean existing, final int position) {
    Log.i("palabras","popup");

    // inflate the layout of the popup window
    LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
    final View popupView = inflater.inflate(R.layout.popup_palabra, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    TextView popupText = (TextView) popupView.findViewById(R.id.popupText);
    popupText.setText("Entra la traduccion nueva que quieres añadir.");
    final EditText textfieldWord = (EditText) popupView.findViewById(R.id.word);
    textfieldWord.setText(key);
    final EditText textfieldTranslation = (EditText) popupView.findViewById(R.id.translation);
    textfieldTranslation.setText(value);

    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);
    Button addList = (Button) popupView.findViewById(R.id.afirmarPalabra);

    addList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        String word = textfieldWord.getText().toString();
        String translation = textfieldTranslation.getText().toString();

        addToList(keys,word);
        addToHashmap(wordsAndTranslations,word,translation);

        addListToSharedPreferences(keys);
        addHashmapToSharedPreferences(wordsAndTranslations);

        popupWindow.dismiss();
        adapter.notifyItemInserted(keys.size());
        myllm.scrollToPositionWithOffset(keys.size() - 1, 0);

        // scroll to that item

        // restart this screen
//        Intent i = new Intent(getBaseContext(), Palabras.class);
//        i.putExtra("list",nameSelectedList);
//        startActivity(i);
      }
    });
    cancelPopup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String word = textfieldWord.getText().toString();
        String translation = textfieldTranslation.getText().toString();

        addToList(keys,word);
        addToHashmap(wordsAndTranslations,word,translation);

        addListToSharedPreferences(keys);
        addHashmapToSharedPreferences(wordsAndTranslations);
        popupWindow.dismiss();
      }
    });
    Button deleteWord = (Button) popupView.findViewById(R.id.borrarPalabra);
    deleteWord.setVisibility(View.INVISIBLE);
    if(existing) {
      deleteWord.setVisibility(View.VISIBLE);
      deleteWord.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          addListToSharedPreferences(keys);
          addHashmapToSharedPreferences(wordsAndTranslations);
          popupWindow.dismiss();
          // restart this screen
//          Intent i = new Intent(getBaseContext(), Palabras.class);
//          i.putExtra("list",nameSelectedList);
//          startActivity(i);
          adapter.notifyItemRemoved(position);
        }
      });
    }
  }

  public void addToList(ArrayList<String> list, String key){
    list.add(key);
  }

  public void addToHashmap(HashMap<String,String> hm, String key,String value){
    hm.put(key, value);
  }
  public void addListToSharedPreferences(ArrayList<String> listsOfLanguage){
    String json = gson.toJson(listsOfLanguage);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(nameSelectedList,json);
    editor.commit();
  }

  public void addHashmapToSharedPreferences(HashMap<String,String> hm){
    String json = gson.toJson(hm);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(nameTranslationList,json);
    editor.commit();
  }



}
