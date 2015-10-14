package com.encryption.jjx;

import java.util.BitSet;
import java.util.Random;

public class AspePerformance {
	int labelLength;
	int dimension;
	BitSet center;
	public AspePerformance(int labelLength, int dimension) {
		this.labelLength = labelLength;
		this.dimension = dimension;
		center=new BitSet(labelLength);
	}
	/**
	 * Test the encryption performance
	 */
	public void encryptPerformance() {
		ASPE aspe = new ASPE(dimension);
	}
	/**
	 * Generate the labels randomly
	 * @param numberOfCenters : The number of the centers
	 */
	public void generateVector(int numberOfCenters) {
		Random random=new Random();
		for (int i = 0; i < numberOfCenters; i++)
			center.set(random.nextInt(labelLength));
	}
	/**
	 * Clear the bitCenters of the label. After encrypting the tested 
	 * label, the bitset should be cleared.
	 */
	public void clearCenters()
	{
		center.clear();
	}
	public void encode()
	{
		
	}
}
