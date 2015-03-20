package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class GPSCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private CanParser canParser;
	
	@SuppressWarnings("deprecation")
	public GPSCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		canParser = new CanParser();
	}
	
	public void testParseGPS(){
		canParser.parseOneFrame("(1215.1251) can0 200 [1] 47");
		assertEquals(canParser.getGpscanframe().getGpsDataInString().contains("G"), true);
	}
	
}
