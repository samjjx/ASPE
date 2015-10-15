package com.encryption.jjx;

import java.util.BitSet;
import java.util.Random;

public class AspePerformance {
	int labelLength;
	int dimension;
	BitSet center;
	int pieces;
	ASPE aspe;
	int discenters;
	double[][] queryVector;
	public AspePerformance(int labelLength, int dimension) {
		this.labelLength = labelLength;
		this.dimension = dimension;
		this.pieces = this.labelLength / this.dimension;
		this.aspe = new ASPE(dimension);
		discenters=labelLength;
		center = new BitSet(labelLength);
	}

	/**
	 * Test the encryption performance
	 */
	public void encryptPerformance() {
		Random random = new Random();
		generateVector(random.nextInt(discenters));
		divide();
		clearCenters();
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
	}
	public void generateQueryVector()
	{
		queryVector=new double[2][dimension];
		for(int i=0;i<dimension;i++)
			queryVector[0][i]=Math.pow(3, i);
	}
	/**
	 * Clear the bitCenters of the label. After encrypting the tested label, the
	 * bitset should be cleared.
	 */
	public void clearCenters() {
		center.clear();
	}

	/**
	 * Divide a label and encrypt it
	 */
	public void divide() {
		double[] pieceLabel = new double[dimension];
		for (int i = 0; i < pieces; i++) {
			for (int j = 0; j < dimension; j++)
				if (center.get(i * dimension + j))
					pieceLabel[j] = 1;
			double[][] temp=aspe.encryptOneLabel(pieceLabel);
			for (int j = 0; j < dimension; j++)
				pieceLabel[j] = 0;
		}
	}
	/**
	 * Making the intersection between lin and lout
	 * @param lin : the encrypted label of lin(v)
	 * @param lout : the encrypted label of lout(u)
	 * @return The sum of the lin(v) and lout(u)
	 */
	public double[][] intersect(double[][] lin, double[][] lout)
	{
		double[][] intersection=new double[2][lin[0].length];
		for(int i=0;i<lin.length;i++)
			for(int j=0;j<lin[i].length;j++)
				intersection[i][j]=lin[i][j]+lout[i][j];
		return intersection;
	}
	/**
	 * Query for the result
	 * @param intersection : The intersection of the lin and lout in 
	 * the encrypted domain
	 * @return : return the query result after the multiplication 
	 */
	public double query(double[][] intersection)
	{
		return 0;
	}
}
