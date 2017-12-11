package decemberCodingChallenge;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CodeChallenge
{

	public static void main(String[] args)
	{

		Scanner sc = new Scanner(System.in);
		int width = 0;
		int height = 0;
		int supplyLength = 0;
		int[] dimensionHolder = new int[2];
		int[] xSupplyPoint = new int[1];
		int[] ySupplyPoint = new int[1];

		supplyLength = getSupplyLengthChoice(sc);
		xSupplyPoint = new int[supplyLength];//
		ySupplyPoint = new int[supplyLength];//
		dimensionHolder = mapDimensions(sc, supplyLength);
		width = dimensionHolder[0];
		height = dimensionHolder[1];

		for (int i = 0; i < supplyLength; i++)
		{
			try
			{
				System.out.println("Please enter the supply coordinates");
				System.out.println("Please enter the x coordinate");
				xSupplyPoint[i] = Math.abs(sc.nextInt());
				System.out.println("Please enter the y coordinate");
				ySupplyPoint[i] = Math.abs(sc.nextInt());
				
				if ((xSupplyPoint[i] > width) || (ySupplyPoint[i] > height))
				{
					System.out.println("That location doesn't exist!");
					System.out.println("------------------------------------------------");
					i--;
				}
				
			} 
			catch (InputMismatchException e)
			{
				sc.next();
				System.out.println("You entered an invalid input!");
				System.out.println("------------------------------------------------");
				i--;
			}		
		}
		
		
		
		int[][] map = createMap(width, height, xSupplyPoint, ySupplyPoint);
		map = manhattenHeuristics(map, width, height);
		map = manhattenCalculate(map, width, height, xSupplyPoint, ySupplyPoint);
		displayMap(map, width, height);
		sc.close();
	}

	public static int getSupplyLengthChoice(Scanner sc)
	{
		int supplyLength;
		boolean check = true;

		while (check == true)
		{
			try
			{
				System.out.println("How many supply points would you like?");
				supplyLength = sc.nextInt();
				if (!(supplyLength == 0))
				{
					return supplyLength;
				} else
				{
					System.out.println("There must be at least 1 supply point!");
					System.out.println("------------------------------------------------");
				}

			} catch (InputMismatchException e)
			{
				sc.next();
				System.out.println("You entered an invalid input");
				System.out.println("------------------------------------------------");
			}
		}
		return 0;

	}

	public static int[] mapDimensions(Scanner sc, int supplyLength)
	{
		boolean check = true;
		int height;
		int width;
		int dimensionHolder[] = new int[2];
		while (check == true)
		{
			try
			{
				System.out.println("Please enter the map x axis: ");
				width = sc.nextInt();
				System.out.println("Please enter the map y axis: ");
				height = sc.nextInt();
				if (!((width * height) < supplyLength))
				{
					dimensionHolder[0] = width;
					dimensionHolder[1] = height;
					return dimensionHolder;
				} else
				{
					System.out.println("You can't have more Supply Points than the map size");
					System.out.println("------------------------------------------------");
				}
			} catch (InputMismatchException e)
			{
				sc.next();
				System.out.println("You entered an invalid input");
				System.out.println("------------------------------------------------");
			}
		}
		return dimensionHolder;
	}


	// In this method I draw the map out filling each element with 8 as a
	// placeholder

	public static int[][] createMap(int width, int height, int[] xSupplyPoint, int[] ySupplyPoint)
	{

		// creates the map 2d array with a width and height value

		int[][] map = new int[height][width];

		// Iterates through the array and gives each index the value 8

		int inc = 8;
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				map[i][j] = inc;
			}
		}

		// iterate through the x and y coords for the supply points, adds a 0 at
		// each index that matches in the map

		for (int i = 0; i < xSupplyPoint.length; i++)
		{
			map[ySupplyPoint[i]][xSupplyPoint[i]] = 0;
		}
		return map;
	}

	// I realised that each adjacent point to a supply point would be of 1 distance
	// as long as the index existed

	public static int[][] manhattenHeuristics(int[][] map, int width, int height)
	{

		// Iterates through the map and if the index contains 0 (denoting a supply
		// point)
		// if it does, check for adjacent grid squares, if they exist place a 1

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (map[i][j] == 0)
				{
					if (!(i - 1 < 0))
					{
						if (!(map[i - 1][j] == 0))
						{
							map[i - 1][j] = 1;
						}
					}
					if (!(j - 1 < 0))
					{
						if (!(map[i][j - 1] == 0))
						{
							map[i][j - 1] = 1;
						}
					}
					if (!(i + 1 >= height))
					{
						if (!(map[i + 1][j] == 0))
						{
							map[i + 1][j] = 1;
						}
					}
					if (!(j + 1 >= width))
					{
						if (!(map[i][j + 1] == 0))	
						{
							map[i][j + 1] = 1;
						}
					}
				}
			}
		}
		return map;
	}

	// This method calculates the manhatten distance to every supply point and then
	// will
	// return the smallest one which will denote the closest supply point distance

	public static int[][] manhattenCalculate(int[][] map, int width, int height, int[] xSupplyPoint, int[] ySupplyPoint)
	{
		int sumA;
		int sumB;
		int sumC;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{

				// If the value of the coord is 8 (not a supply point or 1 away from a
				// supply point then set sumC as max value and compare each manhatten
				// distance and select the smallest. I set sumC as max so that it can
				// be reset every time and then compared downwards.

				if (map[i][j] == 8)
				{
					sumC = 2147483647;

					for (int x = 0; x < xSupplyPoint.length; x++)
					{
						sumA = Math.abs(i - ySupplyPoint[x]);
						sumB = Math.abs(j - xSupplyPoint[x]);
						if ((sumC) > (sumA + sumB))
						{
							sumC = (sumA + sumB);
						}
					}
					map[i][j] = sumC;
				}
			}
		}
		return map;
	}

	// This simple method iterates and prints and displays the 2d array

	public static void displayMap(int[][] map, int width, int height)
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				System.out.print(map[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
