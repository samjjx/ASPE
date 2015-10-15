package com.encryption.jjx;

//import java.util.Random;

import Jama.Matrix;

public class Encryption {
	public static double[] generateVector(int d)
	{
		double[] label=new double[d];
//		Random random=new Random();
		for(int i=0;i<label.length;i++)
//			label[i]=random.nextDouble();
			label[i]=1;
		return label;
	}
	
	public static void main(String[] args) {
//		int d=100;
//		ASPE aspe=new ASPE(d);
//		Matrix testMatrix=aspe.M1.times(aspe.M1T);
//		//testMatrix.print(d, d);
//		double[] label=generateVector(d);
//		double[][] splited=aspe.splitLabel(label);
//		new Matrix(splited[0],1).print(d, 1);
//		double[] encryptA=aspe.encryptSeperateLabel(splited[0], 0);
//		new Matrix(encryptA,1).times(aspe.M1T).print(d, 1);
//		double[] encryptB=aspe.encryptSeperateLabel(splited[1], 1);
		double big=Math.pow(3, 50);
		for(int i=0;i<50;i++)
			System.out.println(big/=3);
	}
	
}
