package dallasapps.shine.stats;

/**
 * Created by Jake on 1/5/2017.
 * This returns cumulative chances for a series of binomial tests
 */
public class CumChance {

    double cum_chance;
    double chance;

    float base;

    double cum_bin_chance( int n, double c){
        /*
        given a chance c, and a number of trials n, this returns the chance at least 1 success
        occuring
        */
        cum_chance = 0;
        int m = 1;  // number of successes

        for(int i=0; i<m; i++){
            // Note: we should actually use n choose k (not 1), but n choose 0 = 1, so we make this assumption.
            // Java doesnt seem to have a good standard library combinatorix,
            // writing my own function for n > 21 is complicated
            // Maybe fix this later
            // This assumption works well because people shouldn't need k != 1

            cum_chance += 1 * ((Math.pow(c, i) * (Math.pow((1-c),(n-i)))));
        }
        return 1 - cum_chance;
    };

    double egg(int n, int sc, int mm){
        base = 1 + 2*sc + 5*mm;

        chance = base/4096;
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };

    double RE(int n, int sc, int fs, int lowGen){
        base = 1+ 2*sc;

        if(fs==1){
            chance = 1.0/512;
        }
        else {
            chance = base / (4096 * (float) lowGen);
        }
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };

    double starter(int n, int lowGen){

        chance = 1 / (4096 * (float) lowGen);
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };

    double fish(int n, int sc, int lowGen){
        float chain;

        if(n <20){
            chain = (float)n;
        }
        else{
            chain = 20;
        }
        base = 1 + 2*(float)sc + 2*chain;

        chance = base/(4096 * (float)lowGen);
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };

    double SOS(int n, int sc){
        double cum_chance1;
        double cum_chance2;
        // This has to be treated differently.
        // See http://www.idigitaltimes.com/pokemon-sun-and-moon-sos-chaining-and-shiny-encounter-rates-emerge-573434

        // first 70 chains have normal chances
        // from 70 to 255, the chance has 3 extra rolls
        // the chain resets to normal after 255

        // in n encounters, how many have n<70 70<n<255

        // find how many times 255 goes into n, and the remainder

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

        cum_chance = Math.pow((1-c1),nC1) * Math.pow((1-c2),nC2);

        return 1 - cum_chance;
    };

    double dex(int n){
//        the jury seems a little out depending on the source you look at as to how the shiny chance
//        is affected by chaining the DexNav and by the shiny charm.
//                according to https://mrnbayoh.github.io/pkmn6gen/shiny_calculator/:
//        the search level and chain make a difference, however, I haven't found any other source to back this up
//
//        according to https://daily.pokecommunity.com/2016/06/13/all-about-shiny-pokemon/ and a similar reddit thread:
//        the chance increase (regardless of chain and search level) to something around 1/308
//
//        I don't know which one to trust at the moment, so let's just use the simpler version until more evidence is found
        base = 1;
        chance = base/308;
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };

    double horde(int n, int sc){
        base = (1 + 2*(float)sc)*5 ;

        chance = base/4096;
        cum_chance = cum_bin_chance(n, chance);

        return cum_chance;
    };
}



