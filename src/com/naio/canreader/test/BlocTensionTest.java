package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;

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

	public void testTextView() {
		textView_tempe();
		textView_tension();

	}

	public void textView_tempe() {
		final String expected = "...";
		final String actual_tempe = mTempe_cpu.getText().toString();
		assertEquals(expected, actual_tempe);
	}

	public void textView_tension() {
		final String expected = "...";
		final String actual_t24 = mTension_24.getText().toString();
		final String actual_t12 = mTension_12.getText().toString();
		final String actual_t5 = mTension_5.getText().toString();
		final String actual_t33 = mTension_33.getText().toString();
		final String actual_tpile = mTension_pile.getText().toString();
		assertEquals(expected, actual_t24);
		assertEquals(expected, actual_t12);
		assertEquals(expected, actual_t5);
		assertEquals(expected, actual_t33);
		assertEquals(expected, actual_tpile);
	}

}
