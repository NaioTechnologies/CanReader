package com.naio.canreader.canframeclasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.naio.canreader.R;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Display the data of the GSM
 * 
 * @author bodereau
 */
public class GSMCanFrame extends CanFrame {

	private List<Integer> gsmData = new ArrayList<Integer>();
	private RelativeLayout rl_second_layout;

	public GSMCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "GSM";
		gsmData = new ArrayList<Integer>();

	}

	public GSMCanFrame() {
		this.type = "GSM";
		gsmData = new ArrayList<Integer>();
	}

	public GSMCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		// 641 is the id where messages are sent. We only need to read the 640
		if (id == 641)
			return this;
		synchronized (lock) {
			// we all the data that we read because the response is sent char by
			// char
			gsmData.add(getData().get(0));
			return this;
		}
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		synchronized (lock) {
			if (vp != null) {
				this.rl_second_layout = (RelativeLayout) vp.getChildAt(1)
						.findViewById(R.id.rl_gsm_activity);
				if (vp.getCurrentItem() != 1) {
					return;
				}
			}
			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "0000":
				display_data_from_gsm(rl);
				break;
			default:
				break;
			}
		}
	}

	public boolean isGsmWorking() {
		synchronized (lock) {

			String text = "";
			for (int i : gsmData) {
				text += (char) i;
			}

			// avoid the textview to be filled at maximum
			if (text.contains("AT+")) {
				gsmData.clear();
				return true;
			}
			return false;
		}
	}

	private void display_data_from_gsm(RelativeLayout rl) {
		String text = "";
		int cpt = 0;
		int idx = -1;
		for (int i : gsmData) {
			text += (char) i;
		}
		for (int i = 0; i < gsmData.size(); i++) {
			if (gsmData.get(i) == 65) {
				cpt = 1;
			} else if (gsmData.get(i) == 84 && cpt == 1) {
				cpt = 2;
			} else if (gsmData.get(i) == 43 && cpt == 2) {
				cpt = 3;
				idx = i;
			} else {
				cpt = 0;
				// idx =-1;
			}
		}

		// avoid the textview to be filled at maximum
		/*
		 * if(text.contains("AT+")){ gsmData.clear(); }
		 */
		if (idx >= 0) {
			Log.e("gsmData", idx + "---- " + gsmData.size());
			gsmData = gsmData.subList(idx, gsmData.size() - 1);
		}

		if (rl == null) {
			((TextView) rl_second_layout.findViewById(R.id.read_sms))
					.setText(text);
			return;
		}
		((TextView) rl.findViewById(R.id.read_sms)).setText(text);
	}

}
