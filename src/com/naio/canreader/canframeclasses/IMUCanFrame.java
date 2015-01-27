package com.naio.canreader.canframeclasses;

import java.text.DecimalFormat;
import java.util.List;

import com.naio.canreader.R;
import com.naio.canreader.utils.BytesFunction;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Display the data of the IMU
 * 
 * @author bodereau
 * 
 */
public class IMUCanFrame extends CanFrame {

	private Integer accelXMSB, accelXLSB, accelYLSB, accelYMSB, accelZLSB,
			accelZMSB;
	private Integer gyroXMSB, gyroXLSB, gyroYMSB, gyroYLSB, gyroZMSB, gyroZLSB;
	private Integer magnetoXMSB, magnetoXLSB, magnetoYMSB, magnetoYLSB,
			magnetoZMSB, magnetoZLSB, resMagnMSB, resMagnLSB;
	private Integer temperature;
	private Integer versionMaj, versionMin;
	private Integer adresse1, donnee1, deviceSEL1;
	private Integer adresse2, donnee2, deviceSEL2;
	private Integer adresse3, deviceSEL3;
	private Integer adresse4, donnee4, deviceSEL4;
	private Integer board, rev;
	private DecimalFormat df, df2;
	private View rlimu;
	static private int indexF, indexA, indexC;
	static private Double freqMagneto, freqAccel, freqGyro;
	static private Double time, timeAccel, timeGyro, timeMagneto;
	static private int indexDisplay, indexDisplay2;

	public IMUCanFrame(int id, int dlc, List<Integer> data, Double time) {
		super(id, dlc, data);
		this.type = "IMU";
		df = new DecimalFormat("###.##");
		df2 = new DecimalFormat("###");
		IMUCanFrame.time = time;
		if (IMUCanFrame.timeMagneto == null) {
			IMUCanFrame.freqMagneto = 0.0;
			IMUCanFrame.freqAccel = 0.0;
			IMUCanFrame.freqGyro = 0.0;
			IMUCanFrame.indexF = 0;
			IMUCanFrame.indexA = 0;
			IMUCanFrame.indexC = 0;
			IMUCanFrame.indexDisplay = 0;
			IMUCanFrame.indexDisplay2 = 0;
			IMUCanFrame.timeMagneto = 0.0;
			IMUCanFrame.timeAccel = 0.0;
			IMUCanFrame.timeGyro = 0.0;
		}
	}

	public IMUCanFrame() {
		this.type = "IMU";
		df = new DecimalFormat("###.##");
		df2 = new DecimalFormat("###");

	}

	public IMUCanFrame setParams(int id, int dlc, List<Integer> data) {
		super.setParams(id, dlc, data);
		return this;
	}

	public void action(RelativeLayout rl, ViewPager vp) {
		if (vp != null) {
			this.rlimu = (RelativeLayout) vp.getChildAt(0).findViewById(
					R.id.rl_imu_activity);
		}
		switch (idMess) {
		case "0000":

			display_data_accel(rl);

			break;
		case "0001":
			display_data_gyro(rl);
			break;
		case "0010":

			display_data_magneto(rl);

			break;
		case "0011":
			display_data_temperature(rl);
			break;
		case "0100":
			display_data_version(rl);
			break;
		case "0101":
			display_data_adresse1(rl);
			break;
		case "0110":
			display_data_adresse2(rl);
			break;
		case "0111":
			display_data_adresse3(rl);
			break;
		case "1000":
			display_data_adresse4(rl);
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
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.magneto_boardf)).setText(""
					+ board);
			((TextView) rlimu.findViewById(R.id.magneto_revf))
					.setText("" + rev);
			return;
		}
		((TextView) rl.findViewById(R.id.magneto_board)).setText("" + board);
		((TextView) rl.findViewById(R.id.magneto_rev)).setText("" + rev);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse4(RelativeLayout rl) {
		adresse4 = getData().get(0);
		donnee4 = getData().get(1);
		deviceSEL4 = getData().get(2);

	}

	/**
	 * @param rl
	 */
	private void display_data_adresse3(RelativeLayout rl) {
		adresse3 = getData().get(0);
		deviceSEL3 = getData().get(1);

	}

	/**
	 * @param rl
	 */
	private void display_data_adresse2(RelativeLayout rl) {
		adresse2 = getData().get(0);
		donnee2 = getData().get(1);
		deviceSEL2 = getData().get(2);

	}

	/**
	 * @param rl
	 */
	private void display_data_adresse1(RelativeLayout rl) {
		adresse1 = getData().get(0);
		donnee1 = getData().get(1);
		deviceSEL1 = getData().get(2);

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
			((TextView) rlimu.findViewById(R.id.imu_majf)).setText(""
					+ versionMaj);
			((TextView) rlimu.findViewById(R.id.imu_minf)).setText(""
					+ versionMin);
			return;
		}
		((TextView) rl.findViewById(R.id.imu_maj)).setText("" + versionMaj);
		((TextView) rl.findViewById(R.id.imu_min)).setText("" + versionMin);
	}

	/**
	 * @param rl
	 */
	private void display_data_temperature(RelativeLayout rl) {
		temperature = BytesFunction.fromTwoComplement(getData().get(0), 8);
		double tempeFinal = (double) temperature * 0.5 + 23;
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			((TextView) rlimu.findViewById(R.id.imu_tempef)).setText(""
					+ tempeFinal);
			return;
		}
		((TextView) rl.findViewById(R.id.imu_tempe)).setText("" + tempeFinal);
	}

	private void display_data_magneto(RelativeLayout rl) {
		magnetoXMSB = getData().get(0);
		magnetoXLSB = getData().get(1);
		magnetoYMSB = getData().get(2);
		magnetoYLSB = getData().get(3);
		magnetoZMSB = getData().get(4);
		magnetoZLSB = getData().get(5);
		resMagnMSB = getData().get(6);
		resMagnLSB = getData().get(7);
		IMUCanFrame.freqMagneto = IMUCanFrame.freqMagneto
				+ (IMUCanFrame.time - IMUCanFrame.timeMagneto) * 0.01;
		IMUCanFrame.timeMagneto = IMUCanFrame.time;
		IMUCanFrame.indexF += 1;
		double val1 = BytesFunction.fromTwoComplement(magnetoXMSB, magnetoXLSB,
				13, 1);
		double val2 = BytesFunction.fromTwoComplement(magnetoYMSB, magnetoYLSB,
				13, 1);
		double val3 = BytesFunction.fromTwoComplement(magnetoZMSB, magnetoZLSB,
				13, 1);
		String txt = "";
		String txt2 = "";
		String txt3 = "";
		if (val1 >= 0)
			txt = " " + val1;
		else
			txt = "" + val1;
		if (val2 >= 0)
			txt2 = " " + val2;
		else
			txt2 = "" + val2;
		if (val3 >= 0)
			txt3 = " " + val3;
		else
			txt3 = "" + val3;
		if (rl == null) {
			if (rlimu == null) {
				return;
			}

			if (IMUCanFrame.indexF == 99) {
				((TextView) rlimu.findViewById(R.id.magneto_time)).setText(""
						+ IMUCanFrame.freqMagneto);
				IMUCanFrame.indexF = 0;
				IMUCanFrame.freqMagneto = 0.0;
			}
			if (IMUCanFrame.indexDisplay2 == 9) {

				((TextView) rlimu.findViewById(R.id.magneto_xmsbf))
						.setText(txt);
				((TextView) rlimu.findViewById(R.id.magneto_ymsbf))
						.setText(txt2);
				((TextView) rlimu.findViewById(R.id.magneto_zmsbf))
						.setText(txt3);
				((TextView) rlimu.findViewById(R.id.magneto_resmsbf))
						.setText("" + (resMagnMSB * 256 + resMagnLSB));
				IMUCanFrame.indexDisplay2 = 0;
			} else {
				IMUCanFrame.indexDisplay2 += 1;
			}
			return;
		}
		if (IMUCanFrame.indexDisplay2 == 9) {

			((TextView) rl.findViewById(R.id.magneto_xmsb)).setText(txt);
			((TextView) rl.findViewById(R.id.magneto_ymsb)).setText(txt2);
			((TextView) rl.findViewById(R.id.magneto_zmsb)).setText(txt3);
			((TextView) rl.findViewById(R.id.magneto_resmsb)).setText(""
					+ (resMagnMSB * 256 + resMagnLSB));
			IMUCanFrame.indexDisplay2 = 0;
		} else {
			IMUCanFrame.indexDisplay2 += 1;
		}

	}

	private void display_data_gyro(RelativeLayout rl) {
		gyroXMSB = getData().get(0);
		gyroXLSB = getData().get(1);
		gyroYMSB = getData().get(2);
		gyroYLSB = getData().get(3);
		gyroZMSB = getData().get(4);
		gyroZLSB = getData().get(5);
		IMUCanFrame.freqGyro = IMUCanFrame.freqGyro
				+ (IMUCanFrame.time - IMUCanFrame.timeGyro) * 0.01;
		IMUCanFrame.timeGyro = IMUCanFrame.time;
		IMUCanFrame.indexA += 1;
		double val1 = BytesFunction.fromTwoComplement(gyroXMSB, gyroXLSB, 16,
				1024 / 32.8);
		double val2 = BytesFunction.fromTwoComplement(gyroYMSB, gyroYLSB, 16,
				1024 / 32.8);
		double val3 = BytesFunction.fromTwoComplement(gyroZMSB, gyroZLSB, 16,
				1024 / 32.8);
		String txt = "";
		String txt2 = "";
		String txt3 = "";
		if (val1 >= 0)
			txt = " " + df2.format(val1);
		else
			txt = "" + df2.format(val1);
		if (val2 >= 0)
			txt2 = " " + df2.format(val2);
		else
			txt2 = "" + df2.format(val2);
		if (val3 >= 0)
			txt3 = " " + df2.format(val3);
		else
			txt3 = "" + df2.format(val3);
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			if (IMUCanFrame.indexA == 99) {
				((TextView) rlimu.findViewById(R.id.gyro_time)).setText(""
						+ IMUCanFrame.freqGyro);
				IMUCanFrame.indexA = 0;
				IMUCanFrame.freqGyro = 0.0;
			}
			((TextView) rlimu.findViewById(R.id.gyro_xmsbf)).setText(txt);
			((TextView) rlimu.findViewById(R.id.gyro_ymsbf)).setText(txt2);
			((TextView) rlimu.findViewById(R.id.gyro_zmsbf)).setText(txt3);
			return;
		}

		((TextView) rl.findViewById(R.id.gyro_xmsb)).setText(txt);
		((TextView) rl.findViewById(R.id.gyro_ymsb)).setText(txt2);
		((TextView) rl.findViewById(R.id.gyro_zmsb)).setText(txt3);

	}

	private void display_data_accel(RelativeLayout rl) {
		accelXMSB = getData().get(0);
		accelXLSB = getData().get(1);
		accelYMSB = getData().get(2);
		accelYLSB = getData().get(3);
		accelZMSB = getData().get(4);
		accelZLSB = getData().get(5);
		IMUCanFrame.freqAccel = IMUCanFrame.freqAccel
				+ (IMUCanFrame.time - IMUCanFrame.timeAccel) * 0.01;
		IMUCanFrame.timeAccel = IMUCanFrame.time;
		IMUCanFrame.indexC += 1;
		double val1 = BytesFunction.fromTwoComplement(accelXMSB, accelXLSB, 12,
				1024);
		double val2 = BytesFunction.fromTwoComplement(accelYMSB, accelYLSB, 12,
				1024);
		double val3 = BytesFunction.fromTwoComplement(accelZMSB, accelZLSB, 12,
				1024);
		String txt = "";
		String txt2 = "";
		String txt3 = "";
		if (val1 >= 0)
			txt = " " + df.format(val1);
		else
			txt = "" + df.format(val1);
		if (val2 >= 0)
			txt2 = " " + df.format(val2);
		else
			txt2 = "" + df.format(val2);
		if (val3 >= 0)
			txt3 = " " + df.format(val3);
		else
			txt3 = "" + df.format(val3);
		if (rl == null) {
			if (rlimu == null) {
				return;
			}
			if (IMUCanFrame.indexC == 99) {
				((TextView) rlimu.findViewById(R.id.accel_time)).setText(""
						+ IMUCanFrame.freqAccel);
				IMUCanFrame.indexC = 0;
				IMUCanFrame.freqAccel = 0.0;
			}
			if (IMUCanFrame.indexDisplay == 9) {

				((TextView) rlimu.findViewById(R.id.accel_xmsbf)).setText(txt);

				((TextView) rlimu.findViewById(R.id.accel_ymsbf)).setText(txt2);

				((TextView) rlimu.findViewById(R.id.accel_zmsbf)).setText(txt3);
				IMUCanFrame.indexDisplay = 0;
			} else {
				IMUCanFrame.indexDisplay += 1;
			}
			return;
		}
		if (IMUCanFrame.indexDisplay == 9) {
			((TextView) rl.findViewById(R.id.accel_xmsb)).setText(txt);
			((TextView) rl.findViewById(R.id.accel_ymsb)).setText(txt2);
			((TextView) rl.findViewById(R.id.accel_zmsb)).setText(txt3);
			IMUCanFrame.indexDisplay = 0;
		} else {
			IMUCanFrame.indexDisplay += 1;
		}

	}

}
