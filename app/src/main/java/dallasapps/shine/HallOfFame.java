package dallasapps.shine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HallOfFame extends AppCompatActivity {
    ArrayList<Pokemon> allPokemon;
    Pokedex pokedex;
    LinkedHashMap<Integer, Pokemon> found_pokemon = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pokedex = new Pokedex();
        allPokemon = pokedex.getNatDex();

        readPokemonFromFile();
        // we want to create a listview or grid view from these pokemon

    }





    private void readPokemonFromFile(){
        String   line;
        String[] info;

        File directory = getBaseContext().getFilesDir();
        try{
            File file = new File(directory, "/found_pokemon.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            while((line = br.readLine()) != null){
                // Info is written as [name, number, count, method]
                info = line.split(" ");

                String  name    = info[0];
                int     number  = Integer.parseInt(info[1]);
                int     count   = Integer.parseInt(info[2]);
                String  method  = info[3];

                Pokemon pokemon = allPokemon.get(number-1);
                pokemon.setCount(count);
                pokemon.setMethod(method);
                found_pokemon.put(number, pokemon);
            }
            br.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

}
