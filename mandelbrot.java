import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;



public class mandelbrot {
	
	public static int SCREEN_WIDTH = 640;
	public static int SCREEN_HEIGHT = 640;
	
	public static class MyPanel extends JPanel{

		public float complexPlaneWidth = 3.5f;
		public float complexPlaneLeftEdgeCoord = -2.2f;
		public float imaginaryPlaneHeight = 3f;
		public float imaginaryPlaneLeftEdgeCoord = 1.4f;
		public int MAXCOUNT = 1000;
		public int NUM_MULTISAMPLES = 10;
		
		public void paintComponent(Graphics gfx) {
			
			gfx.setColor(Color.WHITE);
			gfx.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			Color c;
			for (int y = 0; y < SCREEN_HEIGHT; y++) {
				for (int x = 0; x < SCREEN_WIDTH; x++) {
					
					float[] colorAcc = new float[3];
					
					for (int ms = 0; ms < NUM_MULTISAMPLES; ms++) {
						
						float msx = x + (float)Math.random();
						float msy = y + (float)Math.random();
						
						float cReal = computeRealPartFromX(msx);
						float cImg = computeImaginaryPartFromY(msy);
						
						float zReal = 0;
						float zImg = 0;
						
						int count = 0;
						
						while (zReal*zReal + zImg*zImg <= 2*2 && count < MAXCOUNT) {
							float nextZReal = zReal*zReal - zImg*zImg + cReal;
							float nextZImg = 2*zReal*zImg + cImg; 
							
							zReal = nextZReal;
							zImg = nextZImg;
							
							count += 1;
						}
						
						if (count == MAXCOUNT) {
							colorAcc[0] += 255;
							colorAcc[1] += 255;
							colorAcc[2] += 255;
						} else {
							// System.out.println(count);
							float[] vals = mapCountToColor((int)easeOutQuint(count, 0, 255, MAXCOUNT));
							colorAcc[0] += vals[0];
							colorAcc[1] += vals[1];
							colorAcc[2] += vals[2];
						}
						 
					}
					
					colorAcc[0] /= NUM_MULTISAMPLES;
					colorAcc[1] /= NUM_MULTISAMPLES;
					colorAcc[2] /= NUM_MULTISAMPLES;
					Color pc = new Color((int)colorAcc[0],(int) colorAcc[1],(int) colorAcc[2]);
					
					setPixel(x,y,pc,gfx);
				}
			}
			
		}
		
		private void setPixel(int x, int y, Color c, Graphics gfx) {
			gfx.setColor(c);
			gfx.drawLine(x, y, x, y);
			//System.out.println(""+ x + " " + y + " " + c.toString());
		}
		
		private float computeRealPartFromX(float x) {
			float f = x / SCREEN_WIDTH;
			float realPart = f * complexPlaneWidth;
			realPart = realPart + complexPlaneLeftEdgeCoord;
			
			return realPart;
		}
		
		private float computeImaginaryPartFromY(float y) {
			y = -y;
			float f = y / SCREEN_HEIGHT;
			float imaginaryPart = f * imaginaryPlaneHeight;
			imaginaryPart = imaginaryPart + imaginaryPlaneLeftEdgeCoord;
			
			return imaginaryPart;
		}
		
		private float[] mapCountToColor(int count) {
			float[] vals = new float[3];
			vals[0] = dumbShit(count,0);
			vals[1] = dumbShit(count, 1);
			vals[2] = dumbShit(count, 2);
			return vals;
		}
		
		private float easeOutQuint(float t, float b, float c, float d) {
			t /= d;
			t--;
			return (c*(t*t*t*t*t + 1) + b);
		}
		
		private float dumbShit(int temp, float fudgeFactor) {
			return (float)((Math.sin((float)temp + fudgeFactor)+1)/2)*255;
		}


	}

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		MyPanel p = new MyPanel();
		frame.add(p);
		frame.show();

	}

}
