package com.jamatest.jjx;

import java.math.BigDecimal;
import java.util.Random;

public class CommonTest {

	public static void main(String[] args) {
		int n = 100;
		BigDecimal[] aLabel = new BigDecimal[n];
		BigDecimal[] bLabel = new BigDecimal[n];
		Random random = new Random();
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < aLabel.length; i++) {
			aLabel[i] = new BigDecimal("1");
			bLabel[i] = new BigDecimal("3").pow(i);
			sum.add(aLabel[i].multiply(bLabel[i]));
		}
		System.out.println(sum);
		BigDecimal[][] vectorBigDecimal = new BigDecimal[n][n];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				vectorBigDecimal[i][j] = new BigDecimal(Double.toString(random
						.nextDouble()));
			}
		

	}

}
