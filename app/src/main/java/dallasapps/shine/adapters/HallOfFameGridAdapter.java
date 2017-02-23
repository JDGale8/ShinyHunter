package dallasapps.shine.adapters;

/**
 * Created by Jake on 2/21/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dallasapps.shine.Pokemon;
import dallasapps.shine.R;


/**
 * Created by Jake on 2/15/2017.
 */
public class HallOfFameGridAdapter extends ArrayAdapter<Pokemon> implements Filterable, SectionIndexer {
    private Context context;
    private     int layoutResourceId;

    public  ArrayList<Pokemon> filteredPokemonList;
    public  ArrayList<Pokemon> originalPokemonList;

    private ItemFilter mFilter = new ItemFilter();

    private HashMap<String, Integer> numberIndexer;

    public HallOfFameGridAdapter(Context context, int layoutResourceId, ArrayList<Pokemon> objects) {
        super(context, layoutResourceId, objects);

        this.context             = context;
        this.layoutResourceId    = layoutResourceId;
        filteredPokemonList      = objects;
        originalPokemonList      = objects;

        numberIndexer = new HashMap<String, Integer>();
        int size = objects.size();
    }

    public int getCount() {
        return filteredPokemonList.size();
    }

    public Pokemon getItem(int position) {
        return filteredPokemonList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        PokemonHolder holder;

        Pokemon pokemon = filteredPokemonList.get(position);

        if(rowView == null){
            LayoutInflater vi;
            vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(layoutResourceId, null);

            holder          = new PokemonHolder();
            holder.name     = (TextView)rowView.findViewById(R.id.pokemonNameList);
            holder.sprite   = (ImageView)rowView.findViewById(R.id.pokemonIconList);

            rowView.setTag(holder);
        }
        else holder = (PokemonHolder)rowView.getTag();

        holder.name.setText(pokemon.getName());
        holder.sprite.setImageResource(pokemon.getSpriteId());


        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }



    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            final List<Pokemon> list = originalPokemonList;

            int count = list.size();
            final ArrayList<Pokemon> nlist = new ArrayList<Pokemon>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredPokemonList = (ArrayList<Pokemon>) results.values;
            notifyDataSetChanged();
        }
    }

    // ############################################################################################
    //                                      SECTION HANDLING - UNFINISHED
    // ############################################################################################
    private String[] segments = {"#000-#050", "#050 - #100", "#100 - #150", "#150 - #200", "#200 - #250",
            "#250 - #300", "#300 - #350", "#350 - #400", "#400 - #450", "#450 - #500", "#500 - #550",
            "#550 - #600", "#600 - #650", "#650 - #700", "#700 - #750", "#750 - #800"};


    @Override
    public String[] getSections() {
        return segments;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return numberIndexer.get(segments[position]);
    }

    static class PokemonHolder
    {
        ImageView   sprite;
        TextView    name;
    }
}

