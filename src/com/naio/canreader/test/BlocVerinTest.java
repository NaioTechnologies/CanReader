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
		
	public void testTextView(){
		textView_connection();
		textView_odo();
		textView_position();
		textView_version();
	}
	
	public void textView_connection() {
	    final String expected = "Veuillez appuyer sur le bouton READ";
	    final String actual_co = mCo.getText().toString();
	    assertEquals(true,mCo.isShown());
	    assertEquals(expected, actual_co);
	}
	
	public void textView_odo() {
	    final String expected = "...";
	    final String actual_odo = mVerin_odo.getText().toString();
	    final String actual_odoc = mVerin_odoc.getText().toString();
	    assertEquals(expected, actual_odo);
	    assertEquals(expected, actual_odoc);
	}
	
	public void textView_position() {
	    final String expected = "...";
	    final String actual_position = mVerin_position.getText().toString();
	    final String actual_retour = mVerin_retour.getText().toString();
	    final String actual_commande = mVerin_commande.getText().toString();
	    assertEquals(expected, actual_position);
	    assertEquals(expected, actual_retour);
	    assertEquals(expected, actual_commande);
	}
		
	public void textView_version() {
	    final String expectedMaj = "Maj";
	    final String expectedMin = "Min";
	    final String actual_min = mVerin_min.getText().toString();
	    final String actual_maj = mVerin_maj.getText().toString();
	    assertEquals(expectedMin, actual_min);
	    assertEquals(expectedMaj, actual_maj);
	}
	
	public void testDialogPosition(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Position %");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the position dialog!", han.searchText("Envoyer"));
	}
	
	public void testDialogCommande(){
		Solo han = new Solo(getInstrumentation(),mActivity);
		han.clickOnButton("OK");
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.scrollToSide(Solo.RIGHT);
		getInstrumentation().waitForIdleSync();
		han.clickOnButton("Commande vérin");
		getInstrumentation().waitForIdleSync();
		assertTrue("Could not find the commande vérin dialog!", han.searchText("Envoyer"));
	}
	
}
