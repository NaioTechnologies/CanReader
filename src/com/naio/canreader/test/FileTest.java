package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class FileTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;

	public FileTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	
	public void testWriteRead(){
		String file = "";
		mActivity.write_in_file(mActivity, "unit test");
		try {
			file = mActivity.getStringFromFile(mActivity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(file.contains("unit test"));
	}
	
	public void testDelete(){
		mActivity.delete_file(mActivity);
		String file = "";
		try {
			file = mActivity.getStringFromFile(mActivity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(file.length()>=1 && file.length()<=2);
	}

}
