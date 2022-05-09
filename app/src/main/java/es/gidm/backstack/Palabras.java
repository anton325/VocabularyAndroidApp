package es.gidm.backstack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Palabras extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
  MyRecyclerViewAdapter adapter;

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

    // data to populate the RecyclerView with
    ArrayList<String> animalNames = new ArrayList<>();
    animalNames.add("Horse");
    animalNames.add("Cow");
    animalNames.add("Camel");
    animalNames.add("Sheep");
    animalNames.add("Goat");
    animalNames.add("Horse");
    animalNames.add("Cow");
    animalNames.add("Camel");
    animalNames.add("Sheep");
    animalNames.add("Goat");
    animalNames.add("Horse");
    animalNames.add("Cow");
    animalNames.add("Camel");
    animalNames.add("Sheep");
    animalNames.add("Goat");
    animalNames.add("Horse");
    animalNames.add("Cow");
    animalNames.add("Camel");
    animalNames.add("Sheep");
    animalNames.add("Goat");

    // set up the RecyclerView
    RecyclerView recyclerView = findViewById(R.id.rvAnimals);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new MyRecyclerViewAdapter(this, animalNames);
    adapter.setClickListener(this);
    recyclerView.setAdapter(adapter);
  }
  @Override
  public void onItemClick(View view, int position) {
    Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
  }

}
