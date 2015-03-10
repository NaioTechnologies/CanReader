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
	private RelativeLayout rl_second_layout;
	static private int indexF, indexA, indexC;
	static private Double freqMagneto, freqAccel, freqGyro;
	static private Double time, timeAccel, timeGyro, timeMagneto;
	static private int indexDisplay, indexDisplay2;
	private boolean is_there_data_accel, is_there_data_gyro, is_there_data_magneto, is_there_data_temperature, is_there_data_version, is_there_data_board;

	public IMUCanFrame(int id, int dlc, List<Integer> data, Double time) {
		super(id, dlc, data);
		
		init();
	}



	public IMUCanFrame() {
		init();

	}

	/**
	 * Put all the variables to 0
	 * 
	 */
	private void init() {
		this.type = "IMU";
		df = new DecimalFormat("###.##");
		df2 = new DecimalFormat("###");
		
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
		is_there_data_accel = false;
		is_there_data_gyro = false;
		is_there_data_magneto = false;
		is_there_data_temperature = false;
		is_there_data_version = false;
		is_there_data_board = false;
		
	}
	
	public IMUCanFrame setParams(int id, int dlc, List<Integer> data,
			Double time) {
		super.setParams(id, dlc, data);
		IMUCanFrame.time = time;
		return this;
	}

	public void save_datas() {
		synchronized (lock) {
			if (idMess == null) {
				return;
			}
			switch (idMess) {
			case "0000":
				save_data_accel();
				is_there_data_accel = true;
				break;
			case "0001":
				save_data_gyro();
				is_there_data_gyro = true;
				break;
			case "0010":
				save_data_magneto();
				is_there_data_magneto = true;
				break;
			case "0011":
				save_data_temperature();
				is_there_data_temperature = true;
				break;
			case "0100":
				save_data_version();
				is_there_data_version = true;
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
				this.rl_second_layout = (RelativeLayout) vp.getChildAt(0).findViewById(
						R.id.rl_imu_activity);
				if (vp.getCurrentItem() != 0) {
					return;
				}
			}
			if (idMess == null) {
				return;
			}
			if (is_there_data_accel)
				display_data_accel(rl);

			if (is_there_data_gyro)
				display_data_gyro(rl);

			if (is_there_data_magneto)
				display_data_magneto(rl);

			if (is_there_data_temperature)
				display_data_temperature(rl);

			if (is_there_data_version)
				display_data_version(rl);

			if (is_there_data_board)
				display_data_board(rl);

		}

	}

	/**
	 * @param rl
	 */
	private void display_data_board(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.magneto_boardf)).setText(""
					+ board);
			((TextView) rl_second_layout.findViewById(R.id.magneto_revf))
					.setText("" + rev);
			return;
		}
		((TextView) rl.findViewById(R.id.magneto_board)).setText("" + board);
		((TextView) rl.findViewById(R.id.magneto_rev)).setText("" + rev);
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
	private void display_data_version(RelativeLayout rl) {

		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.imu_majf)).setText(""
					+ versionMaj);
			((TextView) rl_second_layout.findViewById(R.id.imu_minf)).setText(""
					+ versionMin);
			return;
		}
		((TextView) rl.findViewById(R.id.imu_maj)).setText("" + versionMaj);
		((TextView) rl.findViewById(R.id.imu_min)).setText("" + versionMin);
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
	private void display_data_temperature(RelativeLayout rl) {

		double tempeFinal = (double) temperature * 0.5 + 23;
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			((TextView) rl_second_layout.findViewById(R.id.imu_tempef)).setText(""
					+ tempeFinal);
			return;
		}
		((TextView) rl.findViewById(R.id.imu_tempe)).setText("" + tempeFinal);
	}

	/**
	 * 
	 */
	private void save_data_temperature() {
		temperature = BytesFunction.fromTwoComplement(getData().get(0), 8);

	}

	private void display_data_magneto(RelativeLayout rl) {

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
			if (rl_second_layout == null) {
				return;
			}

			if (IMUCanFrame.indexF == 99) {
				((TextView) rl_second_layout.findViewById(R.id.magneto_time)).setText(""
						+ df.format(IMUCanFrame.freqMagneto*1000) + " ms");
				IMUCanFrame.indexF = 0;
				IMUCanFrame.freqMagneto = 0.0;
			}
			if (IMUCanFrame.indexDisplay2 == 9) {

				((TextView) rl_second_layout.findViewById(R.id.magneto_xmsbf))
						.setText(txt);
				((TextView) rl_second_layout.findViewById(R.id.magneto_ymsbf))
						.setText(txt2);
				((TextView) rl_second_layout.findViewById(R.id.magneto_zmsbf))
						.setText(txt3);
				((TextView) rl_second_layout.findViewById(R.id.magneto_resmsbf))
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

	/**
	 * 
	 */
	private void save_data_magneto() {
		magnetoXMSB = getData().get(0);
		magnetoXLSB = getData().get(1);
		magnetoYMSB = getData().get(2);
		magnetoYLSB = getData().get(3);
		magnetoZMSB = getData().get(4);
		magnetoZLSB = getData().get(5);
		resMagnMSB = getData().get(6);
		resMagnLSB = getData().get(7);
		if (IMUCanFrame.indexF == 99) {
			IMUCanFrame.timeMagneto = IMUCanFrame.time;
			return;
		}
		IMUCanFrame.freqMagneto = IMUCanFrame.freqMagneto
				+ (IMUCanFrame.time - IMUCanFrame.timeMagneto) * 0.01;
		IMUCanFrame.timeMagneto = IMUCanFrame.time;
		IMUCanFrame.indexF += 1;

	}

	private void display_data_gyro(RelativeLayout rl) {

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
			if (rl_second_layout == null) {
				return;
			}
			if (IMUCanFrame.indexA == 99) {
				((TextView) rl_second_layout.findViewById(R.id.gyro_time)).setText(""
						+ df.format(IMUCanFrame.freqGyro*1000) + " ms");
				IMUCanFrame.indexA = 0;
				IMUCanFrame.freqGyro = 0.0;
			}
			((TextView) rl_second_layout.findViewById(R.id.gyro_xmsbf)).setText(txt);
			((TextView) rl_second_layout.findViewById(R.id.gyro_ymsbf)).setText(txt2);
			((TextView) rl_second_layout.findViewById(R.id.gyro_zmsbf)).setText(txt3);
			return;
		}

		((TextView) rl.findViewById(R.id.gyro_xmsb)).setText(txt);
		((TextView) rl.findViewById(R.id.gyro_ymsb)).setText(txt2);
		((TextView) rl.findViewById(R.id.gyro_zmsb)).setText(txt3);

	}

	/**
	 * 
	 */
	private void save_data_gyro() {
		gyroXMSB = getData().get(0);
		gyroXLSB = getData().get(1);
		gyroYMSB = getData().get(2);
		gyroYLSB = getData().get(3);
		gyroZMSB = getData().get(4);
		gyroZLSB = getData().get(5);
		if (IMUCanFrame.indexA == 99) {
			IMUCanFrame.timeGyro = IMUCanFrame.time;
			return;
		}
		IMUCanFrame.freqGyro = IMUCanFrame.freqGyro
				+ (IMUCanFrame.time - IMUCanFrame.timeGyro) * 0.01;
		IMUCanFrame.timeGyro = IMUCanFrame.time;
		IMUCanFrame.indexA += 1;

	}

	private void display_data_accel(RelativeLayout rl) {

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
			if (rl_second_layout == null) {
				return;
			}
			if (IMUCanFrame.indexC == 99) {
				((TextView) rl_second_layout.findViewById(R.id.accel_time)).setText(""
						+ df.format(IMUCanFrame.freqAccel*1000) + " ms");
				IMUCanFrame.indexC = 0;
				IMUCanFrame.freqAccel = 0.0;
			}
			if (IMUCanFrame.indexDisplay == 9) {

				((TextView) rl_second_layout.findViewById(R.id.accel_xmsbf)).setText(txt);

				((TextView) rl_second_layout.findViewById(R.id.accel_ymsbf)).setText(txt2);

				((TextView) rl_second_layout.findViewById(R.id.accel_zmsbf)).setText(txt3);
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

	/**
	 * 
	 */
	private void save_data_accel() {
		accelXMSB = getData().get(0);
		accelXLSB = getData().get(1);
		accelYMSB = getData().get(2);
		accelYLSB = getData().get(3);
		accelZMSB = getData().get(4);
		accelZLSB = getData().get(5);
		if (IMUCanFrame.indexC == 99) {
			IMUCanFrame.timeAccel = IMUCanFrame.time;
			return;
		}
		IMUCanFrame.freqAccel = IMUCanFrame.freqAccel
				+ (IMUCanFrame.time - IMUCanFrame.timeAccel) * 0.01;
		IMUCanFrame.timeAccel = IMUCanFrame.time;
		IMUCanFrame.indexC += 1;

	}
	
	/**
	 * @param rl
	 */
	private void display_data_adresse4(RelativeLayout rl) {
		//NOT USE
		adresse4 = getData().get(0);
		donnee4 = getData().get(1);
		deviceSEL4 = getData().get(2);

	}

	/**
	 * @param rl
	 */
	private void display_data_adresse3(RelativeLayout rl) {
		//NOT USE
		adresse3 = getData().get(0);
		deviceSEL3 = getData().get(1);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse2(RelativeLayout rl) {
		//NOT USE
		adresse2 = getData().get(0);
		donnee2 = getData().get(1);
		deviceSEL2 = getData().get(2);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse1(RelativeLayout rl) {
		//NOT USE
		adresse1 = getData().get(0);
		donnee1 = getData().get(1);
		deviceSEL1 = getData().get(2);
	}

}
