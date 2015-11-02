package com.encryption.jjx;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static long queryPerformance(int disCenters, int dimension,
			int times) {
		AspePerformance ap = new AspePerformance(disCenters, dimension);
		int count=0;
		for (int i = 0; i < times; i++)
			count+=ap.queryOneTime();
		System.out.println("Total time\t Encryption time \t Query time \t Descryption time");
		System.out.println(ap.TotalTime+"\t"+ap.EtTime+"\t"+ ap.QtTime+"\t"+ap.DtTime+"\t");
		return count;
	}
	public static long encryptedPerformance(int disCenters, int dimension,
			int times) {
		AspePerformance ap = new AspePerformance(disCenters, dimension);
		long t0 = System.currentTimeMillis();
		for (int i = 0; i < times; i++)
			ap.encryptPerformance(0);
		return System.currentTimeMillis() - t0;
	}
	public static void testError(int disCenters, int dimension, int queryTime) {
		AspePerformance ap = new AspePerformance(disCenters, dimension);
		int errorCount = 0;
		for (int i = 0; i < queryTime; i++) {
			System.out.println("Begin :" + i);
			boolean reachability = ap.queryOneTimeTest();
//			System.out.println("lin"+ap.linBitSet);
//			System.out.println("lout"+ap.loutBitSet);
			ap.linBitSet.and(ap.loutBitSet);
			
			boolean plainText = !ap.linBitSet.isEmpty();
			if (reachability != plainText) {
				System.out.println("encrypt:" + reachability);
				System.out.println("plainText:" + plainText);
				errorCount++;
			} else {
				System.out.println("encrypt:" + reachability);
				System.out.println("plainText:" + plainText);
			}
			ap.linBitSet.clear();
			ap.loutBitSet.clear();
			System.out.println("End");
			System.out.println();
		}
		System.out.println("queryTime:" + queryTime);
		System.out.println("Number of error:" + errorCount);
	}

	public static void testError(int dimension, int queryTime, String dataset)
			throws IOException {
		Statistic st = new Statistic(dataset, 0);
		int disCenters = st.disCenterTotal(st.labelIn, st.labelOut);
		Random random = new Random();

		AspePerformance ap = new AspePerformance(disCenters, dimension);
		int errorCount = 0;
		for (int i = 0; i < queryTime; i++) {
			// System.out.println("Begin :"+ i);
			int u = 0;
			int v = 0;
			while (u == v) {
				u = random.nextInt(disCenters);
				v = random.nextInt(disCenters);
			}
			boolean reachability = ap.queryOneTimeTest(st.labelIn.get(v),
					st.labelOut.get(u));
			ap.linBitSet.and(ap.loutBitSet);
			boolean plainText = !ap.linBitSet.isEmpty();
			if (reachability != plainText) {
				// System.out.println("encrypt:"+ reachability);
				// System.out.println("plainText:"+plainText);
				errorCount++;
			} else {
				// System.out.println("encrypt:"+ reachability);
				// System.out.println("plainText:"+plainText);
			}
			ap.linBitSet.clear();
			ap.loutBitSet.clear();
			// System.out.println("End");
			// System.out.println();
		}
		System.out.println("queryTime:" + queryTime);
		System.out.println("Number of error:" + errorCount);
	}

	public static void main(String[] args) throws IOException {
		// System.out.println(encryptedPerformance(2467, 100, 1000));
//		ArrayList<String> dataList = new ArrayList<String>();
//		
//		dataList.add("p2p-Gnutella25");
//		dataList.add("p2p-Gnutella30");
//		dataList.add("p2p-Gnutella31");
//		dataList.add("Amazon0302");
//		dataList.add("Wiki-Vote");
//		for (String dataset : dataList) {
//			System.out.println("*************************");
//			System.out.println("dataset is :" + dataset);
//			for (int i = 0; i < 10; i++) {
//				System.out.println("The dimension is:" + (20 + i));
//				testError(20 + i, 10000, dataset);
//			}
//		}
		Scanner scan=new Scanner(System.in);
		while(scan.hasNext())
		{
			int centers=scan.nextInt();
			testError(centers, 4, 4);
//			System.out.println((double)queryPerformance(centers, 24, 10000)/10000);
		}
		
	}

}
