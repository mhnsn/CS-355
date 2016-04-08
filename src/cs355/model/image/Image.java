/**
 * 
 */
package cs355.model.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import cs355.model.Filter;

/**
 * @author Mark
 *
 */
public class Image extends CS355Image
{
	private BufferedImage shellImage;
	private static boolean loadNewImage;
	private int rMask = 0x00FF0000;
	private int gMask = 0x0000FF00;
	private int bMask = 0x000000FF;

	/**
	 * 
	 */
	public Image()
	{
		super();
		loadNewImage = true;
	}

	/**
	 * @param width
	 * @param height
	 */
	public Image(int width, int height)
	{
		super(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#getImage()
	 */
	@Override
	public BufferedImage getImage()
	{
		// System.out.println("getImage()");

		checkShellImage();
		return getShellImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#edgeDetection()
	 */
	@Override
	public void edgeDetection()
	{
		// TODO Auto-generated method stub
		checkShellImage();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#sharpen()
	 */
	@Override
	public void sharpen()
	{
		// TODO Auto-generated method stub
		checkShellImage();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#medianBlur()
	 */
	@Override
	public void medianBlur()
	{
		checkShellImage();
		System.out.println("medianBlur()");

		setShellImage(Filter.medianFilter(3, this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#uniformBlur()
	 */
	@Override
	public void uniformBlur()
	{
		// TODO Auto-generated method stub
		checkShellImage();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#grayscale()
	 */
	@Override
	public void grayscale()
	{
		checkShellImage();

		// Convert the image to HSB
		BufferedImage b = getShellImage();
		WritableRaster wr = b.getRaster();

		float[] hsb = { 0, 0, 0 };
		int[] rgb = { 0, 0, 0 };
		int x = 0;
		int w = b.getWidth();
		int y = 0;
		int h = b.getHeight();
		int rgbInt = 0;

		int[] rgbB4 = { 0, 0, 0 };

		for (x = 0; x < w; x++)
		{
			// System.out.println("Row: " + x);
			for (y = 0; y < h; y++)
			{
				// System.out.println("\tColumn: " + y);
				rgb = rgbB4 = wr.getPixel(x, y, rgb);
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				// float s = hsb[1];
				// if (rgb[0] - rgb[1] == 0)
				hsb[1] = 0; // zero the saturation channel
				// if (s != hsb[1])
				// {
				// // System.out.println("Saturation changed.");
				// }
				rgbInt = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]); // convert back
																	// to RGB.

				rgb[0] = (rgbInt & rMask) >> 16; // 1337 H4X0RZ
				rgb[1] = (rgbInt & gMask) >> 8;
				rgb[2] = (rgbInt & bMask);

				// if (rgb[0] != rgbB4[0])
				// {
				// System.out.println("===============================================================");
				// System.out.println((rgb[0] & rgbB4[0]));
				// System.out.println(((rgb[0] & rgbB4[0]) & noChange));
				// System.out.println(noChange & noChange);
				//
				// System.out.println("Red (before): [" + rgbB4[0] + "]");
				// System.out.println("Red (after): [" + rgb[0] + "]");
				// }
				//
				// if (rgb[1] != rgbB4[1])
				// {
				// System.out.println("Green (before): [" + rgbB4[1] + "]");
				// System.out.println("Green (after): [" + rgb[1] + "]");
				// }
				//
				// if (rgb[2] != rgbB4[2])
				// {
				// System.out.println("Blue (before): [" + rgbB4[2] + "]");
				// System.out.println("Blue (after): [" + rgb[2] + "]");
				// }

				wr.setPixel(x, y, rgb);
			}
		}

		b.setData(wr);
		setShellImage(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#contrast(int)
	 */
	@Override
	public void contrast(int amount)
	{
		checkShellImage();

		// Rather than a straight linear operation, we will use a mapping
		// similar to what Photoshop does. In particular, the contrast will be
		// in the range [-100,100] where 0 denotes no change, -100 denotes
		// complete loss of contrast, and 100 denotes maximum enhancement (8x
		// multiplier). If c is the contrast parameter, then the level operation
		// applied is
		//
		// > s = ((c+100)/100)^4)(r − 128)+128
		//
		// For this operation, you should also convert to HSB, apply the
		// operation to the brightness channel, and convert back to RGB.

		BufferedImage b = getShellImage();
		WritableRaster wr = b.getRaster();
		float contrast = (float) amount;

		float[] hsb = { 0, 0, 0 };
		int[] rgb = { 0, 0, 0 };
		int x = 0;
		int w = b.getWidth();
		int y = 0;
		int h = b.getHeight();
		int rgbInt = 0;
		float factor;

		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
				rgb = wr.getPixel(x, y, rgb);
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

				// s = ((c+100)/100)^4 (r−128)+128
				factor = contrast + 100;
				factor /= 100;
				factor *= factor * factor * factor;
				factor *= (hsb[2] - .5);
				factor += .5;

				// hsb[2] = (float) (Math.pow(((contrast + 100) / 100), 4) *
				// (factor)) + 128;
				hsb[2] = factor;

				if (hsb[2] > 1)
				{
					hsb[2] = 1;
				}
				else if (hsb[2] < 0)
				{
					hsb[2] = 0;
				}

				rgbInt = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]); // convert back
																	// to RGB.

				rgb[0] = (rgbInt & rMask) >> 16; // 1337 H4X0RZ
				rgb[1] = (rgbInt & gMask) >> 8;
				rgb[2] = (rgbInt & bMask);

				wr.setPixel(x, y, rgb);
			}
		}

		b.setData(wr);
		setShellImage(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#brightness(int)
	 */
	@Override
	public void brightness(int amount)
	{
		checkShellImage();

		// Convert the image to HSB

		// Make sure to convert the adjustment parameter to the range
		// [-1.0,1.0.].
		//
		// s = r + b
		//
		// where as used in class, r denotes the input brightness and s denotes
		// the output brightness.
		// Do the above operation solely on the brightness channel.

		BufferedImage b = getShellImage();
		WritableRaster wr = b.getRaster();
		double brightness = ((double) amount) / ((double) 100);

		float[] hsb = { 0, 0, 0 };
		int[] rgb = { 0, 0, 0 };
		int x = 0;
		int w = b.getWidth();
		int y = 0;
		int h = b.getHeight();
		int rgbInt = 0;

		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
				rgb = wr.getPixel(x, y, rgb);
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				hsb[2] += brightness; // add the brightness factor to output (s
										// = r + b)
				if (hsb[2] > 1)
				{
					hsb[2] = 1;
				}
				else if (hsb[2] < 0)
				{
					hsb[2] = 0;
				}

				rgbInt = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]); // convert back
																	// to RGB.

				rgb[0] = (rgbInt & rMask) >> 16; // 1337 H4X0RZ
				rgb[1] = (rgbInt & gMask) >> 8;
				rgb[2] = (rgbInt & bMask);

				wr.setPixel(x, y, rgb);
			}
		}

		b.setData(wr);
		setShellImage(b);
	}

	/**
	 * @return the shellImage
	 */
	private BufferedImage getShellImage()
	{
		// System.out.println("\tgetShellImage()");

		return shellImage;
	}

	/**
	 * @param shellImage
	 *            the shellImage to set
	 */
	public void setShellImage(BufferedImage shellImage)
	{
		// System.out.println("Setting shell image.");
		this.shellImage = shellImage;
	}

	private void checkShellImage()
	{
		// System.out.println("Checking shell image.");

		if (loadNewImage && getWidth() != 0 && getHeight() != 0)
		{
			initializeShellImage();
		}
	}

	public void initializeShellImage()
	{
		System.out.println("Initializing shell image.");
		loadNewImage = false;

		int w = super.getWidth();
		int h = super.getHeight();

		BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = b.getRaster();

		int x, y = 0;
		int[] rgba = { 255, 255, 255 };

		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
				wr.setPixel(x, y, super.getPixel(x, y, rgba));
			}
		}

		b.setData(wr);
		setShellImage(b);
	}
}
