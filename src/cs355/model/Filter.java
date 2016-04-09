/**
 * 
 */
package cs355.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import cs355.model.image.Image;

/**
 * @author Mark
 *
 */
public class Filter
{
	private static int rMask = 0x00FF0000;
	private static int gMask = 0x0000FF00;
	private static int bMask = 0x000000FF;
	private static float[] unsharpA2 = { 0, -1, 0, -1, 6, -1, 0, -1, 0 };

	// brightness
	public static BufferedImage brightness(Image source, int amount)
	{
		// Convert the image to HSB

		// Make sure to convert the adjustment parameter to the range
		// [-1.0,1.0.].
		//
		// s = r + b
		//
		// where as used in class, r denotes the input brightness and s denotes
		// the output brightness.
		// Do the above operation solely on the brightness channel.

		BufferedImage b = source.getImage();
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
		return b;
	}

	// grayscale (saturation)
	public static BufferedImage grayscaleSaturation(Image source)
	{
		// Convert the image to HSB
		BufferedImage b = source.getImage();
		WritableRaster wr = b.getRaster();

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
				hsb[1] = 0; // zero the saturation channel
				rgbInt = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]); // convert back
																	// to RGB.

				rgb[0] = (rgbInt & rMask) >> 16; // 1337 H4X0RZ
				rgb[1] = (rgbInt & gMask) >> 8;
				rgb[2] = (rgbInt & bMask);

				wr.setPixel(x, y, rgb);
			}
		}

		b.setData(wr);
		return b;
	}

	// contrast
	public static BufferedImage contrast(Image source, int amount)
	{

		// Rather than a straight linear operation, we will use a mapping
		// similar to what Photoshop does. In particular, the contrast will be
		// in the range [-100,100] where 0 denotes no change, -100 denotes
		// complete loss of contrast, and 100 denotes maximum enhancement (8x
		// multiplier). If c is the contrast parameter, then the level operation
		// applied is
		//
		// > s = ((c+100)/100)^4)(r - 128)+128
		//
		// For this operation, you should also convert to HSB, apply the
		// operation to the brightness channel, and convert back to RGB.

		BufferedImage b = source.getImage();
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

				// s = ((c+100)/100)^4 (r-128)+128
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
		return b;
	}

	/**
	 * median filter
	 * 
	 * In this case, you should find the median of all three channels
	 * individually. Put them together to make the ”median color.”
	 * 
	 * At that point, treat the colors as vectors. Compute the squared distance
	 * from the median color to all nine colors from all nine pixels. The color
	 * you select should be the color with the least squared distance from the
	 * median color.
	 * 
	 */
	public static BufferedImage medianFilter(Image source, int windowSize)
	{
		/*
		 * generic algorithm from Wikipedia:
		 * 
		 * allocate outputPixelValue[image width][image height] allocate
		 * window[window width * window height] edgex := (window width / 2)
		 * rounded down edgey := (window height / 2) rounded down for x from
		 * edgex to image width - edgex { for y from edgey to image height -
		 * edgey { i = 0 for fx from 0 to window width { for fy from 0 to window
		 * height { window[i] := inputPixelValue[x + fx - edgex][y + fy - edgey]
		 * i := i + 1 } } sort entries in window[] outputPixelValue[x][y] :=
		 * window[window width * window height / 2] } }
		 */

		// TODO: optimize this into a snake-scrawl of sorted lists.

		BufferedImage b = source.getImage();
		WritableRaster wr = b.getRaster();
		DataBuffer db = wr.getDataBuffer();

		int w = b.getWidth();
		int h = b.getHeight();

		int edgex = windowSize / 2; // automagically rounded down thanks to int
									// division
		int edgey = windowSize / 2;

		int x = edgex;
		int y = edgey;
		int i;

		// note that an array is allocated for each channel here.
		int[][] window = new int[3][windowSize * windowSize];

		int curPixelData = 0;
		int[] windowR = new int[windowSize * windowSize];
		int[] windowG = new int[windowSize * windowSize];
		int[] windowB = new int[windowSize * windowSize];

		int[] minRGB = { 0, 0, 0 };
		int[] curRGB = { 0, 0, 0 };
		int[] medRGB = { 0, 0, 0 };

		int curDist = Integer.MAX_VALUE;
		int minDist = Integer.MAX_VALUE;

		for (x = edgex; x < (w - edgex); x++)
		{
			for (y = edgey; y < (h - edgey); y++)
			{
				i = 0;
				curDist = Integer.MAX_VALUE;
				minDist = Integer.MAX_VALUE;

				for (int fx = 0; fx < windowSize; fx++)
				{

					for (int fy = 0; fy < windowSize; fy++)
					{
						curPixelData = db.getElem(0, ((y + fy - edgey) * w) + (x + fx - edgex));
						window[0][i] = windowR[i] = (curPixelData & rMask) >> 16;
						window[1][i] = windowG[i] = (curPixelData & gMask) >> 8;
						window[2][i] = windowB[i] = (curPixelData & bMask);

						i += 1;
					}
				}

				// sort entries in window[], and pick median value from that
				medRGB = Filter.sortAndSelectMedian(windowR, windowG, windowB);

				/*
				 * 1. Treat the colors as vectors in 3-space.
				 * 
				 * 2. Compute the squared distance from the median color to all
				 * nine colors from all nine pixels.
				 * 
				 * 3. keep the one with smallest distance squared value
				 */
				i = 0;

				for (int fx = 0; fx < windowSize; fx++)
				{
					for (int fy = 0; fy < windowSize; fy++)
					{
						curDist = Integer.MAX_VALUE;

						curRGB[0] = window[0][i];
						curRGB[1] = window[1][i];
						curRGB[2] = window[2][i];

						curDist = calculateDistance(curRGB, medRGB);

						if (curDist < minDist)
						{
							minDist = curDist;
							minRGB = curRGB.clone();
						}

						i += 1;
					}
				}

				wr.setPixel(x, y, minRGB);
			}
		}

		b.setData(wr);
		return b;
	}

	private static int calculateDistance(int[] curRGB, int[] medRGB)
	{
		// pythagorean theorem, sort of. Just use distance squared.
		// a^2 + b^2 + c^2 = d^2

		int a2 = (medRGB[0] - curRGB[0]) * (medRGB[0] - curRGB[0]);
		int b2 = (medRGB[1] - curRGB[1]) * (medRGB[1] - curRGB[1]);
		int c2 = (medRGB[2] - curRGB[2]) * (medRGB[2] - curRGB[2]);

		return a2 + b2 + c2;
	}

	private static int[] sortAndSelectMedian(int[] windowR, int[] windowG, int[] windowB)
	{
		// NOTE: THIS ONLY APPLIES TO 3x3 WINDOWS AT PRESENT.

		if (windowR.length > 9)
		{
			return new int[] { 128, 128, 128 };
		}

		int[] r = windowR.clone();
		int[] g = windowG.clone();
		int[] b = windowB.clone();
		int n = r.length;

		int minR = Integer.MAX_VALUE;
		int minG = Integer.MAX_VALUE;
		int minB = Integer.MAX_VALUE;

		// lame selection sort (only sorts to find the median of 9 elements.)
		int i, j;
		// for (i = 0; i < n - 1; i++)
		for (i = 0; i <= n / 2; i++)
		{
			minR = minG = minB = i;
			for (j = i + 1; j < n; j++)
			{
				if (r[j] < r[minR])
				{
					minR = j;
				}
				if (g[j] < g[minG])
				{
					minG = j;
				}
				if (b[j] < b[minB])
				{
					minB = j;
				}
			}
			// XOR swap algorithm on R
			if (i != minR)
			{
				r[i] = r[i] ^ r[minR];
				r[minR] = r[i] ^ r[minR];
				r[i] = r[i] ^ r[minR];
			}
			// XOR swap algorithm on G
			if (i != minG)
			{
				g[i] = g[i] ^ g[minG];
				g[minG] = g[i] ^ g[minG];
				g[i] = g[i] ^ g[minG];
			}
			// XOR swap algorithm on B
			if (i != minB)
			{
				b[i] = b[i] ^ b[minB];
				b[minB] = b[i] ^ b[minB];
				b[i] = b[i] ^ b[minB];
			}
		}

		int[] median = { 0, 0, 0 };

		median[0] = r[n / 2];
		median[1] = g[n / 2];
		median[2] = b[n / 2];

		return median;
	}

	// mean filter (uniform blurring)
	public static BufferedImage uniformBlur(Image source, int windowSize)
	{
		// This operation should blur the image using a 3 × 3 uniform averaging
		// kernel.
		// The averaging should happen on all three channels in the RGB color
		// space.

		BufferedImage b = source.getImage();
		WritableRaster wr = b.getRaster();
		DataBuffer db = wr.getDataBuffer();

		int[] rgb = { 0, 0, 0 };
		int w = b.getWidth();
		int h = b.getHeight();

		int edgex = windowSize / 2; // automagically rounded down thanks to int
									// division
		int edgey = windowSize / 2;

		int x = edgex;
		int y = edgey;
		int i;
		int meanR, meanG, meanB;

		int curPixelData = 0;
		int[] windowR = new int[windowSize * windowSize];
		int[] windowG = new int[windowSize * windowSize];
		int[] windowB = new int[windowSize * windowSize];
		int[][] window = new int[3][windowSize * windowSize];

		for (x = edgex; x < (w - edgex); x++)
		{
			for (y = edgey; y < (h - edgey); y++)
			{
				i = 0;
				meanR = meanG = meanB = 0;
				rgb = wr.getPixel(x, y, rgb);

				for (int fx = 0; fx < windowSize; fx++)
				{

					for (int fy = 0; fy < windowSize; fy++)
					{
						curPixelData = db.getElem(0, ((y + fy - edgey) * w) + (x + fx - edgex));
						window[0][i] = windowR[i] = (curPixelData & rMask) >> 16;
						window[1][i] = windowG[i] = (curPixelData & gMask) >> 8;
						window[2][i] = windowB[i] = (curPixelData & bMask);

						meanR += windowR[i];
						meanG += windowG[i];
						meanB += windowB[i];

						i += 1;
					}
				}

				meanR /= 9; // takes the floor value for the mean
				meanG /= 9;
				meanB /= 9;

				rgb[0] = meanR; // 1337 H4X0RZ
				rgb[1] = meanG;
				rgb[2] = meanB;

				wr.setPixel(x, y, rgb);
			}
		}

		b.setData(wr);
		return b;
	}

	// edge detection
	public static BufferedImage edgeDetection(Image source, int windowSize)
	{
		/*
		 * This operation computes the gradient magnitude for the image by first
		 * applying Sobel kernels to the image. You will want to convert to HSB
		 * and apply the operation to the brightness channel.
		 * 
		 * At that point, you should convert the resulting value from a float in
		 * the range [0.0, 1.0] to an int in the range [0, 255]. Then combine
		 * the results into a gradient magnitude image. Use the result as the
		 * value for all three channels of RGB.
		 * 
		 * For the Sobel kernels, make sure to divide the result of the
		 * convolution by 8 before using them for the gradient computations.
		 *
		 */

		BufferedImage b = source.getImage();
		WritableRaster wr = b.getRaster();

		int w = b.getWidth();
		int h = b.getHeight();
		int[] rgbGradientVal = { 0, 0, 0 };

		int edgex = windowSize / 2; // automagically rounded down thanks to int
									// division
		int edgey = windowSize / 2;

		int x = edgex;
		int y = edgey;
		int i;

		float[] sobelX = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
		float[] sobelY = { -1, -2, -1, 0, 0, 0, 1, 2, 1 };

		double sobelXResult;
		double sobelYResult;
		double gradientMagnitude;

		float[][][] imageAsHSB = convertToHSB(wr);
		float[][] brightnessChannel = imageAsHSB[2];
		float[] windowB = new float[windowSize * windowSize];

		int gradientVal;

		for (x = edgex; x < (w - edgex); x++)
		{
			for (y = edgey; y < (h - edgey); y++)
			{
				i = 0;

				for (int fx = 0; fx < windowSize; fx++)
				{
					for (int fy = 0; fy < windowSize; fy++)
					{
						windowB[i] = brightnessChannel[x + fx - edgex][y + fy - edgey];

						i += 1;
					}
				}

				sobelXResult = convolve(windowB, sobelX, (float) .125); // note
				// that
				// convolve
				// is normalized
				sobelYResult = convolve(windowB, sobelY, (float) .125); // (see
				// below)

				gradientMagnitude = Math.sqrt(Math.pow(sobelXResult, 2) + Math.pow(sobelYResult, 2));
				// gradientMagnitude = sobelXResult + sobelYResult;

				gradientVal = (int) (255 * gradientMagnitude);

				rgbGradientVal[0] = 128 + gradientVal;
				rgbGradientVal[1] = 128 + gradientVal;
				rgbGradientVal[2] = 128 + gradientVal;

				wr.setPixel(x, y, rgbGradientVal);
			}
		}

		b.setData(wr);
		return b;
	}

	private static float[][][] convertToHSB(WritableRaster wr)
	{
		int x, y;
		int w = wr.getWidth();
		int h = wr.getHeight();
		int[] rgb = { 0, 0, 0 };

		float[] hsb = { 0, 0, 0 };
		float[][][] convertedImage = new float[3][w][h];

		for (x = 0; x < w; x++)
		{
			for (y = 0; y < h; y++)
			{
				rgb = wr.getPixel(x, y, rgb);
				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

				convertedImage[0][x][y] = hsb[0];
				convertedImage[1][x][y] = hsb[1];
				convertedImage[2][x][y] = hsb[2];
			}
		}

		return convertedImage;
	}

	public static float convolve(float[] window, float[] kernel, float scale)
	{
		if (kernel.length != window.length)
		{
			return 0; // convolution not defined on this
		}

		float channelVal = 0;
		float sum = 0;

		int dimension = kernel.length;
		int i;

		for (i = 0; i < dimension; i++)
		{
			sum += (window[i] * kernel[i]);
		}

		sum *= scale;

		channelVal = sum;
		return channelVal;
	}

	public static float convolve(float[] window, float[] kernel)
	{
		return convolve(window, kernel, 1);
	}

	// sharpen

	public static BufferedImage sharpen(Image source, int windowSize)
	{
		// This operation sharpens the image using an unsharp masking kernel
		// with A = 2 (i.e., a 6 in the middle and -1s for the four-connected
		// neighbors, then divide by 2).
		// This operation should be done in the RGB color space.

		// TODO: optimize this into a snake-scrawl of sorted lists.

		BufferedImage b = source.getImage();
		WritableRaster wr = b.getRaster();
		DataBuffer db = wr.getDataBuffer();

		int w = b.getWidth();
		int h = b.getHeight();

		int edgex = windowSize / 2; // automagically rounded down thanks to int
									// division
		int edgey = windowSize / 2;

		int x = edgex;
		int y = edgey;
		int i;

		// note that an array is allocated for each channel here.
		float[][] window = new float[3][windowSize * windowSize];

		int curPixelData = 0;
		float[] windowR = new float[windowSize * windowSize];
		float[] windowG = new float[windowSize * windowSize];
		float[] windowB = new float[windowSize * windowSize];

		int[] newRGB = { 0, 0, 0 };

		int curPixelValR, curPixelValG, curPixelValB;

		for (x = edgex; x < (w - edgex); x++)
		{
			for (y = edgey; y < (h - edgey); y++)
			{
				i = 0;

				// populate window for this pixel
				for (int fx = 0; fx < windowSize; fx++)
				{
					for (int fy = 0; fy < windowSize; fy++)
					{
						curPixelData = db.getElem(0, ((y + fy - edgey) * w) + (x + fx - edgex));
						window[0][i] = windowR[i] = (curPixelData & rMask) >> 16;
						window[1][i] = windowG[i] = (curPixelData & gMask) >> 8;
						window[2][i] = windowB[i] = (curPixelData & bMask);

						i += 1;
					}
				}

				curPixelValR = (int) convolve(windowR, unsharpA2, (float) .5);
				curPixelValG = (int) convolve(windowG, unsharpA2, (float) .5);
				curPixelValB = (int) convolve(windowB, unsharpA2, (float) .5);

				newRGB[0] = (int) (windowR[5] + curPixelValR);
				newRGB[1] = (int) (windowG[5] + curPixelValG);
				newRGB[2] = (int) (windowB[5] + curPixelValB);

				wr.setPixel(x, y, newRGB);
			}
		}

		b.setData(wr);
		return b;
	}

	// mean filter (gaussian)
	// grayscale (rgb)

	// public static float[] multiChannelConvolve(float[][] window, float[][]
	// kernel, float scale)
	// {
	// float[] channels = { 0, 0, 0 };
	// float channel0Sum = 0, channel1Sum = 0, channel2Sum = 0;
	// channel0Sum = convolve(window[0], kernel[0], scale);
	// channel1Sum = convolve(window[1], kernel[1], scale);
	// channel2Sum = convolve(window[2], kernel[2], scale);
	//
	// if (channel0Sum == 0 || channel1Sum == 0 || channel2Sum == 0)
	// {
	// return null;
	// }
	// channels[0] = channel0Sum;
	// channels[1] = channel1Sum;
	// channels[2] = channel2Sum;
	//
	// return channels;
	// }
	// public static float[] multiChannelConvolve(float[][] window, float[][]
	// kernel)
	// {
	// return multiChannelConvolve(window, kernel, 1);
	// }

	/*
	 * Hilarious and interesting broken edge detection code. To look at later.
	 * 
	 * 
	 * /* This operation computes the gradient magnitude for the image by first
	 * applying Sobel kernels to the image
	 * 
	 * then combining the results into a gradient magnitude image. For the Sobel
	 * kernels, make sure to divide the result of the convolution by 8 before
	 * using them for the gradient computations.
	 * 
	 * /* You will want to convert to HSB and apply the operation to the
	 * brightness channel.
	 * 
	 * At that point, you should convert the resulting value from a float in the
	 * range [0.0, 1.0] to an int in the range [0, 255]
	 * 
	 * and use the result as the value for all three channels of RGB. The reason
	 * for this is because the resulting image will be grayscale.
	 * 
	 * BufferedImage b = source.getImage(); WritableRaster wr = b.getRaster();
	 * DataBuffer db = wr.getDataBuffer();
	 * 
	 * int[] rgb = { 0, 0, 0 }; int w = b.getWidth(); int h = b.getHeight();
	 * 
	 * int edgex = windowSize / 2; // automagically rounded down thanks to int
	 * // division int edgey = windowSize / 2;
	 * 
	 * int x = edgex; int y = edgey; int i;
	 * 
	 * int curPixelData = 0; int[] windowR = new int[windowSize * windowSize];
	 * int[] windowG = new int[windowSize * windowSize]; int[] windowB = new
	 * int[windowSize * windowSize]; int[][] window = new int[3][windowSize *
	 * windowSize]; int[][] sobelX = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 }
	 * }; int[][] sobelY = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
	 * 
	 * int[] sobelXResult = new int[3]; int[] sobelYResult = new int[3];
	 * 
	 * for (x = edgex; x < (w - edgex); x++) { for (y = edgey; y < (h - edgey);
	 * y++) { i = 0; rgb = wr.getPixel(x, y, rgb);
	 * 
	 * // this is still in RGB, so that needs fixing
	 * 
	 * for (int fx = 0; fx < windowSize; fx++) { for (int fy = 0; fy <
	 * windowSize; fy++) { curPixelData = db.getElem(0, ((y + fy - edgey) * w) +
	 * (x + fx - edgex)); window[0][i] = windowR[i] = (curPixelData & rMask) >>
	 * 16; window[1][i] = windowG[i] = (curPixelData & gMask) >> 8; window[2][i]
	 * = windowB[i] = (curPixelData & bMask);
	 * 
	 * i += 1; } }
	 * 
	 * sobelXResult = convolve(window, sobelX); // note that convolve // is
	 * normalized sobelYResult = convolve(window, sobelY); // (see below)
	 * 
	 * rgb[0] = sobelXResult[0] + sobelYResult[0]; rgb[1] = sobelXResult[1] +
	 * sobelYResult[1]; rgb[2] = sobelXResult[2] + sobelYResult[2];
	 * 
	 * wr.setPixel(x, y, rgb); } }
	 * 
	 * b.setData(wr); return b;
	 */

	/*
	 * 
	 * More broken code - this will generate television snow if used on a
	 * grayscale image.
	 * 
	 * BufferedImage b = source.getImage(); WritableRaster wr = b.getRaster();
	 * DataBuffer db = wr.getDataBuffer();
	 * 
	 * int w = b.getWidth(); int h = b.getHeight();
	 * 
	 * int edgex = windowSize / 2; // automagically rounded down thanks to int
	 * // division int edgey = windowSize / 2;
	 * 
	 * int x = edgex; int y = edgey; int i;
	 * 
	 * // note that an array is allocated for each channel here. float[][]
	 * window = new float[3][windowSize * windowSize];
	 * 
	 * int curPixelData = 0; float[] windowR = new float[windowSize *
	 * windowSize]; float[] windowG = new float[windowSize * windowSize];
	 * float[] windowB = new float[windowSize * windowSize];
	 * 
	 * int[] newRGB = { 0, 0, 0 };
	 * 
	 * int curPixelValR, curPixelValG, curPixelValB;
	 * 
	 * for (x = edgex; x < (w - edgex); x++) { for (y = edgey; y < (h - edgey);
	 * y++) { i = 0;
	 * 
	 * // populate window for this pixel for (int fx = 0; fx < windowSize; fx++)
	 * { for (int fy = 0; fy < windowSize; fy++) { curPixelData = db.getElem(0,
	 * ((y + fy - edgey) * w) + (x + fx - edgex)); window[0][i] = windowR[i] =
	 * (curPixelData & rMask) >> 16; window[1][i] = windowG[i] = (curPixelData &
	 * gMask) >> 8; window[2][i] = windowB[i] = (curPixelData & bMask);
	 * 
	 * i += 1; } }
	 * 
	 * curPixelValR = (int) convolve(windowR, unsharpA2, (float) .5);
	 * curPixelValG = (int) convolve(windowG, unsharpA2, (float) .5);
	 * curPixelValB = (int) convolve(windowB, unsharpA2, (float) .5);
	 * 
	 * newRGB[0] = (int) (windowR[5] + curPixelValR); newRGB[1] = (int)
	 * (windowG[5] + curPixelValG); newRGB[2] = (int) (windowB[5] +
	 * curPixelValB);
	 * 
	 * wr.setPixel(x, y, newRGB); } }
	 * 
	 * b.setData(wr); return b;
	 */

}
