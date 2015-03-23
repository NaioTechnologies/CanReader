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
		
	public void testTextView(){
		textView_connection();
		textView_etats();
		textView_version();
		textView_light();
		textView_status();
	}
	public void textView_connection() {
	    final String expected = "Veuillez appuyer sur le bouton READ";
	    final String actual_co = mCo.getText().toString();
	    assertEquals(true,mCo.isShown());
	    assertEquals(expected, actual_co);
	}
	
	public void textView_etats() {
	    final String expected = "...";
	    final String actual_clavier = mEtat_clavier.getText().toString();
	    final String actual_led = mEtat_led.getText().toString();
	    assertEquals(expected, actual_clavier);
	    assertEquals(expected, actual_led);
	}
	
	public void textView_version() {
	    final String expected = "...";
	    final String expectedMaj = "Maj";
	    final String expectedMin = "Min";
	    final String actual_min = mIhm_min.getText().toString();
	    final String actual_maj = mIhm_maj.getText().toString();
	    final String actual_board = mIhm_board.getText().toString();
	    final String actual_rev = mIhm_rev.getText().toString();
	    assertEquals(expectedMin, actual_min);
	    assertEquals(expectedMaj, actual_maj);
	    assertEquals(expected, actual_board);
	    assertEquals(expected, actual_rev);
	}
	
	public void textView_light() {
	    final String expected = "...";
	    final String actual_contrast = mIhm_contrast.getText().toString();
	    final String actual_backlight = mIhm_backlight.getText().toString();
	    assertEquals(expected, actual_contrast);
	    assertEquals(expected, actual_backlight);
	}
	
	public void textView_status() {
	    final String expected = "...";
	    final String actual_status = mIhm_status.getText().toString();
	    assertEquals(expected, actual_status);
	}
	
	public void testDialogBuzzer(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Envoi commande buzzer");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the buzzer dialog!", han.searchText("Send buzzer command"));
	}
	
	public void testDialogAffichage(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Envoi commande affichage");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the display dialog!", han.searchText("Send display command"));
	}
	
	public void testDialogClavier(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Clavier");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the keyboard dialog!", han.searchText("Clavier"));
	}
	
	public void testDialogLed(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("LED");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the led dialog!", han.searchText("LED"));
	}
	
	public void testDialogContrast(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Contraste %");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the contrast dialog!", han.searchText("Envoyer"));
	}
	
	public void testDialogBacklight(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Backlight %");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the backlight dialog!", han.searchText("Envoyer"));
	}
	
}
