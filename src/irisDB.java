import java.util.Vector;


public class irisDB {


	protected static float distance(float [] a, float [] b)
	{
		double d = 0;
		for (int i = 0; i < a.length; i++)
			d += Math.pow(a[i] - b[i], 2);
		return (float) d;
	}
	
	public static String CompareMinkowski(FeatureVector[] currentIris, Vector<FeatureVector[]> irisDb, Vector<String> nameDb) { 
		Vector<Float> distance = new Vector<Float>();
		for(FeatureVector[] iris: irisDb) { 
			float dist = 0f;
			for(int i=0; i<iris.length; i++) { 
				dist = dist + distance(iris[i].vector, currentIris[i].vector);  
			}
			distance.add(dist);
			System.out.println(dist);
		}
		int shortest=0;
		float shortDist=(Float)distance.get(0);
		for(int i=1; i<distance.size(); i++) {
			if((Float)distance.get(i)<shortDist){
				shortest=i;
				shortDist=(Float)distance.get(i);
			}
		}
		return nameDb.get(shortest) + ": " + shortDist;
	}
	
}
