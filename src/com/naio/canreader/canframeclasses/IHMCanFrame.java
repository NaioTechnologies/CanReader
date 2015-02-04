/**
 * Created the 16 janv. 2015 at 13:50:39
 * by bodereau
 * 
 */
package com.naio.canreader.canframeclasses;

import java.util.List;

import com.naio.canreader.R;
import com.naio.canreader.utils.BytesFunction;

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
	private boolean is_there_data_clavier, is_there_data_led, is_there_data_status, is_there_data_delai, is_there_data_version, is_there_data_contraste, is_there_data_backlight, is_there_data_board;

	public IHMCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "IHM";

	}

	public IHMCanFrame() {
		this.type = "IHM";
		is_there_data_clavier = false;
		is_there_data_led = false;
		is_there_data_status = false;
		is_there_data_delai = false;
		is_there_data_version = false;
		is_there_data_contraste = false;
		is_there_data_backlight = false;
		is_there_data_board = false;

	}

	public IHMCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void save_datas() {
		synchronized (lock) {

			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "0001":
				save_data_clavier();
				is_there_data_clavier = true;
				break;
			case "0010":
				save_data_led();
				is_there_data_led = true;
				break;
			case "0100":
				save_data_status();
				is_there_data_status = true;
				break;
			case "0101":
				save_data_delai();
				is_there_data_delai = true;
				break;
			case "0110":
				save_data_version();
				is_there_data_version = true;
				break;
			case "0111":
				save_data_contraste();
				is_there_data_contraste = true;
				break;
			case "1000":
				save_data_backlight();
				is_there_data_backlight = true;
				break;
			case "1111":
				save_data_board();
				is_there_data_board = true;
				break;
			default:
				break;
			}
		}
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		synchronized (lock) {

			if (vp != null) {
				this.rlimu = (RelativeLayout) vp.getChildAt(2).findViewById(
						R.id.rl_ihm_activity);
				if (vp.getCurrentItem() != 2) {
					return;
				}
			}
			if (is_there_data_clavier)
				display_data_etat_clavier(rl);
			if (is_there_data_led)
				display_data_etat_led(rl);
			if (is_there_data_status)
				display_data_status(rl);
			if (is_there_data_delai)
				display_data_delai(rl);
			if (is_there_data_version)
				display_data_version(rl);
			if (is_there_data_contraste)
				display_data_contraste(rl);
			if (is_there_data_backlight)
				display_data_backlight(rl);
			if (is_there_data_board)
				display_data_board(rl);

		}
	}

	/**
	 * @param rl
	 */
	private void display_data_board(RelativeLayout rl) {

		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_board)).setText("" + board);
			((TextView) rlimu.findViewById(R.id.ihm_rev)).setText("" + rev);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_board)).setText("" + board);
		((TextView) rl.findViewById(R.id.ihm_rev)).setText("" + rev);
	}

	/**
	 * 
	 */
	private void save_data_board() {
		board = getData().get(0);
		rev = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_backlight(RelativeLayout rl) {

		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_backlight)).setText(""
					+ backlight);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_backlight))
				.setText("" + backlight);
	}

	/**
	 * 
	 */
	private void save_data_backlight() {
		backlight = getData().get(0);

	}

	/**
	 * @param rl
	 */
	private void display_data_contraste(RelativeLayout rl) {

		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_contraste)).setText(""
					+ contraste);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_contraste))
				.setText("" + contraste);
	}

	/**
	 * 
	 */
	private void save_data_contraste() {
		contraste = getData().get(0);

	}

	/**
	 * @param rl
	 */
	private void display_data_version(RelativeLayout rl) {

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
	 * 
	 */
	private void save_data_version() {
		versionMaj = getData().get(0);
		versionMin = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_delai(RelativeLayout rl) {

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
	 * 
	 */
	private void save_data_delai() {
		delaiLong = getData().get(0);
		delaiRepet = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_status(RelativeLayout rl) {

		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.ihm_status)).setText(""
					+ status);
			return;
		}
		((TextView) rl.findViewById(R.id.ihm_status)).setText("" + status);
	}

	/**
	 * 
	 */
	private void save_data_status() {
		status = getData().get(0);

	}

	/**
	 * @param rl
	 */
	private void display_data_code_2(RelativeLayout rl) {
		//NOT USE
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

		String text = Integer.toBinaryString(etatLed);
		String text2 = Integer.toBinaryString(couleurLed);
		text = BytesFunction.fillWithZeroTheBinaryString(text);
		text2 = BytesFunction.fillWithZeroTheBinaryString(text2);
		String[] data = text.split("(?<!^)");
		String[] data2 = text2.split("(?<!^)");
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
		write += "Gauche:" + data[7] + "," + c1;
		write += "  ;Led 2:" + data[6] + "," + c2;
		write += "\nLed 3:" + data[5] + "," + c3;
		write += " ;Droite:" + data[4] + "," + c4;
		if (rl == null) {
			((TextView) rlimu.findViewById(R.id.etat_led)).setText(write);
			return;
		}
		((TextView) rl.findViewById(R.id.etat_led)).setText(write);
	}

	/**
	 * 
	 */
	private void save_data_led() {
		etatLed = getData().get(0);
		couleurLed = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_etat_clavier(RelativeLayout rl) {

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
	 * 
	 */
	private void save_data_clavier() {
		etatClavier = getData().get(0);

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
