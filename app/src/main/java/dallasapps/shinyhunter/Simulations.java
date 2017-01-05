package dallasapps.shinyhunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jake on 1/5/2017.
 * This contains all of the simulations required by the simulate java activity
 */
public class Simulations {
    int sc = 0;
    int mm = 0;

    String egg(int n, int successes) {  // if you want to have successes as a default value =1, overload the function with a 1 parameter version
        double shiny_chance = (1 + 5 * mm + sc) / 4096;
        List<String> shiny_eggs = new ArrayList<String>();

        for (int i = 0; i < n; i++) {
            Random random = new Random();
            double randomDecimal = random.nextInt(10000001) / 10000000;
            if (randomDecimal < shiny_chance) {
                String stringN = String.valueOf(i);
                shiny_eggs.add(stringN);
            }
        }
        cum_chances cum = new cum_chances();
        double cum_chance = cum.egg(n, sc, mm);

        String message = "DEFAULT";

        if (shiny_eggs.size() > 0) {

            // Join the array of eggs into one string
            String joinedEggs = "";
            for(String j : shiny_eggs) {
                joinedEggs += j + ", ";
            }

            message = String.format("In %i eggs you received %i shiny pokemon.\n " +
                            "They were in the following eggs: %s.\n " +
                            "There was a %d chance of finding a shiny pokemon",
                    n, shiny_eggs.size(), joinedEggs, cum_chance*100);
        }
        return message;
    };

}
