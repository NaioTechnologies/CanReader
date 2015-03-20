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
		canParser.parseOneFrame("(1215.1251) can0 180 [1] 08 02 00 00 00 00");
		assertEquals(canParser.getImucanframe().getAccelXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getAccelXMSB().toString(), "8");
	}
	
	public void testParseGyro(){
		canParser.parseOneFrame("(1215.1251) can0 181 [1] 08 02 00 00 00 00");
		assertEquals(canParser.getImucanframe().getGyroXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getGyroXMSB().toString(), "8");
	}
	
	public void testParseMagneto(){
		canParser.parseOneFrame("(1215.1251) can0 182 [1] 08 02 00 00 00 00 00 00");
		assertEquals(canParser.getImucanframe().getMagnetoXLSB().toString(), "2");
		assertEquals(canParser.getImucanframe().getMagnetoXMSB().toString(), "8");
	}

}
