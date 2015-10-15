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
}
