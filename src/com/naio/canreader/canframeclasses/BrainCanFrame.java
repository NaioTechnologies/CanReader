package com.naio.canreader.canframeclasses;

import java.util.List;

import net.sourceforge.juint.UInt8;

import com.naio.canreader.R;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Display the data of the Brain ( yet only the cpu temperature )
 * 
 * @author bodereau
 * 
 */
public class BrainCanFrame extends CanFrame {
	private UInt8 temperature;
	private View rl_second_layout;
	private boolean is_there_data_temperature;

	public BrainCanFrame(int id, int dlc, List<Integer> data, Double time) {
		super(id, dlc, data);
		this.type = "BRAIN";
	}

	public BrainCanFrame() {
		this.type = "BRAIN";
	}

	public BrainCanFrame setParams(int id, int dlc, List<Integer> data) {
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


	public void save_datas() {
		synchronized (lock) {

			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "1110":
				save_data_temperature();
				is_there_data_temperature = true;
				break;
			}
		}
	}



	/**
	 * 
	 */
	private void save_data_temperature() {
		temperature = new UInt8(getData().get(0));
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
					.setText("" + temperature.toString() + " °C");
			return;
		}
		((TextView) rl.findViewById(R.id.temperature_cpu)).setText(""
				+ temperature.toString() + " °C");
	}
}
