package au.edu.adelaide.sensorlog.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import au.edu.adelaide.sensorlog.app.data.SensorLogService;


public class MainActivity extends ActionBarActivity {

    private final static String TAG = "MainActivity";

    private static boolean readingSensorData = false;
    private SharedPreferences prefs;
    private Intent serviceIntent;
    private boolean stream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsFromPref();
        displayIP();

        serviceIntent = new Intent(getApplicationContext(), SensorLogService.class);
    }

    private void displayIP() {
        TextView ipText = (TextView) findViewById(R.id.text_ip);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        ipText.setText(ip + ":6000");
    }

    // Set activity radio button from preference
    private void setButtonsFromPref() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sensingActivity = prefs.getString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_other));
        RadioButton radioButton;
        if (sensingActivity.equals(getString(R.string.pref_activity_walk))) {
            radioButton = (RadioButton) findViewById(R.id.radio_walk);
        } else if (sensingActivity.equals(getString(R.string.pref_activity_sit))) {
            radioButton = (RadioButton) findViewById(R.id.radio_sit);
        } else if (sensingActivity.equals(getString(R.string.pref_activity_fall))) {
            radioButton = (RadioButton) findViewById(R.id.radio_fall);
        } else {
            radioButton = (RadioButton) findViewById(R.id.radio_other);
        }
        radioButton.setChecked(true);

        stream = prefs.getBoolean(getString(R.string.pref_stream), false);
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle_stream);
        toggleButton.setChecked(stream);
    }

    public void onStreamButtonClicked(View view) {
        stream = ((ToggleButton) view).isChecked();
        SharedPreferences.Editor editor = prefs.edit();
        if (stream) {
            editor.putBoolean(getString(R.string.pref_stream), true);
        } else {
            editor.putBoolean(getString(R.string.pref_stream), false);
        }
        editor.apply();
    }

    // Set activity preference when radio button clicked
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        SharedPreferences.Editor editor = prefs.edit();
        switch (view.getId()) {
            case R.id.radio_walk:
                if (checked) {
                    editor.putString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_walk));
                }
                break;
            case R.id.radio_sit:
                if (checked) {
                    editor.putString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_sit));
                }
                break;
            case R.id.radio_fall:
                if (checked) {
                    editor.putString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_fall));
                }
                break;
            case R.id.radio_other:
                if (checked) {
                    editor.putString(getString(R.string.pref_activity_label), getString(R.string.pref_activity_other));
                }
                break;
        }
        editor.apply();
    }

    private void enableButtons(boolean enable) {
        RadioButton radioButton;
        radioButton = (RadioButton) findViewById(R.id.radio_walk);
        radioButton.setEnabled(enable);
        radioButton = (RadioButton) findViewById(R.id.radio_sit);
        radioButton.setEnabled(enable);
        radioButton = (RadioButton) findViewById(R.id.radio_fall);
        radioButton.setEnabled(enable);
        radioButton = (RadioButton) findViewById(R.id.radio_other);
        radioButton.setEnabled(enable);

        ToggleButton toggleButton;
        toggleButton = (ToggleButton) findViewById(R.id.toggle_stream);
        toggleButton.setEnabled(enable);
    }

    public void switchSensing(final View view) {
        Button sensingButton = (Button) view;
        if (readingSensorData) {
            Log.v(TAG, "Stream: " + stream);
            if (!stream) {
                SaveDialogFragment newFragment = new SaveDialogFragment();
                newFragment.show(getFragmentManager(), "savefile");
            } else {
                stopService(serviceIntent);
            }
            enableButtons(true);
        } else {
//            logManager.startReadingSensorData();
            startService(serviceIntent);
            enableButtons(false);
        }
        switchButtonText(sensingButton);
    }

    private void switchButtonText(final Button sensingButton) {
        readingSensorData = !readingSensorData;
        if (readingSensorData) {
            sensingButton.setText("Stop");
        } else {
            sensingButton.setText("Start");
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
////        stopReadingSensorData();
//        if (readingSensorData) {
//            logManager.stopReadingSensorData(null);
//            switchButtonText((Button) findViewById(R.id.sensing_button));
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_file) {
            startActivity(new Intent(this, FileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
