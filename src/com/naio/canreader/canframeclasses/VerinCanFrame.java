/**
 * Created the 16 janv. 2015 at 14:12:16
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
 * Display the data of the verin and of the ODO
 * 
 * @author bodereau
 * 
 */
public class VerinCanFrame extends CanFrame {

	private Integer requetePosition;
	private Integer retourPosition, etatVerin;
	private Integer commandeMoteur;
	private Integer lectureODO;
	private Integer sortieCapteur, activationUSB;
	private Integer versionMaj, versionMin;
	private Integer t12vLSB, t12vMSB, t33vLSB, t33vMSB, t5vLSB, t5vMSB,
			flagSortie;
	private Integer t24vLSB, t24vMSB, pileLSB, pileMSB;
	private RelativeLayout rl_second_layout;
	static private Integer cptAvg, cptArg, cptAvd, cptArd;
	private boolean is_there_data_requete, is_there_data_retour,
			is_there_data_commande, is_there_data_capteur,
			is_there_data_sortie, is_there_data_version,
			is_there_data_tension_principale, is_there_data_tension_12v;
	private static boolean state_ard;
	private static boolean state_avd;
	private static boolean state_arg;
	private static boolean state_avg;

	public VerinCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		init();
	}

	public VerinCanFrame() {
		init();
	}

	private void init() {
		this.type = "Verin";

		VerinCanFrame.resetCpt();
		is_there_data_requete = false;
		is_there_data_retour = false;
		is_there_data_commande = false;
		is_there_data_capteur = false;
		is_there_data_sortie = false;
		is_there_data_version = false;
		is_there_data_tension_12v = false;
		is_there_data_tension_principale = false;

	}

	public VerinCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void save_datas() {
		synchronized (lock) {

			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "0000":
				save_data_requete();
				is_there_data_requete = true;
				break;
			case "0001":
				save_data_retour();
				is_there_data_retour = true;
				break;
			case "0010":
				save_data_commande();
				is_there_data_commande = true;
				break;
			case "0011":
				save_data_capteur();
				is_there_data_capteur = true;
				break;
			case "0100":
				save_data_sortie();
				is_there_data_sortie = true;
				break;
			case "0101":
				save_data_version();
				is_there_data_version = true;
				break;
			case "0110":
				save_data_tension_12v();
				is_there_data_tension_12v = true;
				break;
			case "0111":
				save_data_tension_principale();
				is_there_data_tension_principale = true;
				break;
			default:
				break;
			}
		}
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		synchronized (lock) {
			if (vp != null) {
				if (vp.getCurrentItem() == 3) {
					this.rl_second_layout = (RelativeLayout) vp.getChildAt(3)
							.findViewById(R.id.rl_verin_activity);
				} else {
					this.rl_second_layout = (RelativeLayout) vp.getChildAt(4)
							.findViewById(R.id.rl_tension_activity);
				}
				/*
				 * if (vp.getCurrentItem() != 3) { return; }
				 */
			}
			if (idMess == null) {
				return;
			}
			if (vp.getCurrentItem() == 3) {
				if (is_there_data_requete)
					display_data_requete(rl);
				if (is_there_data_retour)
					display_data_retour(rl);
				if (is_there_data_commande)
					display_data_commande(rl);
				if (is_there_data_capteur)
					display_data_capteur(rl);
				if (is_there_data_sortie)
					display_data_sortie(rl);
				if (is_there_data_version)
					display_data_version(rl);
			}
			if (vp.getCurrentItem() == 4) {
				if (is_there_data_tension_12v)
					display_data_tension_12v(rl);
				if (is_there_data_tension_principale)
					display_data_tension_principale(rl);
			}

		}
	}

	private void save_data_tension_12v() {
		t12vLSB = getData().get(0);
		t12vMSB = getData().get(1);
		t33vLSB = getData().get(2);
		t33vMSB = getData().get(3);
		t5vLSB = getData().get(4);
		t5vMSB = getData().get(5);
		flagSortie = getData().get(6);
	}

	private void display_data_tension_principale(RelativeLayout rl) {
		double val1 = BytesFunction.fromTwoComplement(t24vMSB, t24vLSB, 16, 1);
		double val2 = BytesFunction.fromTwoComplement(pileMSB, pileLSB, 16, 1);

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}

			((TextView) rl_second_layout.findViewById(R.id.tension_24v))
					.setText("" + val1);
			((TextView) rl_second_layout.findViewById(R.id.tension_pile))
					.setText("" + val2);
			return;
		}

		((TextView) rl.findViewById(R.id.tension_24v)).setText("" + val1);
		((TextView) rl.findViewById(R.id.tension_pile)).setText("" + val2);

	}

	private void save_data_tension_principale() {
		t24vLSB = getData().get(0);
		t24vMSB = getData().get(1);
		pileLSB = getData().get(2);
		pileMSB = getData().get(3);
	}

	private void display_data_tension_12v(RelativeLayout rl) {
		double val1 = BytesFunction.fromTwoComplement(t12vMSB, t12vLSB, 16, 1);
		double val2 = BytesFunction.fromTwoComplement(t33vMSB, t33vLSB, 16, 1);
		double val3 = BytesFunction.fromTwoComplement(t5vMSB, t5vLSB, 16, 1);
		String txtFlag = "";
		switch (flagSortie) {
		case 0:
			txtFlag = "Lidar : X \n CAN : X";
			break;
		case 3:
			txtFlag = "Lidar : OK \n CAN : OK";
			break;
		case 1:
			txtFlag = "Lidar : OK \n CAN : X";
			break;
		case 2:
			txtFlag = "Lidar : X \n CAN : OK";
			break;

		default:
			break;
		}

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}

			((TextView) rl_second_layout.findViewById(R.id.tension_12v))
					.setText("" + val1);
			((TextView) rl_second_layout.findViewById(R.id.tension_33v))
					.setText("" + val2);
			((TextView) rl_second_layout.findViewById(R.id.tension_5v))
					.setText("" + val3);
			((TextView) rl_second_layout.findViewById(R.id.flag_sortie))
					.setText(txtFlag);
			return;
		}

		((TextView) rl.findViewById(R.id.tension_24v)).setText("" + val1);
		((TextView) rl.findViewById(R.id.tension_pile)).setText("" + val2);
		((TextView) rl.findViewById(R.id.tension_5v)).setText("" + val3);
		((TextView) rl.findViewById(R.id.flag_sortie)).setText(txtFlag);

	}

	/**
	 * @param rl
	 */
	private void display_data_version(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.version_verin_maj))
					.setText("" + versionMaj);
			((TextView) rl_second_layout.findViewById(R.id.version_verin_min))
					.setText("" + versionMin);
			return;
		}
		((TextView) rl.findViewById(R.id.version_verin_maj)).setText(""
				+ versionMaj);
		((TextView) rl.findViewById(R.id.version_verin_min)).setText(""
				+ versionMin);
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
	private void display_data_sortie(RelativeLayout rl) {

		String text = Integer.toBinaryString(sortieCapteur);
		String[] data = BytesFunction.fillWithZeroTheBinaryString(text).split(
				"(?<!^)");
		String write = "";
		write += "1:" + data[7];
		write += " 2:" + data[6];
		write += " 3:" + data[5];
		write += " 4:" + data[4];
		text = Integer.toBinaryString(activationUSB);
		data = BytesFunction.fillWithZeroTheBinaryString(text).split("(?<!^)");
		String write2 = "";
		write2 += "0:" + data[7];
		write2 += "1:" + data[6];
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.sortie_12v))
					.setText(write);
			((TextView) rl_second_layout.findViewById(R.id.activation_usb))
					.setText(write2);
			return;
		}
		((TextView) rl.findViewById(R.id.sortie_12v)).setText(write);
		((TextView) rl.findViewById(R.id.activation_usb)).setText(write2);
	}

	/**
	 * 
	 */
	private void save_data_sortie() {
		sortieCapteur = getData().get(0);
		activationUSB = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_capteur(RelativeLayout rl) {

		String text = Integer.toBinaryString(lectureODO);
		String[] data = BytesFunction.fillWithZeroTheBinaryString(text).split(
				"(?<!^)");
		String write = "";
		write += "ARD:" + data[7];
		write += " AVD:" + data[6];
		write += " ARG:" + data[5];
		write += " AVG:" + data[4];
		if (data[7].contains("1") && VerinCanFrame.state_ard) {
			VerinCanFrame.cptArd += 1;
			VerinCanFrame.state_ard = false;
		}
		if (data[6].contains("1") && VerinCanFrame.state_avd) {
			VerinCanFrame.cptAvd += 1;
			VerinCanFrame.state_avd = false;
		}
		if (data[5].contains("1") && VerinCanFrame.state_arg) {
			VerinCanFrame.cptArg += 1;
			VerinCanFrame.state_arg = false;
		}
		if (data[4].contains("1") && VerinCanFrame.state_avg) {
			VerinCanFrame.cptAvg += 1;
			VerinCanFrame.state_avg = false;
		}
		if (data[7].contains("0") && !VerinCanFrame.state_ard) {
			VerinCanFrame.state_ard = true;
		}
		if (data[6].contains("0") && !VerinCanFrame.state_avd) {
			VerinCanFrame.state_avd = true;
		}
		if (data[5].contains("0") && !VerinCanFrame.state_arg) {
			VerinCanFrame.state_arg = true;
		}
		if (data[4].contains("0") && !VerinCanFrame.state_avg) {
			VerinCanFrame.state_avg = true;
		}
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.lecture_odo))
					.setText(write);
			((TextView) rl_second_layout.findViewById(R.id.lecture_odoc))
					.setText("ARD:" + VerinCanFrame.cptArd + " AVD:"
							+ VerinCanFrame.cptAvd + " ARG:"
							+ VerinCanFrame.cptArg + " AVG:"
							+ VerinCanFrame.cptAvg);
			return;
		}
		((TextView) rl.findViewById(R.id.lecture_odo)).setText(write);
		((TextView) rl.findViewById(R.id.lecture_odoc)).setText("ARD:"
				+ VerinCanFrame.cptArd + " AVD:" + VerinCanFrame.cptAvd
				+ " ARG:" + VerinCanFrame.cptArg + " AVG:"
				+ VerinCanFrame.cptAvg);
	}

	/**
	 * 
	 */
	private void save_data_capteur() {
		lectureODO = getData().get(0);

	}

	/**
	 * @param rl
	 */
	private void display_data_commande(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.commande_moteur))
					.setText("" + commandeMoteur);
			return;
		}
		((TextView) rl.findViewById(R.id.commande_moteur)).setText(""
				+ commandeMoteur);
	}

	/**
	 * 
	 */
	private void save_data_commande() {
		commandeMoteur = getData().get(0);

	}

	/**
	 * @param rl
	 */
	private void display_data_retour(RelativeLayout rl) {

		String text = Integer.toBinaryString(etatVerin);
		String write = "";
		Log.e("etatVerin", BytesFunction.fillWithZeroTheBinaryString(text)
				.subSequence(5, 8).toString());
		switch (BytesFunction.fillWithZeroTheBinaryString(text)
				.subSequence(6, 8).toString()) {
		case "11":
			write += "dep. en cours, erreur driver";
			break;
		case "10":
			write += "erreur driver";
			break;
		case "01":
			write += "dep. en cours";
			break;
		case "00":
			write += "no data";
			break;

		}
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout
					.findViewById(R.id.verin_retour_position)).setText(""
					+ retourPosition);
			((TextView) rl_second_layout.findViewById(R.id.etat_verin))
					.setText(write);
			return;
		}
		((TextView) rl.findViewById(R.id.verin_retour_position)).setText(""
				+ retourPosition);
		((TextView) rl.findViewById(R.id.etat_verin)).setText(write);
	}

	/**
	 * 
	 */
	private void save_data_retour() {
		retourPosition = getData().get(0);
		etatVerin = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_requete(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.verin_position))
					.setText("" + requetePosition);
			return;
		}
		((TextView) rl.findViewById(R.id.verin_position)).setText(""
				+ requetePosition);

	}

	/**
	 * 
	 */
	private void save_data_requete() {
		requetePosition = getData().get(0);
	}

	/**
	 * 
	 */
	public static void resetCpt() {
		VerinCanFrame.cptArd = 0;
		VerinCanFrame.cptAvg = 0;
		VerinCanFrame.cptArg = 0;
		VerinCanFrame.cptAvd = 0;
		VerinCanFrame.cptArd = 0;
		VerinCanFrame.cptAvg = 0;
		VerinCanFrame.cptArg = 0;
		VerinCanFrame.cptAvd = 0;
		VerinCanFrame.state_ard = true;
		VerinCanFrame.state_avg = true;
		VerinCanFrame.state_arg = true;
		VerinCanFrame.state_avd = true;

	}
}