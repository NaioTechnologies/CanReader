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
	
	public void testLectureODO(){
		canParser.parseOneFrame("(1215.1251) can0 403 [1] 0F");
		assertEquals(canParser.getVerincanframe().getLectureODO().toString(), "15");
	}
	
	public void testVersionSoft(){
		canParser.parseOneFrame("(1215.1251) can0 405 [1] 01 02");
		assertEquals(canParser.getVerincanframe().getVersionMaj().toString(), "1");
		assertEquals(canParser.getVerincanframe().getVersionMin().toString(), "2");
	}
	
	public void testTension12v33v5vAndFlag(){
		canParser.parseOneFrame("(1215.1251) can0 406 [1] 01 02 03 04 05 06 02");
		assertEquals(canParser.getVerincanframe().getT12vLSB().toString(), "1");
		assertEquals(canParser.getVerincanframe().getT12vMSB().toString(), "2");
		assertEquals(canParser.getVerincanframe().getT33vLSB().toString(), "3");
		assertEquals(canParser.getVerincanframe().getT33vMSB().toString(), "4");
		assertEquals(canParser.getVerincanframe().getT5vLSB().toString(), "5");
		assertEquals(canParser.getVerincanframe().getT5vMSB().toString(), "6");
		assertEquals(canParser.getVerincanframe().getFlagSortie().toString(), "2");
	}
	
	public void testTension24vPile(){
		canParser.parseOneFrame("(1215.1251) can0 407 [1] 01 02 03 04");
		assertEquals(canParser.getVerincanframe().getT24vLSB().toString(), "1");
		assertEquals(canParser.getVerincanframe().getT24vMSB().toString(), "2");
		assertEquals(canParser.getVerincanframe().getPileLSB().toString(), "3");
		assertEquals(canParser.getVerincanframe().getPileMSB().toString(), "4");
	}
	
}
