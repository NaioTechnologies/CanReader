package com.naio.canreader.canframeclasses;

import java.util.List;
/**
 * TODO when ready
 * @author bodereau
 *
 */
public class MotorLeftCanFrame extends MotorCanFrame {
	public MotorLeftCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type="MOTOR LEFT";
	}

	public MotorLeftCanFrame() {
		this.type="MOTOR LEFT";
	}
}
