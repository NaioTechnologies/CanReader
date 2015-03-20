package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

import android.test.ActivityInstrumentationTestCase2;

public class IMUCanFrameTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private CanParser canParser;
	
	@SuppressWarnings("deprecation")
	public IMUCanFrameTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		canParser = new CanParser();
	}
	
	public void testParseAccel(){
		canParser.parseOneFrame("(1215.1251) can0 180 [1] 08 02 07 03 06 04");
		assertEquals(canParser.getImucanframe().getAccelXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getAccelXMSB().toString(), "8");
		assertEquals(canParser.getImucanframe().getAccelYLSB().toString(), "3");
		assertEquals(canParser.getImucanframe().getAccelYMSB().toString(), "7");
		assertEquals(canParser.getImucanframe().getAccelZLSB().toString(), "4");
		assertEquals(canParser.getImucanframe().getAccelZMSB().toString(), "6");
	}
	
	public void testParseGyro(){
		canParser.parseOneFrame("(1215.1251) can0 181 [1] 08 02 07 03 06 04");
		assertEquals(canParser.getImucanframe().getGyroXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getGyroXMSB().toString(), "8");
		assertEquals(canParser.getImucanframe().getGyroYLSB().toString(), "3");
		assertEquals(canParser.getImucanframe().getGyroYMSB().toString(), "7");
		assertEquals(canParser.getImucanframe().getGyroZLSB().toString(), "4");
		assertEquals(canParser.getImucanframe().getGyroZMSB().toString(), "6");
	}
	
	public void testParseMagneto(){
		canParser.parseOneFrame("(1215.1251) can0 182 [1] 08 02 07 03 06 04 09 01");
		assertEquals(canParser.getImucanframe().getMagnetoXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getMagnetoXMSB().toString(), "8");
		assertEquals(canParser.getImucanframe().getMagnetoYLSB().toString(), "3");
		assertEquals(canParser.getImucanframe().getMagnetoYMSB().toString(), "7");
		assertEquals(canParser.getImucanframe().getMagnetoZLSB().toString(), "4");
		assertEquals(canParser.getImucanframe().getMagnetoZMSB().toString(), "6");
		assertEquals(canParser.getImucanframe().getResMagnLSB().toString(), "1");
		assertEquals(canParser.getImucanframe().getResMagnMSB().toString(), "9");
	}
	
	public void testParseTemperature(){
		canParser.parseOneFrame("(1215.1251) can0 183 [1] 01");
		assertEquals(canParser.getImucanframe().getTemperature().toString(), "1");
	}
	
	public void testParseVersionSoft(){
		canParser.parseOneFrame("(1215.1251) can0 184 [1] 01 02");
		assertEquals(canParser.getImucanframe().getVersionMaj().toString(), "1");
		assertEquals(canParser.getImucanframe().getVersionMin().toString(), "2");
	}
	
	public void testParseBoardRev(){
		canParser.parseOneFrame("(1215.1251) can0 18F [1] 01 02");
		assertEquals(canParser.getImucanframe().getBoard().toString(), "1");
		assertEquals(canParser.getImucanframe().getRev().toString(), "2");
	}

}
