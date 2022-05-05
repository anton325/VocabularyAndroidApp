package es.gidm.backstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Actividad2 extends AppCompatActivity implements View.OnClickListener{

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad2);
//    Toolbar toolbar = findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);

    Button boton = findViewById(R.id.button2);
    boton.setOnClickListener(this);


/*
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });
*/

  }

  @Override
  public void onClick(View v){
    Intent mi_intent = new Intent(this, Actividad3.class);
    mi_intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(mi_intent);

    Toast.makeText(this, R.string.texto_toast2, Toast.LENGTH_SHORT).show();
  }

  private void starActivity(Intent mi_intent) {
  }

}
