package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;
import com.robotium.solo.Solo;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class BlocGSMTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager mPager;
	private TextView mSms_read;
	private View mBlocGSMActivity;
	private TextView mCo;

	public BlocGSMTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocGSMActivity =  mPager.getChildAt(1);
		mSms_read = (TextView) mBlocGSMActivity.findViewById(R.id.read_sms);
		mCo = (TextView) mBlocGSMActivity.findViewById(R.id.text_connection);
	}
	
	public void testPreconditions() {
	    assertNotNull("mActivity is null", mActivity);
	    assertNotNull("mBlocGSMActivity is null", mBlocGSMActivity);
	    assertNotNull("mSms_read is null", mSms_read);
	    assertNotNull("mCo is null", mCo);
	}
	
	public void testTextViews(){
		textView_sms();
		textView_connection();
	}
	
	public void testDialogs(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		dialogSendSms(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogCustom(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogPin(han);
	}
	
	public void textView_sms() {
	    final String expected = mActivity.getString(R.string.gsm_display_sms);
	    final String actual_sms = mSms_read.getText().toString();
	    assertEquals(expected, actual_sms);
	}
	
	public void textView_connection() {
	    final String expected = mActivity.getString(R.string.global_text_not_connected);
	    final String actual_co = mCo.getText().toString();
	    assertEquals(true,mCo.isShown());
	    assertEquals(expected, actual_co);
	}
	
	public void dialogSendSms(Solo han){
		han.clickOnButton(mActivity.getString(R.string.gsm_button_send_sms));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the send a sms dialog!", han.searchText(mActivity.getString(R.string.sms_title)));
	}
	
	public void dialogCustom(Solo han){
		han.clickOnButton(mActivity.getString(R.string.gsm_button_custom));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the custom dialog!", han.searchText(mActivity.getString(R.string.at_title)));
	}
	
	private void dialogPin(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.gsm_button_pin));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the pin dialog!", han.searchText(mActivity.getString(R.string.pin_title)));
	}
	
}
