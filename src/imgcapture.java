import com.lti.civil.CaptureDeviceInfo;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver; 
import com.lti.civil.CaptureStream; 
import com.lti.civil.CaptureSystem; 
import com.lti.civil.CaptureSystemFactory; 
import com.lti.civil.DefaultCaptureSystemFactorySingleton; 
import com.lti.civil.Image; 
import com.lti.civil.awt.AWTImageConverter; 
import com.sun.image.codec.jpeg.JPEGCodec; 
import com.sun.image.codec.jpeg.JPEGImageEncoder; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.image.BufferedImage; 
import java.io.ByteArrayInputStream; 
import java.io.ByteArrayOutputStream; 
import java.io.File; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.util.Calendar; 
import java.util.List; 
import javax.imageio.ImageIO; 
import javax.swing.ImageIcon; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JPanel; 
import javax.swing.WindowConstants; 

public class imgcapture implements CaptureObserver{
	
	JButton start = null; 
	JButton shot = null; 
	JButton stop = null; 
	CaptureStream captureStream = null; 
	boolean takeShot=false; 
	
	public imgcapture() { 
		CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance(); 
		CaptureSystem system; 
		try { 
			system = factory.createCaptureSystem(); 
			system.init(); 
			List list = system.getCaptureDeviceInfoList(); 
			int i = 0; 
			if (i < list.size()) { 
				CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i); 
				System.out.println((new StringBuilder()).append("Device ID ").append(i).append(": ").append(info.getDeviceID()).toString()); 
				System.out.println((new StringBuilder()).append("Description ").append(i).append(": ").append(info.getDescription()).toString()); 
				captureStream = system.openCaptureDeviceStream(info.getDeviceID()); 
				captureStream.setObserver(imgcapture.this); 
			} 
		} catch (CaptureException ex) { 
			ex.printStackTrace(); 
		} 
		JFrame frame = new JFrame(); 
		frame.setSize(7000, 800); 
		JPanel panel = new JPanel(); 
		frame.setContentPane(panel); 
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
		start = new JButton("Start"); 
		stop = new JButton("Stop"); 
		shot = new JButton("Shot"); 
		panel.add(start); 
		panel.add(stop); 
		panel.add(shot); 
		panel.revalidate(); 
		start.addActionListener(new ActionListener() { 
		
			public void actionPerformed(ActionEvent e) { 
				try { 
					captureStream.start(); 
				} catch (CaptureException ex) { 
					ex.printStackTrace(); 
				} 
			} 
		}); 
		stop.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try { 
					captureStream.stop(); 
				} catch (CaptureException ex) { 
					ex.printStackTrace(); 
				} 
			} 
		}); 
		shot.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				takeShot=true; 
			} 
		}); 
	} 
	
	public void onNewImage(CaptureStream stream, Image image)  { 
	
		if(!takeShot) return; 
		takeShot=false; 
		System.out.println("New Image Captured"); 
		byte bytes[] = null; 
		try { 
			if (image == null) { 
				bytes = null; 
				return; 
			} 
			try { 
				ByteArrayOutputStream os = new ByteArrayOutputStream(); 
				JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(os); 
				jpeg.encode(AWTImageConverter.toBufferedImage(image)); 
				os.close(); 
				bytes = os.toByteArray(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
				bytes = null; 
			} catch (Throwable t) { 
				t.printStackTrace(); 
				bytes = null; 
			} 
			if (bytes == null) { 
				return; 
			} 
			ByteArrayInputStream is = new ByteArrayInputStream(bytes); 
			File file = new File("/Java/workspace/IrisRecognition/res/Webcam/img" + Calendar.getInstance().getTimeInMillis() + ".jpg"); 
			FileOutputStream fos = new FileOutputStream(file); 
			fos.write(bytes); 
			fos.close(); 
			BufferedImage myImage = ImageIO.read(file);
			myImage.getScaledInstance(640, 480, BufferedImage.SCALE_DEFAULT);
			ImageIO.write(myImage, null, file);
			shot.setText(""); 
			shot.setIcon(new ImageIcon(myImage)); 
			shot.revalidate(); 
		} catch (IOException ex) { 
			ex.printStackTrace(); 
		} 
	} 
	
	public void onError(CaptureStream arg0, CaptureException arg1) { 
	    throw new UnsupportedOperationException("Error is coming "); 
	} 
}