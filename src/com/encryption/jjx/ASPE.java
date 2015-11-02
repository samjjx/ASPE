package com.encryption.jjx;

import java.util.BitSet;
import java.util.Random;

import Jama.Matrix;

public class ASPE {
	Matrix M1, M1T, M2, M2T;
	int d;
	BitSet splitKey;
	double[][][] queryMatrixI;
	double[][][] queryMatrixIcomplement;
	double[] queryMatrixRandomFactor;

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
	public void generateQueryVector() {
		queryMatrixRandomFactor = new double[d];
		Random random = new Random();
		queryMatrixI = new double[2][d][d];
		queryMatrixIcomplement = new double[2][d][d];
		for (int i = 0; i < d; i++)
			queryMatrixRandomFactor[i] = random.nextDouble() + 1;
		for (int i = 0; i < d; i++)
			for (int j = 0; j < d; j++) {
				queryMatrixI[0][i][j] = 2 * queryMatrixRandomFactor[j];
				queryMatrixIcomplement[0][i][j] = 2 * queryMatrixRandomFactor[j];
			}

		for (int i = 0; i < d; i++)
			queryMatrixIcomplement[0][i][i] -= queryMatrixRandomFactor[i];

		new Matrix(queryMatrixI[0]).print(10, 10);
		new Matrix(queryMatrixIcomplement[0]).print(10, 10);
		splitQuery();
	}

	/**
	 * Split the query vector and encrypt them respectively.
	 */
	public void splitQuery() {
		Random random = new Random();
		for (int i = 0; i < queryMatrixI[0].length; i++)
			if (!splitKey.get(i)) {
				for (int j = 0; j < queryMatrixI[0].length; j++) {
					queryMatrixI[1][i][j] = random.nextDouble();
					queryMatrixI[0][i][j] -= queryMatrixI[1][i][j];
					queryMatrixIcomplement[1][i][j] = random.nextDouble();
					queryMatrixIcomplement[0][i][j] -= queryMatrixIcomplement[1][i][j];
				}
			} else
				for (int j = 0; j < queryMatrixI[0].length; j++)
				{
					queryMatrixI[1][i][j] = queryMatrixI[0][i][j];
					queryMatrixIcomplement[1][i][j]=queryMatrixIcomplement[0][i][j];
				}
		queryMatrixI[0] = M1T.times(new Matrix(queryMatrixI[0])).getArray();
		queryMatrixI[1] = M2T.times(new Matrix(queryMatrixI[1])).getArray();

		queryMatrixIcomplement[0] = M1T.times(
				new Matrix(queryMatrixIcomplement[0])).getArray();
		queryMatrixIcomplement[1] = M2T.times(
				new Matrix(queryMatrixIcomplement[1])).getArray();
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
	 * 
	 * @param label
	 *            : the label to be encrypted
	 * @return : The related two encrypted labels
	 */
	public double[][] encryptOneLabel(double[] label) {
		double[][] splited = splitLabel(label);
		double[][] encryptedLabel = new double[2][label.length];
		encryptedLabel[0] = encryptSeperateLabel(splited[0], 0);
		encryptedLabel[1] = encryptSeperateLabel(splited[1], 1);
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
		return new Matrix(label, 1).times(keyMatrix).getColumnPackedCopy();
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
				splitedLabel[0][i] = random.nextDouble();
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
