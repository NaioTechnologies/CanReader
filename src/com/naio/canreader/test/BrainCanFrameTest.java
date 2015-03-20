package com.naio.canreader.test;

import net.sourceforge.juint.UInt8;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class BrainCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private CanParser canParser;
	
	@SuppressWarnings("deprecation")
	public BrainCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		//MainActivity mainActivity = getActivity();
		canParser = new CanParser();
	}
	
	public void testParseTemperature(){
		canParser.parseOneFrame("(1215.1251) can0 00E [1] 08");
		assertEquals(canParser.getBraincanframe().getTemperature(), new UInt8(8));
	}

}
