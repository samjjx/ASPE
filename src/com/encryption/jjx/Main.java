package com.encryption.jjx;

public class Main {
	public static long encryptedPerformance(int disCenters, int dimension, int labels)
	{
		long t0=System.currentTimeMillis();
		AspePerformance ap=new AspePerformance(disCenters, dimension);
		for(int i=0;i<labels;i++)
			ap.encryptPerformance();
		return System.currentTimeMillis()-t0;
	}
	public static void main(String[] args) {
		System.out.println(encryptedPerformance(25710, 100, 1000));
	}

}
