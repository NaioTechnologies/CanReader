package com.naio.canreader.canframeclasses;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

/**
 * CanFrame represents a frame already parsed from a can a frame look like this
 * : "can0 281#42.45.56" "can0" is the interface, "281" is the id of the message
 * in hexadecimal, it contains the type of the message on 4 bits, the sender on
 * 3 bits and the id message on 4 bits after the # there's the data, in
 * hexadecimal. Here it's 42 45 56 so the dlc variable of CanFrame should be 3.
 * 
 * @author bodereau
 * 
 * 
 */
public class CanFrame {

	private String interfaceName;
	private int id;
	protected int dlc;
	protected List<Integer> data;
	protected String type;
	protected String idMess;

	public CanFrame() {
		super();
		this.type = "CANFRAME";
	}

	public CanFrame(int id, int dlc, List<Integer> data) {
		super();
		this.id = id;
		this.dlc = dlc;
		this.data = data;
		this.type = "CANFRAME";
		this.idMess = Integer.toBinaryString(id).substring(
				Integer.toBinaryString(id).length() - 4,
				Integer.toBinaryString(id).length());
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public int getId() {
		return id;
	}

	public int getDlc() {
		return dlc;
	}

	public List<Integer> getData() {
		return data;
	}

	public String getType() {
		return type;
	}

	public CanFrame setParams(int id, int dlc, List<Integer> data) {
		this.id = id;
		this.dlc = dlc;
		this.data = data;
		this.idMess = Integer.toBinaryString(id).substring(
				Integer.toBinaryString(id).length() - 4,
				Integer.toBinaryString(id).length());
		return this;
	}

	public String show() {
		return "CanFrame type=" + type + " id=" + id + " dlc =" + dlc
				+ " data=" + data + "\n";
	}


	/**
	 * @param rl
	 */
	public void action(RelativeLayout rl) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param rl
	 * @param pager
	 */
	public void action(RelativeLayout rl, ViewPager pager) {
		// TODO Auto-generated method stub
		
	}


}
