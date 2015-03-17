package com.naio.canreader.canframeclasses;

import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naio.canreader.R;

public class ErrorCanFrame extends CanFrame {
	private String error;
	private View rl_second_layout;
	private boolean is_there_data_temperature;
	private String complementError;

	public ErrorCanFrame(int id, int dlc, List<Integer> data, Double time) {
		super(id, dlc, data);
		this.type = "ERROR";
		error = "no error";
		complementError = " ";
	}

	public ErrorCanFrame() {
		this.type = "ERROR";
		error = "no error";
		complementError = " ";
	}

	public ErrorCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		synchronized (lock) {
			if (vp != null) {
				this.rl_second_layout = (RelativeLayout) vp.getChildAt(4)
						.findViewById(R.id.rl_tension_activity);
			}
			if (is_there_data_temperature)
				display_data_temperature(rl);
		}
	}

	/**
	 * @param rl
	 */
	private void display_data_temperature(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.temperature_cpu))
					.setText("" + " °C");
			return;
		}
		((TextView) rl.findViewById(R.id.temperature_cpu)).setText("" + " °C");
	}

	public void setError(String frame) {
		synchronized (lock) {
			error = frame;
		}
	}
	
	public String getError(){
		synchronized (lock) {
			return error ;
		}
	}

	public void setComplementError(String frame) {
		synchronized (lock) {
			complementError = frame;
		}
	}

	public String getComplementError() {
		synchronized (lock) {
			return complementError;
		}
	}
}
