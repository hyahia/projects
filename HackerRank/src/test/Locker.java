package test;
/*
* <h1>Amazon Locker Problem!</h1>
* A class to solve Amazon Locker Problem 
* @author  Hossam Yahya
* @version 1.0
* @since   2016-08-27
*/
public class Locker {
	public static void main(String[] args){
		int[][] result = getLockerDistanceGrid(5,3,new int[]{1}, new int[]{1});
		for(int i = 0; i < result.length; i++){
			for(int j = 0; j < result[i].length; j++){
				System.out.print(result[i][j]);
			}
			System.out.println();
		}
	}

/**
 * This method is used to solve the Amazon Locker Problem.
 * @param cityLength This is the city length in blocks.
 * @param cityWidth  This is the city width in blocks.
 * @return int[][] This returns 2-d grid specifying the number of blocks to the closest locker.
 */
  static int[][] getLockerDistanceGrid(int cityLength, int cityWidth, int[] lockerXCoordinates, int[] lockerYCoordinates) {
      int[][] result = new int[cityLength][cityWidth];
      int previousDistance = Integer.MAX_VALUE;
      for(int i = 1; i <= cityLength; i++){
          for(int j = 1; j <= cityWidth; j++){
            for(int k = 1; k <= lockerXCoordinates.length; k++){
                int tempDistance = Math.abs(lockerXCoordinates[k-1]-i) + Math.abs(lockerYCoordinates[k-1]-j);
                if(tempDistance < previousDistance)
                    previousDistance = tempDistance;
            }
            result[i-1][j-1] =  previousDistance; 
            previousDistance = Integer.MAX_VALUE;
          }
      }
      System.out.println(result);
      return result;
  }
}
