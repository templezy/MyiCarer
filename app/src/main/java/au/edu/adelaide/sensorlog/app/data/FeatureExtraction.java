package au.edu.adelaide.sensorlog.app.data;

import java.util.Calendar;


//import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
//import org.apache.commons.math3.transform.DftNormalization;
//import org.apache.commons.math3.transform.FastFourierTransformer;
//import org.apache.commons.math3.transform.TransformType;

/**
 * Created by ligefei on 23/04/2015.
 */
public class FeatureExtraction {

    public double[] mean, absMean, sd, skew, kurtosis, deltaMean, deltaSD, deltaSkew, deltaKurtosis,
            rms, min, max, absMin, absMax, absSD, absSkew, absKurtosis, product2DMean, absProduct2DMean, interquartileRange,
            product3DMean, absProduct3DMean;
    //public double[][] fftFeature;
    private int number = 20;
    public long timestamp;
    private final static char CSV_DELIM = ',';

    public FeatureExtraction(DataBuffer db) {
        DescriptiveStatistics ds;
        //FastFourierTransformer fft=new FastFourierTransformer(DftNormalization.STANDARD);
        int i;
        mean = new double[number];
        absMean = new double[number];
        sd = new double[number];
        skew = new double[number];
        kurtosis = new double[number];
        absSD = new double[number];
        absSkew = new double[number];
        absMean = new double[number];
        absKurtosis = new double[number];
        deltaMean = new double[number];
        deltaSD = new double[number];
        deltaSkew = new double[number];
        deltaKurtosis = new double[number];
        rms = new double[number];
        min = new double[number];
        max = new double[number];
        absMin = new double[number];
        absMax = new double[number];
        interquartileRange = new double[number];
        product2DMean = new double[18];
        absProduct2DMean = new double[18];
        absProduct3DMean = new double[6];
        product3DMean = new double[6];
        //fftFeature = new double[number][20];
        //Complex[] complexes=new Complex[20];
        timestamp = Calendar.getInstance().getTimeInMillis();
        /*for (i = 0; i < 20; ++i) {
            if ((db.arrayFlag >> i & 1) == 1) {
                ds = new DescriptiveStatistics(db.dataset[i]);
                //complexes=fft.transform(db.dataset[i],TransformType.FORWARD);
                //for(j=0;j<20;++j){
                //    fftFeature[i][j]=complexes[j].getReal();
                //}
                mean[i] = ds.getMean();
                sd[i] = ds.getStandardDeviation();
                skew[i] = ds.getSkewness();
                kurtosis[i] = ds.getKurtosis();
                rms[i] = ds.getQuadraticMean();
                max[i] = ds.getMax();
                min[i] = ds.getMin();
                interquartileRange[i] = ds.getPercentile(75) - ds.getPercentile(25);
                ds = new DescriptiveStatistics(db.absDataset[i]);
                absMean[i] = ds.getMean();
                absSD[i] = ds.getStandardDeviation();
                absKurtosis[i] = ds.getKurtosis();
                absMin[i] = ds.getMin();
                absMax[i] = ds.getMax();
                ds = new DescriptiveStatistics(db.differential[i]);
                deltaMean[i] = ds.getMean();
                deltaSD[i] = ds.getStandardDeviation();
                deltaSkew[i] = ds.getSkewness();
                deltaKurtosis[i] = ds.getKurtosis();
            }
        }
        for (i = 0; i < 18; ++i) {
            ds = new DescriptiveStatistics(db.product2D[i]);
            product2DMean[i] = ds.getMean();
            ds = new DescriptiveStatistics(db.absProduct2D[i]);
            absProduct2DMean[i] = ds.getMean();
        }
        for (i = 0; i < 6; ++i) {
            ds = new DescriptiveStatistics(db.product3D[i]);
            product3DMean[i] = ds.getMean();
            ds = new DescriptiveStatistics(db.absProduct3D[i]);
            absProduct3DMean[i] = ds.getMean();
        }*/

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(timestamp);
        int i;
        for (i = 0; i < number; ++i) {
            sb.append(CSV_DELIM).append(mean[i]).append(CSV_DELIM).append(sd[i]).append(CSV_DELIM).append(skew[i])
                    .append(CSV_DELIM).append(kurtosis[i]).append(CSV_DELIM).append(max[i]).append(CSV_DELIM)
                    .append(min[i]).append(CSV_DELIM).append(rms[i]).append(CSV_DELIM).append(absMean[i])
                    .append(CSV_DELIM).append(absSD[i]).append(CSV_DELIM).append(absSkew[i]).append(CSV_DELIM)
                    .append(absKurtosis[i]).append(CSV_DELIM).append(absMax[i]).append(CSV_DELIM).append(absMin[i])
                    .append(CSV_DELIM).append(deltaMean[i]).append(CSV_DELIM).append(deltaSD[i])
                    .append(CSV_DELIM).append(deltaSkew[i]).append(CSV_DELIM).append(deltaKurtosis[i]);
        }
        for (i = 0; i < 18; ++i) {
            sb.append(CSV_DELIM).append(product2DMean[i]).append(CSV_DELIM).append(absProduct2DMean[i]);
        }
        for (i = 0; i < 6; ++i) {
            sb.append(CSV_DELIM).append(product3DMean[i]).append(CSV_DELIM).append(absProduct3DMean[i]);
        }
        return sb.toString();
    }

    public static final String title =
            "Timestamp,AXMean,AXSD,AXSkew,AXKurtosis,AXMax,AXMin,AXRMS,AXAbsMean,AXAbsSD,AXAbsSkew,AXAbsKurtosis,AXAbsMax,AXAbsMin," +
                    "AXDeltaMean,AXDeltaSD,AXDeltaSkew,AXDeltaKurtosis" +
                    ",AYMean,AYSD,AYSkew,AYKurtosis,AYMAY,AYMin,AYRMS,AYAbsMean,AYAbsSD,AYAbsSkew,AYAbsKurtosis,AYAbsMAY,AYAbsMin," +
                    "AYDeltaMean,AYDeltaSD,AYDeltaSkew,AYDeltaKurtosis" +
                    "AZMean,AZSD,AZSkew,AZKurtosis,AZMAZ,AZMin,AZRMS,AZAbsMean,AZAbsSD,AZAbsSkew,AZAbsKurtosis,AZAbsMAZ,AZAbsMin," +
                    "AZDeltaMean,AZDeltaSD,AZDeltaSkew,AZDeltaKurtosis" +
                    ",GYXMean,GYXSD,GYXSkew,GYXKurtosis,GYXMGYX,GYXMin,GYXRMS,GYXAbsMean,GYXAbsSD,GYXAbsSkew,GYXAbsKurtosis,GYXAbsMGYX,GYXAbsMin," +
                    "GYXDeltaMean,GYXDeltaSD,GYXDeltaSkew,GYXDeltaKurtosis" +
                    ",GYYMean,GYYSD,GYYSkew,GYYKurtosis,GYYMGYY,GYYMin,GYYRMS,GYYAbsMean,GYYAbsSD,GYYAbsSkew,GYYAbsKurtosis,GYYAbsMGYY,GYYAbsMin," +
                    "GYYDeltaMean,GYYDeltaSD,GYYDeltaSkew,GYYDeltaKurtosis" +
                    "GYZMean,GYZSD,GYZSkew,GYZKurtosis,GYZMGYZ,GYZMin,GYZRMS,GYZAbsMean,GYZAbsSD,GYZAbsSkew,GYZAbsKurtosis,GYZAbsMGYZ,GYZAbsMin," +
                    "GYZDeltaMean,GYZDeltaSD,GYZDeltaSkew,GYZDeltaKurtosis" +
                    ",MXMean,MXSD,MXSkew,MXKurtosis,MXMMX,MXMin,MXRMS,MXAbsMean,MXAbsSD,MXAbsSkew,MXAbsKurtosis,MXAbsMMX,MXAbsMin," +
                    "MXDeltaMean,MXDeltaSD,MXDeltaSkew,MXDeltaKurtosis" +
                    ",MYMean,MYSD,MYSkew,MYKurtosis,MYMMY,MYMin,MYRMS,MYAbsMean,MYAbsSD,MYAbsSkew,MYAbsKurtosis,MYAbsMMY,MYAbsMin," +
                    "MYDeltaMean,MYDeltaSD,MYDeltaSkew,MYDeltaKurtosis" +
                    "MZMean,MZSD,MZSkew,MZKurtosis,MZMMZ,MZMin,MZRMS,MZAbsMean,MZAbsSD,MZAbsSkew,MZAbsKurtosis,MZAbsMMZ,MZAbsMin," +
                    "MZDeltaMean,MZDeltaSD,MZDeltaSkew,MZDeltaKurtosis" +
                    ",GRXMean,GRXSD,GRXSkew,GRXKurtosis,GRXMGRX,GRXMin,GRXRMS,GRXAbsMean,GRXAbsSD,GRXAbsSkew,GRXAbsKurtosis," +
                    "GRXAbsMGRX,GRXAbsMin," +
                    "GRXDeltaMean,GRXDeltaSD,GRXDeltaSkew,GRXDeltaKurtosis" +
                    ",GRYMean,GRYSD,GRYSkew,GRYKurtosis,GRYMGRY,GRYMin,GRYRMS,GRYAbsMean,GRYAbsSD,GRYAbsSkew,GRYAbsKurtosis," +
                    "GRYAbsMGRY,GRYAbsMin," +
                    "GRYDeltaMean,GRYDeltaSD,GRYDeltaSkew,GRYDeltaKurtosis" +
                    "GRZMean,GRZSD,GRZSkew,GRZKurtosis,GRZMGRZ,GRZMin,GRZRMS,GRZAbsMean,GRZAbsSD,GRZAbsSkew,GRZAbsKurtosis," +
                    "GRZAbsMGRZ,GRZAbsMin," +
                    "GRZDeltaMean,GRZDeltaSD,GRZDeltaSkew,GRZDeltaKurtosis" +
                    ",LAXMean,LAXSD,LAXSkew,LAXKurtosis,LAXMLAX,LAXMin,LAXRMS,LAXAbsMean,LAXAbsSD,LAXAbsSkew,LAXAbsKurtosis,LAXAbsMLAX,LAXAbsMin," +
                    "LAXDeltaMean,LAXDeltaSD,LAXDeltaSkew,LAXDeltaKurtosis" +
                    ",LAYMean,LAYSD,LAYSkew,LAYKurtosis,LAYMLAY,LAYMin,LAYRMS,LAYAbsMean,LAYAbsSD,LAYAbsSkew,LAYAbsKurtosis,LAYAbsMLAY,LAYAbsMin," +
                    "LAYDeltaMean,LAYDeltaSD,LAYDeltaSkew,LAYDeltaKurtosis" +
                    "LAZMean,LAZSD,LAZSkew,LAZKurtosis,LAZMLAZ,LAZMin,LAZRMS,LAZAbsMean,LAZAbsSD,LAZAbsSkew,LAZAbsKurtosis,LAZAbsMLAZ,LAZAbsMin," +
                    "LAZDeltaMean,LAZDeltaSD,LAZDeltaSkew,LAZDeltaKurtosis" +
                    "PMean,PSD,PSkew,PKurtosis,PMP,PMin,PRMS,PAbsMean,PAbsSD,PAbsSkew,PAbsKurtosis,PAbsMP,PAbsMin," +
                    "PDeltaMean,PDeltaSD,PDeltaSkew,PDeltaKurtosis" +
                    ",RXMean,RXSD,RXSkew,RXKurtosis,RXMRX,RXMin,RXRMS,RXAbsMean,RXAbsSD,RXAbsSkew,RXAbsKurtosis,RXAbsMRX,RXAbsMin," +
                    "RXDeltaMean,RXDeltaSD,RXDeltaSkew,RXDeltaKurtosis" +
                    ",RYMean,RYSD,RYSkew,RYKurtosis,RYMRY,RYMin,RYRMS,RYAbsMean,RYAbsSD,RYAbsSkew,RYAbsKurtosis,RYAbsMRY,RYAbsMin," +
                    "RYDeltaMean,RYDeltaSD,RYDeltaSkew,RYDeltaKurtosis" +
                    ",RZMean,RZSD,RZSkew,RZKurtosis,RZMRZ,RZMin,RZRMS,RZAbsMean,RZAbsSD,RZAbsSkew,RZAbsKurtosis,RZAbsMRZ,RZAbsMin," +
                    "RZDeltaMean,RZDeltaSD,RZDeltaSkew,RZDeltaKurtosis" +
                    ",RSMean,RSSD,RSSkew,RSKurtosis,RSMRS,RSMin,RSRMS,RSAbsMean,RSAbsSD,RSAbsSkew,RSAbsKurtosis,RSAbsMRS,RSAbsMin," +
                    "RSDeltaMean,RSDeltaSD,RSDeltaSkew,RSDeltaKurtosis" +
                    ",AX_AY,AbsAX_AY,AY_AZ,AbsAY_AZ,AZ_AX,AbsAZ_AX" +
                    ",GYX_GYY,AbsGYX_GYY,GYY_GYZ,AbsGYY_GYZ,GYZ_GYX,AbsGYZ_GYX" +
                    ",MX_MY,AbsMX_MY,MY_MZ,AbsMY_MZ,MZ_MX,AbsMZ_MX" +
                    ",GRX_GRY,AbsGRX_GRY,GRY_GRZ,AbsGRY_GRZ,GRZ_GRX,AbsGRZ_GRX" +
                    ",LAX_LAY,AbsLAX_LAY,LAY_LAZ,AbsLAY_LAZ,LAZ_LAX,AbsLAZ_LAX" +
                    ",RX_RY,AbsRX_RY,RY_RZ,AbsRY_RZ,RZ_RX,AbsRZ_RX" +
                    ",Axyz,AbsAxyz,GYxyz,AbsGYxyz,Mxyz,AbsMxyz,GRxyz,AbsGRxyz,LAxyz,AbsLAxyz,Rxyz,AbsRxyz";
}
