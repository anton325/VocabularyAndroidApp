package es.gidm.backstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Actividad1 extends AppCompatActivity implements View.OnClickListener{

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_actividad1);

//    Toolbar toolbar = findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);

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


    View boton = findViewById(R.id.button1);
    boton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v){
    Intent mi_intent = new Intent(this, MisPalabras.class);

    startActivity(mi_intent);

    Toast.makeText(this, R.string.texto_toast1, Toast.LENGTH_SHORT).show();
  }

}
