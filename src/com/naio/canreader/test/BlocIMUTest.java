package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;
import com.robotium.solo.Solo;

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

	@Override
	protected void tearDown() throws Exception {
		mBlocIMUActivity.finish();
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

	public void testTextViews() {
		textView_accel();
		textView_gyro();
		textView_magneto();
		textView_gps();
		textView_version();
		textView_res_temp();
	}

	public void testRTR() {
		MainActivity.UNIT_TEST = false;
		mBlocIMUActivity = getActivity();
		Solo han = new Solo(getInstrumentation(), mBlocIMUActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		rtrTempe(han);
		getInstrumentation().waitForIdleSync();
		rtrBoard(han);
		getInstrumentation().waitForIdleSync();
		rtrVersion(han);
		getInstrumentation().waitForIdleSync();
	}

	public void testRead() {
		Solo han = new Solo(getInstrumentation(), mBlocIMUActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.clickOnButton(mBlocIMUActivity.getString(R.string.imu_button_read));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for READ!", han.searchText("CAN"));
	}

	public void testInfo() {
		Solo han = new Solo(getInstrumentation(), mBlocIMUActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.clickOnActionBarItem(R.id.action_settings);
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the info dialog!",
				han.searchText("Utilisation"));
	}

	public void testRTRVcan() {
		MainActivity.UNIT_TEST = true;
		mBlocIMUActivity = getActivity();
		Solo han = new Solo(getInstrumentation(), mBlocIMUActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("READ");
		getInstrumentation().waitForIdleSync();
		rtrTempeVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrBoardVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrVersionVcan(han);
		getInstrumentation().waitForIdleSync();
	}

	private void rtrVersionVcan(Solo han) {
		han.clickOnButton(mBlocIMUActivity
				.getString(R.string.imu_button_version));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!",
				mBlocIMUActivity.getCanParserThread().getCanParser()
						.getRemote_for_unit());
	}

	private void rtrBoardVcan(Solo han) {
		han.clickOnButton(mBlocIMUActivity.getString(R.string.imu_button_board));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!",
				mBlocIMUActivity.getCanParserThread().getCanParser()
						.getRemote_for_unit());
	}

	private void rtrTempeVcan(Solo han) {
		han.clickOnButton(mBlocIMUActivity.getString(R.string.imu_button_tempe));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!",
				mBlocIMUActivity.getCanParserThread().getCanParser()
						.getRemote_for_unit());
	}

	private void rtrVersion(Solo han) {
		han.clickOnButton(mBlocIMUActivity
				.getString(R.string.imu_button_version));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for version!",
				han.searchText("@184"));
	}

	private void rtrBoard(Solo han) {
		han.clickOnButton(mBlocIMUActivity.getString(R.string.imu_button_board));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for board and rev!",
				han.searchText("@18F"));
	}

	private void rtrTempe(Solo han) {
		han.clickOnButton(mBlocIMUActivity.getString(R.string.imu_button_tempe));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for tempé!",
				han.searchText("@183"));
	}

	public void textView_accel() {
		final String expected_x = mBlocIMUActivity
				.getString(R.string.imu_accel_x_resp);
		final String expected_y = mBlocIMUActivity
				.getString(R.string.imu_accel_y_resp);
		final String expected_z = mBlocIMUActivity
				.getString(R.string.imu_accel_z_resp);
		final String actual_xmsb = mAccel_xmsb.getText().toString();
		final String actual_ymsb = mAccel_ymsb.getText().toString();
		final String actual_zmsb = mAccel_zmsb.getText().toString();
		assertEquals(expected_x, actual_xmsb);
		assertEquals(expected_y, actual_ymsb);
		assertEquals(expected_z, actual_zmsb);
	}

	public void textView_gyro() {
		final String expected_x = mBlocIMUActivity
				.getString(R.string.imu_gyro_x_resp);
		final String expected_y = mBlocIMUActivity
				.getString(R.string.imu_gyro_y_resp);
		final String expected_z = mBlocIMUActivity
				.getString(R.string.imu_gyro_z_resp);
		final String actual_xmsb = mGyro_xmsb.getText().toString();
		final String actual_ymsb = mGyro_ymsb.getText().toString();
		final String actual_zmsb = mGyro_zmsb.getText().toString();
		assertEquals(expected_x, actual_xmsb);
		assertEquals(expected_y, actual_ymsb);
		assertEquals(expected_z, actual_zmsb);
	}

	public void textView_magneto() {
		final String expected_x = mBlocIMUActivity
				.getString(R.string.imu_magneto_x_resp);
		final String expected_y = mBlocIMUActivity
				.getString(R.string.imu_magneto_y_resp);
		final String expected_z = mBlocIMUActivity
				.getString(R.string.imu_magneto_z_resp);
		final String actual_xmsb = mMag_xmsb.getText().toString();
		final String actual_ymsb = mMag_ymsb.getText().toString();
		final String actual_zmsb = mMag_zmsb.getText().toString();
		assertEquals(expected_x, actual_xmsb);
		assertEquals(expected_y, actual_ymsb);
		assertEquals(expected_z, actual_zmsb);
	}

	public void textView_gps() {
		final String expected_1 = mBlocIMUActivity
				.getString(R.string.imu_gps_1_resp);
		final String expected_2 = mBlocIMUActivity
				.getString(R.string.imu_gps_2_resp);
		final String expected_3 = mBlocIMUActivity
				.getString(R.string.imu_gps_3_resp);
		final String expected_4 = mBlocIMUActivity
				.getString(R.string.imu_gps_4_resp);
		final String actual_gps1 = mImu_gps_1.getText().toString();
		final String actual_gps2 = mImu_gps_2.getText().toString();
		final String actual_gps3 = mImu_gps_3.getText().toString();
		final String actual_gps4 = mImu_gps_4.getText().toString();
		assertEquals(expected_1, actual_gps1);
		assertEquals(expected_2, actual_gps2);
		assertEquals(expected_3, actual_gps3);
		assertEquals(expected_4, actual_gps4);
	}

	public void textView_version() {
		final String expected_board = mBlocIMUActivity
				.getString(R.string.imu_button_board_resp);
		final String expected_rev = mBlocIMUActivity
				.getString(R.string.imu_rev_resp);
		final String expectedMaj = mBlocIMUActivity
				.getString(R.string.imu_button_version_maj_resp);
		final String expectedMin = mBlocIMUActivity
				.getString(R.string.imu_button_version_min_resp);
		final String actual_min = mImu_min.getText().toString();
		final String actual_maj = mImu_maj.getText().toString();
		final String actual_board = mImu_board.getText().toString();
		final String actual_rev = mImu_rev.getText().toString();
		assertEquals(expectedMin, actual_min);
		assertEquals(expectedMaj, actual_maj);
		assertEquals(expected_board, actual_board);
		assertEquals(expected_rev, actual_rev);
	}

	public void textView_res_temp() {
		final String expected_temp = mBlocIMUActivity
				.getString(R.string.imu_button_tempe_resp);
		final String expected_res = mBlocIMUActivity
				.getString(R.string.imu_resmng_resp);
		final String actual_res = mMag_res.getText().toString();
		final String actual_temp = mImu_temp.getText().toString();
		assertEquals(expected_res, actual_res);
		assertEquals(expected_temp, actual_temp);
	}
}
