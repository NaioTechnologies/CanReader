package com.naio.canreader.test;

import com.naio.canreader.R;
import com.naio.canreader.activities.MainActivity;
import com.robotium.solo.Solo;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

public class BlocVerinTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;
	private ViewPager mPager;
	private View mBlocVerinActivity;
	private TextView mCo;
	private TextView mVerin_maj;
	private TextView mVerin_min;
	private TextView mVerin_odoc;
	private TextView mVerin_odo;
	private TextView mVerin_retour;
	private TextView mVerin_commande;
	private TextView mVerin_position;

	public BlocVerinTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocVerinActivity =  mPager.getChildAt(3);
		mCo = (TextView) mBlocVerinActivity.findViewById(R.id.text_connection);
		mVerin_position = (TextView) mBlocVerinActivity.findViewById(R.id.verin_position);
		mVerin_commande = (TextView) mBlocVerinActivity.findViewById(R.id.commande_moteur);
		mVerin_retour = (TextView) mBlocVerinActivity.findViewById(R.id.verin_retour_position);
		mVerin_odo = (TextView) mBlocVerinActivity.findViewById(R.id.lecture_odo);
		mVerin_odoc = (TextView) mBlocVerinActivity.findViewById(R.id.lecture_odoc);
		mVerin_maj = (TextView) mBlocVerinActivity.findViewById(R.id.version_verin_maj);
		mVerin_min = (TextView) mBlocVerinActivity.findViewById(R.id.version_verin_min);
	}
	
	@Override
	protected void tearDown() throws Exception{
		mActivity.finish();
	}

	public void testPreconditions() {
	    assertNotNull("mActivity is null", mActivity);
	    assertNotNull("mBlocVerinActivity is null", mBlocVerinActivity);
	    assertNotNull("mCo is null", mCo);
	    assertNotNull("mVerin_position is null", mVerin_position);
	    assertNotNull("mVerin_commande is null", mVerin_commande);
	    assertNotNull("mVerin_retour is null", mVerin_retour);
	    assertNotNull("mVerin_odo is null", mVerin_odo);
	    assertNotNull("mVerin_odoc is null", mVerin_odo);
	    assertNotNull("mIhm_maj is null", mVerin_maj);
	    assertNotNull("mIhm_min is null", mVerin_min);
	}
		
	public void testTextViews(){
		textView_connection();
		textView_odo();
		textView_position();
		textView_version();
	}
	
	public void testDialogs(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		dialogPosition(han);
		getInstrumentation().waitForIdleSync();
		han.goBack();
		getInstrumentation().waitForIdleSync();
		dialogCommande(han);
		getInstrumentation().waitForIdleSync();
	}
	
	public void testRTR(){
		MainActivity.UNIT_TEST = false;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocVerinActivity =  mPager.getChildAt(3);
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		rtrRetour(han);
		getInstrumentation().waitForIdleSync();
		rtrVersion(han);
		getInstrumentation().waitForIdleSync();
	}
	
	public void testRTRVcan(){
		MainActivity.UNIT_TEST = true;
		mActivity = getActivity();
		mPager = mActivity.getPager();
		mBlocVerinActivity =  mPager.getChildAt(3);
		Solo han = new Solo(getInstrumentation(),mActivity);
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
		rtrRetourVcan(han);
		getInstrumentation().waitForIdleSync();
		rtrVersionVcan(han);
		getInstrumentation().waitForIdleSync();
	}
	
	private void rtrVersionVcan(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.verin_odo_button_version));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!",mActivity.getCanParserThread().getCanParser().getRemote_for_unit());
		
	}

	private void rtrRetourVcan(Solo han) {
		han.clickOnButton( mActivity.getString(R.string.verin_button_retour));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not send the remote or didn't get it!", mActivity.getCanParserThread().getCanParser().getRemote_for_unit());	
	}

	private void rtrRetour(Solo han) {
		han.clickOnButton( mActivity.getString(R.string.verin_button_retour));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for retour position!", han.searchText("@401"));	
	}
	
	private void rtrVersion(Solo han) {
		han.clickOnButton(mActivity.getString(R.string.verin_odo_button_version));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the toast for version!", han.searchText("@405"));	
	}
	
	public void textView_connection() {
	    final String expected_co = mActivity.getString(R.string.global_text_not_connected);
	    final String actual_co = mCo.getText().toString();
	    assertEquals(true,mCo.isShown());
	    assertEquals(expected_co, actual_co);
	}
	
	public void textView_odo() {
	    final String expected_odo = mActivity.getString(R.string.odo_lecture_resp);
	    final String expected_odoc = mActivity.getString(R.string.odo_button_reset_resp);
	    final String actual_odo = mVerin_odo.getText().toString();
	    final String actual_odoc = mVerin_odoc.getText().toString();
	    assertEquals(expected_odo, actual_odo);
	    assertEquals(expected_odoc, actual_odoc);
	}
	
	public void textView_position() {
	    final String expected_commande = mActivity.getString(R.string.verin_button_commande_resp);
	    final String expected_retour = mActivity.getString(R.string.verin_button_retour_resp);
	    final String expected_position = mActivity.getString(R.string.verin_button_position_resp);
	    final String actual_position = mVerin_position.getText().toString();
	    final String actual_retour = mVerin_retour.getText().toString();
	    final String actual_commande = mVerin_commande.getText().toString();
	    assertEquals(expected_position, actual_position);
	    assertEquals(expected_retour, actual_retour);
	    assertEquals(expected_commande, actual_commande);
	}
		
	public void textView_version() {
	    final String expectedMaj = mActivity.getString(R.string.verin_odo_button_version_maj_resp);
	    final String expectedMin = mActivity.getString(R.string.verin_odo_button_version_min_resp);
	    final String actual_min = mVerin_min.getText().toString();
	    final String actual_maj = mVerin_maj.getText().toString();
	    assertEquals(expectedMin, actual_min);
	    assertEquals(expectedMaj, actual_maj);
	}
	
	public void dialogPosition(Solo han){	
		han.clickOnButton(mActivity.getString(R.string.verin_button_position));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the position dialog!", han.searchText(mActivity.getString(R.string.global_button_envoyer)));
	}
	
	public void dialogCommande(Solo han){
		han.clickOnButton(mActivity.getString(R.string.verin_button_commande));
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the commande vérin dialog!", han.searchText(mActivity.getString(R.string.global_button_envoyer)));
	}
	
}
