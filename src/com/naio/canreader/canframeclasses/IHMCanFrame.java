/**
 * Created the 16 janv. 2015 at 13:50:39
 * by bodereau
 * 
 */
package com.naio.canreader.canframeclasses;

import java.util.List;

import com.naio.canreader.R;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Display the data of the IHM
 * 
 * @author bodereau
 * 
 */
public class IHMCanFrame extends CanFrame {

	private Integer codeCommande1, cc1_arg1, cc1_arg2, cc1_arg3;
	private Integer etatClavier;
	private Integer etatLed, couleurLed;
	private Integer codeCommande2, cc2_arg1, cc2_arg2, cc2_arg3, cc2_arg4;
	private Integer status;
	private Integer delaiLong, delaiRepet;
	private Integer versionMaj, versionMin;
	private Integer contraste;
	private Integer backlight;
	private Integer board, rev;
	private RelativeLayout rlimu;

	public IHMCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "IHM";

	}

	public IHMCanFrame() {
		this.type = "IHM";

	}

	public IHMCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void action(RelativeLayout rl, ViewPager vp) {
		if (vp != null) {
			this.rlimu = (RelativeLayout) vp.getChildAt(2).findViewById(
					R.id.rl_ihm_activity);
		}
		switch (idMess) {
		case "0000":
			display_data_code_1(rl);
			break;
		case "0001":
			display_data_etat_clavier(rl);
			break;
		case "0010":
			display_data_etat_led(rl);
			break;
		case "0011":
			display_data_code_2(rl);
			break;
		case "0100":
			display_data_status(rl);
			break;
		case "0101":
			display_data_delai(rl);
			break;
		case "0110":
			display_data_version(rl);
			break;
		case "0111":
			display_data_contraste(rl);
			break;
		case "1000":
			display_data_backlight(rl);
			break;
		case "1111":
			display_data_board(rl);
			break;
		default:
			break;
		}
	}

	/**
	 * @param rl
	 */
	private void display_data_board(RelativeLayout rl) {
		board = getData().get(0);
		rev = getData().get(1);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_board)).setText("" + board);
			((TextView) rlimu.findViewById(R.id.ihm_rev)).setText("" + rev);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_board)).setText("" + board);
		((TextView) rl.findViewById(R.id.ihm_rev)).setText("" + rev);
	}

	/**
	 * @param rl
	 */
	private void display_data_backlight(RelativeLayout rl) {
		backlight = getData().get(0);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_backlight)).setText(""
					+ backlight);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_backlight))
				.setText("" + backlight);
	}

	/**
	 * @param rl
	 */
	private void display_data_contraste(RelativeLayout rl) {
		contraste = getData().get(0);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_contraste)).setText(""
					+ contraste);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_contraste))
				.setText("" + contraste);
	}

	/**
	 * @param rl
	 */
	private void display_data_version(RelativeLayout rl) {
		versionMaj = getData().get(0);
		versionMin = getData().get(1);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_maj)).setText(""
					+ versionMaj);
			((TextView) rlimu.findViewById(R.id.ihm_min)).setText(""
					+ versionMin);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_maj)).setText("" + versionMaj);
		((TextView) rl.findViewById(R.id.ihm_min)).setText("" + versionMin);

	}

	/**
	 * @param rl
	 */
	private void display_data_delai(RelativeLayout rl) {
		delaiLong = getData().get(0);
		delaiRepet = getData().get(1);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_delai_long)).setText(""
					+ delaiLong);
			((TextView) rlimu.findViewById(R.id.ihm_delai_repet)).setText(""
					+ delaiRepet);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_delai_long)).setText(""
				+ delaiLong);
		((TextView) rl.findViewById(R.id.ihm_delai_repet)).setText(""
				+ delaiRepet);
	}

	/**
	 * @param rl
	 */
	private void display_data_status(RelativeLayout rl) {
		status = getData().get(0);
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_status)).setText(""
					+ status);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_status)).setText("" + status);
	}

	/**
	 * @param rl
	 */
	private void display_data_code_2(RelativeLayout rl) {
		switch (dlc) {
		case 5:
			cc2_arg4 = getData().get(4);
		case 4:
			cc2_arg3 = getData().get(3);
		case 3:
			cc2_arg2 = getData().get(2);
		case 2:
			cc2_arg1 = getData().get(1);
		case 1:
			codeCommande2 = getData().get(0);
		default:
			break;
		}

	}

	/**
	 * @param rl
	 */
	private void display_data_etat_led(RelativeLayout rl) {
		etatLed = getData().get(0);
		couleurLed = getData().get(1);
		String text = Integer.toBinaryString(etatLed);
		String text2 = Integer.toBinaryString(couleurLed);
		Log.e("bin", "before :" + text + "____" + text2);
		switch (text.length()) {
		case 0:
			text = '0' + text;
		case 1:
			text = '0' + text;
		case 2:
			text = '0' + text;
		case 3:
			text = '0' + text;
		case 4:
			text = '0' + text;
		case 5:
			text = '0' + text;
		case 6:
			text = '0' + text;
		case 7:
			text = '0' + text;
		default:
			break;
		}
		switch (text2.length()) {
		case 0:
			text2 = '0' + text2;
		case 1:
			text2 = '0' + text2;
		case 2:
			text2 = '0' + text2;
		case 3:
			text2 = '0' + text2;
		case 4:
			text2 = '0' + text2;
		case 5:
			text2 = '0' + text2;
		case 6:
			text2 = '0' + text2;
		case 7:
			text2 = '0' + text2;
		default:
			break;
		}
		Log.e("bin", "after :" + text + "____" + text2);
		String[] data = text.split("(?<!^)");
		String[] data2 = text2.split("(?<!^)");
		Log.e("bin", "after the split :" + data[0] + "^" + data[1] + "^"
				+ data[2] + "^" + data[3] + "^" + data[4] + "^" + data[5] + "^"
				+ data[6] + "^" + data[7] + "____" + data2[0] + "^" + data2[1]
				+ "^" + data2[2] + "^" + data2[3] + "^" + data2[4] + "^"
				+ data2[5] + "^" + data2[6] + "^" + data2[7]);
		String c1 = "";
		String c2 = "";
		String c3 = "";
		String c4 = "";
		if (data2[7].contains("0"))
			c1 = "Rouge";
		else
			c1 = "Verte";
		if (data2[6].contains("0"))
			c2 = "Rouge";
		else
			c2 = "Verte";
		if (data2[5].contains("0"))
			c3 = "Rouge";
		else
			c3 = "Verte";
		if (data2[4].contains("1"))
			c4 = "Verte";
		else
			c4 = "Rouge";
		String write = "";
		write += "Led gauche:" + data[7] + "," + c1;
		write += "     Led 2:" + data[6] + "," + c2;
		write += " \nLed 3:" + data[5] + "," + c3;
		write += "       Led droite:" + data[4] + "," + c4;
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.etat_led)).setText(write);
			return;
		}
		((TextView) rl.findViewById(R.id.etat_led)).setText(write);
	}

	/**
	 * @param rl
	 */
	private void display_data_etat_clavier(RelativeLayout rl) {
		etatClavier = getData().get(0);
		String text = Integer.toBinaryString(etatClavier);
		Log.e("bin", "before :" + text);
		switch (text.length()) {
		case 0:
			text = '0' + text;
		case 1:
			text = '0' + text;
		case 2:
			text = '0' + text;
		case 3:
			text = '0' + text;
		case 4:
			text = '0' + text;
		case 5:
			text = '0' + text;
		case 6:
			text = '0' + text;
		case 7:
			text = '0' + text;

		default:
			break;
		}
		Log.e("bin", "after :" + text);
		String[] data = text.split("(?<!^)");
		String write = "";
		Log.e("bin", "after the split :" + data[0] + "^" + data[1] + "^"
				+ data[2] + "^" + data[3] + "^" + data[4] + "^" + data[5] + "^"
				+ data[6] + "^" + data[7]);

		write += "valide:" + data[6];
		write += " annuler:" + data[7];
		write += " droite:" + data[3];
		write += " gauche:" + data[2];
		write += " haut:" + data[5];
		write += " bas:" + data[4];
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.etat_clavier)).setText(write);
			return;
		}
		((TextView) rl.findViewById(R.id.etat_clavier)).setText(write);
	}

	/**
	 * @param rl
	 */
	private void display_data_code_1(RelativeLayout rl) {
		switch (dlc) {
		case 4:
			cc1_arg3 = getData().get(3);
		case 3:
			cc1_arg2 = getData().get(2);
		case 2:
			cc1_arg1 = getData().get(1);
		case 1:
			codeCommande1 = getData().get(0);
		default:
			break;
		}

	}
}
