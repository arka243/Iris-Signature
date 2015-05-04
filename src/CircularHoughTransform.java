import java.awt.*;
import java.lang.Math;

public class CircularHoughTransform {

		int[] input;
		int[] output;
		float[] template={-1,0,1,-2,0,2,-1,0,1};;
		double progress;
		int width;
		int height;
		int[] accumulator;
		int accumulatorSize=30;
		int[] results;
		int r;
		public int value;

		public CircularHoughTransform() {
			progress=0;
		}

		public void Initialize(int[] inputIn, int widthIn, int heightIn, int radius) {
			r = radius;
			width=widthIn;
			height=heightIn;
			input = new int[width*height];
			output = new int[width*height];
			input=inputIn;
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					output[x + (width*y)] = 0xff000000;
				}
			}
		}
		public void setLines(int lines) {
			accumulatorSize=lines;		
		}

		public int[] bruteforceTransform(){
			accumulator = new int[width * height*2];
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					accumulator[x*width+y] =0 ;
				}
			}			
			int x0, y0;
			double t;
			progress=0;
				
			for(int x=0;x<width;x++) {
				progress+=0.5;			
				for(int y=0;y<height;y++) {
				
					if ((input[y*width+x] & 0xff)== 255) {
					
						for (int theta=0; theta<360; theta++) {
							t = (theta * 3.14159265) / 180;
							x0 = (int)Math.round(x - r * Math.cos(t));
							y0 = (int)Math.round(y - r * Math.sin(t));
							if(x0 < width && x0 > 0 && y0 < height && y0 > 0) {
								accumulator[x0 + (y0 * width)] += 1;
							}
						}
					}
				}
			}
		
			int max=0;
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {

					if (accumulator[x + (y * width)] > max) {
						max = accumulator[x + (y * width)];
					}
				}
			}
			value = max ;
			return output;
		}

		
		public int[] Transform() {
	
			accumulator = new int[width * height*2];
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					accumulator[x*width+y] =0 ;
				}
			}			
			int x0, y0;
			double t;
			progress=0;
				
			for(int x=0;x<width;x++) {
				progress+=0.5;			
				for(int y=0;y<height;y++) {
				
					if ((input[y*width+x] & 0xff)== 255) {
					
						for (int theta=0; theta<360; theta++) {
							t = (theta * 3.14159265) / 180;
							x0 = (int)Math.round(x - r * Math.cos(t));
							y0 = (int)Math.round(y - r * Math.sin(t));
							if(x0 < width && x0 > 0 && y0 < height && y0 > 0) {
								accumulator[x0 + (y0 * width)] += 1;
							}
						}
					}
				}
			}
		
			int max=0;
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {

					if (accumulator[x + (y * width)] > max) {
						max = accumulator[x + (y * width)];
					}
				}
			}
			int value;
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					value = (int)(((double)accumulator[x + (y * width)]/(double)max)*255.0);
					accumulator[x + (y * width)] = 0xff000000 | (value << 16 | value << 8 | value);
				}
			}
			Maxima();

			return output;
		}
		private int[] Maxima() {
			results = new int[accumulatorSize*3];
			int[] output = new int[width*height];

		
			for(int x=0;x<width;x++) {
				for(int y=0;y<height;y++) {
					int value = (accumulator[x + (y * width)] & 0xff);

					if (value > results[(accumulatorSize-1)*3]) {
						results[(accumulatorSize-1)*3] = value;
						results[(accumulatorSize-1)*3+1] = x;
						results[(accumulatorSize-1)*3+2] = y;
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
			for(int i=accumulatorSize-1; i>=0; i--){
				progress+=ratio;			
				System.out.println("VAL: " + results[i*3] + ", X: " + results[i*3+1] + ", Y: " + results[i*3+2]);
				drawCircle(results[i*3], results[i*3+1], results[i*3+2]);
				centerCords = new Point(results[i*3+1],results[i*3+2]);
				value = results[i*3] ;
			}
			return output;
		}
	
		public Point centerCords ;
	
		private void setPixel(int value, int xPos, int yPos) {
			if(((yPos * width)+xPos)<0)
				return ;
			output[(yPos * width)+xPos] = 0xff000000 | (value << 16 | value << 8 | value);
		}
		
		private void drawCircle(int pix, int xCenter, int yCenter) {
			pix = 250;
			int x, y, r2;
			int radius = r;
			r2 = r * r;
			setPixel(pix, xCenter, yCenter + radius);
			setPixel(pix, xCenter, yCenter - radius);
			setPixel(pix, xCenter + radius, yCenter);
			setPixel(pix, xCenter - radius, yCenter);

			y = radius;
			x = 1;
			y = (int) (Math.sqrt(r2 - 1) + 0.5);
			while (x < y) {
				    setPixel(pix, xCenter + x, yCenter + y);
				    setPixel(pix, xCenter + x, yCenter - y);
				    setPixel(pix, xCenter - x, yCenter + y);
				    setPixel(pix, xCenter - x, yCenter - y);
				    setPixel(pix, xCenter + y, yCenter + x);
				    setPixel(pix, xCenter + y, yCenter - x);
				    setPixel(pix, xCenter - y, yCenter + x);
				    setPixel(pix, xCenter - y, yCenter - x);
				    x += 1;
				    y = (int) (Math.sqrt(r2 - x*x) + 0.5);
			}
			if (x == y) {
				    setPixel(pix, xCenter + x, yCenter + y);
				    setPixel(pix, xCenter + x, yCenter - y);
				    setPixel(pix, xCenter - x, yCenter + y);
				    setPixel(pix, xCenter - x, yCenter - y);
			}
		}
}
