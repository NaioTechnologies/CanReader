package com.naio.canreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naio.canreader.R;
/**
 * Bloc of the can error
 * Every time a error relative to the can occured, it is written in a file.
 * This file is read when 'actualiser' button is pressed.
 * This file is deleted when 'supprimer' button is pressed
 * 
 * @author bodereau
 *
 */
public class BlocErrorCanActivity extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.bloc_error, container, false);
	}
}
