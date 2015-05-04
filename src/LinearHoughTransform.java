import java.awt.*;
import java.lang.Math;

public class LinearHoughTransform {

		int[] input;
		int[] output;
		float[] template={-1,0,1,-2,0,2,-1,0,1};;
		double progress;
		int width;
		int height;
		int[] accumulator;
		int accumulatorSize=30;
		int[] results;

		public LinearHoughTransform() {
			progress=0;
		}

		public void Initialize(int[] inputIn, int widthIn, int heightIn) {
			width=widthIn;
			height=heightIn;
			input = new int[width*height];
			output = new int[width*height*4];
			input=inputIn;
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					output[x*width+y] = 0xff000000;
				}
			}
		}
		public void setLines(int lines) {
			accumulatorSize=lines;	
			startPt = new Point[accumulatorSize] ;
			endPt = new Point[accumulatorSize] ;
			linesStart = 0 ;
			linesEnd = 0 ;
		}

		public int[] Transform() {
			int rmax = (int)Math.sqrt(width*width + height*height);
			accumulator = new int[rmax*180];
			int r;
			progress=0;
				
			for(int x=0;x<width;x++) {
				progress+=0.5;			
				for(int y=0;y<height;y++) {
				
					if ((input[y*width+x] & 0xff)== 255) {
					
						for (int theta=0; theta<180; theta++) {
							r = (int)(x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180));
							if ((r > 0) && (r <= rmax))
								accumulator[r*180+theta] = accumulator[r*180+theta] + 1;
						}
					}
				}
			}
			int max=0;
			for(r=0; r<rmax; r++) {
				for(int theta=0; theta<180; theta++) {
					if (accumulator[r*180+theta] > max) {
						max = accumulator[r*180+theta];
					}
				}
			}		
			int value;
			for(r=0; r<rmax; r++) {
				for(int theta=0; theta<180; theta++) {
					value = (int)(((double)accumulator[r*180+theta]/(double)max)*255.0);
					accumulator[r*180+theta] = 0xff000000 | (value << 16 | value << 8 | value);
				}
			}
			Maxima();

			System.out.println("done");
			return output;
		}
		private int[] Maxima() {
	
			int rmax = (int)Math.sqrt(width*width + height*height);
			results = new int[accumulatorSize*3];
			int[] output = new int[width*height];
			for(int r=0; r<rmax; r++) {
				for(int theta=0; theta<180; theta++) {
					int value = (accumulator[r*180+theta] & 0xff);
					if (value > results[(accumulatorSize-1)*3]) {
						results[(accumulatorSize-1)*3] = value;
						results[(accumulatorSize-1)*3+1] = r;
						results[(accumulatorSize-1)*3+2] = theta;
						int i = (accumulatorSize-2)*3;
						while ((i >= 0) && (results[i+3] > results[i])) {
							for(int j=0; j<3; j++) {
								int temp = results[i+j];
								results[i+j] = results[i+3+j];
								results[i+3+j] = temp;
							}
							i = i - 3;
							if (i < 0) break;
						}
					}
				}
			}
		
			double ratio=(double)(width/2)/accumulatorSize;
			System.out.println("top "+accumulatorSize+" matches:");
			for(int i=accumulatorSize-1; i>=0; i--){
				progress+=ratio;			
				drawPolarLine(results[i*3], results[i*3+1], results[i*3+2]);
			}
			return output;
		}
		int linesStart = 0 ;
		int linesEnd = 0 ;
		private void drawPolarLine(int value, int r, int theta) {
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					int temp = (int)(x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180));
					if((temp - r) == 0){
						output[y*width+x] = 0xff000000 | (value << 16 | value << 8 | value);
						if(x==0&&linesStart<accumulatorSize){
							System.out.print("start["+linesStart+"] ("+x+","+y+") ; ");
							startPt[linesStart] = new Point(x,y);
							linesStart++ ;
						}else if(x==width-1&&linesEnd<accumulatorSize){
							System.out.print("end["+linesEnd+"] ("+x+","+y+") ; ");
							endPt[linesEnd] = new Point(x,y);
							linesEnd++ ;
						}
					}					
				}
			}
		}
		private Point[] startPt ;
		private Point[] endPt ;
		public Point[] getLineCoords(boolean isUpper){
			Point start = new Point();
			Point end = new Point() ;
			if(isUpper){
				end.y = 0 ;
				start.y = 0 ;
			}else{
				end.y = height ;
				start.y = height ;
			}
			for (Point point : startPt) {
				if(point==null)
					continue ;
				System.out.println("stPt ("+point.x+","+point.y+")");
				if(isUpper){
					if(point.y>start.y){
						start.y=point.y ;
						start.x=point.x ;
					}
				}else{
					if(point.y<start.y){
						start.y=point.y ;
						start.x=point.x ;
					}
				}
			}
			
			for (Point point : endPt) {
				if(point!=null){
					if(isUpper){
						if(point.y>end.y){
							end.y=point.y ;
							end.x=point.x ;
						}
					}else{
						if(point.y<end.y){
							end.y=point.y ;
							end.x=point.x ;
						}
					}
				}
			}
			Point []out = new Point[2];
			out[0] = start ;
			out[1] = end ;
			return out ;
		}
}
