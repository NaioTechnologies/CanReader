package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class BlocIMUTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mBlocIMUActivity;
	private TextView mAccel_xmsb;
	private TextView mAccel_zmsb;
	private TextView mAccel_ymsb;
	private TextView mGyro_xmsb;
	private TextView mGyro_ymsb;
	private TextView mGyro_zmsb;
	private TextView mMag_xmsb;
	private TextView mMag_ymsb;
	private TextView mMag_zmsb;
	private TextView mMag_res;
	private TextView mImu_temp;
	private TextView mImu_board;
	private TextView mImu_rev;
	private TextView mImu_maj;
	private TextView mImu_min;
	private TextView mImu_gps_1;
	private TextView mImu_gps_2;
	private TextView mImu_gps_3;
	private TextView mImu_gps_4;

	public BlocIMUTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mBlocIMUActivity = getActivity();
		mAccel_xmsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.accel_xmsbf);
		mAccel_ymsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.accel_ymsbf);
		mAccel_zmsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.accel_zmsbf);
		mGyro_xmsb = (TextView) mBlocIMUActivity.findViewById(R.id.gyro_xmsbf);
		mGyro_ymsb = (TextView) mBlocIMUActivity.findViewById(R.id.gyro_ymsbf);
		mGyro_zmsb = (TextView) mBlocIMUActivity.findViewById(R.id.gyro_zmsbf);
		mMag_xmsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.magneto_xmsbf);
		mMag_ymsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.magneto_ymsbf);
		mMag_zmsb = (TextView) mBlocIMUActivity
				.findViewById(R.id.magneto_zmsbf);
		mMag_res = (TextView) mBlocIMUActivity
				.findViewById(R.id.magneto_resmsbf);
		mImu_temp = (TextView) mBlocIMUActivity.findViewById(R.id.imu_tempef);
		mImu_board = (TextView) mBlocIMUActivity
				.findViewById(R.id.magneto_boardf);
		mImu_rev = (TextView) mBlocIMUActivity.findViewById(R.id.magneto_revf);
		mImu_maj = (TextView) mBlocIMUActivity.findViewById(R.id.imu_majf);
		mImu_min = (TextView) mBlocIMUActivity.findViewById(R.id.imu_minf);
		mImu_gps_1 = (TextView) mBlocIMUActivity
				.findViewById(R.id.textview_gps_text_main_activity);
		mImu_gps_2 = (TextView) mBlocIMUActivity
				.findViewById(R.id.textview_gps_text2_main_activity);
		mImu_gps_3 = (TextView) mBlocIMUActivity
				.findViewById(R.id.textview_gps_text3_main_activity);
		mImu_gps_4 = (TextView) mBlocIMUActivity
				.findViewById(R.id.textview_gps_text4_main_activity);
	}

	public void testPreconditions() {
		assertNotNull("mFirstTestActivity is null", mBlocIMUActivity);
		assertNotNull("mAccel_xmsb is null", mAccel_xmsb);
		assertNotNull("mAccel_ymsb is null", mAccel_ymsb);
		assertNotNull("mAccel_zmsb is null", mAccel_zmsb);
		assertNotNull("mGyro_xmsb is null", mGyro_xmsb);
		assertNotNull("mGyro_ymsb is null", mGyro_ymsb);
		assertNotNull("mGyro_zmsb is null", mGyro_zmsb);
		assertNotNull("mMag_xmsb is null", mMag_xmsb);
		assertNotNull("mMag_ymsb is null", mMag_ymsb);
		assertNotNull("mMag_zmsb is null", mMag_zmsb);
		assertNotNull("mMag_res is null", mMag_res);
		assertNotNull("mImu_temp is null", mImu_temp);
		assertNotNull("mImu_board is null", mImu_board);
		assertNotNull("mImu_rev is null", mImu_rev);
		assertNotNull("mImu_maj is null", mImu_maj);
		assertNotNull("mImu_min is null", mImu_min);
		assertNotNull("mImu_gps_1 is null", mImu_gps_1);
		assertNotNull("mImu_gps_2 is null", mImu_gps_2);
		assertNotNull("mImu_gps_3 is null", mImu_gps_3);
		assertNotNull("mImu_gps_4 is null", mImu_gps_4);
	}

	public void testTextView() {
		textView_accel();
		textView_gyro();
		textView_magneto();
		textView_gps();
		textView_version();
		textView_res_temp();
	}

	public void textView_accel() {
		final String expected = "...";
		final String actual_xmsb = mAccel_xmsb.getText().toString();
		final String actual_ymsb = mAccel_ymsb.getText().toString();
		final String actual_zmsb = mAccel_zmsb.getText().toString();
		assertEquals(expected, actual_xmsb);
		assertEquals(expected, actual_ymsb);
		assertEquals(expected, actual_zmsb);
	}

	public void textView_gyro() {
		final String expected = "...";
		final String actual_xmsb = mGyro_xmsb.getText().toString();
		final String actual_ymsb = mGyro_ymsb.getText().toString();
		final String actual_zmsb = mGyro_zmsb.getText().toString();
		assertEquals(expected, actual_xmsb);
		assertEquals(expected, actual_ymsb);
		assertEquals(expected, actual_zmsb);
	}

	public void textView_magneto() {
		final String expected = "...";
		final String actual_xmsb = mMag_xmsb.getText().toString();
		final String actual_ymsb = mMag_ymsb.getText().toString();
		final String actual_zmsb = mMag_zmsb.getText().toString();
		assertEquals(expected, actual_xmsb);
		assertEquals(expected, actual_ymsb);
		assertEquals(expected, actual_zmsb);
	}

	public void textView_gps() {
		final String expected = "...";
		final String actual_gps1 = mImu_gps_1.getText().toString();
		final String actual_gps2 = mImu_gps_2.getText().toString();
		final String actual_gps3 = mImu_gps_3.getText().toString();
		final String actual_gps4 = mImu_gps_4.getText().toString();
		assertEquals(expected, actual_gps1);
		assertEquals(expected, actual_gps2);
		assertEquals(expected, actual_gps3);
		assertEquals(expected, actual_gps4);
	}

	public void textView_version() {
		final String expected = "...";
		final String expectedMaj = "Maj";
		final String expectedMin = "Min";
		final String actual_min = mImu_min.getText().toString();
		final String actual_maj = mImu_maj.getText().toString();
		final String actual_board = mImu_board.getText().toString();
		final String actual_rev = mImu_rev.getText().toString();
		assertEquals(expectedMin, actual_min);
		assertEquals(expectedMaj, actual_maj);
		assertEquals(expected, actual_board);
		assertEquals(expected, actual_rev);
	}

	public void textView_res_temp() {
		final String expected = "...";
		final String actual_res = mMag_res.getText().toString();
		final String actual_temp = mImu_temp.getText().toString();
		assertEquals(expected, actual_res);
		assertEquals(expected, actual_temp);
	}
}
