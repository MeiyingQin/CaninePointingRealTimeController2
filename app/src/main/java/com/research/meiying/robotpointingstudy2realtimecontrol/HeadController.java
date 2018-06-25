package com.research.meiying.robotpointingstudy2realtimecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class HeadController extends AppCompatActivity {
    private boolean isEnabled = false;
    private int seekBarValue = 100;
    private static final int barRange = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_controller);

        SeekBar seekBar = findViewById(R.id.headControlSeekBar);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBarValue = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                RobotCommand robotCommand = new RobotCommand();
                String command = getString(R.string.set_head_angle) + getString(R.string.deliminator) + String.valueOf(barValueToIntended(seekBarValue));
                robotCommand.sendInfoViaSocket(command);
            }
        });

        syncSeekBar();
    }

    private int barValueToIntended(int bar) {
        return bar - barRange / 2;
    }

    private int intendedToBarValue(int intended) {
        return intended + barRange / 2;
    }

    private void syncSeekBar() {
        SeekBar seekBar = findViewById(R.id.headControlSeekBar);
        RobotCommand robotCommand = new RobotCommand();
        seekBarValue = intendedToBarValue(Integer.valueOf(robotCommand.sendInfoViaSocket(getString(R.string.get_head_angle))));
        seekBar.setProgress(seekBarValue);
    }

    public void onCheckboxClicked(View view) {
        isEnabled = ((CheckBox) view).isChecked();
        SeekBar seekBar = findViewById(R.id.headControlSeekBar);

        if (isEnabled) {
            syncSeekBar();
        }

        seekBar.setEnabled(isEnabled);
    }
}
