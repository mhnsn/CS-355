/**
 * 
 */
package cs355.model.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import cs355.model.Filter;

/**
 * @author Mark
 *
 */
public class Image extends CS355Image
{
	private BufferedImage	shellImage;
	private static boolean	loadNewImage;
	// private int rMask = 0x00FF0000;
	// private int gMask = 0x0000FF00;
	// private int bMask = 0x000000FF;
	
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
		checkShellImage();
		setShellImage(Filter.edgeDetection(this, 3));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#sharpen()
	 */
	@Override
	public void sharpen()
	{
		checkShellImage();
		setShellImage(Filter.sharpen(this, 3));
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
		setShellImage(Filter.medianFilter(this, 3));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cs355.model.image.CS355Image#uniformBlur()
	 */
	@Override
	public void uniformBlur()
	{
		checkShellImage();
		setShellImage(Filter.uniformBlur(this, 3));
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
		setShellImage(Filter.grayscaleSaturation(this));
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
		setShellImage(Filter.contrast(this, amount));
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
		setShellImage(Filter.brightness(this, amount));
	}
	
	/**
	 * @return the shellImage
	 */
	private BufferedImage getShellImage()
	{
		return shellImage;
	}
	
	/**
	 * @param shellImage
	 *            the shellImage to set
	 */
	public void setShellImage(BufferedImage shellImage)
	{
		this.shellImage = shellImage;
	}
	
	private void checkShellImage()
	{
		if (loadNewImage && getWidth() != 0 && getHeight() != 0)
		{
			initializeShellImage();
		}
	}
	
	public void initializeShellImage()
	{
		loadNewImage = false;
		
		int w = super.getWidth();
		int h = super.getHeight();
		
		BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = b.getRaster();
		
		int x, y = 0;
		int[] rgba = { 255, 255, 255 };
		boolean oneChannel = false;
		
		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
				super.getPixel(x, y, rgba);
				
				if (!oneChannel)
				{
					if ((rgba[0] != 0) && (rgba[1] == 0) && (rgba[2] == 0) && ((x == 1) || y == 1))
					{
						oneChannel = true;
					}
					
					wr.setPixel(x, y, rgba);
				}
				else
				{
					rgba[1] = rgba[2] = rgba[0];
					wr.setPixel(x, y, rgba);
				}
			}
		}
		
		b.setData(wr);
		setShellImage(b);
	}
}
