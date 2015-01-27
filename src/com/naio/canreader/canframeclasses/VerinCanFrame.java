/**
 * Created the 16 janv. 2015 at 14:12:16
 * by bodereau
 * 
 */
package com.naio.canreader.canframeclasses;

import java.util.List;

import com.naio.canreader.R;

import android.support.v4.view.ViewPager;
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
	private RelativeLayout rlimu;
	static private Integer cptAvg, cptArg, cptAvd, cptArd;

	public VerinCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "Verin";
		if (VerinCanFrame.cptArd == null) {
			VerinCanFrame.cptArd = 0;
			VerinCanFrame.cptAvg = 0;
			VerinCanFrame.cptArg = 0;
			VerinCanFrame.cptAvd = 0;
		}
	}

	public VerinCanFrame() {
		this.type = "Verin";
	}

	public VerinCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void action(RelativeLayout rl, ViewPager vp) {
		this.rlimu = (RelativeLayout) vp.getChildAt(3).findViewById(
				R.id.rl_verin_activity);
		switch (idMess) {
		case "0000":
			display_data_requete(rl);
			break;
		case "0001":
			display_data_retour(rl);
			break;
		case "0010":
			display_data_commande(rl);
			break;
		case "0011":
			display_data_capteur(rl);
			break;
		case "0100":
			display_data_sortie(rl);
			break;
		case "0101":
			display_data_version(rl);
			break;
		default:
			break;
		}
	}

	/**
	 * @param rl
	 */
	private void display_data_version(RelativeLayout rl) {
		versionMaj = getData().get(0);
		versionMin = getData().get(1);
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.version_verin_maj)).setText(""
					+ versionMaj);
			((TextView) rlimu.findViewById(R.id.version_verin_min)).setText(""
					+ versionMin);
			return;
		}
		((TextView) rl.findViewById(R.id.version_verin_maj)).setText(""
				+ versionMaj);
		((TextView) rl.findViewById(R.id.version_verin_min)).setText(""
				+ versionMin);
	}

	/**
	 * @param rl
	 */
	private void display_data_sortie(RelativeLayout rl) {
		sortieCapteur = getData().get(0);
		activationUSB = getData().get(1);
		String text = Integer.toBinaryString(sortieCapteur);
		String[] data = text.split("");
		String write = "";
		write += "1:" + data[7];
		write += " 2:" + data[6];
		write += " 3:" + data[5];
		write += " 4:" + data[4];
		text = Integer.toBinaryString(activationUSB);
		data = text.split("");
		String write2 = "";
		write2 += "0:" + data[7];
		write2 += "1:" + data[6];
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.sortie_12v)).setText(write);
			((TextView) rlimu.findViewById(R.id.activation_usb))
					.setText(write2);
			return;
		}
		((TextView) rl.findViewById(R.id.sortie_12v)).setText(write);
		((TextView) rl.findViewById(R.id.activation_usb)).setText(write2);
	}

	/**
	 * @param rl
	 */
	private void display_data_capteur(RelativeLayout rl) {
		lectureODO = getData().get(0);
		String text = Integer.toBinaryString(lectureODO);
		String[] data = text.split("");
		String write = "";
		write += "ARD:" + data[7];
		write += " AVD:" + data[6];
		write += " ARG:" + data[5];
		write += " AVG:" + data[4];
		if (data[7] == "1")
			VerinCanFrame.cptArd += 1;
		if (data[6] == "1")
			VerinCanFrame.cptAvd += 1;
		if (data[5] == "1")
			VerinCanFrame.cptArg += 1;
		if (data[4] == "1")
			VerinCanFrame.cptAvg += 1;
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.lecture_odo)).setText(write);
			((TextView) rlimu.findViewById(R.id.lecture_odoc)).setText("ARD:"
					+ VerinCanFrame.cptArd + " AVD:" + VerinCanFrame.cptAvd
					+ " ARG:" + VerinCanFrame.cptArg + " AVG:"
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
	 * @param rl
	 */
	private void display_data_commande(RelativeLayout rl) {
		commandeMoteur = getData().get(0);
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.commande_moteur)).setText(""
					+ commandeMoteur);
			return;
		}
		((TextView) rl.findViewById(R.id.commande_moteur)).setText(""
				+ commandeMoteur);
	}

	/**
	 * @param rl
	 */
	private void display_data_retour(RelativeLayout rl) {
		retourPosition = getData().get(0);
		etatVerin = getData().get(1);
		String text = Integer.toBinaryString(etatVerin);
		String write = "";
		switch (text.subSequence(5, 8).toString()) {
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
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.verin_retour_position))
					.setText("" + retourPosition);
			((TextView) rlimu.findViewById(R.id.etat_verin)).setText(write);
			return;
		}
		((TextView) rl.findViewById(R.id.verin_retour_position)).setText(""
				+ retourPosition);
		((TextView) rl.findViewById(R.id.etat_verin)).setText(write);
	}

	/**
	 * @param rl
	 */
	private void display_data_requete(RelativeLayout rl) {
		requetePosition = getData().get(0);
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.verin_position)).setText(""
					+ requetePosition);
			return;
		}
		((TextView) rl.findViewById(R.id.verin_position)).setText(""
				+ requetePosition);

	}

	/**
	 * 
	 */
	public static void resetCpt() {
		VerinCanFrame.cptArd = 0;
		VerinCanFrame.cptAvg = 0;
		VerinCanFrame.cptArg = 0;
		VerinCanFrame.cptAvd = 0;

	}
}