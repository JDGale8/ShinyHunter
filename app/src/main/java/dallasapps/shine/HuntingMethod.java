package dallasapps.shine;

import java.util.ArrayList;

/**
 * Created by Jake on 1/10/2017.
 *
 */
public class HuntingMethod {
    private String method;
    private    int icon;

    public HuntingMethod(String m, int i){

        this.method =  m;
        this.icon = i;
    }

    public String getMethod(){
        return method;
    }
    public int getIcon(){
        return icon;
    }


    public static ArrayList<HuntingMethod> createHuntingList(){

        // BUILD THE LIST OF HUNTING METHODS
        final ArrayList<HuntingMethod> huntingList = new ArrayList<>();

        huntingList.add(new HuntingMethod("RE", R.drawable.tallgrass_icon));
        huntingList.add(new HuntingMethod("Egg", R.drawable.egg_icon));
        huntingList.add(new HuntingMethod("Horde", R.drawable.horde_icon));
        huntingList.add(new HuntingMethod("SR", R.drawable.sr_icon));
        huntingList.add(new HuntingMethod("Starter", R.drawable.starter_icon));
        huntingList.add(new HuntingMethod("Fishing", R.drawable.fishing_icon));
        huntingList.add(new HuntingMethod("SOS", R.drawable.sos_icon));
        huntingList.add(new HuntingMethod("DexNav", R.drawable.dex_icon));

        return huntingList;

    }

    public static int getMethodPos(String method, int gen){

        switch (method){
            case "RE":
                return 0;
            case "Egg":
                return 1;
            case "Horde":
                return 2;
            case "SR":
                return 3;
            case "Starter":
                return 4;
            case "Fishing":
                return 5;
            case "SOS":
                return 6;
            case "DexNav":
                return 7;
            default:
                return 0;
        }
    }

}
