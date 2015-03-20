package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class VerinCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private CanParser canParser;
	
	@SuppressWarnings("deprecation")
	public VerinCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		canParser = new CanParser();
	}
	
	public void testParseRequetePosition(){
		canParser.parseOneFrame("(1215.1251) can0 400 [1] 32");
		assertEquals(canParser.getVerincanframe().getRequetePosition().toString(), "50");
	}
	
	public void testParseRetourPosition(){
		canParser.parseOneFrame("(1215.1251) can0 401 [1] 64 00");
		assertEquals(canParser.getVerincanframe().getRetourPosition().toString(), "100");
	}
}
