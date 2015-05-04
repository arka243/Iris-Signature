import ij.ImagePlus;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.jhlabs.image.PolarFilter;

public class IrisSignature {

	public int orig[] = null;
	int[] threadsout ;
	int[] values ;
	Image image ;
	Vector <Canvas>gabCanvas = new Vector<Canvas>(); 
	Vector <Label>gabLabels = new Vector<Label>(); 
	private circularThread[] thc ;
	private Scanning sobelObject;
	private Suppression nonMaxSuppressionObject;
	private Thresholding histThresholdObject;
	private LinearHoughTransform lineHoughObject;
	private int width ;
	private int height ;
	private Image SobelImage;
	//private Image MaxSuppImage;
	private Image HystImage;
	private Canvas canvasImage3 = null;
	private Canvas canvasImage4 = null;
	private Image OverlayImage;
	//private Image LinesImage;
	private Image HoughAccImage;
	private CircularHoughTransform houghCircle;
	private int[] origFiltered;
	private Image CircleImage;
	private Canvas canvasImage5 = null;
	private Group groupIris = null;
	private Label labelImage = null;
	private Label labelEdges = null;
	private Label labelIrisThreshold = null;
	private Label labelIrisAcumulator = null;
	private Label labelIrisCircle = null;
	private Text textThresholdIrisDown = null;
	private Text textThresholdIrisUp = null;
	private Group groupNoise = null;
	private Label labelNoiseThreshold = null;
	private Canvas canvasImage6 = null;
	private Canvas canvasImage7 = null;
	private Label labelNoiseAccumulator = null;
	private Label labelNoise = null;
	private Text textThresholdLinesDown = null;
	private Text textThresholdLinesUp = null;
	private TabFolder tabFolder = null;
	private Composite compositeAnalyze = null;
	private Composite compositeNormalisation = null;
	private Composite compositeGabor = null;
	private Image HystImageLines;
	private CircularHoughTransform houghCircle2;
	private Canvas canvasNormalization1 = null;
	private Canvas canvasNormalization2 = null;
	private Canvas canvasNormalization3 = null;
	private Canvas canvasNormalization4 = null;
	private int[] origcp;
	private Image normalizedImage;
	private Image normalizedMask;
	private Label labelPupilDiameter = null;
	private Text textPupilDiameter = null;
	private Text textIrisDiameter = null;
	private Button databaseAddButton = null;
	private Label bazaLength = null;
	private Button compareButton = null;
	private Label compareResult = null;
	private Image imgScaled4;
	private int movedX;
	private int movedY;
	private int size;
	private float scaleFactorY;
	private float scaleFactorX;
	private Image imgScaled2;
	private int rmax;
	private int[] acc;
	private Image IrisAndPupilFullSize;
	private int iris_x;
	private int iris_y;
	private int iris_r;
	private float scalingFactor = (float) 0.5 ;
	private Shell irecShell = null; 
	private Text textFileUrl = null;
	private Button buttonFileUrl = null;
	private Button buttonStart = null;
	private Canvas canvasImage = null;
	private Canvas canvasImage1 = null;
	private Canvas canvasImage2 = null;
	private FeatureVector[] gaborFeature;
	private Vector <FeatureVector[]> irisDb = new Vector<FeatureVector[]>(); 
	private Vector <String> fileNames = new Vector<String>();
	private Text pupilDiameter = null;
	private Label irisDiameter = null;
	private Button loadDB = null;
	private Button savetoDB = null;
	private Canvas gaborCanvas1 = null;
	private Canvas gaborCanvas2 = null;
	private Canvas gaborCanvas3 = null;
	private Canvas gaborCanvas4 = null;
	private Canvas gaborCanvas5 = null;
	private Canvas gaborCanvas6 = null;
	private Canvas gaborCanvas7 = null;
	private Canvas gaborCanvas8 = null;
	private Canvas gaborCanvas9 = null;
	private Canvas gaborCanvas10 = null;
	private Canvas gaborCanvas11 = null;
	private Canvas gaborCanvas12 = null;
	private Label gaborLabel1 = null;
	private Label gaborLabel11 = null;
	private Label gaborLabel12 = null;
	private Label gaborLabel13 = null;
	private Label gaborLabel14 = null;
	private Label gaborLabel15 = null;
	private Label gaborLabel16 = null;
	private Label gaborLabel17 = null;
	private Label gaborLabel18 = null;
	private Label gaborLabel19 = null;
	private Label gaborLabel110 = null;
	private Label gaborLabel111 = null;
	
	private void createGroupIris() {
		groupIris = new Group(compositeAnalyze, SWT.NONE);
		groupIris.setText("Iris");
		groupIris.setBounds(new Rectangle(5, 5, 630, 217));
		createCanvasImage2();
		groupIris.setLayout(null);
		createCanvasImage3();
		createCanvasImage4();
		labelIrisThreshold = new Label(groupIris, SWT.NONE);
		labelIrisThreshold.setBounds(new Rectangle(11, 60, 67, 35));
		labelIrisThreshold.setText("Threshold");
		labelIrisAcumulator = new Label(groupIris, SWT.NONE);
		labelIrisAcumulator.setBounds(new Rectangle(220, 60, 156, 35));
		labelIrisAcumulator.setText("Hough transform");
		labelIrisCircle = new Label(groupIris, SWT.NONE);
		labelIrisCircle.setBounds(new Rectangle(425, 60, 142, 35));
		labelIrisCircle.setText("Iris");
		textThresholdIrisDown = new Text(groupIris, SWT.BORDER);
		textThresholdIrisDown.setBounds(new Rectangle(84, 59, 45, 21));
		textThresholdIrisDown.setText("5");
		textThresholdIrisUp = new Text(groupIris, SWT.BORDER);
		textThresholdIrisUp.setBounds(new Rectangle(133, 59, 44, 21));
		textThresholdIrisUp.setText("10");
		labelPupilDiameter = new Label(groupIris, SWT.NONE);
		labelPupilDiameter.setBounds(new Rectangle(347, 12, 95, 35));
		labelPupilDiameter.setText("Pupil radius");
		textPupilDiameter = new Text(groupIris, SWT.BORDER);
		textPupilDiameter.setBounds(new Rectangle(450, 10, 46, 21));
		textPupilDiameter.setText("34");
		textIrisDiameter = new Text(groupIris, SWT.BORDER);
		textIrisDiameter.setBounds(new Rectangle(285, 10, 45, 21));
		textIrisDiameter.setText("98");
		pupilDiameter = new Text(groupIris, SWT.BORDER);
		pupilDiameter.setBounds(new Rectangle(500, 10, 42, 21));
		pupilDiameter.setEditable(false);
		pupilDiameter.setText("34");
		irisDiameter = new Label(groupIris, SWT.NONE);
		irisDiameter.setBounds(new Rectangle(172, 12, 106, 35));
		irisDiameter.setText("Iris radius");
	}

	private void createGroupNoise() {
		groupNoise = new Group(compositeAnalyze, SWT.NONE);
		groupNoise.setLayout(null);
		groupNoise.setText("Noise");
		groupNoise.setBounds(new Rectangle(5, 226, 627, 154));
		createCanvasImage5();
		labelNoiseThreshold = new Label(groupNoise, SWT.NONE);
		labelNoiseThreshold.setBounds(new Rectangle(7, 20, 65, 20));
		labelNoiseThreshold.setText("Threshold");
		createCanvasImage6();
		createCanvasImage7();
		labelNoiseAccumulator = new Label(groupNoise, SWT.NONE);
		labelNoiseAccumulator.setBounds(new Rectangle(216, 20, 144, 20));
		labelNoiseAccumulator.setText("Hough transform");
		labelNoise = new Label(groupNoise, SWT.NONE);
		labelNoise.setBounds(new Rectangle(425, 20, 105, 20));
		labelNoise.setText("Lines and noise");
		textThresholdLinesDown = new Text(groupNoise, SWT.BORDER);
		textThresholdLinesDown.setBounds(new Rectangle(74, 20, 42, 21));
		textThresholdLinesDown.setText("10");
		textThresholdLinesUp = new Text(groupNoise, SWT.BORDER);
		textThresholdLinesUp.setBounds(new Rectangle(120, 20, 49, 21));
		textThresholdLinesUp.setText("20");
	}
	
	private void createTabFolder() {
		tabFolder = new TabFolder(irecShell, SWT.NONE);
		createCompositeAnalyze();
		createCompositeNormalisation();
		createCompositeGabor();
		tabFolder.setBounds(new Rectangle(14, 190, 654, 413));
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Analyze");
		tabItem.setControl(compositeAnalyze);
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("Normalization");
		tabItem1.setControl(compositeNormalisation);
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("Gabor filter");
		tabItem2.setControl(compositeGabor);		
	}
	
	private void createCanvasImage() {
		canvasImage = new Canvas(irecShell, SWT.NONE);
		canvasImage.setBounds(new Rectangle(40, 61, 199, 124));
	}

	private void createCanvasImage1() {
		canvasImage1 = new Canvas(irecShell, SWT.NONE);
		canvasImage1.setBounds(new Rectangle(373, 62, 199, 124));
	}
	
	private void createCanvasImage2() {
		canvasImage2 = new Canvas(groupIris, SWT.NONE);
		canvasImage2.setLocation(new Point(9, 82));
		canvasImage2.setSize(new Point(199, 124));
	}
	
	private void createCanvasImage3() {
		canvasImage3 = new Canvas(groupIris, SWT.NONE);
		canvasImage3.setLocation(new Point(217, 82));
		canvasImage3.setSize(new Point(199, 124));
	}
	
	private void createCanvasImage4() {
		canvasImage4 = new Canvas(groupIris, SWT.NONE);
		canvasImage4.setLocation(new Point(425, 82));
		canvasImage4.setSize(new Point(199, 124));
	}

	private void createCanvasImage5() {
		canvasImage5 = new Canvas(groupNoise, SWT.NONE);
		canvasImage5.setLocation(new Point(7, 43));
		canvasImage5.setSize(new Point(98, 98));
	}

	private void createCanvasImage6() {
		canvasImage6 = new Canvas(groupNoise, SWT.NONE);
		canvasImage6.setLocation(new Point(215, 43));
		canvasImage6.setSize(new Point(98, 98));
	}

	private void createCanvasImage7() {
		canvasImage7 = new Canvas(groupNoise, SWT.NONE);
		canvasImage7.setLocation(new Point(422, 43));
		canvasImage7.setSize(new Point(98, 98));
	}

	private void createCompositeAnalyze() {
		compositeAnalyze = new Composite(tabFolder, SWT.NONE);
		compositeAnalyze.setLayout(null);
		createGroupIris();
		createGroupNoise();
	}

	private void createCompositeNormalisation() {
		compositeNormalisation = new Composite(tabFolder, SWT.NONE);
		compositeNormalisation.setLayout(null);
		createCanvasNormalization1();
		createCanvasNormalization2();
		createCanvasNormalization3();
		createCanvasNormalization4();
	}
	
	private void createCanvasNormalization1() {
		canvasNormalization1 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization1.setLocation(new Point(14, 29));
		canvasNormalization1.setSize(new Point(199, 124));
	}

	private void createCanvasNormalization2() {
		canvasNormalization2 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization2.setLocation(new Point(270, 31));
		canvasNormalization2.setSize(new Point(199, 124));
	}

	private void createCanvasNormalization3() {
		canvasNormalization3 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization3.setLocation(new Point(19, 194));
		canvasNormalization3.setSize(new Point(199, 124));
	}

	private void createCanvasNormalization4() {
		canvasNormalization4 = new Canvas(compositeNormalisation, SWT.NONE);
		canvasNormalization4.setLocation(new Point(270, 195));
		canvasNormalization4.setSize(new Point(199, 124));
	}
	
	private void createCompositeGabor() {
		compositeGabor = new Composite(tabFolder, SWT.NONE);
		compositeGabor.setLayout(null);
		createGaborCanvas1();
		createGaborCanvas2();
		createGaborCanvas3();
		createGaborCanvas4();
		createGaborCanvas5();
		createGaborCanvas6();
		createGaborCanvas7();
		createGaborCanvas8();
		createGaborCanvas9();
		createGaborCanvas10();
		createGaborCanvas11();
		createGaborCanvas12();
		gaborLabel1 = new Label(compositeGabor, SWT.NONE);
		gaborLabel1.setBounds(new Rectangle(30, 10, 145, 20));
		gaborLabel1.setText("");
		gaborLabel11 = new Label(compositeGabor, SWT.NONE);
		gaborLabel11.setBounds(new Rectangle(30, 73, 148, 18));
		gaborLabel11.setText("");
		gaborLabel12 = new Label(compositeGabor, SWT.NONE);
		gaborLabel12.setBounds(new Rectangle(31, 133, 145, 18));
		gaborLabel12.setText("");
		gaborLabel13 = new Label(compositeGabor, SWT.NONE);
		gaborLabel13.setBounds(new Rectangle(31, 192, 142, 22));
		gaborLabel13.setText("");
		gaborLabel14 = new Label(compositeGabor, SWT.NONE);
		gaborLabel14.setBounds(new Rectangle(32, 252, 137, 20));
		gaborLabel14.setText("");
		gaborLabel15 = new Label(compositeGabor, SWT.NONE);
		gaborLabel15.setBounds(new Rectangle(30, 313, 137, 19));
		gaborLabel15.setText("");
		gaborLabel16 = new Label(compositeGabor, SWT.NONE);
		gaborLabel16.setBounds(new Rectangle(360, 12, 157, 24));
		gaborLabel16.setText("");
		gaborLabel17 = new Label(compositeGabor, SWT.NONE);
		gaborLabel17.setBounds(new Rectangle(360, 73, 155, 20));
		gaborLabel17.setText("");
		gaborLabel18 = new Label(compositeGabor, SWT.NONE);
		gaborLabel18.setBounds(new Rectangle(361, 135, 152, 23));
		gaborLabel18.setText("");
		gaborLabel19 = new Label(compositeGabor, SWT.NONE);
		gaborLabel19.setBounds(new Rectangle(361, 192, 150, 22));
		gaborLabel19.setText("");
		gaborLabel110 = new Label(compositeGabor, SWT.NONE);
		gaborLabel110.setBounds(new Rectangle(361, 253, 149, 23));
		gaborLabel110.setText("");
		gaborLabel111 = new Label(compositeGabor, SWT.NONE);
		gaborLabel111.setBounds(new Rectangle(361, 313, 150, 23));
		gaborLabel111.setText("");
		gabCanvas.add(gaborCanvas1);
		gabCanvas.add(gaborCanvas2);
		gabCanvas.add(gaborCanvas3);
		gabCanvas.add(gaborCanvas4);
		gabCanvas.add(gaborCanvas5);
		gabCanvas.add(gaborCanvas6);
		gabCanvas.add(gaborCanvas7);
		gabCanvas.add(gaborCanvas8);
		gabCanvas.add(gaborCanvas9);
		gabCanvas.add(gaborCanvas10);
		gabCanvas.add(gaborCanvas11);
		gabCanvas.add(gaborCanvas12);
		gabLabels.add(gaborLabel1);
		gabLabels.add(gaborLabel11);
		gabLabels.add(gaborLabel12);
		gabLabels.add(gaborLabel13);
		gabLabels.add(gaborLabel14);
		gabLabels.add(gaborLabel15);
		gabLabels.add(gaborLabel16);
		gabLabels.add(gaborLabel17);
		gabLabels.add(gaborLabel18);
		gabLabels.add(gaborLabel19);
		gabLabels.add(gaborLabel110);
		gabLabels.add(gaborLabel111);
	}

	private void createGaborCanvas1() {
		gaborCanvas1 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas1.setBounds(new Rectangle(30, 30, 256, 32));
	}

	private void createGaborCanvas2() {
		gaborCanvas2 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas2.setBounds(new Rectangle(30, 90, 256, 32));
	}

	private void createGaborCanvas3() {
		gaborCanvas3 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas3.setBounds(new Rectangle(30, 150, 256, 32));
	}

	private void createGaborCanvas4() {
		gaborCanvas4 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas4.setBounds(new Rectangle(30, 210, 256, 32));
	}

	private void createGaborCanvas5() {
		gaborCanvas5 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas5.setBounds(new Rectangle(30, 270, 256, 32));
	}

	private void createGaborCanvas6() {
		gaborCanvas6 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas6.setBounds(new Rectangle(30, 330, 256, 32));
	}

	private void createGaborCanvas7() {
		gaborCanvas7 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas7.setBounds(new Rectangle(360, 30, 256, 32));
	}

	private void createGaborCanvas8() {
		gaborCanvas8 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas8.setBounds(new Rectangle(360, 90, 256, 32));
	}

	private void createGaborCanvas9() {
		gaborCanvas9 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas9.setBounds(new Rectangle(360, 150, 256, 32));
	}

	private void createGaborCanvas10() {
		gaborCanvas10 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas10.setBounds(new Rectangle(360, 210, 256, 32));
	}

	private void createGaborCanvas11() {
		gaborCanvas11 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas11.setBounds(new Rectangle(360, 270, 256, 32));
	}

	private void createGaborCanvas12() {
		gaborCanvas12 = new Canvas(compositeGabor, SWT.NONE);
		gaborCanvas12.setBounds(new Rectangle(360, 330, 256, 32));
	}

	private void createirecShell() {
		irecShell = new Shell();
		irecShell.setText("Iris Signature – Webcam Based Iris Recognition");
		irecShell.setSize(new Point(689, 684));
		irecShell.setLayout(null);
		textFileUrl = new Text(irecShell, SWT.BORDER);
		textFileUrl.setBounds(new Rectangle(15, 15, 215, 25));
		textFileUrl.setText("");
		buttonFileUrl = new Button(irecShell, SWT.NONE);
		buttonFileUrl.setBounds(new Rectangle(240, 15, 101, 25));
		buttonFileUrl.setText("Select file...");
		buttonFileUrl.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(irecShell, SWT.NULL);
		        String path = dialog.open();
		        if (path != null) {
		
		          File file = new File(path);
		          if (file.isFile())
		            textFileUrl.setText(file.toString());
		        }
			}
		});
		buttonFileUrl = new Button(irecShell, SWT.NONE);
		buttonFileUrl.setBounds(new Rectangle(240, 45, 101, 25));
		buttonFileUrl.setText("Capture New");
		buttonFileUrl.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				imgcapture ImageCapture = new imgcapture();
			}
		});
		buttonStart = new Button(irecShell, SWT.NONE);
		buttonStart.setBounds(new Rectangle(560, 608, 99, 25));
		buttonStart.setText("START");
		buttonStart.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				endedThreads = 0 ;
				threadsToEnd = 0 ;
				threadRadius = 0 ;
				threadVariation = 0 ;
				
				image = new Image(Display.getDefault(),textFileUrl.getText());
				Image imgScaled = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(canvasImage.getBounds().width),(int)(canvasImage.getBounds().height)));
				canvasImage.setBackgroundImage(imgScaled);
				buttonStart.setEnabled(false);
				scaleImage();
			}
		});
		createCanvasImage();
		createCanvasImage1();
		labelImage = new Label(irecShell, SWT.NONE);
		labelImage.setBounds(new Rectangle(42, 42, 116, 35));
		labelImage.setText("Eye image");
		labelEdges = new Label(irecShell, SWT.NONE);
		labelEdges.setBounds(new Rectangle(372, 43, 128, 35));
		labelEdges.setText("Found edges");
		createTabFolder();
		
		databaseAddButton = new Button(irecShell, SWT.NONE);
		databaseAddButton.setBounds(new Rectangle(20, 608, 92, 23));
		databaseAddButton.setText("Add to DB");
		databaseAddButton.setVisible(false);
		databaseAddButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				System.out.println("Adding person to DB");
				irisDb.add(gaborFeature);
				String filename = (textFileUrl.getText().substring(textFileUrl.getText().lastIndexOf("\\")+1));
				fileNames.add(filename);
				bazaLength.setVisible(true);
				bazaLength.setText(fileNames.size() + " persons in DB.");
				databaseAddButton.setVisible(false);
				compareButton.setVisible(false);
			}
		});
		bazaLength = new Label(irecShell, SWT.NONE);
		bazaLength.setBounds(new Rectangle(138, 608, 128, 33));
		bazaLength.setText("x persons in DB");
		bazaLength.setVisible(false);
		compareButton = new Button(irecShell, SWT.NONE);
		compareButton.setBounds(new Rectangle(268, 608, 106, 25));
		compareButton.setText("Compare irises");
		compareButton.setVisible(false);
		compareButton
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						System.out.println("Comparing iris with db"); 
						compareResult.setText("Iris won:\n" + irisDB.CompareMinkowski(gaborFeature, irisDb, fileNames));
						compareResult.setVisible(false);
					}
				});
		compareResult = new Label(irecShell, SWT.NONE);
		compareResult.setBounds(new Rectangle(380, 618, 157, 31));
		compareResult.setText("Iris won:\n");
		compareResult.setVisible(false);
		loadDB = new Button(irecShell, SWT.NONE);
		loadDB.setBounds(new Rectangle(463, 13, 94, 29));
		loadDB.setText("Open db...");
		loadDB.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(irecShell, SWT.NULL);
				String ext[] = {"*.iris"};
				dialog.setFilterExtensions(ext);
		        String path = dialog.open();
		        if (path != null) {
		        	FileInputStream fis = null;
		        	ObjectInputStream in = null;
		        	try
		        	{
		        	  fis = new FileInputStream(path);
		        	  in = new ObjectInputStream(fis);
		        	  Vector database = (Vector)in.readObject();
		        	  in.close();
		        	  irisDb = (Vector)database.get(0);
		        	  fileNames = (Vector)database.get(1);
					  bazaLength.setVisible(true);
					  bazaLength.setText(fileNames.size() + " persons in DB.");
					  System.out.println("Opened database from " + path);
					  if(gaborFeature!=null) {
						  compareButton.setVisible(true);
					  }
		        	}
		        	catch(IOException ex)
		        	  {
		        	   ex.printStackTrace();
		        	  }
		        	 catch(ClassNotFoundException ex)
		        	 {
		        	    ex.printStackTrace();
		        	 }
		        	}
		        }
		});
		savetoDB = new Button(irecShell, SWT.NONE);
		savetoDB.setBounds(new Rectangle(573, 13, 89, 29));
		savetoDB.setText("Save DB...");
		savetoDB.setVisible(false);
		savetoDB.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(irecShell, SWT.NULL);
				String ext[] = {"*.iris"};
				dialog.setFilterExtensions(ext);
		        String path = dialog.open();
		        if(!path.endsWith("iris")) path = path.concat(".iris");
		        if (path != null) {
			        	  FileOutputStream fos = null;
			        	  ObjectOutputStream out = null;
			        	  Vector database = new Vector();
			        	  database.add(irisDb);
			        	  database.add(fileNames);
			        	  try
				       	     {
				       	        fos = new FileOutputStream(path);
				       	        out = new ObjectOutputStream(fos);
				       	        out.writeObject(database);
				       	        System.out.println("Database saved at " + path);
				       	     }
				       	  catch(IOException ex)
				       	     {
				       	           ex.printStackTrace();
				             }
				          
		        }
			}
		});
	}
	
	private void iris() {
		orig = TransformOperations.convertArraytoRect(houghCircle.r, houghCircle.centerCords.x, 
				houghCircle.centerCords.y, orig,width);
		origcp = TransformOperations.convertArraytoRect(houghCircle.r, houghCircle.centerCords.x, 
				houghCircle.centerCords.y, origcp,width);
		movedX  = iris_x-iris_r ;
		movedY = iris_y-iris_r ;
		size = 2*houghCircle.r ;
		System.out.println("Moved by "+movedX+","+movedY+" sred "+2* houghCircle.r);
		width = height = 2* houghCircle.r ;
		IrisAndPupilFullSize = new Image(Display.getDefault(),ImageProcessing.createSWTimage(origcp,size,size));
	}

	private void pupil() {
		histThresholdObject.initializeThread(orig,width,height,10,20);
		origFiltered = histThresholdObject.processThread();

		int pupil_r = (int)(Integer.parseInt(textPupilDiameter.getText())*scalingFactor) ;
		findCircleBruteForce(houghCircle2,pupil_r,origFiltered,width,height);
	}

	private void houghtransform() {
		System.out.println("IRIS - Hough");

		iris_r = (int)(Integer.parseInt(textIrisDiameter.getText())*scalingFactor) ;
		houghCircle.Initialize(origFiltered,width,height,iris_r);
		houghCircle.setLines(1);
		origFiltered = houghCircle.Transform();

		iris_x = houghCircle.centerCords.x ;
		iris_y = houghCircle.centerCords.y ;
		iris_r = houghCircle.r ;


		CircleImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(origFiltered,width,height));
		Image imgScaled = new Image(Display.getDefault(), CircleImage.getImageData().scaledTo((int)(canvasImage4.getBounds().width),(int)(canvasImage4.getBounds().height)));
		imgScaled2 = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(canvasImage4.getBounds().width),(int)(canvasImage4.getBounds().height)));

		GC gc = new GC(imgScaled);
		scaleFactorY = (float)image.getBounds().height/imgScaled2.getBounds().height ;
		scaleFactorX = (float)image.getBounds().width/imgScaled2.getBounds().width ;

		System.out.println(image.getBounds().width+"/"+imgScaled2.getBounds().width+" ScaleX = "+scaleFactorX+" "+image.getBounds().height+"/"+imgScaled2.getBounds().height+" ScaleY = "+scaleFactorY);
		gc.drawImage(imgScaled2, 0,0);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		gc.drawOval((int)((iris_x-iris_r)/scaleFactorX), (int)((iris_y-iris_r)/scaleFactorY), (int)((2*iris_r)/scaleFactorX), (int)((2*iris_r)/scaleFactorY));
		gc.dispose();

		imgScaled4 = imgScaled;
		canvasImage4.setBackgroundImage(imgScaled);

		rmax = (int)Math.sqrt(width*width + height*height);
		acc = new int[width * height];
		acc=houghCircle.accumulator;
		HoughAccImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(acc,width,height));
		imgScaled = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,124));
		canvasImage3.setBackgroundImage(imgScaled);
	}

	private void scanimg() {
		double direction[] = new double[width*height];
		direction=sobelObject.direction;

		PaletteData palette = new PaletteData(0xFF , 0xFF00 , 0xFF0000);
		ImageData imgData = new ImageData(width,height,24,palette);
		imgData.setPixels(0, 0, width, orig, 0);

		SobelImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(orig,width,height));
		Image imgScaled = new Image(Display.getDefault(), SobelImage.getImageData().scaledTo((int)(canvasImage1.getBounds().width),(int)(canvasImage1.getBounds().height)));
		canvasImage1.setBackgroundImage(imgScaled);

		nonMaxSuppressionObject.init(orig,direction,width,height);
		orig = nonMaxSuppressionObject.process();
		int[] orig2 = orig.clone();
		histThresholdObject.initializeThread(orig2,width,height,Integer.parseInt(textThresholdIrisDown.getText()),Integer.parseInt(textThresholdIrisUp.getText()));
		origFiltered = histThresholdObject.processThread();
		HystImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(origFiltered,width,height));
		imgScaled = new Image(Display.getDefault(), HystImage.getImageData().scaledTo((int)(canvasImage2.getBounds().width),(int)(canvasImage2.getBounds().height)));
		canvasImage2.setBackgroundImage(imgScaled);
	}
	
	private void scaleImage(){
		image = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(image.getBounds().width*scalingFactor),(int)(image.getBounds().height*scalingFactor)));
		width = image.getBounds().width ;
		height = image.getBounds().height ;
		orig=new int[width*height];

		BufferedImage bufImg = ImageProcessing.convertToAWT(image.getImageData());
		
		PixelGrabber grabber = new PixelGrabber(bufImg, 0, 0, width, height, orig, 0, width);
		try {
			grabber.grabPixels();
			origcp = orig.clone() ;
		}
		catch(InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		sobelObject = new Scanning();
		nonMaxSuppressionObject = new Suppression();
		histThresholdObject = new Thresholding();
		lineHoughObject = new LinearHoughTransform();
		houghCircle = new CircularHoughTransform();
		houghCircle2 = new CircularHoughTransform();

		sobelObject.initializeScanning(orig,width,height);
		orig = sobelObject.processScanning();
		scanimg();
		houghtransform();
		iris();
		pupil();
	}

	private void threadsCompleted(){
		pupilDiameter.setText((int)(houghCircle2.r*1/scalingFactor)+"");

		int pupil_x = houghCircle2.centerCords.x ;
		int pupil_y = houghCircle2.centerCords.y ;
		int pupil_r = houghCircle2.r ;
		CircleImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(origFiltered,width,height));
		Image imgScaled = new Image(Display.getDefault(), CircleImage.getImageData().scaledTo((int)(0.5*CircleImage.getBounds().width),(int)(0.5*CircleImage.getBounds().height)));
		GC gc = new GC(canvasImage4);
		gc.drawImage(imgScaled4, 0, 0);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		System.out.println("movX+pupX-pupR = "+movedX+"+"+pupil_x+"-"+pupil_r+"="+(movedX+pupil_x-pupil_r));
		gc.drawOval((int)((movedX+pupil_x-pupil_r)/scaleFactorX), (int)((movedY+pupil_y-pupil_r)/scaleFactorY), (int)(2*pupil_r/scaleFactorX), (int)(2*pupil_r/scaleFactorY));
		Image imageIrisAndPupil = new Image(Display.getDefault(),canvasImage4.getBounds());
		gc.copyArea(imageIrisAndPupil , 0, 0);
		gc.dispose();
		canvasImage4.setBackgroundImage(imageIrisAndPupil);
		histThresholdObject.initializeThread(orig,width,height, Integer.parseInt(textThresholdLinesDown.getText()),Integer.parseInt(textThresholdLinesUp.getText()));
		orig = histThresholdObject.processThread(); 

		HystImageLines = new Image(Display.getDefault(),ImageProcessing.createSWTimage(orig,width,height));
		imgScaled = new Image(Display.getDefault(), HystImageLines.getImageData().scaledTo((int)(canvasImage5.getBounds().width),(int)(canvasImage5.getBounds().height)));
		canvasImage5.setBackgroundImage(imgScaled);

		int halfHeight = (int)(height/2) ;
		int[] upper = TransformOperations.half(orig, halfHeight, width, true);
		int[] lower = TransformOperations.half(orig, halfHeight, width, false);

		lineHoughObject.Initialize(upper,width,height);
		lineHoughObject.setLines(5);
		upper = lineHoughObject.Transform();
		int[] accUpper = lineHoughObject.accumulator; 

		java.awt.Point[] upperLinePoints = lineHoughObject.getLineCoords(true);

		lineHoughObject.Initialize(lower,width,height);
		lineHoughObject.setLines(2);
		lower = lineHoughObject.Transform();
		int[] accLower = lineHoughObject.accumulator; 
		java.awt.Point[] lowerLinePoints = lineHoughObject.getLineCoords(false);

		System.out.println("Points in lines:\n up[ ("+upperLinePoints[0].x+","+upperLinePoints[0].y+") , ("+upperLinePoints[1].x+","+upperLinePoints[1].y+") ] down [ ("+lowerLinePoints[0].x+","+lowerLinePoints[0].y+") , ("+lowerLinePoints[1].x+","+lowerLinePoints[1].y+") ]");

		int mergePoint = (int)(orig.length/2) ;
		orig = TransformOperations.mergetwoArrays(upper, lower,mergePoint);

		OverlayImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(orig,width,height));
		imgScaled = new Image(Display.getDefault(), OverlayImage.getImageData().scaledTo((int)(OverlayImage.getBounds().width/scaleFactorX),(int)(OverlayImage.getBounds().height/scaleFactorY)));
		imgScaled2 = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(image.getBounds().width/scaleFactorX),(int)(image.getBounds().height/scaleFactorY)));

		gc = new GC(imgScaled);
		gc.setAlpha(90);
		int startX = (int)((houghCircle.centerCords.x-houghCircle.r)/scaleFactorX) ;
		int startY = (int)((houghCircle.centerCords.y-houghCircle.r)/scaleFactorY) ;
		System.out.println("Start X = " + startX +" startY "+startY+" bok ="+2*houghCircle.r+ " rys  = "+imgScaled2.getBounds().height+" , "+imgScaled2.getBounds().width);
		int hwx = (int)(2*houghCircle.r/scaleFactorX) ;
		int hwy = (int)(2*houghCircle.r/scaleFactorY) ;
		gc.drawImage(imgScaled2,startX ,startY,hwx,hwy,0,0,hwx,hwy);

		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.drawLine((int)(upperLinePoints[0].x/scaleFactorX), (int)(upperLinePoints[0].y/scaleFactorY), (int)(upperLinePoints[1].x/scaleFactorX), (int)(upperLinePoints[1].y/scaleFactorY));
		gc.drawLine((int)(upperLinePoints[0].x/scaleFactorX), (int)(lowerLinePoints[0].y/scaleFactorY), (int)(lowerLinePoints[1].x/scaleFactorX), (int)(lowerLinePoints[1].y/scaleFactorY));
		gc.dispose();
		imgScaled = new Image(Display.getDefault(), imgScaled.getImageData().scaledTo((int)(canvasImage7.getBounds().width),(int)(canvasImage7.getBounds().height)));
		canvasImage7.setBackgroundImage(imgScaled);
		rmax = (int)Math.sqrt(width*width + height*height);
		HoughAccImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(accUpper,180,rmax));
		imgScaled = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,62));

		HoughAccImage = new Image(Display.getDefault(),ImageProcessing.createSWTimage(accLower,180,rmax));
		imgScaled2 = new Image(Display.getDefault(), HoughAccImage.getImageData().scaledTo(199,62));
		gc = new GC(canvasImage6);
		gc.drawImage(imgScaled,0 ,0);
		gc.drawImage(imgScaled2,0 ,62);
		imgScaled = new Image(Display.getDefault(),HoughAccImage.getBounds()) ;
		gc.copyArea(imgScaled, 0, 0);
		gc.dispose();
		canvasImage6.setBackgroundImage(imgScaled);
		gc = new GC(imageIrisAndPupil);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.drawLine((int)((movedX+upperLinePoints[0].x)/scaleFactorX), (int)((movedY+upperLinePoints[0].y)/scaleFactorY), (int)((movedX+upperLinePoints[1].x)/scaleFactorX), (int)((movedY+upperLinePoints[1].y)/scaleFactorY));
		gc.drawLine((int)((movedX+upperLinePoints[0].x)/scaleFactorX), (int)((movedY+lowerLinePoints[0].y)/scaleFactorY), (int)((movedX+lowerLinePoints[1].x)/scaleFactorX), (int)((movedY+lowerLinePoints[1].y)/scaleFactorY));
		gc.dispose();
		canvasNormalization2.setBackgroundImage(imageIrisAndPupil);

		PolarFilter polFilter = new PolarFilter();
		polFilter.setType(PolarFilter.POLAR_TO_RECT);
		BufferedImage imageNormalized1temp = new BufferedImage(IrisAndPupilFullSize.getBounds().width,IrisAndPupilFullSize.getBounds().height,BufferedImage.TYPE_INT_RGB);
		polFilter.filter(ImageProcessing.convertToAWT(IrisAndPupilFullSize.getImageData()), imageNormalized1temp);
		normalizedImage = new Image(Display.getDefault(),ImageProcessing.convertToSWT(imageNormalized1temp)) ;
		imgScaled = new Image(Display.getDefault(), normalizedImage.getImageData().scaledTo(canvasNormalization1.getBounds().width,canvasNormalization1.getBounds().height));
		canvasNormalization1.setBackgroundImage(imgScaled);
		int imgwidth = IrisAndPupilFullSize.getBounds().width ;
		int imgheight = IrisAndPupilFullSize.getBounds().height ;

		normalizedMask = new Image(Display.getDefault(),imgwidth,imgheight) ;
		gc = new GC(normalizedMask);
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(0, 0, imgwidth, imgheight);
		gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		gc.fillOval(0,0, 2*iris_r, 2*iris_r);

		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		gc.fillOval((int)((pupil_x-pupil_r)), (int)((pupil_y-pupil_r)), 2*pupil_r, 2*pupil_r);
		gc.fillPolygon(new int[]{0,upperLinePoints[0].y,imgwidth,upperLinePoints[1].y,imgwidth,0,0,0});
		gc.fillPolygon(new int[]{0,lowerLinePoints[0].y,imgwidth,lowerLinePoints[1].y,imgwidth,imgheight,0,imgheight});
		gc.dispose();
		imageNormalized1temp = new BufferedImage(IrisAndPupilFullSize.getBounds().width,IrisAndPupilFullSize.getBounds().height,BufferedImage.TYPE_INT_RGB);
		polFilter.filter(ImageProcessing.convertToAWT(normalizedMask.getImageData()), imageNormalized1temp);
		normalizedMask = new Image(Display.getDefault(),ImageProcessing.convertToSWT(imageNormalized1temp));
		imgScaled = new Image(Display.getDefault(), normalizedMask.getImageData().scaledTo(canvasNormalization1.getBounds().width,canvasNormalization1.getBounds().height));
		canvasNormalization2.setBackgroundImage(imgScaled);
		buttonStart.setEnabled(true);				
		gaborFiltering() ;
	}

	private void findCircleBruteForce(CircularHoughTransform circleHoughObject3, int radius,int[] orig,int width, int height) {
		int variation = 7 ;
		values = new int[2*variation];
		thc = new circularThread[variation*2];
		int a = 0 ;
		for(int i = radius -variation; i< radius+variation ; i++,a++){
			System.out.println("Starting thread "+a+" radius "+i);
			thc[a] = new circularThread(orig,width,height,i,a,this);
			thc[a].start();
		}
		threadsToEnd = a ;
		threadRadius = radius ;
		threadVariation = variation ;
		threadCircleHough = circleHoughObject3 ;
	}
	int endedThreads = 0 ;
	int threadsToEnd = 0 ;
	int threadRadius = 0 ;
	int threadVariation = 0 ;
	CircularHoughTransform threadCircleHough ;
	
	public synchronized void threadEnded(){
		endedThreads ++ ;
		if(endedThreads>=threadsToEnd){
			threadsout = findCircleBruteForceContinue() ;
			origFiltered = threadsout ;
			System.out.println("All threads ENDED - phase2");
			threadsCompleted();
		}
		
	}
	
	private int[] findCircleBruteForceContinue(){
		for(int i =0 ; i<thc.length;i++){
			values[i] = (thc[i]).val ;
		}
		int maxval = -10 ;
		int maxindex = -1; 
		for (int i = 0 ;i<values.length;i++) {
			if(values[i]>maxval){
				maxval = values[i] ;
				maxindex = i ;
			}
		}
		
		int minval = 10000 ;
		int minidex = -1; 
		for (int i = 0 ;i<values.length;i++) {
			if(values[i]<=minval){
				minval = values[i] ;
				minidex = i ;
			}
		}
		
		System.out.println("maxval = "+maxval+" dla i= "+maxindex);
		System.out.println("minval = "+minval+" dla i= "+minidex);
		threadCircleHough.Initialize(orig,width,height,threadRadius-threadVariation+maxindex);
		threadCircleHough.setLines(1);
		return threadCircleHough.Transform();
	}

    private void gaborFiltering() {
        Image imgScaled = new Image(Display.getDefault(), normalizedImage.getImageData().scaledTo(256,32));
        ij.ImagePlus imga = new ImagePlus("img", ImageProcessing.convertToAWT(imgScaled.getImageData()));
        ij.process.ImageProcessor ip = imga.getProcessor();
        ij.measure.Calibration cal = imga.getCalibration();
        ip.setCalibrationTable(cal.getCTable());
        ip = ip.convertToFloat();
        imga = new ImagePlus("img", ip);
        float[] img = bijnum.BIJutil.vectorFromImageStack(imga, 0);
        Image maskScaled = new Image(Display.getDefault(), normalizedMask.getImageData().scaledTo(256,32));
        ij.ImagePlus maska = new ImagePlus("mask", ImageProcessing.convertToAWT(maskScaled.getImageData()));
        ip = maska.getProcessor();
        cal = maska.getCalibration();
        ip.setCalibrationTable(cal.getCTable());
        ip = ip.convertToFloat();
        maska = new ImagePlus("mask", ip);
        float[] mask = bijnum.BIJutil.vectorFromImageStack(maska, 0); //mask to float[]
        
        
        float[] scales = {2, 16}; //parametr scales
        
        gaborFeature = GaborFilter.filter(img, mask, imga.getWidth(), scales); //obliczamy gabora
        System.out.println("Factors number: " + gaborFeature.length + " x " + gaborFeature[0].vector.length);
        
        int i=0;
        for(FeatureVector ficzer: gaborFeature) {
        	gabLabels.get(i).setText(ficzer.name);
        	ImagePlus gab = bijnum.BIJutil.showVectorAsImage(ficzer.vector, imga.getWidth());
        	gab.getWindow().close();
        	gabCanvas.get(i).setBackgroundImage(new Image(compositeGabor.getDisplay(), 
        			ImageProcessing.convertToSWT(TransformOperations.toBufferedImage(gab.getImage()))));
        	i++;
        }       
        	
        databaseAddButton.setVisible(true);
        if(fileNames.size() > 1) {
                compareButton.setVisible(true);
                savetoDB.setVisible(true);
        }
    }
    
    public static void main(String[] args) {
		Display display = Display.getDefault();
		IrisSignature thisClass = new IrisSignature();
		thisClass.createirecShell();
		thisClass.irecShell.open();

		while (!thisClass.irecShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
