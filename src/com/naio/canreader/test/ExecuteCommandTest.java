package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

public class ExecuteCommandTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mainActivity;

	@SuppressWarnings("deprecation")
	public ExecuteCommandTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		mainActivity = new MainActivity();
	}
	
	public void testExecuteCommand(){
		//should return 'SIOCGIFINDEX: No such device'
		String response = mainActivity.executeCommand("su -c /sbin/candump can0");
		assertEquals("Execute a sudo command impossible",response.contains("device"), true);
	}

}
