import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

public class FeatureVector implements Serializable
{

    public float vector[];
    public String name;
    static final long serialNumber = 0;

    public FeatureVector(String name, float vector[])
    {
        this.vector = vector;
        this.name = name;
    }

    public FeatureVector(String name, byte vector[])
    {
        float fv[] = new float[vector.length];
        for(int i = 0; i < vector.length; i++)
            fv[i] = vector[i];

        this.vector = fv;
        this.name = name;
    }

    public static float[][] converttoMatrix(Vector features)
    {
        float matrix[][] = new float[features.size()][];
        int i = 0;
        for(Enumeration e = features.elements(); e.hasMoreElements();)
            matrix[i++] = (float[])(float[])e.nextElement();

        return matrix;
    }

    public static Vector featureAdd(Vector features, FeatureVector newfeatures[])
    {
        for(int i = 0; i < newfeatures.length; i++)
            features.addElement(newfeatures[i]);

        return features;
    }

    public static Vector featureAdd(Vector features, FeatureVector newfeature)
    {
        features.addElement(newfeature);
        return features;
    }

    public static Vector featureAdd(Vector features, Vector newfeatures)
    {
        for(Enumeration e = newfeatures.elements(); e.hasMoreElements(); features.addElement(e.nextElement()));
        return features;
    }

    public static String[] toString(Vector features)
    {
        String sa[] = new String[features.size()];
        int i = 0;
        for(Enumeration e = features.elements(); e.hasMoreElements();)
            sa[i++] = (String)e.nextElement();

        return sa;
    }
}
