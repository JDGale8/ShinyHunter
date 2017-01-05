package dallasapps.shinyhunter;

/**
 * Created by Jake on 1/5/2017.
 * This returns cumulative chances for a series of binomial tests
 */
public class cum_chances {
    int sc; // shiny charm
    int mm; // masuda method
    int fs; // friend safari

    double cum_bin_chance(double c, int n){
        /*
        given a chance c, and a number of trials n, this returns the chance at least 1 success
        should have occured
        */
        double cum_chance = 0;
        usefulFunctions UF = new usefulFunctions();

        for(int i=0; i<n; i++){
            cum_chance += UF.comb(n, i) * ((Math.pow(c, i) * (Math.pow((1-c),(n-i)))));

        }
        return 1- cum_chance;
    };

    double egg(int n, int sc, int mm){
        int base = 1 + 2*sc + mm;

        double chance = base/4096;
        double cum_chance = cum_bin_chance(chance, n);

        return cum_chance;
    }


}


