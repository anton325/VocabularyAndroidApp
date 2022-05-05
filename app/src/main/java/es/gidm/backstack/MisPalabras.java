package es.gidm.backstack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.Gravity;


public class MisPalabras extends AppCompatActivity {


  RadioGroup radioGroup1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mis_palabras);
    // assigning ID of the toolbar to a variable
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Mis Palabras");
    // using toolbar as ActionBar
    setSupportActionBar(toolbar);



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
            in = new Intent(getBaseContext(),Actividad3.class);
            startActivity(in);
            overridePendingTransition(0, 0);
            break;
          case R.id.ajustes:

            Log.i("matching", "matching inside1 rate" + checkedId);
            in = new Intent(getBaseContext(),Actividad3.class);
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
    View popupView = inflater.inflate(R.layout.popup_lengua, null);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    boolean focusable = true; // lets taps outside the popup also dismiss it
    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    // show the popup window
    // which view you pass in doesn't matter, it is only used for the window tolken
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    // dismiss the popup window when touched
//    popupView.setOnTouchListener(new View.OnTouchListener() {
//      @Override
//      public boolean onTouch(View v, MotionEvent event) {
//        popupWindow.dismiss();
//        return true;
//      }
//    });
  }
}