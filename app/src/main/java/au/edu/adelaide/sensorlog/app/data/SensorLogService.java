package au.edu.adelaide.sensorlog.app.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import au.edu.adelaide.sensorlog.app.R;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by yangjie on 20/04/15.
 */
public class SensorLogService extends Service {

    private final static String TAG = "SensorLogService";
    private final static int RATE = SensorManager.SENSOR_DELAY_FASTEST;

    private SensorManager sensorManager;
    private SensorListener listener;

    private SharedPreferences pref;
    private boolean stream;
    private PowerManager.WakeLock mWakeLock = null;

//    public SensorLogService(SensorManager sensorManager, Integer[] sensorTypes, File dir) {
//        this.sensorManager = sensorManager;
//        this.dir = dir;
//        ArrayList<Integer> validSensorTypes = new ArrayList<Integer>();
//        sensorFlag = 0;
//        for (int i = 0; i < sensorTypes.length; ++i) {
//            int tmp = sensorTypes[i];
//            if (sensorManager.getDefaultSensor(sensorTypes[i]) != null) {
//                validSensorTypes.add(tmp);
//                sensorFlag |= 1 << i;
//            }
//        }
//        this.sensorTypes = validSensorTypes.toArray(new Integer[validSensorTypes.size()]);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        File dir = getExternalFilesDir(null);
        Integer[] sensorTypes = new Integer[]{
                Sensor.TYPE_ACCELEROMETER,
                Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_MAGNETIC_FIELD,
                Sensor.TYPE_GRAVITY,
                Sensor.TYPE_LINEAR_ACCELERATION,
                Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_PRESSURE,
//                Sensor.TYPE_STEP_DETECTOR,
        };
        ArrayList<Integer> validSensorTypes = new ArrayList<Integer>();
        int sensorFlag = 0;
        for (int i = 0; i < sensorTypes.length; ++i) {
            int tmp = sensorTypes[i];
            if (sensorManager.getDefaultSensor(sensorTypes[i]) != null) {
                validSensorTypes.add(tmp);
                sensorFlag |= 1 << i;
            }
        }
        sensorTypes = validSensorTypes.toArray(new Integer[validSensorTypes.size()]);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        stream = pref.getBoolean(getString(R.string.pref_stream), false);
        listener = new SensorListener(dir, sensorFlag, stream);
        for (int i = 0; i < sensorTypes.length; i++) {
            sensorManager.registerListener(listener, sensorManager.getDefaultSensor(sensorTypes[i]), RATE);
        }
        mWakeLock.acquire();
        Log.d(TAG, "Started reading sensor data");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        String fileName = pref.getString(getString(R.string.pref_filename), null);
        if (listener != null) {
            sensorManager.unregisterListener(listener);
            listener.stop();
            if (!stream && fileName != null) {
                listener.save(fileName);
            }
            Log.d(TAG, "Stop reading sensor data");
        }
        mWakeLock.release();
        super.onDestroy();
    }

//    public void startReadingSensorData() {
//        listener = new SensorListener(dir, sensorFlag);
//        for (int i = 0; i < sensorTypes.length; i++) {
//            sensorManager.registerListener(listener, sensorManager.getDefaultSensor(sensorTypes[i]), RATE);
//        }
//        Log.d(TAG, "Started reading sensor data");
//    }
//
//    public void stopReadingSensorData(String fileName) {
//        if (listener != null) {
//            sensorManager.unregisterListener(listener);
//            if (fileName != null && !fileName.isEmpty()) {
//                listener.save(fileName);
//            }
//            Log.d(TAG, "Stop reading sensor data");
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private static String getSensorName(int sensorType) {
//        switch (sensorType) {
//            case Sensor.TYPE_ACCELEROMETER:
//                return "accelerometer";
//            case Sensor.TYPE_AMBIENT_TEMPERATURE:
//                return "ambient_temperature";
//            case Sensor.TYPE_GAME_ROTATION_VECTOR:
//                return "game_rotation_vector";
//            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
//                return "geomagnetic_rotation_vector";
//            case Sensor.TYPE_GRAVITY:
//                return "gravity";
//            case Sensor.TYPE_GYROSCOPE:
//                return "gyroscope";
//            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
//                return "gyroscope_uncalibrated";
//            case Sensor.TYPE_LINEAR_ACCELERATION:
//                return "linear_acceleration";
//            case Sensor.TYPE_MAGNETIC_FIELD:
//                return "magnetic_field";
//            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
//                return "magnetic_field_uncalibrated";
//            case Sensor.TYPE_PRESSURE:
//                return "pressure";
//            case Sensor.TYPE_ROTATION_VECTOR:
//                return "rotation_vector";
//            case Sensor.TYPE_LIGHT:
//                return "light";
//            case Sensor.TYPE_PROXIMITY:
//                return "proximity";
//            default:
//                return "undefined";
//        }
//    }
}
