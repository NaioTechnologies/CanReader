package com.naio.canreader.parser;

import java.util.ArrayList;
import java.util.List;

import com.naio.canreader.canframeclasses.BrainCanFrame;
import com.naio.canreader.canframeclasses.CanFrame;
import com.naio.canreader.canframeclasses.ErrorCanFrame;
import com.naio.canreader.canframeclasses.GPSCanFrame;
import com.naio.canreader.canframeclasses.GSMCanFrame;
import com.naio.canreader.canframeclasses.IHMCanFrame;
import com.naio.canreader.canframeclasses.IMUCanFrame;
import com.naio.canreader.canframeclasses.ISMCanFrame;
import com.naio.canreader.canframeclasses.MotorLeftCanFrame;
import com.naio.canreader.canframeclasses.MotorRightCanFrame;
import com.naio.canreader.canframeclasses.VerinCanFrame;
import com.naio.canreader.utils.BytesFunction;

/**
 * @author bodereau
 * 
 *         CanParser parse String passed as argument and return one or several
 *         CanFrame a String should be format like this : interface id [dlc]
 *         data , here an example : can0 280 [5] 45 56 12 2E 14
 * 
 *         In the parseOneFrame function, we return new instance of CanFrame but
 *         also we setParams of other, this is because some data need to be
 *         stocked, so we keep instance of.
 * 
 */
public class CanParser {

	GPSCanFrame gpscanframe;
	GSMCanFrame gsmcanframe;
	private VerinCanFrame verincanframe;
	private IHMCanFrame ihmcanframe;
	private IMUCanFrame imucanframe;
	private final Object lock1 = new Object();
	private BrainCanFrame braincanframe;
	private ErrorCanFrame errorcanframe;
	private Integer count;
	private Boolean remote_for_unit;

	public CanParser() {
		super();
		gpscanframe = new GPSCanFrame();
		gsmcanframe = new GSMCanFrame();
		imucanframe = new IMUCanFrame();
		gsmcanframe = new GSMCanFrame();
		ihmcanframe = new IHMCanFrame();
		verincanframe = new VerinCanFrame();
		braincanframe = new BrainCanFrame();
		errorcanframe = new ErrorCanFrame();
		remote_for_unit = false;
		count = 0;
	}

	/**
	 * @param frame
	 * 
	 *            Parse the String and return a CanFrame corresponding to its
	 *            id. frame need to be trim() or there will be an error. if
	 *            frame is a remote request, we don't parse the message
	 * 
	 * @return
	 */
	public CanFrame parseOneFrame(String frame) {

		if (frame.contains("remote")){
			remote_for_unit = true;
			return null;
		}
		if(count==1){
			errorcanframe.setComplementError(frame);
			count=0;
			return null;
		}
		if(frame.contains("ERRORFRAME")){
			errorcanframe.setError(frame);
			count =1;
			return null;
		}
		String[] split = frame.split("\\s+");// split with the space character
		List<Integer> data = new ArrayList<Integer>();
		for (int i = 4; i < split.length; i++) {
			data.add(Integer.parseInt(split[i], 16));
		}
		Double time = Double.parseDouble(split[0].substring(1,
				split[0].length() - 1));
		int id = Integer.parseInt(split[2], 16);
		String binaryString = Integer.toBinaryString(id);
		if (binaryString.length() <= 7) {
			binaryString = BytesFunction.fillWithZeroTheBinaryString(Integer
					.toBinaryString(id));
			binaryString = "000" + binaryString;
		}

		switch (binaryString.substring(0, binaryString.length() - 7)) {
		case "0":
		case "00":
		case "000":
		case "0000":
			braincanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data);
			braincanframe.save_datas();
			return null;
		case "1":
		case "01":
		case "001":
		case "0001":
			return new MotorRightCanFrame(id, Integer.parseInt(split[3]
					.substring(1, 2)), data);
		case "10":
		case "010":
		case "0010":
			return new MotorLeftCanFrame(id, Integer.parseInt(split[3]
					.substring(1, 2)), data);
		case "11":
		case "011":
		case "0011":
			imucanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data, time);
			imucanframe.save_datas();
			return null;
		case "100":
		case "0100":
			return gpscanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data);
		case "101":
		case "0101":
			return gsmcanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data);
		case "110":
		case "0110":
			return new ISMCanFrame(id, Integer.parseInt(split[3]
					.substring(1, 2)), data);
		case "111":
		case "0111":
			ihmcanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data);
			ihmcanframe.save_datas();
			return null;
		case "1000":
			verincanframe.setParams(id,
					Integer.parseInt(split[3].substring(1, 2)), data);
			verincanframe.save_datas();
			return null;
		default:
			return null;
		}

	}

	public ArrayList<CanFrame> parseFrames(String frames) {

		String[] frame = frames.split("\n");
		ArrayList<CanFrame> arrayListFrame = new ArrayList<CanFrame>();

		for (String f : frame) {
			CanFrame canframe = parseOneFrame(f.trim());
			if (canframe == null)
				continue;
			arrayListFrame.add(canframe);
		}
		return arrayListFrame;
	}

	/**
	 * @param gsmcanframe
	 *            the gsmcanframe to set
	 */
	public void setGsmcanframe(GSMCanFrame gsmcanframe) {
		this.gsmcanframe = gsmcanframe;
	}

	/**
	 * @return the gpscanframe
	 */
	public GPSCanFrame getGpscanframe() {
		return gpscanframe;
	}

	/**
	 * @return the gsmcanframe
	 */
	public GSMCanFrame getGsmcanframe() {
		return gsmcanframe;
	}

	/**
	 * @return the verincanframe
	 */
	public VerinCanFrame getVerincanframe() {
		return verincanframe;
	}

	/**
	 * @return the ihmcanframe
	 */
	public IHMCanFrame getIhmcanframe() {
		return ihmcanframe;
	}

	/**
	 * @return the imucanframe
	 */
	public IMUCanFrame getImucanframe() {
		synchronized (lock1) {
			return imucanframe;
		}

	}

	public CanFrame[] getCanFrames() {
		return new CanFrame[] { imucanframe, ihmcanframe, verincanframe,
				gsmcanframe, gpscanframe, braincanframe };
	}

	/**
	 * @return
	 */
	public BrainCanFrame getBraincanframe() {
		return braincanframe;
	}

	public ErrorCanFrame getErrorcanframe() {
		return errorcanframe;
	}
	
	/**
	 * @return the remote_for_unit
	 */
	public Boolean getRemote_for_unit() {
		Boolean tmp = remote_for_unit;
		remote_for_unit = false;
		return tmp;
	}

}
