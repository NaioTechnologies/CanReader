package com.naio.canreader.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.naio.canreader.canframeclasses.CanFrame;
import com.naio.canreader.canframeclasses.GPSCanFrame;
import com.naio.canreader.canframeclasses.GSMCanFrame;
import com.naio.canreader.canframeclasses.IHMCanFrame;
import com.naio.canreader.canframeclasses.IMUCanFrame;
import com.naio.canreader.canframeclasses.ISMCanFrame;
import com.naio.canreader.canframeclasses.MotorLeftCanFrame;
import com.naio.canreader.canframeclasses.MotorRightCanFrame;
import com.naio.canreader.canframeclasses.VerinCanFrame;

/**
 * @author bodereau
 * 
 *         CanParser parse String passed as argument and return one or several
 *         CanFrame a String should be format like this : interface id [dlc]
 *         data , here an example : can0 280 [5] 45 56 12 2E 14
 * 
 *         In the parseOneFrame function, we return new instance of CanFrame but
 *         also we setParams of other, this is because some data need to be
 *         bufferized, so we keep instance of.
 * 
 */
public class CanParser {

	GPSCanFrame gpscanframe;
	GSMCanFrame gsmcanframe;

	public CanParser() {
		super();
		gpscanframe = new GPSCanFrame();
		gsmcanframe = new GSMCanFrame();
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

		if (frame.contains("remote"))
			return null;

		String[] split = frame.split("\\s+");// split with the space character
		List<Integer> data = new ArrayList<Integer>();
		for (int i = 4; i < split.length; i++) {
			data.add(Integer.parseInt(split[i], 16));
		}
		Double time =  Double.parseDouble(split[0].substring(1,split[0].length() - 1));
		int id = Integer.parseInt(split[2], 16);
		switch (Integer.toBinaryString(id).substring(0,
				Integer.toBinaryString(id).length() - 7)) {
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
			return new IMUCanFrame(id, Integer.parseInt(split[3]
					.substring(1, 2)), data,time);
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
			return new IHMCanFrame(id, Integer.parseInt(split[3]
					.substring(1, 2)), data);
		case "1000":
			return new VerinCanFrame(id, Integer.parseInt(split[3].substring(1,
					2)), data);
		default:
			return new CanFrame(id, Integer.parseInt(split[3].substring(1, 2)),
					data);
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

}
