package com.naio.canreader.canframeclasses;

import java.util.List;

public class ISMCanFrame extends CanFrame {

	public ISMCanFrame(int id, int dlc, List<Integer> data) {
		super(id, dlc, data);
		this.type="ISM";
	}

	public ISMCanFrame() {
		this.type="ISM";
	}

}
