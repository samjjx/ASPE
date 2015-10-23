package com.encryption.jjx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;

public class AspePerformance {

	int labelLength;
	int dimension;
	BitSet center;
	int pieces;
	ASPE aspe;
	int discenters;
	ArrayList<double[][]> piece;
	BitSet linBitSet,loutBitSet;

	public AspePerformance(int labelLength, int dimension) {
		this.labelLength = labelLength;
		this.dimension = dimension;
		this.pieces = this.labelLength / this.dimension;
		this.aspe = new ASPE(dimension);
		discenters = labelLength;
		center = new BitSet(labelLength);
		piece = new ArrayList<double[][]>();
	}

	/**
	 * Test the encryption performance
	 * @param inOrOut : indicate the lin or the lout
	 * @return the encrypted label
	 */
	public ArrayList<double[][]> encryptPerformance(int inOrOut) {
		Random random = new Random();
		generateVector(random.nextInt(discenters));
		if(inOrOut==0)
			linBitSet=(BitSet)center.clone();
		else
			loutBitSet=(BitSet)center.clone();
		ArrayList<double[][]> result = divide();
		clearCenters();
		return result;
	}
	/**
	 * Test the encryption performance on the real datasets
	 * @param inOrOut : indicate the lin or the lout 
	 * @param label : the real label
	 * @return
	 */
	public ArrayList<double[][]> encryptPerformance(int inOrOut,LinkedList<Integer> label) {
		generateVector(label);
		if(inOrOut==0)
			linBitSet=(BitSet)center.clone();
		else
			loutBitSet=(BitSet)center.clone();
		ArrayList<double[][]> result = divide();
		clearCenters();
		return result;
	}
	/**
	 * Generate the labels randomly
	 * 
	 * @param numberOfCenters
	 *            : The number of the centers
	 */
	public void generateVector(int numberOfCenters) {
		Random random = new Random();
		for (int i = 0; i < numberOfCenters; i++)
			center.set(random.nextInt(labelLength));
//		System.out.println(center);
	}
	/**
	 * Generate the labels on the real datasets
	 * @param label : The real label
	 */
	public void generateVector(LinkedList<Integer> label)
	{
		for(int vertex:label)
			center.set(vertex);
//		System.out.println(center);
	}
	/**
	 * Clear the bitCenters of the label. After encrypting the tested label, the
	 * Bitset should be cleared.
	 */
	public void clearCenters() {
		center.clear();
		piece.clear();
	}

	/**
	 * Divide a label and encrypt it
	 * 
	 * @return return the encrypted vector after division
	 */
	public ArrayList<double[][]> divide() {
		double[] pieceLabel = new double[dimension];
		ArrayList<double[][]> encryption = new ArrayList<double[][]>();
		for (int i = 0; i < pieces; i++) {
			for (int j = 0; j < dimension; j++)
				if (center.get(i * dimension + j))
					pieceLabel[j] = 1;
			double[][] encryptLabel = aspe.encryptOneLabel(pieceLabel);
			encryption.add(encryptLabel);
			for (int j = 0; j < dimension; j++)
				pieceLabel[j] = 0;
		}
		return encryption;
	}

	/**
	 * Making the intersection between lin and lout
	 * 
	 * @param lin
	 *            : the encrypted label of lin(v)
	 * @param lout
	 *            : the encrypted label of lout(u)
	 * @return The sum of the lin(v) and lout(u)
	 */
	public double[][] intersect(double[][] lin, double[][] lout) {
		double[][] intersection = new double[2][lin[0].length];
		for (int i = 0; i < lin.length; i++)
			for (int j = 0; j < lin[i].length; j++)
				intersection[i][j] = lin[i][j] + lout[i][j];
		return intersection;
	}

	/**
	 * Intersect all the pieces of the label
	 * 
	 * @return the intersection results.
	 */
	public ArrayList<double[][]> intersectAll(ArrayList<double[][]> lin,
			ArrayList<double[][]> lout) {
		ArrayList<double[][]> result = new ArrayList<double[][]>();
		for (int i = 0; i < lin.size(); i++)
			result.add(intersect(lin.get(i), lout.get(i)));
		return result;
	}

	/**
	 * Query for the result
	 * 
	 * @param intersection
	 *            : The intersection of the lin and lout in the encrypted domain
	 * @return : return the query result after the multiplication
	 */
	public double queryByPiece(double[][] intersection) {
		double sum = 0;
		for (int i = 0; i < intersection.length; i++)
			for (int j = 0; j < intersection[i].length; j++)
				sum = sum + aspe.queryVector[i][j] * intersection[i][j];
		return sum;
	}

	/**
	 * Query all the piece
	 * 
	 * @param intersection
	 *            Contain all the pieces
	 * @return query all the piece
	 */
	public ArrayList<Double> query(ArrayList<double[][]> intersection) {
		ArrayList<Double> resultVector = new ArrayList<Double>();
		for (int i = 0; i < intersection.size(); i++)
			resultVector.add(queryByPiece(intersection.get(i)));
		return resultVector;
	}
	/**
	 * Test the query performance on the random label
	 * @return
	 */
	public long queryOneTime() {
		ArrayList<double[][]> lin = encryptPerformance(0);
		ArrayList<double[][]> lout = encryptPerformance(1);

		long t0 = System.currentTimeMillis();
		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		ArrayList<Double> sum = query(intersection);
		for(int i=0;i<discenters/dimension;i++)
		aspe.generateQueryVector();
		decode(sum);
		return System.currentTimeMillis() - t0;
	}
	/**
	 * Test the correctness on random label
	 * @return
	 */
	public boolean queryOneTimeTest() {
		ArrayList<double[][]> lin = encryptPerformance(0);
		ArrayList<double[][]> lout = encryptPerformance(1);

		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		ArrayList<Double> sum = query(intersection);
		return decode(sum);
	}
	/**
	 * Test the correctness on real label
	 * @return
	 */
	public boolean queryOneTimeTest(LinkedList<Integer> linList,LinkedList<Integer> loutList) {
		ArrayList<double[][]> lin = encryptPerformance(0,linList);
		ArrayList<double[][]> lout = encryptPerformance(1,loutList);

		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		ArrayList<Double> sum = query(intersection);
		return decode(sum);
	}
	/**
	 * Decode the result
	 * 
	 * @param sum
	 *            : sum is the ca*qa+cb*qb
	 * @return : If reachable, return true; else false
	 */
	public boolean decode(ArrayList<Double> resultVector) {
		for (int i = 0; i < resultVector.size(); i++) {
			double sum = resultVector.get(i);
			long sumInt = new BigDecimal(sum).setScale(0,
					BigDecimal.ROUND_HALF_UP).longValue();
			while (sumInt >= 1) {
				if (sumInt % 3 == 2)
					{
//						System.out.println("In the Piece:" + i);
						return true;
					}
				sumInt /= 3;
			}
		}
		return false;
	}
}
