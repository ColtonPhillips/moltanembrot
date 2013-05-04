import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class mandelbrot {
    
    public static int SCREEN_WIDTH = 640;
	public static int SCREEN_HEIGHT = 640;
    public static float ZOOM_FACTOR = 0.8999999f;
    
	public static class MyPanel extends JPanel{

        public float REAL_PLANE_WIDTH = 3.5f;
        public float REAL_PLANE_X = -2.2f;

        public float IMAGINARY_PLANE_HEIGHT = 3f;
        public float IMAGINARY_PLANE_Y = 1.4f;
        
		public int MAX_COUNT = 1000;
		
        public Rectangle2D.Float complexView;

        public MyPanel() {
            complexView = new Rectangle2D.Float(
                    REAL_PLANE_X, 
                    IMAGINARY_PLANE_Y,
                    REAL_PLANE_WIDTH,
                    IMAGINARY_PLANE_HEIGHT
            );
        }

        private int mandel(float cReal, float cImg) {
        	int count = 0;
        	float zReal = 0;
			float zImg = 0;
			
			while (zReal*zReal + zImg*zImg <= 2*2 && count < MAX_COUNT) {
				float nextZReal = zReal*zReal - zImg*zImg + cReal;
				float nextZImg = 2*zReal*zImg + cImg; 
				zReal = nextZReal;
				zImg = nextZImg;
				count += 1;
			}
        	return count;
        }
        
		public void paintComponent(Graphics gfx) {
	
            System.out.println("" + complexView.x + " " + complexView.y + " " + complexView.height + " " + complexView.width);    
           
            complexView = zoomedInRect2DF(complexView,ZOOM_FACTOR);
			gfx.setColor(Color.WHITE);
			gfx.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

			Color c;
			for (int y = 0; y < SCREEN_HEIGHT; y++) {
				for (int x = 0; x < SCREEN_WIDTH; x++) {
					float[] colorAcc = new float[3];	
					float cReal = computeRealPartFromX(x);
					float cImg = computeImaginaryPartFromY(y);
					float count = mandel(cReal, cImg);
					
					if (count == MAX_COUNT) {
						colorAcc[0] = 255;
						colorAcc[1] = 255;
						colorAcc[2] = 255;
					} 
					else {
						float[] vals = mapCountToColor((int)easeOutQuint(count, 0, 255, MAX_COUNT));
						colorAcc[0] = vals[0];
						colorAcc[1] = vals[1];
						colorAcc[2] = vals[2];
					}
					
					Color pc = new Color((int)colorAcc[0],(int) colorAcc[1],(int) colorAcc[2]);
					setPixel(x,y,pc,gfx);
				}
			}
		}
		
        private Rectangle2D.Float zoomedInRect2DF(Rectangle2D.Float rect, float scale) {
            float newHeight = rect.height * scale;
            float newWidth = rect.width * scale;
            //float deltaY = (newHeight - rect.height) * 0.5f;

            Rectangle2D.Float newRect = new Rectangle2D.Float(
                    rect.x,
                    rect.y,
                    newWidth,
                    newHeight
            );

            return newRect;
        }

		private void setPixel(int x, int y, Color c, Graphics gfx) {
			gfx.setColor(c);
			gfx.drawLine(x, y, x, y);
		}
		
		private float computeRealPartFromX(float x) {
			float f = x / SCREEN_WIDTH;
			float realPart = f * complexView.width;
			realPart = realPart + complexView.x;
			return realPart;
		}
		
		private float computeImaginaryPartFromY(float y) {
			y = -y;
			float f = y / SCREEN_HEIGHT;
			float imaginaryPart = f * complexView.height;
			imaginaryPart = imaginaryPart + complexView.y;
			return imaginaryPart;
		}
		
		private float[] mapCountToColor(int count) {
			float[] vals = new float[3];
			vals[0] = dumbShit(count,0);
			vals[1] = dumbShit(count, 0);
			vals[2] = dumbShit(count, 0);
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
