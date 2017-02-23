package dallasapps.shine.stats;

/**
 * Created by Jake on 1/7/2017.
 * Calculates shiny chances depending on count and hunting method
 * Also controls the counters
 */
public class Counter {
    private int count = 0;

    private int sc = 0;
    private int mm = 0;
    private int fs = 0;
    private int lowGen = 1;

    private double chance;
    private String method;


    public void inc_count() { count ++; }

    public void dec_count() { if(count>0) count --; }

    public String calc_chance(){

        switch(method) {
            case "RE":
                if (fs == 1) chance = 1.0 / 512;
                else         chance = (1.0 + 2 * (float)sc) / (4096 * (float)lowGen);
                break;
            case "Egg":
                chance = (1.0 + 2 * (float)sc + 5 * (float)mm) / (4096 * (float)lowGen);
                break;
            case "SR":
                chance = (1.0 + 2 * (float)sc) / (4096 * (float)lowGen);
                break;
            case "Horde":
                chance = 5 * (1.0 + 2 * (float)sc) / 4096;
                break;
            case "DexNav":
                chance = 1.0 / 308;
                break;
            case "Fishing":
                int chain;
                if (count > 20) chain = 20;
                else            chain = count;
                chance = (1.0 + 2 * (float)sc + 2 * (float)chain) / 4096;
                break;

            case "Starter":
                chance = 1.0 / (4096 * (float)lowGen);
                break;
            case "SOS":
                if (count % 255 < 70) chance = (1.0 + 2 * (float)sc) / 4096;
                else chance = (1.0 + 2 * sc + 3) / 4096;
                break;
        }
        return "1/" + Integer.toString((int)Math.round(1.0/chance));
    }

    public int getCount(){
        return count;
    }

    public void setMethod(String method){
        this.method = method;
    }

    public String getMethod(){
        return method;
    }

    public int getShinyCharm(){
        return sc;
    }

    public int getMasudaMethod(){
        return mm;
    }

    public void setShinyCharm(int p){
        sc = p;
    }

    public void setmasudaMethod(int p){
        mm = p;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
