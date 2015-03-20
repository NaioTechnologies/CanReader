package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class IHMCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private CanParser canParser;
	
	@SuppressWarnings("deprecation")
	public IHMCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		//MainActivity mainActivity = getActivity();
		canParser = new CanParser();
	}
	
	public void testParseLed(){
		canParser.parseOneFrame("(1215.1251) can0 382 [1] 0F 0F");
		assertEquals(canParser.getIhmcanframe().getEtatLed().toString(), "15");
		assertEquals(canParser.getIhmcanframe().getCouleurLed().toString(), "15");
	}
	
	public void testParseContrast(){
		canParser.parseOneFrame("(1215.1251) can0 387 [1] 64");
		assertEquals(canParser.getIhmcanframe().getContraste().toString(), "100");
	}
	
	public void testParseBacklight(){
		canParser.parseOneFrame("(1215.1251) can0 388 [1] 64");
		assertEquals(canParser.getIhmcanframe().getBacklight().toString(), "100");
	}
	
	public void testParseClavier(){
		canParser.parseOneFrame("(1215.1251) can0 381 [1] 01");
		assertEquals(canParser.getIhmcanframe().getEtatClavier().toString(), "1");
	}
	
	public void testParseVersionSoft(){
		canParser.parseOneFrame("(1215.1251) can0 386 [1] 01 02");
		assertEquals(canParser.getIhmcanframe().getVersionMaj().toString(), "1");
		assertEquals(canParser.getIhmcanframe().getVersionMin().toString(), "2");
	}
	
	public void testParseBoardRev(){
		canParser.parseOneFrame("(1215.1251) can0 38F [1] 01 02");
		assertEquals(canParser.getIhmcanframe().getBoard().toString(), "1");
		assertEquals(canParser.getIhmcanframe().getRev().toString(), "2");
	}

}
