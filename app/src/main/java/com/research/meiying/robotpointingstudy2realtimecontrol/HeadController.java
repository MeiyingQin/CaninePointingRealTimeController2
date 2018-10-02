package com.research.meiying.robotpointingstudy2realtimecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

        setUpWalkButtons(R.id.walkForwardButton, R.string.robot_command_walk_forward);
        setUpWalkButtons(R.id.walkBackwardButton, R.string.robot_command_walk_backward);
        setUpWalkButtons(R.id.turnLeftButton, R.string.robot_command_turn_left);
        setUpWalkButtons(R.id.turnRightButton, R.string.robot_command_turn_right);
        setUpWalkButtons(R.id.walkLeftButton, R.string.robot_command_walk_left);
        setUpWalkButtons(R.id.walkRightButton, R.string.robot_command_walk_right);

        enableWalkButtons(false);
    }

    private void setUpWalkButtons(int viewId, final int walkType) {
        Button walkButton = findViewById(viewId);
        walkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new RobotCommand().sendInfoViaSocket(getString(walkType));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        new RobotCommand().sendInfoViaSocket(getString(R.string.robot_command_walk_stop));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    private void enableWalkButtons(boolean isEnabled) {
        findViewById(R.id.walkForwardButton).setEnabled(isEnabled);
        findViewById(R.id.walkBackwardButton).setEnabled(isEnabled);
        findViewById(R.id.turnLeftButton).setEnabled(isEnabled);
        findViewById(R.id.turnRightButton).setEnabled(isEnabled);
        findViewById(R.id.walkLeftButton).setEnabled(isEnabled);
        findViewById(R.id.walkRightButton).setEnabled(isEnabled);
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

    public void onWalkCheckboxClicked(View view) {
        isEnabled = ((CheckBox) view).isChecked();
        enableWalkButtons(isEnabled);

        if (isEnabled) {
            new RobotCommand().sendInfoViaSocket(getString(R.string.robot_command_walk_init));
        } else {
            new RobotCommand().sendInfoViaSocket(getString(R.string.robot_command_stand));
        }
    }

    public void onDispenserRotateButtonClicked(View view){
        new RobotCommand().sendInfoViaSocket(getString(R.string.robot_command_rotate_dispenser));
    }

    public void onRefreshDispenserButtonClicked(View view){
        new RobotCommand().sendInfoViaSocket(getString(R.string.robot_command_refresh_dispenser));
    }
}
