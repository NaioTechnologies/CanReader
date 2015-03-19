/**
 * Created the 15 janv. 2015 at 15:49:51
 * by bodereau
 * 
 */
package com.naio.canreader.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.util.Log;

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
	private String response;

	public void run() {
		synchronized (lock12) {

			for (String cmd : getCmd()) {
				if ((response = readOnce(cmd)) != null) {
					return;
				}
			}
		}
	}

	private String executeCommand(String command) {
		Process p;
		String answer = " ";
		try {
			// execute a command and wait for the response
			p = Runtime.getRuntime().exec(command);
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			String s = "";

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				answer += s;
			}
			p.waitFor();
			p.destroy();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(answer.length() > 2) //if an error occurred, we return it
			return answer;
		else
			return null;
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

	public String readOnce(String cmd) {
		synchronized (lock12) {
			return executeCommand(cmd);
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

	public String getResponse() {
		synchronized (lock12) {
			return response;
		}
	}
}
