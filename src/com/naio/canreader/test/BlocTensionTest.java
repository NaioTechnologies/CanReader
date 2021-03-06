package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;
import com.robotium.solo.Solo;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class BlocTensionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager mPager;
	private View mBlocTensionActivity;
	private TextView mCo;
	private TextView mTempe_cpu;
	private TextView mTension_24;
	private TextView mTension_12;
	private TextView mTension_pile;
	private TextView mTension_5;
	private TextView mTension_33;
	private TextView mTension_flag;

	public BlocTensionTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocTensionActivity = mPager.getChildAt(4);
		mCo = (TextView) mBlocTensionActivity
				.findViewById(R.id.text_connection);
		mTempe_cpu = (TextView) mBlocTensionActivity
				.findViewById(R.id.temperature_cpu);
		mTension_24 = (TextView) mBlocTensionActivity
				.findViewById(R.id.tension_24v);
		mTension_12 = (TextView) mBlocTensionActivity
				.findViewById(R.id.tension_12v);
		mTension_pile = (TextView) mBlocTensionActivity
				.findViewById(R.id.tension_pile);
		mTension_5 = (TextView) mBlocTensionActivity
				.findViewById(R.id.tension_5v);
		mTension_33 = (TextView) mBlocTensionActivity
				.findViewById(R.id.tension_33v);
		mTension_flag = (TextView) mBlocTensionActivity
				.findViewById(R.id.flag_sortie);
	}
	
	@Override
	protected void tearDown() throws Exception{
		mActivity.finish();
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
		assertNotNull("mBloctensionActivity is null", mBlocTensionActivity);
		assertNotNull("mCo is null", mCo);
		assertNotNull("mTempe_cpu is null", mTempe_cpu);
		assertNotNull("mTension_24 is null", mTension_24);
		assertNotNull("mTension_12 is null", mTension_12);
		assertNotNull("mTension_pile is null", mTension_pile);
		assertNotNull("mTension_5 is null", mTension_5);
		assertNotNull("mTension_33 is null", mTension_33);
		assertNotNull("mTension_flag is null", mTension_flag);
	}

	public void testTextViews() {
		textView_tempe();
		textView_tension();
	}

	public void testRTR() {
		MainActivity.UNIT_TEST = false;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocTensionActivity =  mPager.getChildAt(4);
		Solo han = new Solo(getInstrumentation(), mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		rtrTension(han);
		getInstrumentation().waitForIdleSync();
		rtrTension2(han);
		getInstrumentation().waitForIdleSync();
	}

	public void testRTRVcan() {
		MainActivity.UNIT_TEST = true;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocTensionActivity = mPager.getChildAt(4);
		Solo han = new Solo(getInstrumentation(), mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("READ");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		rtrTensionVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrTension2Vcan(han);
		getInstrumentation().waitForIdleSync();
	}

	private void rtrTension2Vcan(Solo han) {
		han.clickOnButton(mActivity
				.getString(R.string.tension_button_tension_1));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!", mActivity
				.getCanParserThread().getCanParser().getRemote_for_unit());

	}

	private void rtrTensionVcan(Solo han) {
		han.clickOnButton(mActivity
				.getString(R.string.tension_button_tension_2));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!", mActivity
				.getCanParserThread().getCanParser().getRemote_for_unit());

	}

	private void rtrTension(Solo han) {
		han.clickOnButton(mActivity
				.getString(R.string.tension_button_tension_1));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for tension!",
				han.searchText("@407"));
	}

	private void rtrTension2(Solo han) {
		han.clickOnButton(mActivity
				.getString(R.string.tension_button_tension_2));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for tension 2!",
				han.searchText("@406"));
	}

	public void textView_tempe() {
		final String expected_tempe = mActivity
				.getString(R.string.tempe_cpu_resp);
		final String actual_tempe = mTempe_cpu.getText().toString();
		assertEquals(expected_tempe, actual_tempe);
	}

	public void textView_tension() {
		final String expected_24 = mActivity
				.getString(R.string.tension_24v_resp);
		final String expected_12 = mActivity
				.getString(R.string.tension_12v_resp);
		final String expected_33 = mActivity
				.getString(R.string.tension_33v_resp);
		final String expected_5 = mActivity.getString(R.string.tension_5v_resp);
		final String expected_pile = mActivity
				.getString(R.string.tension_pile_resp);
		final String actual_t24 = mTension_24.getText().toString();
		final String actual_t12 = mTension_12.getText().toString();
		final String actual_t5 = mTension_5.getText().toString();
		final String actual_t33 = mTension_33.getText().toString();
		final String actual_tpile = mTension_pile.getText().toString();
		assertEquals(expected_24, actual_t24);
		assertEquals(expected_12, actual_t12);
		assertEquals(expected_5, actual_t5);
		assertEquals(expected_33, actual_t33);
		assertEquals(expected_pile, actual_tpile);
	}

}
