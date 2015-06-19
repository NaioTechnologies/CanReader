package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.utils.BytesFunction;

import android.test.ActivityInstrumentationTestCase2;

public class BytesFunctionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public BytesFunctionTest() {
		super("com.naio.canreader.activities", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// MainActivity mainActivity = getActivity();
	}

	public void testFillBinariesStrings() {
		assertEquals(BytesFunction.fillWithZeroTheBinaryString("0"), "00000000");
	}

	public void testFromTwoComplement() {
		assertEquals(BytesFunction.fromTwoComplement(16, 16), 16);
	}

}
