package com.encryption.jjx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class AspePerformance {
	
	int labelLength;
	int dimension;
	BitSet center;
	int pieces;
	ASPE aspe;
	int discenters;
	ArrayList<double[][]> piece;
	
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
	 * @return the encrypted label
	 */
	public ArrayList<double[][]> encryptPerformance() {
		Random random = new Random();
		generateVector(random.nextInt(10));
		ArrayList<double[][]> result=divide();
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
		System.out.println(center);
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
	public double query(ArrayList<double[][]> intersection) {
		double sum = 0;
		for (int i = 0; i < intersection.size(); i++)
			sum += queryByPiece(intersection.get(i));
		return sum;
	}
	
	public long queryOneTime()
	{
		ArrayList<double[][]> lin=encryptPerformance();
		ArrayList<double[][]> lout=encryptPerformance();
		
		long t0=System.currentTimeMillis();
		ArrayList<double[][]> intersection=intersectAll(lin, lout);
		double sum=query(intersection);
		System.out.println(decode(sum));
		return System.currentTimeMillis()-t0;
	}
	/**
	 * Decode the result
	 * 
	 * @param sum
	 *            : sum is the ca*qa+cb*qb
	 * @return : If reachable, return true; else false
	 */
	public boolean decode(double sum) {
		long sumInt = new BigDecimal(sum).setScale(0, BigDecimal.ROUND_HALF_UP)
				.longValue();
		while (sumInt >= 1) {
			if (sumInt % 3 == 2)
				return true;
			sumInt /= 3;
		}
		return false;
	}
}
