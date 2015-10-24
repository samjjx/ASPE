package com.jamatest.jjx;

import java.util.Scanner;

public class FirstClass {

	private static char[] as;
	private static int[] num;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		String line = input.nextLine();
		int n = line.length();
		int k = 0;
		as = new char[n];
		num = new int[n];
		as[0] = line.charAt(0);

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < as.length; j++) {
				if (as[j] == line.charAt(i)) {
					num[j]++;
					break;
				} else {
					as[k] = line.charAt(i);
					num[k]++;
					k++;
				}
			}
		}

	}
}
