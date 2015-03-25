package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;
import com.robotium.solo.Solo;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class BlocIHMTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager mPager;
	private View mBlocIHMActivity;
	private TextView mCo;
	private TextView mEtat_clavier;
	private TextView mEtat_led;
	private TextView mIhm_contrast;
	private TextView mIhm_backlight;
	private TextView mIhm_status;
	private TextView mIhm_maj;
	private TextView mIhm_min;
	private TextView mIhm_board;
	private TextView mIhm_rev;

	public BlocIHMTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocIHMActivity =  mPager.getChildAt(2);
		mCo = (TextView) mBlocIHMActivity.findViewById(R.id.text_connection);
		mEtat_clavier = (TextView) mBlocIHMActivity.findViewById(R.id.etat_clavier);
		mEtat_led = (TextView) mBlocIHMActivity.findViewById(R.id.etat_led);
		mIhm_contrast = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_contraste);
		mIhm_backlight = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_backlight);
		mIhm_status = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_status);
		mIhm_maj = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_maj);
		mIhm_min = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_min);
		mIhm_board = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_board);
		mIhm_rev = (TextView) mBlocIHMActivity.findViewById(R.id.ihm_rev);
	}
	
	@Override
	protected void tearDown() throws Exception{
		mActivity.finish();
	}

	
	public void testPreconditions() {
	    assertNotNull("mActivity is null", mActivity);
	    assertNotNull("mBlocIHMActivity is null", mBlocIHMActivity);
	    assertNotNull("mCo is null", mCo);
	    assertNotNull("mEtat_clavier is null", mEtat_clavier);
	    assertNotNull("mEtat_led is null", mEtat_led);
	    assertNotNull("mIhm_contrast is null", mIhm_contrast);
	    assertNotNull("mIhm_backlight is null", mIhm_backlight);
	    assertNotNull("mIhm_status is null", mIhm_status);
	    assertNotNull("mIhm_maj is null", mIhm_maj);
	    assertNotNull("mIhm_min is null", mIhm_min);
	    assertNotNull("mIhm_board is null", mIhm_board);
	    assertNotNull("mIhm_rev is null", mIhm_rev);
	}
		
	public void testTextViews(){
		textView_connection();
		textView_etats();
		textView_version();
		textView_light();
		textView_status();
	}
	
	public void testDialogs(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		dialogBuzzer(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogAffichage(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogClavier(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogLed(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogContrast(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogBacklight(han);
		han.goBack();
		getInstrumentation().waitForIdleSync();
	}
	
	public void testRTR(){
		MainActivity.UNIT_TEST = false;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocIHMActivity =  mPager.getChildAt(2);
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		rtrStatus(han);
		getInstrumentation().waitForIdleSync();
		rtrVersion(han);
		getInstrumentation().waitForIdleSync();
		rtrBoard(han);
		getInstrumentation().waitForIdleSync();
	}
	
	public void testRTRVcan(){
		MainActivity.UNIT_TEST = true;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocIHMActivity =  mPager.getChildAt(2);
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("READ");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		rtrStatusVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrVersionVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrBoardVcan(han);
		getInstrumentation().waitForIdleSync();
	}
	
	private void rtrStatusVcan(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_statut));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't read it!", mActivity.getCanParserThread().getCanParser().getRemote_for_unit());	
		
	}

	private void rtrVersionVcan(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_statut));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't read it!", mActivity.getCanParserThread().getCanParser().getRemote_for_unit());	
		
	}

	private void rtrBoardVcan(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_board));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't read it!", mActivity.getCanParserThread().getCanParser().getRemote_for_unit());	
		
	}

	private void rtrBoard(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_board));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for board!", han.searchText("@38F"));	
	}
	
	private void rtrStatus(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_statut));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for status!", han.searchText("@384"));	
	}
	
	private void rtrVersion(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.ihm_button_version));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for version!", han.searchText("@386"));	
	}
	
	public void textView_connection() {
	    final String expected = mActivity.getString(R.string.global_text_not_connected);
	    final String actual_co = mCo.getText().toString();
	    assertEquals(true,mCo.isShown());
	    assertEquals(expected, actual_co);
	}
	
	public void textView_etats() {
	    final String expected_clavier = mActivity.getString(R.string.ihm_button_clavier_resp);
	    final String expected_led = mActivity.getString(R.string.ihm_button_led_resp);
	    final String actual_clavier = mEtat_clavier.getText().toString();
	    final String actual_led = mEtat_led.getText().toString();
	    assertEquals(expected_clavier, actual_clavier);
	    assertEquals(expected_led, actual_led);
	}
	
	public void textView_version() {
	    final String expected_board = mActivity.getString(R.string.ihm_button_board_resp);
	    final String expected_rev = mActivity.getString(R.string.ihm_button_rev_resp);
	    final String expectedMaj = mActivity.getString(R.string.ihm_button_version_maj_resp);
	    final String expectedMin = mActivity.getString(R.string.ihm_button_version_min_resp);
	    final String actual_min = mIhm_min.getText().toString();
	    final String actual_maj = mIhm_maj.getText().toString();
	    final String actual_board = mIhm_board.getText().toString();
	    final String actual_rev = mIhm_rev.getText().toString();
	    assertEquals(expectedMin, actual_min);
	    assertEquals(expectedMaj, actual_maj);
	    assertEquals(expected_board, actual_board);
	    assertEquals(expected_rev, actual_rev);
	}
	
	public void textView_light() {
	    final String expected_contraste = mActivity.getString(R.string.ihm_button_contraste_resp);
	    final String expected_backlight = mActivity.getString(R.string.ihm_button_backlight_resp);
	    final String actual_contrast = mIhm_contrast.getText().toString();
	    final String actual_backlight = mIhm_backlight.getText().toString();
	    assertEquals(expected_contraste, actual_contrast);
	    assertEquals(expected_backlight, actual_backlight);
	}
	
	public void textView_status() {
	    final String expected_status = mActivity.getString(R.string.ihm_button_statut_resp);
	    final String actual_status = mIhm_status.getText().toString();
	    assertEquals(expected_status, actual_status);
	}

	public void dialogBuzzer(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_envoi_buzzer));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the buzzer dialog!", han.searchText(mActivity.getString(R.string.buzzer_title)));
	}
	
	public void dialogAffichage(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_envoi_affichage));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the display dialog!", han.searchText(mActivity.getString(R.string.ecran_title)));
	}
	
	public void dialogClavier(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_clavier));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the keyboard dialog!", han.searchText(mActivity.getString(R.string.clavier_title)));
	}
	
	public void dialogLed(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_led));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the led dialog!", han.searchText(mActivity.getString(R.string.led_title_dialog)));
	}
	
	public void dialogContrast(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_contraste));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the contrast dialog!", han.searchText(mActivity.getString(R.string.global_button_envoyer)));
	}
	
	public void dialogBacklight(Solo han){
		han.clickOnButton(mActivity.getString(R.string.ihm_button_backlight));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the backlight dialog!", han.searchText(mActivity.getString(R.string.global_button_envoyer)));
	}
	
}
