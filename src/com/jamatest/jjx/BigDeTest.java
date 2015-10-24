package com.jamatest.jjx;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDeTest {
	public static void main(String[] args)
	{
		BigDecimal bd=new BigDecimal("0.6");
		System.out.println(bd.setScale(0,RoundingMode.HALF_UP).toBigInteger());
	}

}
