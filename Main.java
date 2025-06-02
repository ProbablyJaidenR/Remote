/*************************************************************************
 *
 *  Pace University
 *  Spring 2024
 *  Algorithms and Computing Theory
 *
 *  Course: CS 242
 *  Team members: Jaiden Roman, Mohamed El Amine Ais
 *  Collaborators: None
 *  References: https://www.topcoder.com/thrive/articles/kadanes-algorithms
 *
 *  Assignment: 1
 *  Problem: Maximum Subarray Problem
 *  Description: The Maximum Subarray Problem is the task of finding the contiguous subarray, within an array of numbers, that has the largest sum.
 *
 *  Input:  Array (of random integers)
 *  Output: Integer (Subarray Sum)
 *
 *  Visible data fields:
 *  rand, scan, list, startTime, max, sum, mid, high, low, right_sum, left_sum, cross_sum, max_so_far, max_end_here
 *
 *  Visible methods:
 *  subArrayBrute, maxSubarray, maxCrossingSubarray, kadaneSubArray
 *
 *
 *   Remarks
 *   -------
 *                       10^3             10^4            10^5          500000          10^6
 *   Brute Force:       2101600         20324000        1280852100    33153499700   128818934800
 *   Divide/Conquer:    452799          2277600         17720700      28514000      39128500
 *   Kadane's:          38600           226300          2306300       4382600       4454100
 * 
 *  Based on the running times observed, draw conclusions about the running times obtained in the analysis. Do they match or not?
 * 
 *  Using 10 as a base for increasing powers, we can accurately define what values a linear and quadratic function would produce. For our case, a 
 *  linear growth implies that for every power over, the digits should increase by one because we're doing n work 10 times more. Kadane's algorithm directly 
 *  follows this logic. It starts off with 5 digits (38600), then increase by 1 for every power of 10 as apparent on our chart. 
 * 
 *  A quadratic function of n^2 implies that for every power over, the digits should increase by two because we're doing n work 10 times more, which is 
 *  multiplied by itself to get n^2. Since quadratic functions grow exponentially over time, as our n grows larger, we can see a quardratic growth more evidently.
 *  From 20324000 (8 digits) to 1280852100 (10 digits), there is a 2 digit jump. The same could be said from 1280852100 (10 digits) to 128818934800 (12 digits).
 * 
 *  Our divide and conquer algorithm is somewhat between the values produced by Kadane's and Brute Force. Noteably, Divide and Conquer generally grows similarly to 
 *  our linear algorithm. It tends to also grow by 1 digit, showing a more linear trend.  Though we do see it runs slower than Kadane's, it grows similarly even with 
 *  large inputs. From 38600 to 226300, there's a 1 digit jump. For 226300 to 2306300, there is also a 1 digit jump.
 * 
 *
 *
 *************************************************************************/
import java.util.Random;
import java.util.Scanner;


public class Main
//Author: Jaiden/Amine
//Date: 2/15/2025
//Description: Used to test the running times of three different algorithms for finding the contiguous subarray.
//Visible Methods and Data: scan, size, rand, list, subArrayBrute, maxSubarray, kadanaeSubArray, maxCrossingSubarray
{
    public static void main(String[] args) 
    {
        Scanner scan = new Scanner(System.in);
        //Initializes where we ask for user input 

        System.out.println("Please enter the size of your array: ");
        int size = scan.nextInt();
        //Stores the user inputed data for the size

        Random rand = new Random();
        //Initializes the randomization process for array elements.

        int[] list = new int[size];
        //Creates our array from user's input

        for(int i = 0; i < size; i++)
        {
            list[i] = rand.nextInt();
        }
        //Randomizes each element of our array

        long startTime = System.nanoTime();
        //stores the time now
        subArrayBrute(list);
        //displays the time elapsed.
        System.out.println("Brute Force t= "+(System.nanoTime() - startTime)+" nanosecs.");

        startTime = System.nanoTime();
        maxSubarray(list, 0, size-1);
        System.out.println("Divide and Conquer t= "+(System.nanoTime() - startTime)+" nanosecs.");

        startTime = System.nanoTime();
        kadaneSubArray(list);
        System.out.println("Kadane t= "+(System.nanoTime() - startTime)+" nanosecs.");


    }

    public static int subArrayBrute(int[] A)
    //Algorithm meant to brute force their way through finding the contiguous subarray by making n comparisons n times, taking n^2 time.
    {
        int max = Integer.MIN_VALUE;
        //Sets a placeholder for our maximum sum.

        for(int i=0; i < A.length ; i++)
        {
            int sum = 0;
            //The sum found to be compared with max's current maximum.
            
            for(int j = i; j < A.length; j++)
            {
                sum = sum + A[j];
                max = Math.max(max, sum);
            } 
            //For each element in the array, add it to the sum and find out if our maximum is that sum.
        }
        //For each element of our array, keep searching for the max sum in comparison with the other elements.

        return max;
    }

    public static int maxSubarray(int[] A, int low, int high)
    //Recursive algorithm meant to find the contiguous subarray. It splits our array to find 3 sums before comparing them, costing nlog(n) time. It uses the maxCrossingSubarray method along with itself.
    {
        if(high == low) return A[low];
        else
        {
            int mid = (low+high)/2;
            //indicates the middle position of the array.
            int left_sum = maxSubarray(A, low, mid);
            //Indicates the sum on the left side of that middle position.
            int right_sum = maxSubarray(A, mid+1, high);
            //Indicates the sum on the right side of that middle position.
            int cross_sum = maxCrossingSubarray(A, low, mid, high);
            //Indicates the crossing subarray of the array, expanding outward from the middle to find the largest sum.
            
            int max = Math.max(left_sum, Math.max(right_sum, cross_sum));
            //Finds the maximum sum out of the three sums produced.
            
            return max;
        }
    }

    static int maxCrossingSubarray(int[] A, int low, int mid, int high)
    //Recursive step called by the recursive algorithm, maxSubarray. After taking both maximum sums to the left and right of the middle, it combines them.
    {
        int left_sum = Integer.MIN_VALUE;
        //Indicates the sum on the left side, expanding from the middle.
        int sum = 0;
        //Indicates the sum in which we'll compare with values found for both our left and right sum.

        for(int i = mid; i >= low; i--)
        {
            sum = sum + A[i];
            if(sum > left_sum) left_sum = sum;
            //Sets left_sum to sum if sum's value is larger.
        }
        //Expanding from the middle and to the left of our array, we find the maximum sum going that way.

        int right_sum = Integer.MIN_VALUE;
        //Indicates the sum on the right side, expanding from the middle.
        sum = 0;

        for(int j = mid; j <= high; j++)
        {
            sum = sum + A[j];
            if(sum > right_sum) right_sum = sum;
            //Sets right_sum to sum if sum's value is larger.
        }
        //Expanding from the middle and to the right of our array, we find the maximum sum going that way.

        return left_sum + right_sum;
    }

    public static int kadaneSubArray(int[] A)
    //Algorithm to find the contiguous subarray, taking n time.
    {
        int max_so_far = Integer.MIN_VALUE;
        //Indicates our current maximum sum.

        int max_end_here = 0;
        //Indicates the maximum sum given it's above 0.

        for(int i = 0; i < A.length; i++)
        {
            max_end_here = max_end_here + A[i];
            if(max_so_far < max_end_here) max_so_far = max_end_here;
            //Replaces max_so_far if max_end_here is greater.
            if(max_end_here < 0) max_end_here = 0;
            //If max_end_here is less than 0, then we should set it back to 0 for our next loop.
        }
        //For each element of the list, increase max_end_here by the element to compare it with our current maximum sum. If that sum is less, then max_end_here replaces the value in it. If max_end_here is less than 0, then we set it to 0 for our next loop.
        
        return max_so_far;
    }
}


