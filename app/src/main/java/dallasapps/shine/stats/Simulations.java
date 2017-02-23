package dallasapps.shine.stats;

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
    // Class to simulate a number of trials to determine likelihood of shiny encounters
    // This is a great way to physically see how likely an encounter is after n trials, given your
    // specific circumstances, shiny charm, horde encoutners, etc etc.

    public int sc = 0;
    public int mm = 0;
    public int fs = 0;
    public int lowGen = 1;

    int successes = 1;
    double shiny_chance;
    double cum_chance;
    CumChance cum = new CumChance();

    private static DecimalFormat df2 = new DecimalFormat(".##");


    public String egg(int n)
    {
        shiny_chance = (1 + 5 * (float)mm + 2*(float)sc) / 4096;

        List<String> shiny_eggs = simulate(n, shiny_chance);
        cum_chance = cum.egg(n, sc, mm);

        return genMessage(n, "egg", shiny_eggs, cum_chance);
    };

    public String RE(int n)
    {

        if(fs==1){
            shiny_chance = 1.0/512;
        }
        else {
            shiny_chance = (1 + 2 * (float)sc) / (4096 *(float)lowGen);
        }

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.RE(n, sc, fs, lowGen);

        return genMessage(n, "encounter", shiny_pok, cum_chance);
    };

    public String horde(int n)
    {
        shiny_chance = (1 + 2 * (float)sc)*5 / 4096;

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.horde(n, sc);

        return genMessage(n, "horde encounter", shiny_pok, cum_chance);
    };

    public String SOS(int n)
    {
        int div = (int)Math.floor(n/255);
        int rem = n%255;

        int nC1 = div * 70;
        int nC2 = div * (255 -70);

        if(rem > 70){
            nC1 += 70;
            nC2 += (rem-70);
        }
        else{
            nC1 += rem;
        }

        double c1 = (1 + 2*(float)sc)/4096;
        double c2 = (1 + 2*(float)sc + 3)/4096;

        List<String> shiny_pok = new ArrayList<String>();

        for (int i = 0; i < n; i++) {
            Random random = new Random();
            double randomDecimal = random.nextFloat();
            if (n % 255 < 70) {
                if (randomDecimal < c1) {
                    String stringN = String.valueOf(i);
                    shiny_pok.add(stringN);
                }
            } else {
                if (randomDecimal < c2) {
                    String stringN = String.valueOf(i);
                    shiny_pok.add(stringN);
                }
            }
        }



        cum_chance = cum.SOS(n, sc);

        return genMessage(n, "SOS call", shiny_pok, cum_chance);
    };

    public String fish(int n)
    {
        float chain;

        if(n <20){
            chain = (float)n;
        }
        else{
            chain = 20;
        }

        shiny_chance = (1 + 2*(float)sc + 2*chain)/ 4096;

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.fish(n, sc, lowGen);

        return genMessage(n, "reel", shiny_pok, cum_chance);
    };

    public String starter(int n)
    {
        shiny_chance =  1/ (4096 *(float)lowGen);

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.starter(n, lowGen);

        return genMessage(n, "reset", shiny_pok, cum_chance);
    };

    public String SR(int n)
    {
        shiny_chance =  (1 + 2*(float)sc)/ (4096 *(float)lowGen);

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.RE(n, sc, 0, lowGen);

        return genMessage(n, "reset", shiny_pok, cum_chance);
    };

    public String dex(int n)
    {
        shiny_chance =  1.0/308;

        List<String> shiny_pok = simulate(n, shiny_chance);

        cum_chance = cum.dex(n);

        return genMessage(n, "encounter", shiny_pok, cum_chance);
    };


    private String genMessage(int n, String type, List<String> shinyList, double cum_chance){
        // Generates the output message telling the user the outcome of the
        if (cum_chance < 0.00005){
            cum_chance = 0;
        }

        String message =
                "In " +Integer.toString(n) + " " + type + "s  you did not receive any shiny pokemon.\n\n"+
                        "There was a "+ df2.format(cum_chance*100)+"% chance of finding a shiny pokemon.";;

        if (shinyList.size() > 0) {

            String joined = join(shinyList);
            String nEggs = Integer.toString(shinyList.size());

            message = "In " + Integer.toString(n) + " " + type + "s you received " + nEggs + " shiny pokemon.\n\n" +
                    "They were in the following " + type + "(s): " + joined + ".\n\n" +
                    "There was a " + df2.format(cum_chance * 100) + "% chance of finding a shiny pokemon.";
        }
        return message;
    };


    private List<String> simulate(int n, double chance){
        // over n trials, run n simulations and fill an array with each success

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
