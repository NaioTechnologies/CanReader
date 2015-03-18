/**
 * Created the 15 janv. 2015 at 11:51:00
 * by bodereau
 * 
 */
package com.naio.canreader.threads;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Handler;
import android.util.Log;

/**
 * CanDumpThread is the thread which execute the command candump and which place
 * the data of the stdout in a FIFO. queue is the FIFO
 * 
 * @author bodereau
 * 
 */
public class CanDumpThread extends Thread {

	private final Object lock1 = new Object();
	private final Object entreThread = new Object();
	private Integer count ;

	/**
	 * @return the entreThread
	 */
	public Object getEntreThread() {
		return entreThread;
	}

	private final Object lock3 = new Object();

	Handler handler = new Handler();
	private String cmd;
	public ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
	Runnable runnable;
	private boolean quitTheThread = false;

	/**
	 * @return the quitTheThread
	 */
	public boolean isQuitTheThread() {
		synchronized (lock3) {
			return quitTheThread;
		}
	}

	/**
	 * @param quitTheThread
	 *            the quitTheThread to set
	 */
	public void setQuitTheThread(boolean quitTheThread) {
		synchronized (lock3) {
			this.quitTheThread = quitTheThread;
		}
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		synchronized (lock1) {

			return cmd;
		}
	}

	/**
	 * @param cmd
	 *            the cmd to set
	 */
	public void setCmd(String cmd) {
		synchronized (lock1) {
			this.cmd = cmd;
		}
	}

	/**
	 * execute a command and read the output of it until the thread is killed
	 * 
	 * @param command
	 */
	private void executeCommandLoop(String command) {
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			while (true) {
				line = null;
				while ((line = reader.readLine()) != null) {
					//get the info of the error
					/*if(count == 1){
						Log.e("errrrr",line);
						reader.close();
						Thread.currentThread().interrupt();
						break;
					}
					//get the error
					if(line.contains("ERRORFRAME")){
						//Thread.sleep(10,0);
						Log.e("errrrrr",line);
						count++;
						continue;
					}*/
					synchronized (entreThread) {
						getQueue().offer(line);
						entreThread.notify();
					}

					Thread.sleep(0, 10);

					if (quitTheThread) {
						reader.close();
						Thread.currentThread().interrupt();
						break;
					}
				}
				if (quitTheThread) {
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the queue
	 */
	public ConcurrentLinkedQueue<String> getQueue() {
		return queue;

	}

	public String getOnePoll() {
		return getQueue().poll();
	}

	/**
	 * Retrieve 100 values or less in the FIFO. data is initialize to a value
	 * that doesn't mean anything to avoid error if there is nothing in the FIFO
	 * 
	 * @return
	 */
	public String get100Poll() {
		// false trame to avoid return null
		String fakeTrame = "(000.0000000) can0 480 [2] remote";
		String poll = "";
		for (int i = 0; i < 100; i++) {
			poll = getOnePoll();
			if (poll == null)
				return fakeTrame;
			fakeTrame += "\n" + poll;
		}
		return fakeTrame;
	}

	public void run() {
		count =0;
		setQuitTheThread(false);
		queue = new ConcurrentLinkedQueue<String>();
		executeCommandLoop(cmd);
	}

	public void quit() {
		setQuitTheThread(true);
	}

}
