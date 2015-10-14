package com.jamatest.jjx;

import Jama.Matrix;

public class Main {
	public static void main(String[] args)
	{
		double[] label=new double[3];
		for(int i=0;i<label.length;i++)
			label[i]=i;
		Matrix test=Matrix.random(3, 3);
		test.times(new Matrix(label,1).transpose());
	}

}
