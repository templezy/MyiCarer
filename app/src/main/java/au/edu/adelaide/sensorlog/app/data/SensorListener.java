package au.edu.adelaide.sensorlog.app.data;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.io.*;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by yangjie on 6/04/15.
 */
public class SensorListener implements SensorEventListener {

    private final static String TAG = "SensorListener";
    private final static char CSV_DELIM = ',';
    private static String CSV_HEADER;

    private File folder;
    private String time;
    private DataBuffer dataBuffer;
    private ProcessingThread processingThread;

    public SensorListener(File folder, int sensorFlag, boolean stream) {
        Calendar c = Calendar.getInstance();
        time = c.get(Calendar.HOUR_OF_DAY) + "-" + c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + " "
                + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
        this.folder = folder;
        dataBuffer = new DataBuffer(sensorFlag);
        processingThread = new ProcessingThread(dataBuffer, stream);
        CSV_HEADER = "Timestamp";
        for (Label label : Label.values()) {
            CSV_HEADER += "," + label.name();
        }
    }

    public void stop() {
        processingThread.interrupt();
    }

    public void save(String fileNamePrefix) {
        String fileName;
        if (!fileNamePrefix.isEmpty()) {
            fileName = fileNamePrefix + " " + time + ".csv";
        } else {
            fileName = time + ".csv";
        }
        File dataFile = new File(folder, fileName);
        StringBuffer sb;
        double[] values;
        long timestamp;
        int i, j;
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            printWriter.println(CSV_HEADER);
            Iterator<double[]> it = dataBuffer.dataRecord.iterator();
            Iterator<Long> it_time = dataBuffer.timeRecord.iterator();
            for (i = 0; i < dataBuffer.dataRecord.size(); ++i) {
                values = it.next();
                timestamp = it_time.next();
                sb = new StringBuffer().append(timestamp);
                for (j = 0; j < dataBuffer.number; ++j) {
                    sb.append(CSV_DELIM).append(values[j]);
                }
                printWriter.println(sb.toString());
            }
            printWriter.close();
            /*if (!fileNamePrefix.isEmpty()) {
                fileName = fileNamePrefix + " " + time + "_feature.csv";
            } else {
                fileName = time + "_feature.csv";
            }
            dataFile = new File(folder, fileName);
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
            printWriter.println(FeatureExtraction.title);
            Iterator<FeatureExtraction> it_feature=dataBuffer.featureList.iterator();
            for(i=0;i<dataBuffer.featureList.size();++i){
                tmp=it_feature.next().toString();
                printWriter.println(tmp);
            }
            printWriter.close();*/
        } catch (IOException e) {
            Log.e(TAG, "Error writing CSV file(s)", e);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GYROSCOPE:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_GRAVITY:
            case Sensor.TYPE_LINEAR_ACCELERATION:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_ROTATION_VECTOR:
                break;
            default:
                return;
        }
        if (dataBuffer.updateDataBuffer(event) && !processingThread.isAlive()) {
            processingThread.start();
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
