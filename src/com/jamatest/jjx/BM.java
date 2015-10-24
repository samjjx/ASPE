package com.jamatest.jjx;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.math.linear.BigMatrixImpl;

public class BM {
	public static void main(String[] args) {
		int n = 56;

		Random random = new Random();
		BigDecimal[][] vectorBigDecimal = new BigDecimal[n][n];
		BigDecimal[][] aLabel = new BigDecimal[1][n];
		BigDecimal[][] bLabel = new BigDecimal[1][n];
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < n; i++) {
			aLabel[0][i] = new BigDecimal("1".toString());
			bLabel[0][i] = new BigDecimal("3".toString()).pow(i);
			BigDecimal tempBigDecimal = aLabel[0][i].multiply(bLabel[0][i]);
			sum = sum.add(tempBigDecimal);
		}
		System.out.println(sum);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				vectorBigDecimal[i][j] = new BigDecimal(Double.toString(random
						.nextDouble()));
			}
		BigMatrixImpl M = new BigMatrixImpl(vectorBigDecimal);
		BigMatrixImpl MT=(BigMatrixImpl)M.inverse();
		BigMatrixImpl aVector=new BigMatrixImpl(aLabel);
		BigMatrixImpl bVector=new BigMatrixImpl(bLabel);
		BigMatrixImpl aM=aVector.multiply(M);
		BigMatrixImpl MTb=(BigMatrixImpl) MT.multiply(bVector.transpose());
		long t0=System.currentTimeMillis();
		BigMatrixImpl result;
		for(int i=0;i<1000;i++)
			result=aM.multiply(MTb);
		System.out.println(System.currentTimeMillis()-t0);
	}

}
