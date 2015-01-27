package com.naio.canreader.canframeclasses;

import java.util.List;

import android.widget.RelativeLayout;

public class MotorCanFrame extends CanFrame {

	protected Integer consigne, sensRotation;
	protected Integer courantMax, tension, rampeDemarrage, rampeFreinage;
	protected Integer couple, vitesse, statut;
	protected Integer temperature;
	protected Integer versionMaj, versionMin;
	protected Integer nbImpOdom, odomStatut;
	protected Integer partMSB, partLSB;

	public MotorCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type = "MOTOR";
	}

	public MotorCanFrame() {
		this.type = "MOTOR";
	}

	public MotorCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void action(RelativeLayout rl) {
		switch (idMess) {
		case "0000":
			display_data_consigne(rl);
			break;
		case "0001":
			display_data_courant(rl);
			break;
		case "0010":
			display_data_couple(rl);
			break;
		case "0011":
			display_data_temperature(rl);
			break;
		case "0100":
			display_data_version(rl);
			break;
		case "0101":
			display_data_odom(rl);
			break;
		case "1111":
			display_data_part(rl);
			break;
		default:
			break;
		}
	}

	private void display_data_part(RelativeLayout rl) {
		partMSB = getData().get(0);
		partLSB = getData().get(1);
	}

	private void display_data_odom(RelativeLayout rl) {
		nbImpOdom = getData().get(0);
		odomStatut = getData().get(1);
		

	}

	private void display_data_version(RelativeLayout rl) {
		versionMaj = getData().get(0);
		versionMin = getData().get(1);

	}

	private void display_data_temperature(RelativeLayout rl) {
		temperature = getData().get(0);

	}

	private void display_data_couple(RelativeLayout rl) {
		couple = getData().get(0);
		vitesse = getData().get(1);
		statut = getData().get(2);

	}

	private void display_data_courant(RelativeLayout rl) {
		courantMax = getData().get(0);
		tension = getData().get(1);
		rampeDemarrage = getData().get(2);
		rampeFreinage = getData().get(3);

	}

	private void display_data_consigne(RelativeLayout rl) {
		consigne = getData().get(0);
		sensRotation = getData().get(1);

	}
}
