package com.naio.canreader.canframeclasses;

import java.util.ArrayList;
import java.util.List;

import com.naio.canreader.R;

import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * GPSCanFrame display the data of the GPS: the latitude, longitude, UTC time,
 * and the precision of the gps with the PDOP, HDOP and VDOP and the number of
 * satellites
 * 
 * @author bodereau
 * 
 */
public class GPSCanFrame extends CanFrame {

	List<Integer> gpsData = new ArrayList<Integer>();
	private boolean goForAction;
	private RelativeLayout rlimu;

	public GPSCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "GPS";
		goForAction = false;
	}

	public GPSCanFrame() {
		this.type = "GPS";
		goForAction = false;
	}

	public GPSCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		synchronized (lock) {
			if (goForAction) {
				goForAction = false;
				gpsData = new ArrayList<Integer>();
			}
			if (getData().get(0) == 10) {
				goForAction = true;
				gpsData.add(getData().get(0));
				return this;
			}
			gpsData.add(getData().get(0));
			return this;
		}
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		if (!goForAction)
			return;
		synchronized (lock) {

			if (vp != null) {
				this.rlimu = (RelativeLayout) vp.getChildAt(0).findViewById(
						R.id.rl_imu_activity);
				if (vp.getCurrentItem() != 0) {
					return;
				}
			}
			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "0001":
			case "0010":
			case "0000":
				display_data_gps(rl);
				break;
			default:
				break;
			}
		}
	}

	private void display_data_gps(RelativeLayout rl) {

		String text = "";
		for (int a : gpsData) {
			text += (char) a;
		}

		String[] gps = text.split(",");
		if (gps.length <= 2) {
			return;
		}
		if (gps[0].contains("GPGLL")) {
			if (rl == null) {
				if (rlimu == null) {
					return;
				}
				((TextView) rlimu
						.findViewById(R.id.textview_gps_text_main_activity))
						.setText(convert_data_GPGLL(gps));
				return;
			}
			((TextView) rl.findViewById(R.id.textview_gps_text_main_activity))
					.setText(convert_data_GPGLL(gps));
			return;
		}
		if (gps[0].contains("GPGSA")) {
			if (rl == null) {
				if (rlimu == null) {
					return;
				}
				((TextView) rlimu
						.findViewById(R.id.textview_gps_text2_main_activity))
						.setText(convert_data_GPGSA(gps));
				return;
			}
			((TextView) rl.findViewById(R.id.textview_gps_text2_main_activity))
					.setText(convert_data_GPGSA(gps));
			return;
		}

		if (gps[0].contains("GPVTG")) {
			if (rl == null) {
				if (rlimu == null) {
					return;
				}
				((TextView) rlimu
						.findViewById(R.id.textview_gps_text3_main_activity))
						.setText(convert_data_GPVTG(gps));
				return;
			}
			((TextView) rl.findViewById(R.id.textview_gps_text3_main_activity))
					.setText(convert_data_GPVTG(gps));
			return;
		}

		if (gps[0].contains("GPGSV")) {
			if (rl == null) {
				if (rlimu == null) {
					return;
				}
				((TextView) rlimu
						.findViewById(R.id.textview_gps_text4_main_activity))
						.setText(convert_data_GPGSV(gps));
				return;
			}
			((TextView) rl.findViewById(R.id.textview_gps_text4_main_activity))
					.setText(convert_data_GPGSV(gps));
			return;
		}
	}

	/**
	 * @param gps
	 * @return
	 */
	private CharSequence convert_data_GPGSV(String[] gps) {
		return "nbr satellites :" + gps[3];
	}

	/**
	 * @param gps
	 * @return
	 */
	private CharSequence convert_data_GPVTG(String[] gps) {
		if (gps[7].isEmpty()) {
			return "";
		}
		Double vitesse = Double.parseDouble(gps[7]);
		return "v =" + vitesse + " km/h";
	}

	/**
	 * @param gps
	 * @return
	 */
	private String convert_data_GPGSA(String[] gps) {
		return "HDOP:"
				+ gps[gps.length - 2]
				+ "\nPDOP:"
				+ gps[gps.length - 3]
				+ "\nVDOP:"
				+ gps[gps.length - 1].substring(0,
						gps[gps.length - 1].length() - 5);

	}

	/**
	 * @param gps
	 * @return
	 */
	private String convert_data_GPGLL(String[] gps) {
		long lat_degrees = 0;
		double latf_degrees = 0;
		double lonf_degrees = 0;
		if (gps[1].length() > 5) {
			lat_degrees = Long.parseLong(gps[1].substring(0, 2));
			long lat_minutes = Long.parseLong(gps[1].substring(2, 4));
			long lat_seconds = Long.parseLong(gps[1].substring(5,
					gps[1].length()));

			latf_degrees = (double) lat_degrees + (double) lat_minutes / 60.0
					+ ((double) lat_seconds) / 6000000.0;

			if (gps[2] == "S")
				latf_degrees *= -1;
		}
		long lon_degrees = 0;
		if (gps[3].length() > 5) {
			lon_degrees = Long.parseLong(gps[3].substring(0, 3));
			long lon_minutes = Long.parseLong(gps[3].substring(3, 5));
			long lon_seconds = Long.parseLong(gps[3].substring(6,
					gps[3].length()));
			lonf_degrees = (double) lon_degrees + ((double) lon_minutes) / 60.0
					+ ((double) lon_seconds) / 6000000.0;
			if (gps[4] == "W")
				lonf_degrees *= -1;
		}
		String utctime = gps[5];
		return "Lat:" + latf_degrees + "\nLon:" + lonf_degrees + "\nUTC:"
				+ utctime;
	}
}
