package es.gidm.backstack;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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


public class MisPalabras extends AppCompatActivity {


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
        Toast.makeText(getApplicationContext(), "show popup_edit", Toast.LENGTH_SHORT).show();
        onButtonShowPopupWindowClickEdit(view, view.getId());
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

    // setup add language botton
    Button addLanguage = (Button) findViewById(R.id.anadirlengua);
    addLanguage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Toast.makeText(getApplicationContext(),"show popup",Toast.LENGTH_SHORT).show();
        onButtonShowPopupWindowClick(view);
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
  public void onButtonShowPopupWindowClick(View view) {
    // inflate the layout of the popup window
    LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
    final View popupView = inflater.inflate(R.layout.popup_lengua, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    // make it close on touch outside of popup window
    popupWindow.setOutsideTouchable(true);
    popupWindow.setFocusable(true);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);

    Button addLengua = (Button) popupView.findViewById(R.id.afirmarlengua);
    addLengua.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        EditText textfieldNuevaLengua = (EditText) popupView.findViewById(R.id.textoLengua);
        String nuevaLengua = textfieldNuevaLengua.getText().toString();
        addLanguage(nuevaLengua);
        addLanguageToSharedPreferences(languages);
        popupWindow.dismiss();
        // restart this screen
        Intent i = new Intent(getBaseContext(), MisPalabras.class);
        startActivity(i);
      }
    });

    cancelPopup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        popupWindow.dismiss();
      }
    });
  }

  public void addLanguage(String language){
    languages.add(language);
  }
  public void deleteLanguage(String language) {
    languages.remove(language);
  }

  public void addLanguageToSharedPreferences(ArrayList<String> languages){
    String json = gson.toJson(languages);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString("languages",json);
    editor.commit();
  }

  public void onButtonShowPopupWindowClickEdit(View view, final int buttonID) {
    // inflate the layout of the popup window
    LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
    final View popupView = inflater.inflate(R.layout.popup_edit, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    // make it close on touch outside of popup window
    popupWindow.setOutsideTouchable(true);
    popupWindow.setFocusable(true);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    final TextView tvEdit = (TextView) popupView.findViewById(R.id.fieldToEdit);
    tvEdit.setText(languages.get(buttonID));

    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);
    Button afirmarEdit = (Button) popupView.findViewById(R.id.afirmar);
    afirmarEdit.setVisibility(View.INVISIBLE);
//    afirmarEdit.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v){
//        String nuevaLengua = tvEdit.getText().toString();
//        addLanguage(nuevaLengua);
//        // delete old word
//        deleteLanguage(languages.get(buttonID));
//        addLanguageToSharedPreferences(languages);
//
//        // delete all the lists that are associated with that language
//
//        // delete all the words that are connected with each list
//
//        popupWindow.dismiss();
//        // restart this screen
//        Intent i = new Intent(getBaseContext(), MisPalabras.class);
//        startActivity(i);
//      }
//    });

    final Button deleteLanguage = (Button) popupView.findViewById(R.id.borrar);
    deleteLanguage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteLanguage(languages.get(buttonID));

        // delete all the associated lists and words


        addLanguageToSharedPreferences(languages);
        popupWindow.dismiss();
        // restart this screen
        Intent i = new Intent(getBaseContext(), MisPalabras.class);
        startActivity(i);
      }
    });
    cancelPopup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        popupWindow.dismiss();
      }
    });
    Button nextScreen = (Button) popupView.findViewById(R.id.continuar);
    nextScreen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("StartIntent",languages.get(buttonID));
        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
        Log.i("mispalabras",Integer.toString(buttonID));
        i.putExtra("language", languages.get(buttonID));
        startActivity(i);
      }
    });
  }
}