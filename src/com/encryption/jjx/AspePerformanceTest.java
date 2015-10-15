package com.encryption.jjx;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AspePerformanceTest {
	private static AspePerformance ap=new AspePerformance(1000, 10); 
	@Before
	public void setUp() throws Exception {
		ap.clearCenters();
	}

	@Test
	public void testAspePerformance() {
		
	}

	@Test
	public void testEncryptPerformance() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testClearCenters() {
		fail("Not yet implemented");
	}

	@Test
	public void testDivide() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersect() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersectAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryByPiece() {
		fail("Not yet implemented");
	}

	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecode() {
		fail("Not yet implemented");
	}

}
