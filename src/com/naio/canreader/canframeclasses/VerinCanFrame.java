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
	private RelativeLayout rl_second_layout;
	static private Integer cptAvg, cptArg, cptAvd, cptArd;
	private boolean is_there_data_requete, is_there_data_retour,
			is_there_data_commande, is_there_data_capteur,
			is_there_data_sortie, is_there_data_version;
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
			default:
				break;
			}
		}
	}

	public void display_on(RelativeLayout rl, ViewPager vp) {
		synchronized (lock) {
			if (vp != null) {
				this.rl_second_layout = (RelativeLayout) vp.getChildAt(3)
						.findViewById(R.id.rl_verin_activity);
				if (vp.getCurrentItem() != 3) {
					return;
				}
			}
			if (idMess == null) {
				return;
			}
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
		VerinCanFrame.state_avg=true;
		VerinCanFrame.state_arg = true;
		VerinCanFrame.state_avd = true;

	}
}