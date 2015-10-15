package com.encryption.jjx;

import java.util.BitSet;
import java.util.Random;

import Jama.Matrix;

public class ASPE {
	Matrix M1, M1T, M2, M2T;
	int d;
	BitSet splitKey;
	double[][] queryVector;
	public ASPE(int d) {
		this.d = d;
		M1 = generatekey(d);
		M1T = M1.inverse();
		M2 = generatekey(d);
		M2T = M2.inverse();
		splitKey = new BitSet(d);
		setSplitKey();
		generateQueryVector();
	}
	/**
	 * Generate the query vector like 3 9 27 81...3^n. n is the dimension
	 */
	public void generateQueryVector()
	{
		queryVector=new double[2][d];
		for(int i=0;i<d;i++)
			queryVector[0][i]=Math.pow(3, i);
	}
	/**
	 * Split the query vector and encrypt them respectively.
	 */
	public void splitQuery()
	{
		Random random=new Random();
		for(int i=0;i<queryVector[0].length;i++)
			if(!splitKey.get(i))
			{
				queryVector[1][i]=random.nextDouble();
				queryVector[0][i]-=queryVector[1][i];
			}
			else
				queryVector[1][i]=queryVector[0][i];
		queryVector[0]=M1T.times(new Matrix(queryVector[0],1).transpose()).
				getColumnPackedCopy();
		queryVector[1]=M2T.times(new Matrix(queryVector[1],1).transpose()).
				getColumnPackedCopy();
	}
	/**
	 * initialize the splitKey
	 */
	public void setSplitKey() {
		Random rand = new Random();
		for (int i = 0; i < 10; i++)
			splitKey.set(rand.nextInt(d));
	}

	/**
	 * Generate the key of ASPE
	 * 
	 * @param d
	 *            : The dimension of the dimension
	 * @return : Return a Matrix whose det is not 0
	 */
	public Matrix generatekey(int d) {
		Matrix key = Matrix.random(d, d);
		while (true) {
			if (key.det() != 0)
				return key;
			key = Matrix.random(d, d);
		}
	}
	/**
	 * Encrypt a label
	 * @param label : the label to be encrypted
	 * @return : The related two encrypted labels
	 */
	public double[][] encryptOneLabel(double[] label)
	{
		double[][] splited=splitLabel(label);
		double[][] encryptedLabel=new double[2][label.length];
		encryptedLabel[0]=encryptSeperateLabel(splited[0], 0);
		encryptedLabel[1]=encryptSeperateLabel(splited[1], 1);
		return encryptedLabel;
	}
	/**
	 * Encrypt a split label
	 * 
	 * @param label
	 *            : The label to be encrypt
	 * @param matrixOption
	 *            : Choose the related Matrix. 0 for M1, 1 for M2.
	 * @return : Return the related encrypted label
	 */
	public double[] encryptSeperateLabel(double[] label, int matrixOption) {
		Matrix keyMatrix;
		if (matrixOption == 0)
			keyMatrix = M1;
		else
			keyMatrix = M2;
		double[] result = new double[d];
		if (label.length != this.d)
			return result;
		return new Matrix(label, 1).times(keyMatrix)
				.getColumnPackedCopy();
	}

	/**
	 * Splitting the label based the method of the enhanced ASPE
	 * 
	 * @param label
	 *            : The label to be split
	 * @return : two labels which form the original label
	 */
	public double[][] splitLabel(double[] label) {
		double[][] splitedLabel = new double[2][label.length];
		Random random = new Random();
		for (int i = 0; i < label.length; i++)
			if (splitKey.get(i)) {
				splitedLabel[0][i] = random.nextInt(100);
				splitedLabel[1][i] = label[i] - splitedLabel[0][i];
			} else {
				splitedLabel[0][i] = label[i];
				splitedLabel[1][i] = label[i];
			}

		return splitedLabel;
	}

	public void query() {

	}
}
