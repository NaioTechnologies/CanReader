package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.threads.CanSendThread;

import android.test.ActivityInstrumentationTestCase2;

public class CanSendThreadTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private CanSendThread canSendThread;

	@SuppressWarnings("deprecation")
	public CanSendThreadTest() {
		super("com.naio.canreader.activities",MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		canSendThread = new CanSendThread();
	}
	
	public void testExecuteCommand(){
		
		canSendThread.addCmd("su -c /sbin/cansend can0 280#21");
		canSendThread.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//should return 'SIOCGIFINDEX: No such device'
		String response = canSendThread.getResponse();
		assertEquals("Execute a sudo command impossible",response.contains("device"), true);
		canSendThread.interrupt();
	}

}
