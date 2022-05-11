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


public class ListsInLanguage extends AppCompatActivity {


  RadioGroup radioGroup1;
  private String selectedLanguage;
  private ArrayList<String> listsOfLanguage;
  private Gson gson;
  private SharedPreferences sharedpreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i("listsinlanguage","started");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.listsinlanguage_style);
    // assigning ID of the toolbar to a variable
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Mis Listas");
    // using toolbar as ActionBar
    setSupportActionBar(toolbar);

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


    // dynamically add a button for each language
    // the onClickListener they're all gonna share
    View.OnClickListener btnclick = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onButtonShowPopupWindowClickEdit(view, view.getId());

      }
    };
    // and save them in array
    ArrayList<Button> buttons = new ArrayList<Button>();
    int buttonID = 0;
    for (final String list : listsOfLanguage) {
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


    // setup add list botton
    Button addList = (Button) findViewById(R.id.anadirLista);
    addList.setOnClickListener(new View.OnClickListener() {
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
  public void onButtonShowPopupWindowClick(View view) {
    Log.i("listsinlanguage","popup");


    // inflate the layout of the popup window
    LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
    final View popupView = inflater.inflate(R.layout.popup_lengua, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    TextView popupText = (TextView) popupView.findViewById(R.id.popupText);
    popupText.setText("Entra la lista nueva que quieres añadir.");
    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);
    Button addList = (Button) popupView.findViewById(R.id.afirmarlengua);
    addList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        EditText textfieldNuevaLengua = (EditText) popupView.findViewById(R.id.textoLengua);
        String nuevaList = textfieldNuevaLengua.getText().toString();
        addList(nuevaList);
        addListToSharedPreferences(listsOfLanguage);
        popupWindow.dismiss();
        // restart this screen
        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
        i.putExtra("language",selectedLanguage);
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

  public void addList(String list){
    listsOfLanguage.add(list);
  }
  public void deleteList(String list) {
    listsOfLanguage.remove(list);
  }
  public void addListToSharedPreferences(ArrayList<String> listsOfLanguage){
    String json = gson.toJson(listsOfLanguage);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(selectedLanguage,json);
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

    final TextView tvEdit = (TextView) popupView.findViewById(R.id.fieldToEdit);
    tvEdit.setText(listsOfLanguage.get(buttonID));

    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);
    Button afirmarEdit = (Button) popupView.findViewById(R.id.afirmar);
    afirmarEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        String nuevaLengua = tvEdit.getText().toString();
        addList(nuevaLengua);
        // delete old word
        deleteList(listsOfLanguage.get(buttonID));
        addListToSharedPreferences(listsOfLanguage);

        // delete all the words that are connected with this list

        popupWindow.dismiss();
        // restart this screen
        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
        i.putExtra("language",selectedLanguage);
        startActivity(i);
      }
    });

    final Button deleteListButton = (Button) popupView.findViewById(R.id.borrar);
    deleteListButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteList(listsOfLanguage.get(buttonID));
        addListToSharedPreferences(listsOfLanguage);
        popupWindow.dismiss();
        // restart this screen
        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
        i.putExtra("language",selectedLanguage);
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
    nextScreen.setText("Abrir lista");
    nextScreen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.i("StartIntent",listsOfLanguage.get(buttonID));
        Intent i = new Intent(getBaseContext(), Palabras.class);
        Log.i("listsinlanguage",Integer.toString(buttonID));
        i.putExtra("list", listsOfLanguage.get(buttonID));
        startActivity(i);
      }
    });
  }

}