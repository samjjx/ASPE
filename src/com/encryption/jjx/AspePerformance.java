package com.encryption.jjx;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;

import Jama.Matrix;

public class AspePerformance {

	int labelLength;
	int dimension;
	BitSet center;
	int pieces;
	ASPE aspe;
	int discenters;
	ArrayList<double[][]> piece;
	BitSet linBitSet, loutBitSet;

	long QtTime = 0;
	long DtTime = 0;
	long TotalTime = 0;
	BigDecimal randomFactor;
	public AspePerformance(int labelLength, int dimension) {
		this.labelLength = labelLength;
		this.dimension = dimension;
		this.pieces = (this.labelLength - 1) / this.dimension + 1;
		this.aspe = new ASPE(dimension);
		discenters = labelLength;
		center = new BitSet(labelLength);
		piece = new ArrayList<double[][]>();
		randomFactor=aspe.divisionFactor.pow(pieces);
	}

	/**
	 * Test the encryption performance
	 * 
	 * @param inOrOut
	 *            : indicate the lin or the lout
	 * @return the encrypted label
	 */
	public ArrayList<double[][]> encryptPerformance(int inOrOut) {
		Random random = new Random();
		generateVector(random.nextInt(discenters));
		if (inOrOut == 0)
			linBitSet = (BitSet) center.clone();
		else
			loutBitSet = (BitSet) center.clone();
		ArrayList<double[][]> result = divide(inOrOut);
		clearCenters();
		return result;
	}

	/**
	 * Test the encryption performance on the real datasets
	 * 
	 * @param inOrOut
	 *            : indicate the lin or the lout
	 * @param label
	 *            : the real label
	 * @return
	 */
	public ArrayList<double[][]> encryptPerformance(int inOrOut,
			LinkedList<Integer> label) {
		generateVector(label);
		if (inOrOut == 0)
			linBitSet = (BitSet) center.clone();
		else
			loutBitSet = (BitSet) center.clone();
		ArrayList<double[][]> result = divide(inOrOut);
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
		// System.out.println(center);
	}

	/**
	 * Generate the labels on the real datasets
	 * 
	 * @param label
	 *            : The real label
	 */
	public void generateVector(LinkedList<Integer> label) {
		for (int vertex : label)
			center.set(vertex);
		// System.out.println(center);
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
	public ArrayList<double[][]> divide(int inOrOut) {
		double[] pieceLabel = new double[dimension];
		ArrayList<double[][]> encryption = new ArrayList<double[][]>();
		for (int i = 0; i < pieces; i++) {
			pieceLabel = encode(inOrOut, i);
			double[][] encryptLabel = aspe.encryptOneLabel(pieceLabel);
			encryption.add(encryptLabel);
			for (int j = 0; j < dimension; j++)
				pieceLabel[j] = 0;
		}
		return encryption;
	}

	/**
	 * 
	 * @param inOrOut
	 *            Indicate Lin or Lout
	 * @param fragment
	 *            The i-th fragment
	 * @return
	 */
	public double[] encode(int inOrOut, int fragment) {
		double[] pieceLabel = new double[dimension];
		if (inOrOut == 0)
			for (int i = 0; i < dimension; i++)
				pieceLabel[i] = 0;
		if (inOrOut == 1)
			for (int i = 0; i < dimension; i++)
				pieceLabel[i] = 1;

		if (inOrOut == 0) {
			for (int j = 0; j < dimension; j++)
				if (center.get(fragment * dimension + j))
					pieceLabel[j] = 1;
				else
					pieceLabel[j] = 0;

		} else {
			for (int j = 0; j < dimension; j++)
				if (center.get(fragment * dimension + j))
					pieceLabel[j] = 2;
				else
					pieceLabel[j] = 1;
		}
		return pieceLabel;
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
		double[][] queryResultI = new double[2][intersection[0].length];
		double[][] queryResultIcomplement = new double[2][intersection[0].length];
		double[] result = new double[intersection[0].length];

		queryResultI[0] = new Matrix(intersection[0], 1).times(
				new Matrix(aspe.queryMatrixI[0])).getColumnPackedCopy();
		queryResultI[1] = new Matrix(intersection[1], 1).times(
				new Matrix(aspe.queryMatrixI[1])).getColumnPackedCopy();

		queryResultIcomplement[0] = new Matrix(intersection[0], 1).times(
				new Matrix(aspe.queryMatrixIcomplement[0]))
				.getColumnPackedCopy();
		queryResultIcomplement[1] = new Matrix(intersection[1], 1).times(
				new Matrix(aspe.queryMatrixIcomplement[1]))
				.getColumnPackedCopy();

		for (int i = 0; i < result.length; i++)
			result[i] = queryResultI[0][i]
					+ queryResultI[1][i]
					- (queryResultIcomplement[0][i] + queryResultIcomplement[1][i]);
		double sum = 1;
		for (int i = 0; i < result.length; i++)
			sum *= result[i];
		return sum;
	}

	/**
	 * Query all the piece
	 * 
	 * @param intersection
	 *            Contain all the pieces
	 * @return query all the piece
	 */
	public BigDecimal query(ArrayList<double[][]> intersection) {
		ArrayList<Double> resultVector = new ArrayList<Double>();
		for (int i = 0; i < intersection.size(); i++)
			resultVector.add(queryByPiece(intersection.get(i)));
		BigDecimal result=BigDecimal.ONE;
		for(double pieceResult:resultVector)
			result=result.multiply(new BigDecimal(Double.toString(pieceResult)));
		return result;
	}

	long EtTime = 0;

	/**
	 * Test the query performance on the random label
	 * 
	 * @return
	 */
	public long queryOneTime() {
		ArrayList<double[][]> lin = encryptPerformance(0);
		ArrayList<double[][]> lout = encryptPerformance(1);

		long t0 = System.currentTimeMillis();
		long start = t0;
		aspe.generateQueryVector();
		EtTime += System.currentTimeMillis() - t0;

		t0 = System.currentTimeMillis();
		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		BigDecimal sum = query(intersection);

		QtTime += System.currentTimeMillis() - t0;
		t0 = System.currentTimeMillis();

		decode(sum);
		DtTime += System.currentTimeMillis() - t0;
		TotalTime += System.currentTimeMillis() - start;

		return System.currentTimeMillis() - start;
	}

	/**
	 * Test the query performance on the real dataset
	 * 
	 * @return
	 */
	public long queryOneTime(LinkedList<Integer> linList,
			LinkedList<Integer> loutList) {
		ArrayList<double[][]> lin = encryptPerformance(0, linList);
		ArrayList<double[][]> lout = encryptPerformance(1, loutList);

		long t0 = System.currentTimeMillis();
		long start = t0;
		aspe.generateQueryVector();
		EtTime += System.currentTimeMillis() - t0;

		t0 = System.currentTimeMillis();
		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		BigDecimal sum = query(intersection);

		QtTime += System.currentTimeMillis() - t0;
		t0 = System.currentTimeMillis();

		decode(sum);
		DtTime += System.currentTimeMillis() - t0;
		TotalTime += System.currentTimeMillis() - start;

		return System.currentTimeMillis() - start;
	}

	/**
	 * Test the correctness on random label
	 * 
	 * @return
	 */
	public boolean queryOneTimeTest() {
		ArrayList<double[][]> lin = encryptPerformance(0);
		ArrayList<double[][]> lout = encryptPerformance(1);

		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		BigDecimal sum = query(intersection);
		return decode(sum);
	}

	/**
	 * Test the correctness on real label
	 * 
	 * @return
	 */
	public boolean queryOneTimeTest(LinkedList<Integer> linList,
			LinkedList<Integer> loutList) {
		ArrayList<double[][]> lin = encryptPerformance(0, linList);
		ArrayList<double[][]> lout = encryptPerformance(1, loutList);

		ArrayList<double[][]> intersection = intersectAll(lin, lout);
		BigDecimal sum = query(intersection);
		return decode(sum);
	}

	/**
	 * Decode the result
	 * 
	 * @param sum
	 *            : sum is the ca*qa+cb*qb
	 * @return : If reachable, return true; else false
	 */
	public boolean decode(BigDecimal result) {
		long t0=System.currentTimeMillis();
//		System.out.println(randomFactor);
//		System.out.println(result);
		result=result.divide(randomFactor,3,RoundingMode.HALF_UP);
		System.out.println(System.currentTimeMillis()-t0);
		BigInteger queryResult=result.toBigInteger().mod(new BigInteger("3"));
		if(queryResult.equals(BigInteger.ZERO))
			return true;
		else
			return false;
	}
}
