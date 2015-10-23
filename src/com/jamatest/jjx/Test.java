package com.jamatest.jjx;

import java.math.BigDecimal;
import java.util.Random;

import flanagan.math.Matrix;

public class Test {
	public static void main(String[] args) {
		int n=100;
		BigDecimal[][] aLabel=new BigDecimal[1][n];
		BigDecimal[][] bLabel=new BigDecimal[1][n];
		Random random=new Random();
		BigDecimal sum=new BigDecimal("0");
		for(int i=0;i<n;i++)
		{
			aLabel[0][i]=new BigDecimal("1".toString());
			bLabel[0][i]=new BigDecimal("3".toString()).pow(i);
			BigDecimal tempBigDecimal=aLabel[0][i].multiply(bLabel[0][i]);
			sum=sum.add(tempBigDecimal);
		}
		System.out.println(sum);
		BigDecimal[][] vectorBigDecimal = new BigDecimal[n][n];
		
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				vectorBigDecimal[i][j] = new BigDecimal(Double.toString(random
						.nextDouble()));
			}
		
		Matrix M1 = new Matrix(vectorBigDecimal);
		Matrix M_1 = M1.inverse();
		Matrix aMatrix=new Matrix(aLabel);
		Matrix sumBig=Matrix.times(Matrix.times(Matrix.times(new Matrix(aLabel),M1),M_1),new Matrix(bLabel).transpose());

	}
}
