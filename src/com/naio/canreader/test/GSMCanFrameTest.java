package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class GSMCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private CanParser canParser;
	
	public GSMCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		canParser = new CanParser();
	}
	
	public void testParseGSM(){
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 41");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 54");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 2B");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 43");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 50");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 49");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 4E");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 3F");
		canParser.parseOneFrame("(1215.1251) can0 280 [1] 0D");
		assertEquals(canParser.getGsmcanframe().isGsmWorking(), true);
	}
	
}
