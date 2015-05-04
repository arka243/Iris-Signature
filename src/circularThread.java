import org.eclipse.swt.widgets.Display;


public class circularThread extends Thread {

	
	private int width;
	private int height;
	private int radius;
	private int[] orig;
	private int threadnum ;
	private IrisSignature irisRecognition ;
	public int val = -1 ;

	public circularThread(int[] orig,int width,int height,int radius, int threadnum, IrisSignature irisRecognition){
		this.orig = orig ;
		this.width = width ;
		this.height = height ;
		this.radius = radius ;
		this.threadnum = threadnum ;
		this.irisRecognition = irisRecognition ;
	}
	
	public void run() {
		CircularHoughTransform circHobj = new CircularHoughTransform();
		circHobj.Initialize(orig,width,height,radius);
		circHobj.setLines(1);
		circHobj.Transform();
		val = circHobj.value ;
		System.out.println("Thread num "+threadnum+" val ="+val+" radius = "+circHobj.r);
		Display.getDefault().asyncExec(new Runnable() {
		 public void run() {
			 irisRecognition.threadEnded();
		 }
		});
	}

}
