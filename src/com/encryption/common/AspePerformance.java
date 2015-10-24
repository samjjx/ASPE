package com.encryption.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
	ArrayList<BigDecimal[][]> piece;
	BitSet linBitSet,loutBitSet;

	public AspePerformance(int labelLength, int dimension) {
		this.labelLength = labelLength;
		this.dimension = dimension;
		this.pieces = this.labelLength / this.dimension;
		this.aspe = new ASPE(dimension);
		discenters = labelLength;
		center = new BitSet(labelLength);
		piece = new ArrayList<BigDecimal[][]>();
	}

	/**
	 * Test the encryption performance
	 * @param inOrOut : indicate the lin or the lout
	 * @return the encrypted label
	 */
	public ArrayList<BigDecimal[][]> encryptPerformance(int inOrOut) {
		Random random = new Random();
		generateVector(random.nextInt(discenters));
		if(inOrOut==0)
			linBitSet=(BitSet)center.clone();
		else
			loutBitSet=(BitSet)center.clone();
		ArrayList<BigDecimal[][]> result = divide();
		clearCenters();
		return result;
	}
	/**
	 * Test the encryption performance on the real datasets
	 * @param inOrOut : indicate the lin or the lout 
	 * @param label : the real label
	 * @return
	 */
	public ArrayList<BigDecimal[][]> encryptPerformance(int inOrOut,LinkedList<Integer> label) {
		generateVector(label);
		if(inOrOut==0)
			linBitSet=(BitSet)center.clone();
		else
			loutBitSet=(BitSet)center.clone();
		ArrayList<BigDecimal[][]> result = divide();
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
	public ArrayList<BigDecimal[][]> divide() {
		BigDecimal[] pieceLabel = new BigDecimal[dimension];
		ArrayList<BigDecimal[][]> encryption = new ArrayList<BigDecimal[][]>();
		for (int i = 0; i < pieces; i++) {
			for (int j = 0; j < dimension; j++)
				if (center.get(i * dimension + j))
					pieceLabel[j] = new BigDecimal("1");
				else
					pieceLabel[j] = new BigDecimal("0");
			BigDecimal[][] encryptLabel = aspe.encryptOneLabel(pieceLabel);
			encryption.add(encryptLabel);
			for (int j = 0; j < dimension; j++)
				pieceLabel[j] = new BigDecimal("0");
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
	public BigDecimal[][] intersect(BigDecimal[][] lin, BigDecimal[][] lout) {
		BigDecimal[][] intersection = new BigDecimal[2][lin[0].length];
		for (int i = 0; i < lin.length; i++)
			for (int j = 0; j < lin[i].length; j++)
				intersection[i][j] = lin[i][j] .add( lout[i][j]);
		return intersection;
	}

	/**
	 * Intersect all the pieces of the label
	 * 
	 * @return the intersection results.
	 */
	public ArrayList<BigDecimal[][]> intersectAll(ArrayList<BigDecimal[][]> lin,
			ArrayList<BigDecimal[][]> lout) {
		ArrayList<BigDecimal[][]> result = new ArrayList<BigDecimal[][]>();
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
	public BigDecimal queryByPiece(BigDecimal[][] intersection) {
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < intersection.length; i++)
			for (int j = 0; j < intersection[i].length; j++)
				sum = sum.add(aspe.queryVector[i][j].multiply(intersection[i][j]));
		return sum;
	}

	/**
	 * Query all the piece
	 * 
	 * @param intersection
	 *            Contain all the pieces
	 * @return query all the piece
	 */
	public ArrayList<BigDecimal> query(ArrayList<BigDecimal[][]> intersection) {
		ArrayList<BigDecimal> resultVector = new ArrayList<BigDecimal>();
		for (int i = 0; i < intersection.size(); i++)
			resultVector.add(queryByPiece(intersection.get(i)));
		return resultVector;
	}
	/**
	 * Test the query performance on the random label
	 * @return
	 */
	public long queryOneTime() {
		ArrayList<BigDecimal[][]> lin = encryptPerformance(0);
		ArrayList<BigDecimal[][]> lout = encryptPerformance(1);

		long t0 = System.currentTimeMillis();
		ArrayList<BigDecimal[][]> intersection = intersectAll(lin, lout);
		ArrayList<BigDecimal> sum = query(intersection);
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
		ArrayList<BigDecimal[][]> lin = encryptPerformance(0);
		ArrayList<BigDecimal[][]> lout = encryptPerformance(1);

		ArrayList<BigDecimal[][]> intersection = intersectAll(lin, lout);
		ArrayList<BigDecimal> sum = query(intersection);
		return decode(sum);
	}
	/**
	 * Test the correctness on real label
	 * @return
	 */
	public boolean queryOneTimeTest(LinkedList<Integer> linList,LinkedList<Integer> loutList) {
		ArrayList<BigDecimal[][]> lin = encryptPerformance(0,linList);
		ArrayList<BigDecimal[][]> lout = encryptPerformance(1,loutList);

		ArrayList<BigDecimal[][]> intersection = intersectAll(lin, lout);
		ArrayList<BigDecimal> sum = query(intersection);
		return decode(sum);
	}
	/**
	 * Decode the result
	 * 
	 * @param sum
	 *            : sum is the ca*qa+cb*qb
	 * @return : If reachable, return true; else false
	 */
	public boolean decode(ArrayList<BigDecimal> resultVector) {
		for (int i = 0; i < resultVector.size(); i++) {
			BigDecimal sum = resultVector.get(i);
			BigInteger sumInt = sum.setScale(0,
					RoundingMode.HALF_UP).toBigInteger();
			while (sumInt.compareTo(BigInteger.ONE)>=0) {
				if (sumInt.mod(new BigInteger("3")).compareTo(new BigInteger("2"))==0)
					{
//						System.out.println("In the Piece:" + i);
						return true;
					}
				sumInt=sumInt.divide(new BigInteger("3"));
			}
		}
		return false;
	}
}
