package com.naio.canreader.canframeclasses;

import java.util.List;

public class MotorRightCanFrame extends MotorCanFrame {
	public MotorRightCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type="MOTOR RIGHT";
	}

	public MotorRightCanFrame() {
		this.type="MOTOR RIGHT";
	}
}
