package dallasapps.shine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dallasapps.shine.HuntingMethod;
import dallasapps.shine.R;

/**
 * Created by Jake on 2/15/2017.
 *
 * Adapter for the Method spinner
 *
 */
// Creating an Adapter Class
public class MethodSpinnerAdapter extends ArrayAdapter<HuntingMethod> {
    private Context context;
    List<HuntingMethod> methodList;

    public MethodSpinnerAdapter(Context context, int textViewResourceId, ArrayList<HuntingMethod> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;

        this.methodList = objects;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        // Inflating the layout for the custom Spinner
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.spinner_row, parent, false);

        // Declaring and Typecasting the textview in the inflated layout
        TextView methodText = (TextView) layout.findViewById(R.id.spinnerText);

        // Setting the text using the array
        HuntingMethod method = methodList.get(position);
        methodText.setText(method.getMethod());

        // Declaring and Typecasting the imageView in the inflated layout
        ImageView icon = (ImageView) layout.findViewById(R.id.spinnerImage);

        // Setting an image using the id's in the array
        icon.setImageResource(method.getIcon());

        return layout;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
