/**
 * Created the 23 janv. 2015 at 15:24:52
 * by bodereau
 * 
 */
package com.naio.canreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naio.canreader.R;

/**
 *  Return the inflated bloc gsm
 *  
 * @author bodereau
 *
 */
public class BlocGPSActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.bloc_gsm, container, false);
	}
}
