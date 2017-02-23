package dallasapps.shine;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LaunchPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        final Vibrator vibr = (Vibrator)getSystemService(this.VIBRATOR_SERVICE);

        final ImageButton loadSimulateBtn = (ImageButton) findViewById(R.id.simulateBtn);
        final ImageButton loadTrackBtn = (ImageButton) findViewById(R.id.pokedexBtn);
        final ImageButton loadHoF       = (ImageButton) findViewById(R.id.HoFBtn);

        /* Called when the user hits the Simulate imageButton - the ditto */
        loadSimulateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent simulateIntent = new Intent(LaunchPage.this, Simulate.class);
                startActivity(simulateIntent);
                vibr.vibrate(28);
            }
        });

        /* Called when the user clicks the Track imageButton - the Pokedex */
        loadTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent simulateIntent = new Intent(LaunchPage.this, Track.class);
                startActivity(simulateIntent);
                vibr.vibrate(28);
            }
        });

        loadHoF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HoFIntent = new Intent(LaunchPage.this, HallOfFame.class);
                startActivity(HoFIntent);
                vibr.vibrate(28);
            }
        });

    }
}
