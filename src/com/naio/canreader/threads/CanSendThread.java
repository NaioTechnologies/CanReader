/**
 * Created the 15 janv. 2015 at 15:49:51
 * by bodereau
 * 
 */
package com.naio.canreader.threads;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 * CanSendThread is the thread which execute the command cansend
 * 
 * @author bodereau
 * 
 */
public class CanSendThread extends Thread {
	private final Object lock12 = new Object();
	Handler handler = new Handler();
	private List<String> cmd = new ArrayList<String>();

	public void run() {
		for (String cmd : getCmd()) {
			readOnce(cmd);
		}
	}
	
	private void executeCommand(String command) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			p.destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute as many as cansend that there are characters in data. Send a
	 * command to a GSM work only if characters are sent one by one
	 * 
	 * @param string
	 * @param string2
	 * 
	 * 
	 */
	public void addStringCommandForGSM(String id, String data) {
		String base = "su -c /sbin/cansend can0 " + id + "#";
		for (char i : data.toCharArray()) {
			addCmd(base + String.format("%02x", (int) i));
		}
	}

	/**
	 * add a cansend command to the CanSendThread thread. id and data are in
	 * hexadecimal. exemple of cansend : "cansend can0 280#AA.FF.00"
	 * 
	 * @param id
	 * @param data
	 * 
	 * 
	 */
	public void addStringCommand(String id, String data) {
		String base = "su -c /sbin/cansend can0 " + id + "#";
		addCmd(base + data);
	}
	
	/**
	 * @param cmd
	 *            the cmd to set
	 */
	public void addCmd(String cmd) {
		synchronized (lock12) {
			this.cmd.add(cmd);
		}
	}

	public void readOnce(String cmd) {
		synchronized (lock12) {
			executeCommand(cmd);
		}
	}
	
	/**
	 * @return the cmd
	 */
	public List<String> getCmd() {
		synchronized (lock12) {
			return cmd;
		}
	}
}
