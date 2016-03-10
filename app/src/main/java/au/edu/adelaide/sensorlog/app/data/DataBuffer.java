package au.edu.adelaide.sensorlog.app.data;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by ligefei on 18/04/2015.
 */
public class DataBuffer {

    //public enum Label{AX,AY,AZ,GYX,GYY,GYZ,MX,MY,MZ,GRX,GRY,GRZ,LAX,LAY,P,RX,RY,RZ,RS};
    //public float ax,ay,az,gyx,gyy,gyz,mx,my,mz,grx,gry,grz,lax,lay,laz,p,rx,ry,rz,rs;
    public final double[] values;
    private int bufferFlag;
    public int arrayFlag, sensorFlag;
    //For bufferFlag, 0~6 positions are for the sensors.
    //7th position is whether the dataset more than one record.
    //8th position is whether the dataset contain 20 records
    //public long timestamp;
    private int size = 20;
    public final int number = 20;
    private int index;
    //public ArrayDeque<float[]> dataset;
    public ArrayDeque<Long> timeRecord;
    public ArrayDeque<double[]> dataRecord;
    public String lastRecord;
    //public double[][] differential,absDataset,product2D, product3D, absProduct2D, absProduct3D;

    public ArrayDeque<FeatureExtraction> featureList;
    public double[][] dataset;


    public DataBuffer(int sf) {
        bufferFlag = 0;
        sensorFlag = sf;
        values = new double[20]; // Todo: Not hardcode the size
        dataRecord = new ArrayDeque<double[]>();
        timeRecord = new ArrayDeque<Long>();
        //differential = new double[number][size - 1];
        dataset = new double[number][size];
        //absDataset = new double[number][size];
        //product2D = new double[18][size];
        //product3D = new double[6][size];
        //absProduct2D = new double[18][size];
        //absProduct3D = new double[6][size];
        featureList = new ArrayDeque<FeatureExtraction>();
        index = 0;
        for (int i = 0; i < 6; ++i) {
            if ((sensorFlag >> i & 1) == 1) {
                arrayFlag |= 7 << (i * 3);
            }
        }
        if ((sensorFlag >> 6 & 1) == 1) {
            arrayFlag |= 1 << 19;
        }
    }

    public boolean updateDataBuffer(SensorEvent event) {
        synchronized (values) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER://0
                    values[Label.AX.ordinal()] = event.values[0];
                    values[Label.AY.ordinal()] = event.values[1];
                    values[Label.AZ.ordinal()] = event.values[2];
                    bufferFlag |= 1;
                    break;
                case Sensor.TYPE_GYROSCOPE://1
                    values[Label.GYX.ordinal()] = event.values[0];
                    values[Label.GYY.ordinal()] = event.values[1];
                    values[Label.GYZ.ordinal()] = event.values[2];
                    bufferFlag |= 2;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD://2
                    values[Label.MX.ordinal()] = event.values[0];
                    values[Label.MY.ordinal()] = event.values[1];
                    values[Label.MZ.ordinal()] = event.values[2];
                    bufferFlag |= 4;
                    break;
                case Sensor.TYPE_GRAVITY://3
                    values[Label.GRX.ordinal()] = event.values[0];
                    values[Label.GRY.ordinal()] = event.values[1];
                    values[Label.GRZ.ordinal()] = event.values[2];
                    bufferFlag |= 8;
                    break;
                case Sensor.TYPE_LINEAR_ACCELERATION://4
                    values[Label.LAX.ordinal()] = event.values[0];
                    values[Label.LAY.ordinal()] = event.values[1];
                    values[Label.LAZ.ordinal()] = event.values[2];
                    bufferFlag |= 16;
                    break;
                case Sensor.TYPE_PRESSURE://6
                    values[Label.P.ordinal()] = event.values[0];
                    bufferFlag |= 64;
                    break;
                case Sensor.TYPE_ROTATION_VECTOR://5
                    values[Label.RX.ordinal()] = event.values[0];
                    values[Label.RY.ordinal()] = event.values[1];
                    values[Label.RZ.ordinal()] = event.values[2];
                    if (event.values.length == 4) {
                        values[Label.RS.ordinal()] = event.values[3];
                    }
                    bufferFlag |= 32;
                    break;
                default:
                    return false;
            }
        }
        if (bufferFlag == sensorFlag) {
            bufferFlag |= 128;
            return true;
        }
        return false;
    }

    public synchronized String updateDataset() {
        //DataBuffer db=new DataBuffer(dataBuffer);
        Long time = Calendar.getInstance().getTimeInMillis();
        timeRecord.add(time);
        int i;
        double tmp;
        for (i = 0; i < number; ++i) {
            tmp = values[i];
            dataset[i][index] = tmp;
            //absDataset[i][index] = Math.abs(tmp);
        }
        /*if ((bufferFlag >> 7 & 1) == 1) {
            int previous = (index + 19) % 20;
            int differential_index = (index + 18) % 19;
            for (i = 0; i < 20; ++i) {
                differential[i][differential_index] = dataset[i][previous] - dataset[i][index];
            }
        }*/
        dataRecord.add(Arrays.copyOf(values, values.length));
        lastRecord = joinStr(time, values);
        bufferFlag |= (1 << 7);
        //updateProduct();
        index++;
        /*if ((bufferFlag >> 8 & 1) == 0 && index == 20) {
            bufferFlag |= (1 << 8);
        }
        if ((bufferFlag >> 8 & 1) == 1) {
            featureList.add(new FeatureExtraction(this));
        }*/
        index %= 20;
        return lastRecord;
    }

    private String joinStr(long time, double[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        for (int i = 0; i < number; i++) {
            sb.append(',').append(values[i]);
        }
        return sb.toString();
    }

    /*private synchronized void updateProduct() {
        int index1, index2, index3, index_2D = 0, index_3D = 0;
        if ((sensorFlag & 1) == 1) {
            index1 = Label.AX.ordinal();
            index2 = Label.AY.ordinal();
            index3 = Label.AZ.ordinal();
            index_2D = updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        } else {
            index_2D += 3;
        }
        ++index_3D;
        if ((sensorFlag >> 1 & 1) == 1) {
            index1 = Label.GYX.ordinal();
            index2 = Label.GYY.ordinal();
            index3 = Label.GYZ.ordinal();
            index_2D = updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        } else {
            index_2D += 3;
        }
        ++index_3D;
        if ((sensorFlag >> 2 & 1) == 1) {
            index1 = Label.MX.ordinal();
            index2 = Label.MY.ordinal();
            index3 = Label.MZ.ordinal();
            index_2D = updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        } else {
            index_2D += 3;
        }
        ++index_3D;
        if ((sensorFlag >> 3 & 1) == 1) {
            index1 = Label.GRX.ordinal();
            index2 = Label.GRY.ordinal();
            index3 = Label.GRZ.ordinal();
            index_2D = updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        } else {
            index_2D += 3;
        }
        ++index_3D;
        if ((sensorFlag >> 4 & 1) == 1) {
            index1 = Label.LAX.ordinal();
            index2 = Label.LAY.ordinal();
            index3 = Label.LAZ.ordinal();
            index_2D = updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        } else {
            index_2D += 3;
        }
        ++index_3D;
        if ((sensorFlag >> 5 & 1) == 1) {
            index1 = Label.RX.ordinal();
            index2 = Label.RY.ordinal();
            index3 = Label.RZ.ordinal();
            updataProductSubFuntion(index1, index2, index3, index_2D, index_3D);
        }
    }*/

    /*private synchronized int updataProductSubFuntion(int index1, int index2, int index3, int index_2D, int index_3D) {
        double tmp;
        //1*2
        tmp = dataset[index1][index] * dataset[index2][index];
        product2D[index_2D][index] = tmp;
        absProduct2D[index_2D][index] = Math.abs(tmp);
        ++index_2D;
        //1*2*3
        tmp *= dataset[index3][index];
        product3D[index_3D][index] = tmp;
        absProduct3D[index_3D][index] = Math.abs(tmp);
        //2*3
        tmp = dataset[index2][index] * dataset[index3][index];
        product2D[index_2D][index] = tmp;
        absProduct2D[index_2D][index] = Math.abs(tmp);
        ++index_2D;
        //3*1
        tmp = dataset[index3][index] * dataset[index1][index];
        product2D[index_2D][index] = tmp;
        absProduct2D[index_2D][index] = Math.abs(tmp);
        ++index_2D;
        return index_2D;

    }*/
}
