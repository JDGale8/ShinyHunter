package dallasapps.shine;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import dallasapps.shine.R;
import dallasapps.shine.stats.Simulations;
import dallasapps.shine.utils.Utils;

public class Simulate extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulate);

        final Vibrator vibr             = (Vibrator)getSystemService(this.VIBRATOR_SERVICE);

        final Simulations sim           = new Simulations();  // a simulator class is created to run off when buttons are clicked

        final Button runBtn             = (Button) findViewById(R.id.runSimBtn);
        final TextView simResultsText   = (TextView) findViewById(R.id.simResultsText);
        final EditText nSim             = (EditText) findViewById(R.id.nSimulations);


        // call radio buttons and checkboxes

        final CheckBox scChk = (CheckBox) findViewById(R.id.scChk);
        final CheckBox mmChk = (CheckBox) findViewById(R.id.mmChk);
        final CheckBox fsChk = (CheckBox) findViewById(R.id.fsChk);

        final RadioGroup lRadGrp = (RadioGroup) findViewById(R.id.leftRadGrp);
        final RadioGroup mRadGrp = (RadioGroup) findViewById(R.id.midRadGrp);
        final RadioGroup rRadGrp = (RadioGroup) findViewById(R.id.rightRadGrp);

        final RadioButton REnRad = (RadioButton) findViewById(R.id.RERadio);
        final RadioButton eggRad = (RadioButton) findViewById(R.id.eggRadio);
        final RadioButton hrdRad = (RadioButton) findViewById(R.id.hordeRadio);
        final RadioButton SRsRad = (RadioButton) findViewById(R.id.SRRadio);
        final RadioButton strRad = (RadioButton) findViewById(R.id.starterRadio);
        final RadioButton fshRad = (RadioButton) findViewById(R.id.fishRadio);
        final RadioButton SOSRad = (RadioButton) findViewById(R.id.SOSRadio);
        final RadioButton dexRad = (RadioButton) findViewById(R.id.dexRadio);

        REnRad.toggle();


        // set the member variables for shiny charm, masuda etc when clicking the checkboxes

        scChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (scChk.isChecked()){
                    sim.sc = 1;
                }
                else{
                    sim.sc = 0;
                }
            }
        });

        mmChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mmChk.isChecked()){
                    sim.mm = 1;
                }
                else{
                    sim.mm = 0;
                }
            }
        });

        fsChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fsChk.isChecked()){
                    sim.fs = 1;
                }
                else{
                    sim.fs = 0;
                }
            }
        });


        /* Called when the user hits run */
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "MESSAGE NOT SET";
                int n;

                String nSims = nSim.getText().toString();

                if(!TextUtils.isEmpty(nSims)) {
                    n = Integer.parseInt(nSims);

                    // here put all the conditionals for the radio buttons
                    // if eggRadio.isChecked() { etc
                    // every time a check box (sc, mm, fs etc) is pressed. Update details of Simulations and Simulations.CumChance
                    if (REnRad.isChecked()) {
                        message = sim.RE(n);
                    } else if (eggRad.isChecked()) {
                        message = sim.egg(n);
                    } else if (hrdRad.isChecked()) {
                        message = sim.horde(n);
                    } else if (SRsRad.isChecked()) {
                        message = sim.SR(n);
                    } else if (strRad.isChecked()) {
                        message = sim.starter(n);
                    } else if (fshRad.isChecked()) {
                        message = sim.fish(n);
                    } else if (SOSRad.isChecked()) {
                        message = sim.SOS(n);
                    } else if (dexRad.isChecked()) {
                        message = sim.dex(n);
                    }
                }
                else{
                    message = getString(R.string.defaultSimText);
                }

                simResultsText.setText(message);
                vibr.vibrate(28);
            }
        });

        nSim.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String message = "MESSAGE NOT SET";
                    int n;

                    String nSims = nSim.getText().toString();

                    if(!TextUtils.isEmpty(nSims)) {
                        n = Integer.parseInt(nSims);

                        // here put all the conditionals for the radio buttons
                        // if eggRadio.isChecked() { etc
                        // every time a check box (sc, mm, fs etc) is pressed. Update details of Simulations and Simulations.CumChance
                        if (REnRad.isChecked()) {
                            message = sim.RE(n);
                        } else if (eggRad.isChecked()) {
                            message = sim.egg(n);
                        } else if (hrdRad.isChecked()) {
                            message = sim.horde(n);
                        } else if (SRsRad.isChecked()) {
                            message = sim.SR(n);
                        } else if (strRad.isChecked()) {
                            message = sim.starter(n);
                        } else if (fshRad.isChecked()) {
                            message = sim.fish(n);
                        } else if (SOSRad.isChecked()) {
                            message = sim.SOS(n);
                        } else if (dexRad.isChecked()) {
                            message = sim.dex(n);
                        }
                    }
                    else{
                        message = getString(R.string.defaultSimText);
                    }

                    simResultsText.setText(message);
                    vibr.vibrate(28);
                    return true;
                }
                return false;
            }
        });


        // clear the other radio groups if one is selected
        REnRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                mRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });
        SRsRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                mRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });
        SOSRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                mRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });




        eggRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                lRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });
        strRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                lRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });
        dexRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                lRadGrp.clearCheck();
                rRadGrp.clearCheck();
            }
        });



        hrdRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                lRadGrp.clearCheck();
                mRadGrp.clearCheck();
            }
        });
        fshRad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // clear the other two radio groups
                lRadGrp.clearCheck();
                mRadGrp.clearCheck();
            }
        });


        // hides soft keyboard when editText's focus is changed
        nSim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(nSim);
                }
            }
        });




    }

    private void hideKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}