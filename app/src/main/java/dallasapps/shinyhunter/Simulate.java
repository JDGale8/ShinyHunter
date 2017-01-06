package dallasapps.shinyhunter;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate);

        final Vibrator vibr = (Vibrator)getSystemService(this.VIBRATOR_SERVICE);

        Simulations sim = new Simulations();  // a simulator class is created to run off when buttons are clicked

        final Button runBtn = (Button) findViewById(R.id.rumSim);
        final TextView simResultsText = (TextView) findViewById(R.id.simResultsText);
        final EditText nSim = (EditText) findViewById(R.id.nSimulations);

                /* Called when the user hits the Simulate imageButton - the ditto */
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n;

                String nSims = nSim.getText().toString();

                if(!TextUtils.isEmpty(nSims)){
                    n = Integer.parseInt(nSims);
                }
                else {
                    n = 0;
                }


                String message = sim.egg(n);

                System.out.println(message);

                simResultsText.setText(message);
                vibr.vibrate(28);
            }
        });


    }
}