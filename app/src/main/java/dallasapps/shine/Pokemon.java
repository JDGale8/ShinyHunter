package dallasapps.shine;

import android.content.Context;

import java.io.Serializable;

import dallasapps.shine.stats.Counter;

/**
 * Created by Jake on 1/8/2017.
 *
 * describes all the information for a pokemon
 */

public class Pokemon{
    // core info
    private String  name;
    private int     no;
    private String  numberString;

    private String  imageName;
    private String  shinyImageName;
    private int     icon;
    private int     shinyIcon;
    private boolean evolves;
    private boolean devolves;
    private boolean alolan;
    private boolean shiny;
    private boolean local = false;
    private int     gen;


    // discovery information
    private String  discoveryMethod;
    private String  game;

    private String location;

    private Counter counter;



    public Pokemon(String name, int n, int icon){
        this.name       = name;
        this.no         = n;
        this.icon       = icon;
        this.counter    = new Counter();
        fixNumbers(n);
    }

    public void setLocal(boolean local){
        this.local = local;
    }

    public int getNationalNumber(){
        if(!local){
            return no;
        }
        else{
            switch(gen){
                case 1:
                    return no;
                case 2:
                    if(no>151){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+151;
                case 3:
                    if(no>251){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+251;
                case 4:
                    if(no>386){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+386;
                case 5:
                    if(no>494){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+494;
                case 6:
                    if(no>649){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+649;
                case 7:
                    if(no>721){
                        throw new NumberFormatException(String.format("Error: national number called on national pokedex entry, number %s", no));
                    }
                    return no+721;
                default:
                    throw new IllegalArgumentException(String.format("Illegal generation (%s), must be integer <=7.", gen));
            }
        }
    }

    public int getLocalNumber(){
        if(local){
           return no;
        }
        else {
            switch(gen){
                case 1:
                    return no;
                case 2:
                    if(no<152){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-151;
                case 3:
                    if(no<252){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-251;
                case 4:
                    if(no<387){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-386;
                case 5:
                    if(no<495){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-494;
                case 6:
                    if(no<650){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-649;
                case 7:
                    if(no<720){
                        throw new NumberFormatException(String.format("Error: local number called on local pokedex entry, number %s", no));
                    }
                    return no-721;
                default:
                    throw new IllegalArgumentException(String.format("Illegal generation (%s), must be integer <=7.", gen));
            }
        }
    }

    public String getName(){
        return name;
    }

    public String getNameAndNumber(){
        return numberString + " " + name;
    }

    public int getSpriteId(){
        return icon;
    }

    public String getMethod(){
        return this.getCounter().getMethod();
    };

    public String getSpriteName(Context context){
        return context.getResources().getResourceEntryName(icon);
    }


    public void setSprite(Context context, String iconName) {
        icon = context.getResources().getIdentifier(iconName, "drawable", context.getApplicationContext().getPackageName());
    }

    private void fixNumbers(int n) {
        // changes number to the correct pokemon format of #N with trailing 0's up to 3 digits

        if      (n < 10)  this.numberString = "#00" + n;
        else if (n < 100) this.numberString = "#0"  + n;
        else              this.numberString = "#"   + n;
    }

    public void evolve(){
        if(evolves) {
            this.no ++;
            fixNumbers(this.no);
        }
    }

    final public void setMethod(String m){
        this.getCounter().setMethod(m);
    }

    final public void setSc(int sc){
        this.getCounter().setShinyCharm(sc);
    }

    final public void setMm(int mm){
        this.getCounter().setmasudaMethod(mm);
    }

    public void toggleShiny() {
        shiny = !shiny;
    }

    public boolean isShiny() {
        return shiny;
    }

    public Counter getCounter(){
        return counter;
    }

    public int getCount(){
        return counter.getCount();
    }
    public void setCount(int count) {
        counter.setCount(count);
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    @Override
    public String toString() {
        return numberString + " " + name;
    }
}
