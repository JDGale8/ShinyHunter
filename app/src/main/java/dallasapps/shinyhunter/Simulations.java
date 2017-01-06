package dallasapps.shinyhunter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jake on 1/5/2017.
 *
 * Contains all the simulations for the different shiny hunting types
*/
 public class Simulations {
    int sc = 0;
    int mm = 0;

    private static DecimalFormat df2 = new DecimalFormat(".##");


    String egg(int n)
    {
        int successes = 1;
        double shiny_chance = (1 + 5 * (float)mm + (float)sc) / 4096;

        List<String> shiny_eggs = simulate(n, shiny_chance);

        cum_chances cum = new cum_chances();
        double cum_chance = cum.egg(n, sc, mm);

        String message =
                "In " +Integer.toString(n)+" eggs you did not receive any shiny pokemon.\n\n"+
                "There was a "+ df2.format(cum_chance*100)+"% chance of finding a shiny pokemon.";;

        if (shiny_eggs.size() > 0) {

            String joinedEggs = join(shiny_eggs);
            String nEggs = Integer.toString(shiny_eggs.size());

            message = "In " +Integer.toString(n) + " eggs you received " +nEggs + " shiny pokemon.\n\n " +
                      "They were in the following egg(s): " + joinedEggs +  ".\n\n " +
                      "There was a "+ df2.format(cum_chance*100)+"% chance of finding a shiny pokemon.";
        }
        return message;
    };





    private List<String> simulate(int n, double chance){
        List<String> shiny_pok = new ArrayList<String>();

        for (int i = 0; i < n; i++) {
            Random random = new Random();
            double randomDecimal = random.nextFloat();
            if (randomDecimal < chance) {
                String stringN = String.valueOf(i);
                shiny_pok.add(stringN);
            }
        }
        return shiny_pok;
    }


    private String join(List<String> list) {
        // Joins together lists of strings into strings, separated by commas

        String joinedString = "";
        if(list.size()>0)
        {
            int i = 0;
            for (String j : list) {
                if (i == 0) {
                    joinedString += j;
                }
                else {
                    joinedString += ", " + j;
                }
                i++;
            }
        }
        return joinedString;
    }
}
