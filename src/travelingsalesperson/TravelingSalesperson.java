
package travelingsalesperson;

/*
Name:	John Hunsaker
Course: CPSC335
Project: Rectilinear Traveling Salesperson Problem
Implementation: Java (created using Netbeans IDE)
*/

import java.util.*;
import java.io.*;
import java.math.*;

public class TravelingSalesperson {

	public static void main(String[] args) throws IOException{

		//Introduction.
		System.out.println("\nRectilinear Traveling Salesperson Problem.");
		System.out.println("\nThe purpose of this program is to collect a set of points on a rectilinear graph, ");
		System.out.println("then use two different algorithms to output a list of points representing a Hamiltonian cycle of minimum total weight.");
		
		//Input: a positive integer n and a list P of n distinct points representing vertices of a rectilinear graph.
		System.out.println("\nEnter the number of points (larger than 2) to be on the graph, then press enter: ");
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		int n = (Integer.parseInt(input));
		
		//Check number of points is greater than 2.
		if (n < 3) {
			System.out.println("You did not enter a number larger than 3. Program is terminating. Goodbye. ");
			System.exit(0);
		}
		
		//Make space for sequence of 2D points.
		double point[][] = new double[n][2];
		
		//User enters the coordinate data.
		double a, b;
		System.out.println("\nEnter each point and press enter; make sure that they are distinct: ");
		for (int i = 0; i < n; i++) {
			System.out.print("x = ");
			input = scanner.nextLine();
			a = Double.parseDouble(input);
			point[i][0] = a;
		   System.out.print("y = ");
			input = scanner.nextLine();
			b = Double.parseDouble(input);
			point[i][1] = b;
		}
		

		
		
		
		/* Begin Exhaustive Optimization Algorithm */
		
		//Record beginning time.
		final long start = System.nanoTime();

		//Calculate the farthest pair of vertices.
		double biggestDist = 0;
		double currDist = 0;
		double x = 0;
		double y = 0;
		for (int i = 0; i < n; i++) {
			for (int c = 0; c < n; c++) {
				x = Math.abs(point[i][0] - point[c][0]);
				y = Math.abs(point[i][1] - point[c][1]);
				currDist = x + y;
				if (currDist > biggestDist) {
					biggestDist = currDist;
				}
			}
		}
		
		//Set the largest possible distance path to use as a max weight.
		double farthestDist = n * biggestDist;
		
		//Create and populate array A with the values in the range 0 .. n-1.
		int A[] = new int[n];
		for(int i = 0; i < n; i++) {
			A[i] = i;
		}

		//Calculate number of permutations i.e. n!.
		int numPerms = 1;
		for (int i = 1; i <= n; i++) {
			numPerms *= i;
		}
		
		//Create temporary output file for permutation indices.
		PrintWriter permutationsFile = new PrintWriter("tempFile(ignore this file).txt");
		
		//Generate all the permutations of the indices recursively.
		permutations(n, A, n, farthestDist, permutationsFile);
		
		//Close the temporary output file.
		permutationsFile.close();
		
		//Create array to store the permutation indexes.
		int B[][] = new int[numPerms][n];
		
		//Read permuation indicies from file to new array.
		int currRow = 0;
		int currCol;
		File myFile = new File("tempFile(ignore this file).txt");
		Scanner permFile = new Scanner(myFile);
		while (permFile.hasNext()) {
			String s = permFile.nextLine();
			currCol = 0;

			while (s.length() > 0) {
				if (Character.isDigit(s.charAt(0))) {
					int i = 0;
					while (i + 1 < s.length() && (Character.isDigit(s.charAt(i+1)))) {
						i++;
					}

					B[currRow][currCol] = Integer.parseInt(s.substring(0, i+1));
					s = s.substring(i+1, s.length());
					currCol++;
			   }
				else if (s.charAt(0) == 32) {
					s = s.substring(1, s.length());
				}
			}
			currRow++;
		}
		
		//Close the permutations.txt.
		permFile.close();
		
		//Find the permutation with minimum weight path.
		int C[] = new int[n];
		C = bestPermutation(n, point, B, numPerms, farthestDist);

		//Calculate the weight of the minimal weight ordering that bestPermutation() figured out.
		farthestDist = 0;
		for (int i = 1; i < n; i++) {
			x = Math.abs(point[C[i]][0] - point[C[i - 1]][0]);
			y = Math.abs(point[C[i]][1] - point[C[i - 1]][1]);
			farthestDist = farthestDist + x + y;
		}
			
		//Add the weight from the last vertex back to the beginning, which bestPermutation() also did too.
		farthestDist = farthestDist + Math.abs(point[C[n - 1]][0] - point[C[0]][0]) + 
		 Math.abs(point[C[n - 1]][1] - point[C[0]][1]);
		
		//Record end time.
		final long end = System.nanoTime();
		double totalTime = (((double)end - (double)start)/1000000000);
		
		//Create output file for EOA.
		PrintWriter eoaFile = new PrintWriter("EOA.txt");
		
		//Re-Write Command Prompt info to EOA File.
		eoaFile.println("CPSC335-01 - Programming Assignment #3.");
		eoaFile.println(" ");
		eoaFile.println("Rectilinear Traveling Salesperson Problem.");
		eoaFile.println("Using Exhaustive Optimization Algorithm.");
		eoaFile.println(" ");
		eoaFile.println("The purpose of this program is to collect a set of points on a rectilinear graph, ");
		eoaFile.println("then use two different algorithms to output a list of points");
		eoaFile.println("representing a Hamiltonian cycle of minimum total weight.");
		eoaFile.println(" ");
		eoaFile.println("Enter the number of points (larger than 2) to be on the graph, then press enter: ");
		eoaFile.println(n);
		eoaFile.println(" ");
		eoaFile.println("Enter each point and press enter; make sure that they are distinct: ");
		for (int i = 0; i < n; i++) {
			eoaFile.print("x = ");
			eoaFile.println(point[i][0]);
		   eoaFile.print("y = ");
			eoaFile.println(point[i][1]);
		}
		
		//Output: A list of n points from P representing a Hamiltonian cycle of minimum total weight for the graph.
		eoaFile.println(" ");
		eoaFile.println("Input: n");
		eoaFile.println("n = " + n);
		eoaFile.println(" ");
		eoaFile.println("The Hamiltonian cycle of the minimum length: ");
		for (int i = 0; i < n; i++) {
			eoaFile.print("(" + point[C[i]][0] + ", " + point[C[i]][1] + ")" + " ");
		}
		eoaFile.print("(" + point[C[0]][0] + ", " + point[C[0]][1] + ")");
		eoaFile.print(" ");
		eoaFile.println(" ");
		eoaFile.println("\nRelative minimum length: " + farthestDist);
		eoaFile.println("Elapsed Time: " + totalTime + " seconds");
		
		System.out.println("\nResults of \"Exhaustive Optimization Algorithm\" have been written to "
		 + "\"EOA.txt\" in the directory where this program resides.");
		
		//Close the EOA file.
		eoaFile.close();
		
		
		
		
		
		/* Begin Improved Nearest Neighbor Algorithm */
		
		//Record beginning time.
		final long strt = System.nanoTime();
		
		//Calculate the farthest pair of vertices and their distance.
		farthestDist = 0;
		currDist = 0;
		x = 0;
		y = 0;
		double startVertex[] = new double[2];
		double otherVertex[] = new double[2];
		double nextVertex[]= new double[2];
		
		for (int i = 0; i < n; i++) {
			for (int c = 0; c < n; c++) {
				x = Math.abs(point[i][0] - point[c][0]);
				y = Math.abs(point[i][1] - point[c][1]);
				currDist = x + y;
				if (currDist > farthestDist) {
					farthestDist = currDist;
					startVertex[0] = point[i][0];
					startVertex[1] = point[i][1];
					otherVertex[0] = point[c][0];
					otherVertex[1] = point[c][1];
				}
			}
		}
		
		//Create variable to flag visited vertices, set all to false.
		boolean Visited[] = new boolean[n];
		for (int i = 0; i < n; i++) {
			Visited[i] = false;
		}
		
		//Start at a vertex startVertex, travel to the nearest vertex not yet visted.
		//Record the path at each step, and continue until no more vertices remain.
		currDist = 0;
		double weight = 0;
		double lastShortestDist = farthestDist;
		otherVertex[0] = startVertex[0];
		otherVertex[1] = startVertex[1];
		int indexChosen = -1;
		int indexOrderSelected[] = new int[n];

		for (int c = 0; c < n; c++) {
			for (int i = 0; i < n; i++) {
				x = Math.abs(otherVertex[0] - point[i][0]);
				y = Math.abs(otherVertex[1] - point[i][1]);
				currDist = x + y;
				if (currDist < lastShortestDist) {
					if (Visited[i] != true) {
						lastShortestDist = currDist;
						nextVertex[0] = point[i][0];
						nextVertex[1] = point[i][1];
						indexChosen = i;
					}
				}
			}
			Visited[indexChosen] = true;
			indexOrderSelected[c] = indexChosen;
			otherVertex[0] = nextVertex[0];
			otherVertex[1] = nextVertex[1];
			lastShortestDist = farthestDist;
		}
		
		//Record end time.
		final long ed = System.nanoTime();
		totalTime = (((double)ed - (double)strt)/1000000000);
		
		//Calculate the weight of the minimal weight ordering.
		farthestDist = 0;
		for (int i = 1; i < n; i++) {
			x = Math.abs(point[indexOrderSelected[i]][0] - point[indexOrderSelected[i - 1]][0]);
			y = Math.abs(point[indexOrderSelected[i]][1] - point[indexOrderSelected[i - 1]][1]);
			farthestDist = farthestDist + x + y;
		}
			
		//Add the weight from the last vertex back to the beginning.
		farthestDist = farthestDist + Math.abs(point[indexOrderSelected[n - 1]][0] - point[indexOrderSelected[0]][0]) + 
		 Math.abs(point[indexOrderSelected[n - 1]][1] - point[indexOrderSelected[0]][1]);
		
		//Create output file for INNA.
		PrintWriter innaFile = new PrintWriter("INNA.txt");
		
		//Re-Write Command Prompt info to INNA File.
		innaFile.println("CPSC335-01 - Programming Assignment #3.");
		innaFile.println(" ");
		innaFile.println("Rectilinear Traveling Salesperson Problem.");
		innaFile.println("Using Improved Nearest Neighbor Algorithm.");
		innaFile.println(" ");
		innaFile.println("The purpose of this program is to collect a set of points on a rectilinear graph, ");
		innaFile.println("then use two different algorithms to output a list of points");
		innaFile.println("representing a Hamiltonian cycle of minimum total weight.");
		innaFile.println(" ");
		innaFile.println("Enter the number of points (larger than 2) to be on the graph, then press enter: ");
		innaFile.println(n);
		innaFile.println(" ");
		innaFile.println("Enter each point and press enter; make sure that they are distinct: ");
		for (int i = 0; i < n; i++) {
			innaFile.print("x = ");
			innaFile.println(point[i][0]);
		   innaFile.print("y = ");
			innaFile.println(point[i][1]);
		}
		
		//Output: A list of n points from P representing a Hamiltonian cycle of the best-guess minimum total weight for the graph.
		innaFile.println(" ");
		innaFile.println("Input: n");
		innaFile.println("n = " + n);
		innaFile.println(" ");
		innaFile.println("The Hamiltonian cycle of the minimum length: ");
		for (int i = 0; i < n; i++) {
			innaFile.print("(" + point[indexOrderSelected[i]][0] + ", " + point[indexOrderSelected[i]][1] + ")" + " ");
		}
		innaFile.print("(" + point[indexOrderSelected[0]][0] + ", " + point[indexOrderSelected[0]][1] + ")");
		innaFile.print(" ");
		innaFile.println(" ");
		innaFile.println("\nRelative minimum length: " + farthestDist);
		innaFile.println("Elapsed Time: " + totalTime + " seconds");
		
		//Close the INNA file.
		innaFile.close();
		
		System.out.println("Results of \"Improved Nearest Neighbor Algorithm\" have been written to "
		 + "\"INNA.txt\" in the directory where this program resides.");
	}
	
	
	
	//Generates a file containing every permutation of the index numbers.
	public static void permutations(int n, int[] A, int sizeA, double farthestDist, PrintWriter out) throws IOException{
		
		int i;
		
		if (n == 1) {
			for(i = 0; i < sizeA; i++) {
				out.print(A[i] + " " );
			}
			out.println(" ");
		}
		else {
			for(i = 0 ; i < (n - 1); i++) {
			permutations((n - 1), A, sizeA, farthestDist, out);
				if (n % 2 == 0) {
					//Swap(A[i], A[n-1]).
					int temp = A[i];
					A[i] = A[n-1];
					A[n-1]=temp;
				}
				else {
					//Swap(A[0], A[n-1]).
					int temp = A[0];
					A[0] = A[n-1];
					A[n-1]=temp;
				}
			}
			permutations(n - 1, A, sizeA, farthestDist, out);
		}
	}
	
	//Calculates the weight of every permuation, returns the best path.
	public static int[] bestPermutation(int n, double[][] point, int[][] B, int numPerms, double farthestDist) {
		double x, y;
		double weight = 0;
		int currRow = 0;
		double temp[] = new double[n];
		int s[] = new int[n];
		
		while (currRow < numPerms) {
			
			//Sum the weights for this permutation.
			for (int i = 1; i < n; i++) {
				x = Math.abs(point[(B[currRow][i])][0] - point[(B[currRow][i - 1])][0]);
				y = Math.abs(point[(B[currRow][i])][1] - point[(B[currRow][i - 1])][1]);
				weight = weight + x + y;
			}

			//Add the weight from the last vertex back to the beginning vertex.
			weight = weight + (Math.abs(point[B[currRow][n - 1]][0] - point[B[currRow][0]][0])) + 
			 (Math.abs(point[B[currRow][n - 1]][1] - point[B[currRow][0]][1]));

			//Check if edge weight is the smallest so far.
			if (weight < farthestDist) {
				farthestDist = weight;
				for(int i = 0; i < n; i++) {
					s[i] = B[currRow][i];
				}
			}
			weight = 0;
			currRow++;
		}
		return s;
	}
}