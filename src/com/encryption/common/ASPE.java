package com.encryption.common;

import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Random;

import org.apache.commons.math.linear.BigMatrixImpl;


@SuppressWarnings("deprecation")
public class ASPE {
	BigMatrixImpl M1, M1T, M2, M2T;
	int d;
	BitSet splitKey;
	BigDecimal[][] queryVector;
	public ASPE(int d) {
		this.d = d;
		M1 = generatekey(d);
		M1T = (BigMatrixImpl) M1.inverse();
		M2 = generatekey(d);
		M2T = (BigMatrixImpl) M2.inverse();
		splitKey = new BitSet(d);
		setSplitKey();
		generateQueryVector();
	}
	/**
	 * Generate the query vector like 3 9 27 81...3^n. n is the dimension
	 */
	public void generateQueryVector()
	{
		queryVector=new BigDecimal[2][d];
		for(int i=0;i<d;i++)
			queryVector[0][i]=new BigDecimal(3).pow(i);
		splitQuery();
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
				queryVector[1][i]=new BigDecimal(Double.toString(random.nextDouble()));
				queryVector[0][i]=queryVector[0][i].subtract(queryVector[1][i]);
			}
			else
				queryVector[1][i]=queryVector[0][i];
		queryVector[0]=M1T.multiply((new BigMatrixImpl(queryVector[0])))
				.getColumn(0);
		queryVector[1]=M2T.multiply((new BigMatrixImpl(queryVector[1])))
				.getColumn(0);
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
	 * @return : Return a BigMatrixImpl whose det is not 0
	 */
	public BigMatrixImpl generatekey(int d) {
		BigDecimal[][] key=new BigDecimal[d][d];
		Random random=new Random();
		for(int i=0;i<d;i++)
			for(int j=0;j<d;j++)
				key[i][j]=new BigDecimal(Double.toString(random.nextDouble()));
		BigMatrixImpl keyMatrix = new BigMatrixImpl(key);
		while (true) {
			if (!keyMatrix.isSingular())
				return keyMatrix;
			for(int i=0;i<d;i++)
				for(int j=0;j<d;j++)
					key[i][j]=new BigDecimal(Double.toString(random.nextDouble()));
		}
	}
	/**
	 * Encrypt a label
	 * @param label : the label to be encrypted
	 * @return : The related two encrypted labels
	 */
	public BigDecimal[][] encryptOneLabel(BigDecimal[] label)
	{
		BigDecimal[][] splited=splitLabel(label);
		BigDecimal[][] encryptedLabel=new BigDecimal[2][label.length];
		encryptedLabel[0]=encryptSeperateLabel(splited[0], 0);
		encryptedLabel[1]=encryptSeperateLabel(splited[1], 1);
		return encryptedLabel;
	}
	/**
	 * Encrypt a split label
	 * 
	 * @param label
	 *            : The label to be encrypt
	 * @param BigMatrixImplOption
	 *            : Choose the related BigMatrixImpl. 0 for M1, 1 for M2.
	 * @return : Return the related encrypted label
	 */
	public BigDecimal[] encryptSeperateLabel(BigDecimal[] label, int BigMatrixImplOption) {
		BigMatrixImpl keyBigMatrixImpl;
		if (BigMatrixImplOption == 0)
			keyBigMatrixImpl = M1;
		else
			keyBigMatrixImpl = M2;
		BigDecimal[] result = new BigDecimal[d];
		if (label.length != this.d)
			return result;
		return new BigMatrixImpl(label).transpose().multiply(keyBigMatrixImpl)
				.getColumn(0);
	}

	/**
	 * Splitting the label based the method of the enhanced ASPE
	 * 
	 * @param label
	 *            : The label to be split
	 * @return : two labels which form the original label
	 */
	public BigDecimal[][] splitLabel(BigDecimal[] label) {
		BigDecimal[][] splitedLabel = new BigDecimal[2][label.length];
		Random random = new Random();
		for (int i = 0; i < label.length; i++)
			if (splitKey.get(i)) {
				splitedLabel[0][i] = new BigDecimal(random.nextDouble());
				splitedLabel[1][i] = label[i].subtract(splitedLabel[0][i]);
			} else {
				splitedLabel[0][i] = label[i];
				splitedLabel[1][i] = label[i];
			}

		return splitedLabel;
	}

	public void query() {

	}
}
