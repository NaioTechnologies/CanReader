package com.naio.canreader.canframeclasses;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.naio.canreader.R;

/*
 * handle the error.
 * lib.c for more detail ( in cansoft )
 * */
public class ErrorCanFrame extends CanFrame {
	private String error;
	private View rl_second_layout;
	private boolean is_there_data_temperature;
	private String complementError;
	private int errorClassNumber;
	private int controllerNumber;
	private int protocolLocationNumber;
	private int protocolTypesNumber;
	private ArrayList<String> historyError = new ArrayList<String>();

	private static String error_classes[] = { "tx-timeout", "lost-arbitration",
			"controller-problem", "protocol-violation", "transceiver-status",
			"no-acknowledgement-on-tx", "bus-off", "bus-error",
			"restarted-after-bus-off", };

	private static String controller_problems[] = { "rx-overflow",
			"tx-overflow", "rx-error-warning", "tx-error-warning",
			"rx-error-passive", "tx-error-passive", };

	private static String protocol_violation_types[] = { "single-bit-error",
			"frame-format-error", "bit-stuffing-error",
			"tx-dominant-bit-error", "tx-recessive-bit-error", "bus-overload",
			"back-to-error-active", "error-on-tx", };

	private static String protocol_violation_locations[] = { "id.28-to-id.28",
			"start-of-frame", "bit-srtr", "bit-ide", "id.20-to-id.18",
			"id.17-to-id.13", "crc-sequence", "reserved-bit-0", "data-field",
			"data-length-code", "bit-rtr", "reserved-bit-1", "id.4-to-id.0",
			"id.12-to-id.5", "active-error-flag", "intermission",
			"tolerate-dominant-bits", "passive-error-flag", "error-delimiter",
			"crc-delimiter", "acknowledge-slot", "end-of-frame",
			"acknowledge-delimiter", "overload-flag", "unspecified" };

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

	public void nomPasChoisi() {
		String errorToAdd = "";
		
		synchronized (lock) {
			for (int i = 0; i < error_classes.length; i++) {
				if (complementError.contains(error_classes[i])) {
					errorClassNumber = i;
					break;
				}
			}
			
			if (errorClassNumber == 2) {
				for (int i = 0; i < controller_problems.length; i++) {
					if (complementError.contains(controller_problems[i])) {
						controllerNumber = i;
						break;
					}
				}
			}
			if (errorClassNumber == 3) {
				for (int i = 0; i < protocol_violation_locations.length; i++) {
					if (complementError
							.contains(protocol_violation_locations[i])) {
						protocolLocationNumber = i;
						break;
					}
				}
				for (int i = 0; i < protocol_violation_types.length; i++) {
					if (complementError.contains(protocol_violation_types[i])) {
						protocolTypesNumber = i;
						break;
					}
				}
			}
		}
	}

	public void setError(String frame) {
		synchronized (lock) {
			error = frame;
		}
	}

	public String getError() {
		synchronized (lock) {
			return error;
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
