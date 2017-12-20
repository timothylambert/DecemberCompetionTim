package decemberCodingChallenge;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CodeChallenge
{

	public static void main(String[] args)
	{

		// Initalises the scanner and a number of variables/arrays/arraylist

		Scanner sc = new Scanner(System.in);
		int width = 0;
		int height = 0;
		int supplyLength = 0;
		int[] dimensionHolder = new int[2];
		ArrayList<supplyHolder> coordList = new ArrayList<supplyHolder>();

		// gets the supply length(amount of supply points) and then gets the map
		// dimensions and returns it as a 2 size array, this is then entered into the
		// individual height and weight variables, I could've just used 
		// dimensionHolder[0] and [1] but I feel this makes it easier to read. 
		// Then fill the coordList with supplypoint coords and return it

		supplyLength = getSupplyLengthChoice(sc);
		dimensionHolder = mapDimensions(sc, supplyLength);
		width = dimensionHolder[0];
		height = dimensionHolder[1];
		coordList = getSupplyChoices(supplyLength, sc, width, height, coordList);

		// create the 2d array called map and fill it using the heuristics and calculate
		// methods, finally display the finished map and close the scanner

		int[][] map = createMap(width, height, coordList);
		map = manhattenHeuristics(map, width, height);
		map = manhattenCalculate(map, width, height, coordList);
		displayMap(map, width, height);
		sc.close();
	}

	// This method gets the amount of supply points from the user, contains input
	// validation as there must be at least 1 supply point for the map to function

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

	// This method gets the width and height of the map as chosen by the user and
	// returns it in a 1d int array with 2 slots (one for each value) this method
	// also has input validation as the map has to be bigger than the max possible
	// amount of supply points

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

	// Returns an ArrayList filled with supplyHolder objects which contain the x and y
	// coords of all the supply points the user has entered, there is input
	// validation but if the user places a supply point on an already existing point 
	// nothing happens

	public static ArrayList<supplyHolder> getSupplyChoices(int supplyLength, Scanner sc, int width, int height,
			ArrayList<supplyHolder> coordList)
	{
		boolean temp = true;
		int xSupplyPoint;
		int ySupplyPoint;
		for (int i = 0; i < supplyLength; i++)
		{
			try
			{
				System.out.println("Please enter the supply coordinates");
				System.out.println("Please enter the x coordinate");
				xSupplyPoint = (sc.nextInt());
				System.out.println("Please enter the y coordinate");
				ySupplyPoint = (sc.nextInt());
				temp = true;

				if ((xSupplyPoint >= width) || (ySupplyPoint >= height) || (xSupplyPoint < 0) || ySupplyPoint < 0)
				{
					System.out.println("That location doesn't exist!");
					System.out.println("------------------------------------------------");
					i--;
					temp = false;
				}
				if (temp == true)
				{
					coordList.add(new supplyHolder(xSupplyPoint, ySupplyPoint));
				}

			} catch (InputMismatchException e)
			{
				sc.next();
				System.out.println("You entered an invalid input!");
				System.out.println("------------------------------------------------");
				i--;
			}
		}
		return coordList;
	}

	// In this method I draw the map out filling each element with 8 as a
	// placeholder

	public static int[][] createMap(int width, int height, ArrayList<supplyHolder> coordList)
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

		for (int i = 0; i < coordList.size(); i++)
		{
			map[coordList.get(i).getYCoord()][coordList.get(i).getXCoord()] = 0;
		}
		return map;
	}

	// I realised that each adjacent point to a supply point would be of 1 distance
	// as long as the index existed

	public static int[][] manhattenHeuristics(int[][] map, int width, int height)
	{

		// Iterates through the map and if the index contains 0 (denoting a supply
		// point) if it does, check for adjacent grid squares, if they exist and
		// don't have a 0 in them, place a 1. This is computationally more efficient than 
		// doing the main manhattenCalculate method as that one has as many operations as 
		// there are always number of operations(n) times number of supply points(s) so 
		// manhattenCalculate could potentally have thousands of operations depending 
		// on the number of supplyPoints. manhattenHeuristic has a much smaller worse 
		// case amount of operations

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
	// will return the smallest one which will denote the closest supply point distance

	public static int[][] manhattenCalculate(int[][] map, int width, int height, ArrayList<supplyHolder> coordList)
	{
		int sumA;
		int sumB;
		int sumC;

		// If the value of the coord is 8 (not a supply point or 1 away from a
		// supply point then set sumC as max value and compare each manhatten
		// distance and select the smallest. I set sumC as max so that it can
		// be reset every time and then compared downwards.

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (map[i][j] == 8)
				{
					sumC = 2147483647;

					for (int x = 0; x < coordList.size(); x++)
					{
						if ((sumC) > ((sumA = Math.abs(i - coordList.get(x).getYCoord()))
								+ (sumB = Math.abs(j - coordList.get(x).getXCoord()))))
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
