/**
 * 
 */
package cs355.model;

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

	// brightness
	// grayscale (saturation)
	// contrast

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
	// public static Image medianFilter(int windowSize, Image source)
	public static BufferedImage medianFilter(int windowSize, Image source)
	{
		System.out.println("medianFilter()");

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

		int[][] channelR = new int[w][h];
		int[][] channelG = new int[w][h];
		int[][] channelB = new int[w][h];

		int edgex = windowSize / 2; // automagically rounded down thanks to int
									// division
		int edgey = windowSize / 2;

		int x = edgex;
		int y = edgey;
		int i;

		// note that an array is allocated for each channel here.
		int[][] window = new int[3][windowSize * windowSize];

		int[] windowR = new int[windowSize * windowSize];
		int[] windowG = new int[windowSize * windowSize];
		int[] windowB = new int[windowSize * windowSize];

		int[] minRGB = { 0, 0, 0 };
		int[] curRGB = { 0, 0, 0 };
		int[] medRGB = { 0, 0, 0 };

		int curDist = Integer.MAX_VALUE;
		int minDist = Integer.MAX_VALUE;
		int curPixelData = 0;

		for (x = edgex; x < (w - edgex); x++)
		{
			// System.out.println("Row " + x);
			for (y = edgey; y < (h - edgey); y++)
			{
				// System.out.println("\tCol " + y);
				i = 0;
				curDist = Integer.MAX_VALUE;
				minDist = Integer.MAX_VALUE;

				for (int fx = 0; fx < windowSize; fx++)
				{
					// System.out.println("\t--Read windowX " + fx);

					for (int fy = 0; fy < windowSize; fy++)
					{
						// System.out.println("\t----Read windowY " + fy);

						// TODO: verify behavior
						// rgb[0] = (rgbInt & rMask) >> 16; // 1337 H4X0RZ
						// rgb[1] = (rgbInt & gMask) >> 8;
						// rgb[2] = (rgbInt & bMask);

						// window[0][i] = windowR[i] = (db.getElem(0, ((x + fx -
						// edgex) * w) + (y + fy - edgey))
						// & rMask) >> 16;
						// window[1][i] = windowG[i] = (db.getElem(0, ((x + fx -
						// edgex) * w) + (y + fy - edgey))
						// & gMask) >> 8;
						// window[2][i] = windowB[i] = (db.getElem(0, ((x + fx -
						// edgex) * w) + (y + fy - edgey)) & bMask);

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
				 * 1. Treat the colors as vectors.
				 * 
				 * 2. Compute the squared distance from the median color to all
				 * nine colors from all nine pixels.
				 * 
				 * 3. The color you select should be the color with the least
				 * squared distance from the median color.
				 */

				// calculate XYZ distance for each pixel
				// keep the one with smallest distance

				i = 0;

				for (int fx = 0; fx < windowSize; fx++)
				{
					// System.out.println("\t--Calculate distance windowX " +
					// fx);
					for (int fy = 0; fy < windowSize; fy++)
					{
						// System.out.println("\t--Calculate distance windowY "
						// + fy);
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

				// System.out.println("\tAssigning pixel (" + x + ", " + y +
				// ")");
				wr.setPixel(x, y, minRGB);
			}
		}

		b.setData(wr);
		return b;
	}
	// mean filter (uniform blurring)
	// sharpen
	// edge detection

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

		// List r = Arrays.asList(windowR);
		// List g = Arrays.asList(windowG);
		// List b = Arrays.asList(windowB);
		//
		// Collections.sort(r);
		// Collections.sort(g);
		// Collections.sort(b);
		//
		int[] median = { 0, 0, 0 };

		median[0] = r[n / 2];
		median[1] = g[n / 2];
		median[2] = b[n / 2];

		return median;
	}

	// mean filter (gaussian)
	// grayscale (rgb)
}
