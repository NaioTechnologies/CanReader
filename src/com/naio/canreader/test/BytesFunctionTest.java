package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.utils.BytesFunction;

import android.test.ActivityInstrumentationTestCase2;

public class BytesFunctionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	@SuppressWarnings("deprecation")
	public BytesFunctionTest() {
		super("com.naio.canreader.activities", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		// MainActivity mainActivity = getActivity();
	}

	public void testFillBinariesStrings() {
		// see robotium
		// Solo solo = new Solo(getInstrumentation(), getActivity());
		// getInstrumentation().waitForIdleSync();
		// // Now do whatever you need to do to trigger your dialog.
		//
		// // Let's assume a properly lame dialog title.
		// assertTrue("Could not find the dialog!",
		// solo.searchText("My Dialog Title"));
		// or
		// ViewAsserts.assertOnScreen(origin, view)
		assertEquals(BytesFunction.fillWithZeroTheBinaryString("0"), "00000000");
	}

	public void testFromTwoComplement() {
		assertEquals(BytesFunction.fromTwoComplement(16, 16), 16);
	}

}
