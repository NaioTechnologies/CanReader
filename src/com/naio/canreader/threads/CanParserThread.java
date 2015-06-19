/**
 * Created the 28 janv. 2015 at 17:55:32
 * by bodereau
 * 
 */
package com.naio.canreader.threads;

import com.naio.canreader.activities.MainActivity;
import com.naio.canreader.parser.CanParser;

/**
 * @author bodereau
 * 
 */
public class CanParserThread extends Thread {

	private boolean stop;
	private CanDumpThread canDumpThread;
	private CanParser canParser;
	private final Object lock1 = new Object();
	

	public CanParserThread(CanDumpThread candumpthread) {
		super();
		this.stop = true;
		canDumpThread = candumpthread;
		canParser = new CanParser();
	}

	/**
	 * @param canDumpThread2
	 * @param mainActivity
	 */
	public CanParserThread(CanDumpThread canDumpThread2,
			MainActivity mainActivity) {
		super();
		this.stop = true;
		canDumpThread = canDumpThread2;
		canParser = new CanParser();
	}

	public void run() {
		while (stop) {
			synchronized (canDumpThread.getEntreThread()) {

				try {
					//wait that the CanDumpThread had something
					canDumpThread.getEntreThread().wait();
					getCanParser().parseFrames(canDumpThread.get100Poll());
					Thread.sleep(0,100);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param stop
	 *            the stop to set
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	/**
	 * @return the canParser
	 */
	public CanParser getCanParser() {
		synchronized (lock1) {
			return canParser;
		}

	}
}
