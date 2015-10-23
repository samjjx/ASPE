package com.jamatest.jjx;

import java.util.Random;

import Jama.Matrix;

public class Main {
	public static void main(String[] args)
	{
		int n=28;
		double[] aLabel=new double[n];
		double[] bLabel=new double[n];
		Random random=new Random();
		double sum=0;
		for(int i=0;i<aLabel.length;i++)
		{
			aLabel[i]=random.nextInt(2);
			bLabel[i]=1*Math.pow(3, i);
			sum+=aLabel[i]*bLabel[i];
		}
		double[][] matrix =new double[n][n];
		for(int i=0;i<n;i++)
			for(int j=0;j<n;j++)
				matrix[i][j]=random.nextDouble();
		System.out.println(sum);
		Matrix test=new Matrix(matrix);
		new Matrix(aLabel, 1).times(test).times(test.inverse()).times(new Matrix(bLabel,1).transpose()).print(16, 16);
//		Matrix iMatrix=test.times(test.inverse());
//		iMatrix.print(1, 1);
	}

}
