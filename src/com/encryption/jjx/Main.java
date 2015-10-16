package com.encryption.jjx;

public class Main {
	public static long encryptedPerformance(int disCenters, int dimension, int labels)
	{
		long t0=System.currentTimeMillis();
		AspePerformance ap=new AspePerformance(disCenters, dimension);
		for(int i=0;i<labels;i++)
			ap.encryptPerformance(0);
		return System.currentTimeMillis()-t0;
	}
	public static void testError(int disCenters, int dimension,int queryTime)
	{
		AspePerformance ap=new AspePerformance(disCenters, dimension);
		int errorCount=0;
		for(int i=0;i<queryTime;i++)
		{
			System.out.println("Begin :"+ i);
			boolean reachability=ap.queryOneTimeTest();
			ap.linBitSet.and(ap.loutBitSet);
			boolean plainText=!ap.linBitSet.isEmpty();
			if(reachability!=plainText)
			{
				System.out.println("encrypt:"+ reachability);
				System.out.println("plainText:"+plainText);
				errorCount++;
			}
			else {
				System.out.println("encrypt:"+ reachability);
				System.out.println("plainText:"+plainText);
			}
			ap.linBitSet.clear();
			ap.loutBitSet.clear();
			System.out.println("End");
			System.out.println();
		}
		System.out.println("queryTime:"+queryTime);
		System.out.println("Number of error:"+errorCount);
	}
	public static void main(String[] args) {
		//System.out.println(encryptedPerformance(2467, 100, 1000));
		testError(25710, 64, 10000);
	}

}
