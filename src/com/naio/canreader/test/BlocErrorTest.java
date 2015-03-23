package com.naio.canreader.test;

import com.naio.canreader.activities.MainActivity;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

public class BlocErrorTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager mPager;
	private View mBlocErreurActivity;

	public BlocErrorTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocErreurActivity = mPager.getChildAt(4);
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
		assertNotNull("mBlocErreurActivity is null", mBlocErreurActivity);
	}



}
