
public class DecChallenge
{

	public static void main(String[] args)
	{
		//Initialise variables, please replace these with whatever values you'd like
		
		int height = 5;
		int width = 5;
		String[][] map = new String[height][width];
		int[] xSupply = new int[] {4, 2, 0, 5};
		int[] ySupply = new int[] {4, 1, 4, 5};
		
		//fills map with method mapFill and then fills it with calculated distances from
		//mapCalculate then displays map with displayMap

		map = mapFill(map, xSupply, ySupply, height, width);
		map = mapCalculate(map, xSupply, ySupply, height, width);
		displayMap(map, height, width);
		}
	
	//This Method fills the map with supply points where 
	//applicable. If there are no valid supply points, leave all as null as default,
	//display and exit. If there are points, use a heuristic calculation to work out
	//that each adjacent cell in the map will contain a 1. This might not be more efficient
	//than the later mapCalculate if there's only 1 or 2 supply points but if you
	//have many supply points this heuristic calculation will become more and more
	//efficient
	
	public static String[][] mapFill(String[][] map, int[] xSupply, 
			int[] ySupply, int height, int width)
	{
		boolean pointPassed = false;
		
		//Iterate through the paired x and y supply coord arrays and if they aren't out
		//of bounds then set the location as true and set pointPassed to true to indicate
		//that we've got at least 1 successful supply point placed
		
		for (int i = 0; i < xSupply.length; i++)
		{
			if (!(xSupply[i] < 0) && !(ySupply[i] < 0) && 
					!(xSupply[i] >= width) && !(ySupply[i] >= height))
			{
				pointPassed = true;
				map[ySupply[i]][xSupply[i]] = "0";
				
				//If an adjacent cell is in bounds and it's null (not another supply point)
				//then set it as 1, this sets all the available adjacent cells as 1 without
				//having to iterate through the map, we do that only once
				
				if (!((ySupply[i] - 1) < 0))
				{
					if ((map[ySupply[i] - 1][xSupply[i]] == null))
					{
						map[ySupply[i] - 1][xSupply[i]] = "1";
					}
				}
				if (!((xSupply[i] - 1) < 0))
				{
					if ((map[ySupply[i]][xSupply[i] - 1] == null))
					{
						map[ySupply[i]][xSupply[i] - 1] = "1";
					}
				}
				if (!((ySupply[i] + 1) >= height))
				{
					if ((map[ySupply[i] + 1][xSupply[i]] == null))
					{
						map[ySupply[i] + 1][xSupply[i]] = "1";
					}
				}	
				if (!((xSupply[i] + 1) >= width))
				{
					if ((map[ySupply[i]][xSupply[i] + 1] == null))
					{
						map[ySupply[i]][xSupply[i] + 1] = "1";
					}
				}
			}
			else 
			{
				System.out.println("point (" + xSupply[i] + "," 
						+ ySupply[i] + ") is invalid");
			}
		}
		System.out.println("-------------------------------");
		
		//If there are no valid supply points, display the current null map and then
		//exit from the system to avoid any other code execution
		
		if (pointPassed == false)
		{
			System.out.println("No valid supply points!");
			System.out.println("-------------------------------");
			displayMap(map, height, width);
			System.exit(0);
		}
		
		return map;
	}
	
	//This method calculates the distances from supply points that have not been 
	//calculated using the heuristics calculation in 
	
	public static String[][] mapCalculate(String[][] map, int[] xSupply,
			int[] ySupply, int height, int width)
	{
		int sumA, sumB, sumC;
		
		//for each index, if map[i][j] == null (won't contain 1 or 0), check the distance
		//from the point to each supply point and then take the lowest value from those
		//results and fill in the map with that value
				
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (map[i][j] == null)
				{
					sumC = 2147483647;
					
					//This uses the manhatten calculation to work out the distances apart
					//from the ones calculated before algorithm = (|a - c| + |b - d|)
					//with a and b being the current map index and c and d being the
					//supply point co-ords
					
					for (int x = 0; x < xSupply.length; x++)
					{
						if (!(xSupply[x] < 0) && !(ySupply[x] < 0) && 
								!(xSupply[x] >= width) && !(ySupply[x] >= height))
						{
							if ((sumC) > ((sumA = Math.abs(i - ySupply[x]))
									+ (sumB = Math.abs(j - xSupply[x]))))
							{
								sumC = (sumA + sumB);
							}
						}
					}
					map[i][j] = Integer.toString(sumC);
				}
				
			}
		}
		
		return map;
	}
	
	//This method displays the map, simply iterating through it and printing the values
	//in a grid format
	
	public static void displayMap(String[][] map, int height, int width)
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
	}

}
