package dallasapps.shine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import dallasapps.shine.adapters.MethodSpinnerAdapter;
import dallasapps.shine.adapters.PokedexArrayAdapter;


//
// Still to add:
//
// Options menu for changing generation
// Pokedex button for switching between pokedexes
// Found database for pokemon
// In found have add preexisting shinies]
// Call shiny library the Hall of Fame
// Add shiny locked list, and for what games, e.g. Xerneas in X etc.
// All this information can be in one place, so - Shiny locks, then tabs for each generation and what pokemon are locked
// make listview on the pokedex searchable
//
// When you select a new generation, repopulate the drop down list so that things like SOS arent in there any more
// Have options to change the forms of the pokemon too, mega, alolan, etc

// Add in PokeRadar
// Post the app on reddit
// Send the app to twitch streamers so that they can announce it?
// Searchable listview - https://www.youtube.com/watch?v=nQnyAXJxngY

// Star for favourite pokemon
// Maybe a Hunt list
// Hunts in progress
// Add Guides for all shiny types
// Add section scrolling for listview - https://www.youtube.com/watch?v=l6EAOIXy0JA

// FOR A PERFORMANCE BOOST, I NEED TO RESIZE ALL MY POKEMON SPRITES TO HAVE A SMALL VERSION THAT THE LISTVIEW CAN USE
// Or make a small version (box sprites)

// redesign front page
// HERE IS A BIG PROBLEM
// WHEN YOU FILTER THE POKEMON LIST, A NEW LIST IS GENERATED
// This means that any progress (which is tied to the pokemon) is replaced. Perhaps counter should be independent of the pokemon list

// TODO Pokemon should have a nat dex number and a number for its local dex
// TODO add the word pokedex to side buttton
// TODO add a currently hunting short list which allows removals and stars in the side bar
// TODO add gridview for HallOfFame


public class Track extends AppCompatActivity{

    private static final String LOG_TAG = LaunchPage.class.getSimpleName();

    SlidingContainer root;
    Button side;
    String currentMethod;

    private HashMap<Integer, Pokemon> initializedPokemon = new HashMap<>(); // This will have a key of the national dex number
    private LinkedHashMap<Integer, Pokemon> foundPokemon = new LinkedHashMap<>();
    private boolean loadedFile;

    private ListView pokedexListView;
    public PokedexArrayAdapter pokedexAdapter;
    public ArrayList<Pokemon> pokemonList;
    EditText pokedexSearch;
    CheckBox scChk;
    CheckBox mmChk;
    TextView counter;
    TextView chceTxt;
    Pokedex  mPokedex = new Pokedex();
    Pokemon  trackedPokemon;
    String   trackedChance;
    boolean  isNationalDex;
    int      currentGen;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_options:
                Toast.makeText(Track.this, "Change Gen", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete_saves:
                deleteSaves();
                Toast.makeText(Track.this, "Saves successfully deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPause() {

        pushTrackedPokemonToList();
        savePokemonToFile();
            super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ########################################################################################
        //                              LOAD UI ELEMENTS
        // ########################################################################################
        final Vibrator vibr                 = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        this.root                           = (SlidingContainer) this.getLayoutInflater().inflate(R.layout.activity_track, null);
        this.setContentView(root);

        final Button plusBtn                = (Button) findViewById(R.id.plusBtn);
        final Button minBtn                 = (Button) findViewById(R.id.minBtn);

        final Button foundBtn               = (Button) findViewById(R.id.foundBtn);

        counter                             = (TextView) findViewById(R.id.counterTxt);
        chceTxt                             = (TextView) findViewById(R.id.chanceTxt);

        final TextView pokemonName          = (TextView) findViewById(R.id.pokedexNo);
        final ImageView pokemonIcon         = (ImageView) findViewById(R.id.trackedPokemon);
        final ImageView whiteTop            = (ImageView) findViewById(R.id.whiteTop);

        // initialize side drawer pokedex
        final ImageButton openPkdx          = (ImageButton) findViewById(R.id.pokedexBtn);
        final Switch pokedexTgl             = (Switch) findViewById(R.id.pokedexSwitch);
        final TextView pokedexTypeTxt       = (TextView) findViewById(R.id.pokedexTypeTxt);

        pokemonList                         = mPokedex.getNatDex(); // Here we can initialise other pokedex types

        initPokedex(pokemonList);
        isNationalDex = pokedexTgl.isChecked();


        // Set the method spinner
        final Spinner methodSpinner         = (Spinner) findViewById(R.id.methodSpinner);
        final ArrayList<HuntingMethod> huntingList = HuntingMethod.createHuntingList();
        methodSpinner.setAdapter(new MethodSpinnerAdapter(Track.this, R.layout.spinner_row,
                huntingList));

        scChk                               = (CheckBox) findViewById(R.id.scTrackChk);
        mmChk                               = (CheckBox) findViewById(R.id.mmTrackChk);

        side                                = (Button) findViewById(R.id.sideBtn);
        pokedexSearch                       = (EditText) findViewById(R.id.pokedexSearchEditText);

        // ########################################################################################

        // Get saved information from Shared Preferences
        SharedPreferences prefs = getSharedPreferences("private preference", Context.MODE_PRIVATE);
        scChk.setChecked(prefs.getBoolean("shiny checkbox", false));
        mmChk.setChecked(prefs.getBoolean("masuda checkbox", false));
        currentGen = 7; // TODO - This can be saved to a shared preferences file later
        mPokedex.setGen(currentGen);

        // Load any information
        readPokemonFromFile();

        // If no saved data is loaded, use defaults below
        if(!loadedFile){
            currentMethod = "RE";
            trackedPokemon = new Pokemon("Bulbasaur", 1, R.drawable.p1);
            trackedPokemon.setLocal(false);
        }

        HuntingMethod.getMethodPos(currentMethod, currentGen);
        assignTrackingVariables();
        recalcChance();

        setCheckBoxesFromMethod();
        setTrackingDisplay(trackedPokemon, pokemonName, pokemonIcon, methodSpinner);

        //set the listener to the list view
        pokedexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Save the current tracked pokemon to the list
                pushTrackedPokemonToList();

                Pokemon pok     = pokedexAdapter.filteredPokemonList.get(position-1);
                pok.setLocal(!isNationalDex);
                pok.setGen(currentGen);
                trackedPokemon  = getTrackedPokemonFromList(pok);
                assignTrackingVariables();
                setTrackingDisplay(trackedPokemon, pokemonName, pokemonIcon, methodSpinner);
                setCheckBoxesFromMethod();

                pokedexSearch.setText("");
                hideKeyboard();
            }
        });

        // Set the on changed listener for the pokedex toggle
        pokedexTgl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!pokedexTgl.isChecked()){
                    switch(currentGen){
                        case 1:
                            pokedexAdapter.filteredPokemonList = mPokedex.getKantoDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getKantoDex();
                            break;
                        case 2:
                            pokedexAdapter.filteredPokemonList = mPokedex.getJohtoDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getJohtoDex();
                            break;
                        case 3:
                            pokedexAdapter.filteredPokemonList = mPokedex.getHoennDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getHoennDex();
                            break;
                        case 4:
                            pokedexAdapter.filteredPokemonList = mPokedex.getUnovaDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getUnovaDex();
                            break;
                        case 5:
                            pokedexAdapter.filteredPokemonList = mPokedex.getSinnohDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getSinnohDex();
                            break;
                        case 6:
                            pokedexAdapter.filteredPokemonList = mPokedex.getKalosDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getKalosDex();
                            break;
                        case 7:
                            pokedexAdapter.filteredPokemonList = mPokedex.getAlolaDex();
                            pokedexAdapter.originalPokemonList = mPokedex.getAlolaDex();
                            break;
                    }
                    pokedexTypeTxt.setText(mPokedex.getPokedexName() + " Dex");
                }
                else {
                    pokedexAdapter.filteredPokemonList = mPokedex.getNatDex();
                    pokedexAdapter.originalPokemonList = mPokedex.getNatDex();
                    pokedexTypeTxt.setText("National Dex");
                }
                isNationalDex = pokedexTgl.isChecked();
                pokedexAdapter.notifyDataSetChanged();
                pokedexListView.smoothScrollToPosition(0);
            }
        });


        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HuntingMethod item  = huntingList.get(position);
                currentMethod       = item.getMethod();
                assignTrackingVariables();

                recalcChance();
                chceTxt.setText(trackedChance);
                setCheckBoxesFromMethod();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(root.menuCurrentState == SlidingContainer.menuState.OPEN){
                    hideKeyboard();

                    toggleMenu(root);
                    side.setVisibility(View.INVISIBLE);
                }
            }
        });

        openPkdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu(root);
                side.setVisibility(View.VISIBLE);
            }
        });


        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibr.vibrate(28);
                trackedPokemon.getCounter().inc_count();
                counter.setText(String.format("%s", trackedPokemon.getCount()));

                recalcChance();
                chceTxt.setText(trackedChance);
            }
        });


        minBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackedPokemon.getCount() == 0) vibr.vibrate(200); else vibr.vibrate(28);
                trackedPokemon.getCounter().dec_count();
                counter.setText(String.format("%s", trackedPokemon.getCount()));

                recalcChance();
                chceTxt.setText(trackedChance);
            }
        });

        foundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the sprite to shiny and save the information to a database somewhere
                if (!trackedPokemon.isShiny()) {

                    vibr.vibrate(500);
                    String iconName = trackedPokemon.getSpriteName(getBaseContext());
                    trackedPokemon.setSprite(getBaseContext(), iconName + "_shiny");
                    pokemonIcon.setImageResource(trackedPokemon.getSpriteId());

                    trackedPokemon.toggleShiny();

                    flashWhiteTop(whiteTop);
                }

                saveFoundPokemonToFile();
                // Animate the flare for the shiny reveal

            }
        });
    }

    private void setCheckBoxesFromMethod() {
        switch (currentMethod){
            case "RE":
                mmChk.setEnabled(false);
                scChk.setEnabled(true);
                break;
            case "Egg":
                mmChk.setEnabled(true);
                scChk.setEnabled(true);
                break;
            case "Horde":
                mmChk.setEnabled(false);
                scChk.setEnabled(true);
                break;
            case "SR":
                mmChk.setEnabled(false);
                scChk.setEnabled(true);
                break;
            case "Starter":
                mmChk.setEnabled(false);
                scChk.setEnabled(false);
                break;
            case "Fishing":
                mmChk.setEnabled(false);
                scChk.setEnabled(true);
                break;
            case "SOS":
                mmChk.setEnabled(false);
                scChk.setEnabled(true);
                break;
            case "DexNav":
                mmChk.setEnabled(false);
                scChk.setEnabled(false);
                break;
            default:
                Log.d(LOG_TAG, "checkbox method defaulted with " + currentMethod);
                mmChk.setEnabled(true);
                scChk.setEnabled(true);
                break;
        }
    }


    private void setTrackingDisplay(Pokemon pok, TextView name, ImageView im, Spinner spin) {
        Log.d(LOG_TAG, "Adding " + pok.getName() + " to the display");
        name.setText(pok.getNameAndNumber());
        im.setImageResource(pok.getSpriteId());
        spin.setSelection(HuntingMethod.getMethodPos(pok.getMethod(), currentGen));

        recalcChance();
        chceTxt.setText(trackedChance);
        counter.setText(String.format("%s", trackedPokemon.getCount()));
    }

    public void detectCheckbox(View view){

        SharedPreferences prefs = getSharedPreferences("private preference", Context.MODE_PRIVATE);

        prefs.edit()
                .putBoolean("masuda checkbox", mmChk.isChecked())
                .putBoolean("shiny checkbox", scChk.isChecked())
                .apply();

        assignTrackingVariables();
        recalcChance();
        chceTxt.setText(trackedChance);
    }

    public void toggleMenu(View view) {
        this.root.toggleMenu();
    }

    void recalcChance(){
        trackedChance = trackedPokemon.getCounter()
                .calc_chance();
    }

    private void initPokedex(ArrayList<Pokemon> poks){

        View header = getLayoutInflater().inflate(R.layout.pokedex_header, null);

        pokedexAdapter  = new PokedexArrayAdapter(this, R.layout.pokedex_entry, poks);
        pokedexListView = (ListView)findViewById(R.id.pokedexListView);
        pokedexListView.setAdapter(pokedexAdapter);
        pokedexListView.addHeaderView(header);

    }


    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pokedexSearch.getWindowToken(), 0);
    }

    private void assignTrackingVariables(){

        if(scChk.isChecked()) trackedPokemon.setSc(1);
        else trackedPokemon.setSc(0);

        if(mmChk.isChecked()) trackedPokemon.setMm(1);
        else trackedPokemon.setMm(0);

        trackedPokemon.setMethod(currentMethod);
        trackedPokemon.setGen(currentGen);
        trackedPokemon.setLocal(!isNationalDex);
    }

    @Override
    public void onBackPressed(){
        // because opening the side drawer doesnt add to the isntance stack, pressing back doesn't automatically
        // get rid of it. This overrides this to close the menu if it's open

        if(root.menuCurrentState == SlidingContainer.menuState.OPEN){
            toggleMenu(root);
            side.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    private void pushTrackedPokemonToList(){
        // Put the tracked pokemon into the save list
        Log.d(LOG_TAG, "Pushing "+ trackedPokemon.getName() + ", number " + trackedPokemon.getNationalNumber() + " to hashmap");
        initializedPokemon.put(trackedPokemon.getNationalNumber(), trackedPokemon);
    }

    private Pokemon getTrackedPokemonFromList(Pokemon pok){
        int number = pok.getNationalNumber();
        if(initializedPokemon.containsKey(number)){
            Log.d(LOG_TAG, "Getting " + pok.getName() + ", number " + number + " from map");
            Log.d(LOG_TAG, "This returns "+ initializedPokemon.get(number));
            return initializedPokemon.get(number);
        }
        else{
            pok.setMethod(currentMethod);
            return pok;
        }
    }


    private void savePokemonToFile(){
        try {
            FileOutputStream fileOut = openFileOutput("initializedPokemon.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);

            // First line tells the tracked Pokemon

            int trackedNumber      = trackedPokemon.getNationalNumber();
            outputWriter.write(trackedPokemon.getName() + " "
                    + trackedNumber              + " "
                    + trackedPokemon.getCount()  + " "
                    + trackedPokemon.getMethod() + " "
                    + true + "\n");

            Iterator it = initializedPokemon.entrySet().iterator();

            // iterate through initialized pokemon
            while(it.hasNext()){
                Map.Entry entry = (Map.Entry)it.next();
                Pokemon pokemon = (Pokemon)entry.getValue();
                int number      = pokemon.getNationalNumber();
                int count       = pokemon.getCount();
                String method   = pokemon.getMethod();
                String name     = pokemon.getName();
                Log.v(LOG_TAG, "Writing " + name + " to File");
                outputWriter.write(name + " " + number + " " + count + " " + method + " " + false + "\n");
            }
            outputWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFoundPokemonToFile(){
        try {
            FileOutputStream fileOut = openFileOutput("found_pokemon.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileOut);

            // iterate through found pokemon
            for (Object o : foundPokemon.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Pokemon pokemon = (Pokemon) entry.getValue();
                int number      = pokemon.getNationalNumber();
                int count       = pokemon.getCount();
                String method   = pokemon.getMethod();
                String name     = pokemon.getName();

                Log.v(LOG_TAG, "Found " + name + ", writing to File");
                outputWriter.write(name + " " + number + " " + count + " " + method + "\n");
            }
            outputWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSaves(){
        File directory = getBaseContext().getFilesDir();

        try{
            // Empty the hashmaps so they dont get resaved
            initializedPokemon = new HashMap<>();
            foundPokemon       = new LinkedHashMap<>();
            File initFile         = new File(directory, "/initializedPokemon.txt");
            File foundPokemonFile = new File(directory, "/found_pokemon.txt");
            initFile.delete();
            foundPokemonFile.delete();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void readPokemonFromFile(){
        String   line;
        String[] info;

        File directory = getBaseContext().getFilesDir();
        try{
            Log.d(LOG_TAG, "Reading from File in " + directory);
            File file = new File(directory, "/initializedPokemon.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            ArrayList<Pokemon> allPokemon = mPokedex.getNatDex();

            while((line = br.readLine()) != null){
                // Info is written as [name, number, count, method]
                info = line.split(" ");

                String  name    = info[0];
                int     number  = Integer.parseInt(info[1]);
                int     count   = Integer.parseInt(info[2]);
                String  method  = info[3];
                String  tracked = info[4];

                Pokemon pokemon = allPokemon.get(number-1);
                Log.d(LOG_TAG, "Retrieving " + pokemon.getName() + ", number: " + pokemon.getNationalNumber() + " from save file");
                pokemon.setCount(count);
                pokemon.setMethod(method);

                if(tracked.equals("true")){
                    trackedPokemon = pokemon;
                    currentMethod  = method;
                }
                initializedPokemon.put(number, pokemon);
            }
            loadedFile = true;
            br.close();
        }

        catch (FileNotFoundException e){
            // no save file
            loadedFile = false;
            e.printStackTrace();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void flashWhiteTop(ImageView whiteTop) {

        whiteTop.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);
        alphaAnim.setDuration(1500);
        alphaAnim.setInterpolator(new AccelerateInterpolator());
        whiteTop.setAlpha(1f);
        whiteTop.startAnimation(alphaAnim);
        whiteTop.setVisibility(View.GONE);
    }

}



