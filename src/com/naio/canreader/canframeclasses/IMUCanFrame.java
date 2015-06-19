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
	private boolean is_there_data_accel, is_there_data_gyro,
			is_there_data_magneto, is_there_data_temperature,
			is_there_data_version, is_there_data_board;

	public IMUCanFrame(int id, int dlc, List<Integer> data, Double time) {
		super(id, dlc, data);
		init();
	}

	public IMUCanFrame() {
		init();
	}

	/**
	 * Put all the variables to 0.0
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
				this.rl_second_layout = (RelativeLayout) vp.getChildAt(0)
						.findViewById(R.id.rl_imu_activity);
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
			((TextView) rl_second_layout.findViewById(R.id.magneto_boardf))
					.setText("" + board);
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
			((TextView) rl_second_layout.findViewById(R.id.imu_majf))
					.setText("" + versionMaj);
			((TextView) rl_second_layout.findViewById(R.id.imu_minf))
					.setText("" + versionMin);
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
			((TextView) rl_second_layout.findViewById(R.id.imu_tempef))
					.setText("" + tempeFinal);
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

		double valMagnetoX = BytesFunction.fromTwoComplement(magnetoXMSB,
				magnetoXLSB, 13, 1);
		double valMagnetoY = BytesFunction.fromTwoComplement(magnetoYMSB,
				magnetoYLSB, 13, 1);
		double valMagnetoZ = BytesFunction.fromTwoComplement(magnetoZMSB,
				magnetoZLSB, 13, 1);
		String magnetoX = "";
		String magnetoY = "";
		String magnetoZ = "";
		if (valMagnetoX >= 0)
			magnetoX = " " + valMagnetoX;
		else
			magnetoX = "" + valMagnetoX;
		if (valMagnetoY >= 0)
			magnetoY = " " + valMagnetoY;
		else
			magnetoY = "" + valMagnetoY;
		if (valMagnetoZ >= 0)
			magnetoZ = " " + valMagnetoZ;
		else
			magnetoZ = "" + valMagnetoZ;
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}

			if (IMUCanFrame.indexF == 99) {
				((TextView) rl_second_layout.findViewById(R.id.magneto_time))
						.setText("" + df.format(IMUCanFrame.freqMagneto * 1000)
								+ " ms");
				IMUCanFrame.indexF = 0;
				IMUCanFrame.freqMagneto = 0.0;
			}
			if (IMUCanFrame.indexDisplay2 == 9) {

				((TextView) rl_second_layout.findViewById(R.id.magneto_xmsbf))
						.setText(magnetoX);
				((TextView) rl_second_layout.findViewById(R.id.magneto_ymsbf))
						.setText(magnetoY);
				((TextView) rl_second_layout.findViewById(R.id.magneto_zmsbf))
						.setText(magnetoZ);
				((TextView) rl_second_layout.findViewById(R.id.magneto_resmsbf))
						.setText("" + (resMagnMSB * 256 + resMagnLSB));
				IMUCanFrame.indexDisplay2 = 0;
			} else {
				IMUCanFrame.indexDisplay2 += 1;
			}
			return;
		}
		if (IMUCanFrame.indexDisplay2 == 9) {

			((TextView) rl.findViewById(R.id.magneto_xmsb)).setText(magnetoX);
			((TextView) rl.findViewById(R.id.magneto_ymsb)).setText(magnetoY);
			((TextView) rl.findViewById(R.id.magneto_zmsb)).setText(magnetoZ);
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

		double factor = 1024 / 32.8;
		double valGyroX = BytesFunction.fromTwoComplement(gyroXMSB, gyroXLSB,
				16, factor);
		double valGyroY = BytesFunction.fromTwoComplement(gyroYMSB, gyroYLSB,
				16, factor);
		double valGyroZ = BytesFunction.fromTwoComplement(gyroZMSB, gyroZLSB,
				16, factor);
		String gyroX = "";
		String gyroY = "";
		String gyroZ = "";
		if (valGyroX >= 0)
			gyroX = " " + df2.format(valGyroX);
		else
			gyroX = "" + df2.format(valGyroX);
		if (valGyroY >= 0)
			gyroY = " " + df2.format(valGyroY);
		else
			gyroY = "" + df2.format(valGyroY);
		if (valGyroZ >= 0)
			gyroZ = " " + df2.format(valGyroZ);
		else
			gyroZ = "" + df2.format(valGyroZ);
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			if (IMUCanFrame.indexA == 99) {
				((TextView) rl_second_layout.findViewById(R.id.gyro_time))
						.setText("" + df.format(IMUCanFrame.freqGyro * 1000)
								+ " ms");
				IMUCanFrame.indexA = 0;
				IMUCanFrame.freqGyro = 0.0;
			}
			((TextView) rl_second_layout.findViewById(R.id.gyro_xmsbf))
					.setText(gyroX);
			((TextView) rl_second_layout.findViewById(R.id.gyro_ymsbf))
					.setText(gyroY);
			((TextView) rl_second_layout.findViewById(R.id.gyro_zmsbf))
					.setText(gyroZ);
			return;
		}
		((TextView) rl.findViewById(R.id.gyro_xmsb)).setText(gyroX);
		((TextView) rl.findViewById(R.id.gyro_ymsb)).setText(gyroY);
		((TextView) rl.findViewById(R.id.gyro_zmsb)).setText(gyroZ);
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

		double valAccelX = BytesFunction.fromTwoComplement(accelXMSB,
				accelXLSB, 12, 1024);
		double valAccelY = BytesFunction.fromTwoComplement(accelYMSB,
				accelYLSB, 12, 1024);
		double valAccelZ = BytesFunction.fromTwoComplement(accelZMSB,
				accelZLSB, 12, 1024);
		String accelX = "";
		String accelY = "";
		String accelZ = "";
		if (valAccelX >= 0)
			accelX = " " + df.format(valAccelX);
		else
			accelX = "" + df.format(valAccelX);
		if (valAccelY >= 0)
			accelY = " " + df.format(valAccelY);
		else
			accelY = "" + df.format(valAccelY);
		if (valAccelZ >= 0)
			accelZ = " " + df.format(valAccelZ);
		else
			accelZ = "" + df.format(valAccelZ);
		if (rl == null) {
			if (rl_second_layout == null) {
				return;
			}
			if (IMUCanFrame.indexC == 99) {
				((TextView) rl_second_layout.findViewById(R.id.accel_time))
						.setText("" + df.format(IMUCanFrame.freqAccel * 1000)
								+ " ms");
				IMUCanFrame.indexC = 0;
				IMUCanFrame.freqAccel = 0.0;
			}
			if (IMUCanFrame.indexDisplay == 9) {

				((TextView) rl_second_layout.findViewById(R.id.accel_xmsbf))
						.setText(accelX);

				((TextView) rl_second_layout.findViewById(R.id.accel_ymsbf))
						.setText(accelY);

				((TextView) rl_second_layout.findViewById(R.id.accel_zmsbf))
						.setText(accelZ);
				IMUCanFrame.indexDisplay = 0;
			} else {
				IMUCanFrame.indexDisplay += 1;
			}
			return;
		}
		if (IMUCanFrame.indexDisplay == 9) {
			((TextView) rl.findViewById(R.id.accel_xmsb)).setText(accelX);
			((TextView) rl.findViewById(R.id.accel_ymsb)).setText(accelY);
			((TextView) rl.findViewById(R.id.accel_zmsb)).setText(accelZ);
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
		// NOT USE
		adresse4 = getData().get(0);
		donnee4 = getData().get(1);
		deviceSEL4 = getData().get(2);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse3(RelativeLayout rl) {
		// NOT USE
		adresse3 = getData().get(0);
		deviceSEL3 = getData().get(1);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse2(RelativeLayout rl) {
		// NOT USE
		adresse2 = getData().get(0);
		donnee2 = getData().get(1);
		deviceSEL2 = getData().get(2);
	}

	/**
	 * @param rl
	 */
	private void display_data_adresse1(RelativeLayout rl) {
		// NOT USE
		adresse1 = getData().get(0);
		donnee1 = getData().get(1);
		deviceSEL1 = getData().get(2);
	}

	/**
	 * @return the accelXMSB
	 */
	public Integer getAccelXMSB() {
		return accelXMSB;
	}

	/**
	 * @return the accelXLSB
	 */
	public Integer getAccelXLSB() {
		return accelXLSB;
	}

	/**
	 * @return the gyroXMSB
	 */
	public Integer getGyroXMSB() {
		return gyroXMSB;
	}

	/**
	 * @return the gyroXLSB
	 */
	public Integer getGyroXLSB() {
		return gyroXLSB;
	}

	/**
	 * @return the magnetoXMSB
	 */
	public Integer getMagnetoXMSB() {
		return magnetoXMSB;
	}

	/**
	 * @return the magnetoXLSB
	 */
	public Integer getMagnetoXLSB() {
		return magnetoXLSB;
	}

	/**
	 * @return the accelYLSB
	 */
	public Integer getAccelYLSB() {
		return accelYLSB;
	}

	/**
	 * @return the accelYMSB
	 */
	public Integer getAccelYMSB() {
		return accelYMSB;
	}

	/**
	 * @return the accelZLSB
	 */
	public Integer getAccelZLSB() {
		return accelZLSB;
	}

	/**
	 * @return the accelZMSB
	 */
	public Integer getAccelZMSB() {
		return accelZMSB;
	}

	/**
	 * @return the gyroYMSB
	 */
	public Integer getGyroYMSB() {
		return gyroYMSB;
	}

	/**
	 * @return the gyroYLSB
	 */
	public Integer getGyroYLSB() {
		return gyroYLSB;
	}

	/**
	 * @return the gyroZMSB
	 */
	public Integer getGyroZMSB() {
		return gyroZMSB;
	}

	/**
	 * @return the gyroZLSB
	 */
	public Integer getGyroZLSB() {
		return gyroZLSB;
	}

	/**
	 * @return the magnetoYMSB
	 */
	public Integer getMagnetoYMSB() {
		return magnetoYMSB;
	}

	/**
	 * @return the magnetoYLSB
	 */
	public Integer getMagnetoYLSB() {
		return magnetoYLSB;
	}

	/**
	 * @return the magnetoZMSB
	 */
	public Integer getMagnetoZMSB() {
		return magnetoZMSB;
	}

	/**
	 * @return the magnetoZLSB
	 */
	public Integer getMagnetoZLSB() {
		return magnetoZLSB;
	}

	/**
	 * @return the resMagnMSB
	 */
	public Integer getResMagnMSB() {
		return resMagnMSB;
	}

	/**
	 * @return the resMagnLSB
	 */
	public Integer getResMagnLSB() {
		return resMagnLSB;
	}

	/**
	 * @return the temperature
	 */
	public Integer getTemperature() {
		return temperature;
	}

	/**
	 * @return the versionMaj
	 */
	public Integer getVersionMaj() {
		return versionMaj;
	}

	/**
	 * @return the versionMin
	 */
	public Integer getVersionMin() {
		return versionMin;
	}

	/**
	 * @return the board
	 */
	public Integer getBoard() {
		return board;
	}

	/**
	 * @return the rev
	 */
	public Integer getRev() {
		return rev;
	}
	
	
}
