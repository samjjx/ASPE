package com.jamatest.jjx;

import java.math.BigDecimal;

public class BigTest {
	public static void main(String[] args)
	{
		BigDecimal firstBigDecimal=new BigDecimal("1");
		BigDecimal secondBigDecimal=new BigDecimal("6");
		firstBigDecimal=secondBigDecimal;
		secondBigDecimal.subtract(new BigDecimal("3"));
		System.out.println(firstBigDecimal);
	}

}
