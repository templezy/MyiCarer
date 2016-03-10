package au.edu.adelaide.sensorlog.app.data;

/**
 * Created by ligefei on 19/04/2015.
 */
public class MathExtension {
    public static float InvSqrt(float x) {
        float xhalf = -0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        x = x * (1.5f - xhalf * x * x);
        return x;
    }

    public static int countBits(int x) {
        x -= ((x >> 1) & 0x55555555);
        x = (x & 0x33333333) + ((x >> 2) & 0x33333333);
        x += (x >> 4);
        x &= 0xF0F0F0F;
        x = (x * 0x01010101) >> 24;
        return x;
    }
}
