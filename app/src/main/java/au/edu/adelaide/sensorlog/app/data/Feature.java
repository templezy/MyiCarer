package au.edu.adelaide.sensorlog.app.data;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by ligefei on 19/04/2015.
 */
public class Feature {

    public float[] mean, absMean, sd, skew, kurtosis, deltaMean, deltaSD, deltaSkew, deltaKurtosis,
            rms, min, max, absMin, absMax, product, absProduct,interquartileRange;
    private int number = 20, size;
    private float f_size;
    public long timestamp;
    private final static char CSV_DELIM = ',';

    /*public Feature(int s, DataBuffer db) {
        Iterator<float[]> it_data = db.dataset.iterator(), it_diff = db.differential.iterator();
        int i, j;
        float tmp1, tmp2, tmp3;
        mean = new float[number];
        absMean = new float[number];
        sd = new float[number];
        skew = new float[number];
        kurtosis = new float[number];
        deltaMean = new float[number];
        deltaSD = new float[number];
        deltaSkew = new float[number];
        deltaKurtosis = new float[number];
        rms = new float[number];
        min = new float[number];
        max = new float[number];
        absMin = new float[number];
        absMax = new float[number];
        interquartileRange=new float[number];
        product = new float[18];
        absProduct = new float[18];
        timestamp= Calendar.getInstance().getTimeInMillis();
        float[] tmpArray;
        float[] tmpArray_diff;
        size = s;
        f_size = s;
        //number: the number of features, such as rx,...az
        //size: the length of deque
        //mean
        tmpArray = it_data.next();
        tmpArray_diff = it_diff.next();
        for (j = 0; j < number; ++j) {
            tmp1 = tmpArray[j];
            tmp2 = Math.abs(tmp1);
            min[j] = tmp1;
            max[j] = tmp1;
            absMin[j] = tmp2;
            absMax[j] = tmp2;
            mean[j] += tmp1;
            rms[j] += tmp1 * tmp1;
            absMean[j] += tmp2;
            deltaMean[j] += tmpArray_diff[j];
        }
        for (i = 1; i < size - 1; ++i) {
            tmpArray = it_data.next();
            tmpArray_diff = it_diff.next();
            for (j = 0; j < number; ++j) {
                tmp1 = tmpArray[j];
                tmp2 = Math.abs(tmp1);
                if (min[j] > tmp1) {
                    min[j] = tmp1;
                } else if (max[j] < tmp1) {
                    max[j] = tmp1;
                }
                if (absMin[j] > tmp2) {
                    absMin[j] = tmp2;
                } else if (absMax[j] < tmp2) {
                    absMax[j] = tmp2;
                }
                mean[j] += tmp1;
                rms[j] += tmp1 * tmp1;
                absMean[j] += tmp2;
                deltaMean[j] += tmpArray_diff[j];
            }
            if(i==3){

            }else if(i==9){

            }
        }
        tmpArray = it_data.next();
        tmp1 = f_size - 1.0f;
        for (i = 0; i < number; ++i) {
            mean[i] += tmpArray[i];
            absMean[i] += Math.abs(tmpArray[i]);
            mean[i] /= f_size;
            absMean[i] /= f_size;
            rms[i] = (float) Math.sqrt(rms[i] / f_size);
            deltaMean[i] /= tmp1;
        }
        //sd,skew,kutosis,product
        it_data = db.dataset.iterator();
        it_diff = db.differential.iterator();
        for (i = 0; i < size - 1; ++i) {
            tmpArray = it_data.next();
            tmpArray_diff = it_diff.next();
            for (j = 0; j < number; ++j) {
                tmp1 = Math.abs(tmpArray[j] - mean[j]);
                tmp2 = tmp1 * tmp1;
                sd[j] += tmp2;
                tmp2 *= tmp1;
                skew[j] += tmp2;
                tmp2 *= tmp1;
                kurtosis[j] += tmp2;
                tmp1 = Math.abs(tmpArray_diff[j] - absMean[j]);
                tmp2 = tmp1 * tmp1;
                deltaSD[j] += tmp2;
                tmp2 *= tmp1;
                deltaSkew[j] += tmp2;
                tmp2 *= tmp1;
                deltaKurtosis[j] += tmp2;
            }
            //AX,AY,AZ
            tmp1 = tmpArray[0] * tmpArray[1];
            product[0] += tmp1;
            absProduct[0] += Math.abs(tmp1);
            tmp1 = tmpArray[1] * tmpArray[2];
            product[1] += tmp1;
            absProduct[1] += Math.abs(tmp1);
            tmp1 = tmpArray[2] * tmpArray[0];
            product[2] += tmp1;
            absProduct[2] += Math.abs(tmp1);
            tmp1 = tmpArray[3] * tmpArray[4];
            //GYX,GYY,GYZ
            product[3] += tmp1;
            absProduct[3] += Math.abs(tmp1);
            tmp1 = tmpArray[4] * tmpArray[5];
            product[4] += tmp1;
            absProduct[4] += Math.abs(tmp1);
            tmp1 = tmpArray[5] * tmpArray[3];
            product[5] += tmp1;
            absProduct[5] += Math.abs(tmp1);
            //MX,MY,MZ
            tmp1 = tmpArray[6] * tmpArray[7];
            product[6] += tmp1;
            absProduct[6] += Math.abs(tmp1);
            tmp1 = tmpArray[7] * tmpArray[8];
            product[7] += tmp1;
            absProduct[7] += Math.abs(tmp1);
            tmp1 = tmpArray[8] * tmpArray[6];
            product[8] += tmp1;
            absProduct[8] += Math.abs(tmp1);
            //GRX,GRY,GRZ
            tmp1 = tmpArray[9] * tmpArray[10];
            product[9] += tmp1;
            absProduct[9] += Math.abs(tmp1);
            tmp1 = tmpArray[10] * tmpArray[11];
            product[10] += tmp1;
            absProduct[10] += Math.abs(tmp1);
            tmp1 = tmpArray[11] * tmpArray[9];
            product[11] += tmp1;
            absProduct[11] += Math.abs(tmp1);
            //LAX,LAY,LAZ
            tmp1 = tmpArray[12] * tmpArray[13];
            product[12] += tmp1;
            absProduct[12] += Math.abs(tmp1);
            tmp1 = tmpArray[13] * tmpArray[14];
            product[13] += tmp1;
            absProduct[13] += Math.abs(tmp1);
            tmp1 = tmpArray[14] * tmpArray[12];
            product[14] += tmp1;
            absProduct[14] += Math.abs(tmp1);
            //RX,RY,RZ
            tmp1 = tmpArray[16] * tmpArray[17];
            product[15] += tmp1;
            absProduct[15] += Math.abs(tmp1);
            tmp1 = tmpArray[17] * tmpArray[18];
            product[16] += tmp1;
            absProduct[16] += Math.abs(tmp1);
            tmp1 = tmpArray[18] * tmpArray[16];
            product[17] += tmp1;
            absProduct[17] += Math.abs(tmp1);
        }
        for (j = 0; j < number; ++j) {
            tmp1 = Math.abs(db.values[j] - mean[j]);
            tmp2 = tmp1 * tmp1;
            sd[j] += tmp2;
            tmp2 *= tmp1;
            skew[j] += tmp2;
            tmp2 *= tmp1;
            kurtosis[j] += tmp2;
        }
        tmp1 = MathExtension.InvSqrt(f_size);
        tmp3 = MathExtension.InvSqrt(f_size - 1.0f);
        for (j = 0; j < number; ++j) {
            sd[j] *= tmp1;
            tmp2 = sd[j] * sd[j] * sd[j];
            skew[j] *= MathExtension.InvSqrt(tmp2);
            tmp2 *= sd[j];
            kurtosis[j] *= MathExtension.InvSqrt(tmp2);
            deltaSD[j] *= tmp3;
            tmp2 = deltaSD[j] * deltaSD[j] * deltaSD[j];
            deltaSkew[j] *= MathExtension.InvSqrt(tmp2);
            tmp2 *= deltaSD[j];
            deltaKurtosis[j] *= MathExtension.InvSqrt(tmp2);
        }

    }*/

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(timestamp).append(CSV_DELIM);
        for (int i = 0; i < number; ++i) {
            //sb.append(mean[i]
        }
        return sb.toString();
    }
}
