package es.gidm.backstack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.HashMap;


public class ListsInLanguage extends AppCompatActivity implements MyRecyclerViewAdapter_languages.ItemClickListener{

  MyRecyclerViewAdapter_languages adapter;
  private HashMap<String, Integer> colorsOfLists;
  private LinearLayoutManager myllm;
  private int scrollToPosition;


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

    colorsOfLists = new HashMap<String, Integer>();
    for(String listName : listsOfLanguage) {
      colorsOfLists.put(listName,0);
    }
    Log.i("elements in list",Integer.toString(listsOfLanguage.size()));
    Log.i("palabras listas","reached recycler");

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.recyclerLists);
    myllm = new LinearLayoutManager(this);
    myllm.scrollToPositionWithOffset(scrollToPosition, 0);
    recyclerView.setLayoutManager(myllm);
    adapter = new MyRecyclerViewAdapter_languages(this, listsOfLanguage,colorsOfLists,getApplicationContext());
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);



    // dynamically add a button for each language
    // the onClickListener they're all gonna share
//    View.OnClickListener btnclick = new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        onButtonShowPopupWindowClickEdit(view, view.getId());
//
//      }
//    };
//    // and save them in array
//    ArrayList<Button> buttons = new ArrayList<Button>();
//    int buttonID = 0;
//    for (final String list : listsOfLanguage) {
//      Button myButton = new Button(this);
//      myButton.setText(list);
//      myButton.setOnClickListener(btnclick);
//      Log.i("list button: ",list);
//      LinearLayout ll = (LinearLayout)findViewById(R.id.buttonLayout);
//      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//      lp.setMargins(75,20,75,0);
//      myButton.setBackground(getDrawable(R.drawable.custom_rectangle_buttons));
//      ll.addView(myButton, lp);
//      myButton.setId(buttonID);
//      buttonID++;
//      buttons.add(myButton);
//    }


    // setup add list botton
    Button addList = (Button) findViewById(R.id.anadirLista);
    addList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
//        Toast.makeText(getApplicationContext(),"show popup",Toast.LENGTH_SHORT).show();
        onButtonShowPopupWindowClick(view);
      }
    });


    // color where we are
    RadioButton currentScreenButton =(RadioButton) findViewById(R.id.palabras);
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

  @Override
  public void onItemClick(View view, int position) {
//    Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    String selectedList = listsOfLanguage.get(position);
    onButtonShowPopupWindowClickEdit(view,selectedList,position);
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

    // make it close on touch outside of popup window
    popupWindow.setOutsideTouchable(true);
    popupWindow.setFocusable(true);
    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    TextView popupText = (TextView) popupView.findViewById(R.id.popupText);
    popupText.setText("Entra la lista nueva que quieres añadir.");
    Button cancelPopup = (Button) popupView.findViewById(R.id.cancellengua);

    final EditText textfieldNuevaLengua = (EditText) popupView.findViewById(R.id.textoLengua);
    textfieldNuevaLengua.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    Button addList = (Button) popupView.findViewById(R.id.afirmarlengua);
    addList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        String nuevaList = textfieldNuevaLengua.getText().toString();
        colorsOfLists.put(nuevaList,0);
        addList(nuevaList);
        addListToSharedPreferences(listsOfLanguage);
        popupWindow.dismiss();
        adapter.notifyItemInserted(listsOfLanguage.size()-1);
        myllm.scrollToPositionWithOffset(listsOfLanguage.size()-1, 0);
        // restart this screen
//        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
//        i.putExtra("language",selectedLanguage);
//        startActivity(i);
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

  public void onButtonShowPopupWindowClickEdit(View view, final String listName, final int position) {
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
    tvEdit.setText(listName);
    tvEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
//        addList(nuevaLengua);
//        // delete old word
//        deleteList(listsOfLanguage.get(buttonID));
//        addListToSharedPreferences(listsOfLanguage);
//
//        // delete all the words that are connected with this list
//
//        popupWindow.dismiss();
//        // restart this screen
//        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
//        i.putExtra("language",selectedLanguage);
//        startActivity(i);
//      }
//    });

    final Button deleteListButton = (Button) popupView.findViewById(R.id.borrar);
    deleteListButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        deleteList(listName);
        addListToSharedPreferences(listsOfLanguage);
        popupWindow.dismiss();
        colorsOfLists.remove(listName);
        adapter.notifyItemRemoved(position);
        // restart this screen
//        Intent i = new Intent(getBaseContext(), ListsInLanguage.class);
//        i.putExtra("language",selectedLanguage);
//        startActivity(i);
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
        Log.i("StartIntent",listName);
        Intent i = new Intent(getBaseContext(), Palabras.class);
        Log.i("listsinlanguage",listName);
        i.putExtra("list", listName);
        startActivity(i);
      }
    });
  }

}