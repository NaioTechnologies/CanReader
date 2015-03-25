package com.naio.canreader.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.naio.canreader.R;

import com.naio.canreader.canframeclasses.GSMCanFrame;
import com.naio.canreader.canframeclasses.VerinCanFrame;
import com.naio.canreader.parser.CanParser;
import com.naio.canreader.threads.CanDumpThread;
import com.naio.canreader.threads.CanParserThread;
import com.naio.canreader.threads.CanSendThread;
import com.naio.canreader.utils.MyPagerAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * MainActivity call a function every 10 ms when the Read button is pressed,
 * this function read the FIFO filled by CanDumpThread and display it on the
 * screen. Also the button connect allow to mount the can interface.
 * 
 * @author bodereau
 */
public class MainActivity extends FragmentActivity {

	private CanDumpThread canDumpThread;
	private CanSendThread canSendThread;
	private final Object lock = new Object();
	private int indexDebug;
	private static final int MILLISECONDS_RUNNABLE = 10;

	// KEEP_CONTROL_CAN_LOOP * MILLISECONDS_RUNNABLE for re sending the keep
	// control message
	private static final int KEEP_CONTROL_CAN_LOOP = 50;
	// message for keeping the hand over the Pascal's code ( only the '69' is
	// important )
	private static final String KEEP_CONTROL_CAN_LOOP_MESSAGE = "69.55.21.23.25.12.11.FF";

	private static boolean binary_added = false;

	public static boolean UNIT_TEST = false;

	/**
	 * @return the lock
	 */
	public Object getLock() {
		return lock;
	}

	Handler handler = new Handler();
	Boolean reading = false;
	CanParser canParser = new CanParser();
	Runnable runnable = new Runnable() {
		public void run() {
			display_the_can();

		}
	};
	private RelativeLayout rl;
	private MyPagerAdapter mPagerAdapter;
	private ViewPager pager;

	/**
	 * @return the pager
	 */
	public ViewPager getPager() {
		return pager;
	}

	private CanParserThread canParserThread;
	private boolean gsmWork;
	private int cptGsm;
	private boolean stopTheHandler;
	private static boolean layoutPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getBoolean("layout", false)) {
				setContentView(R.layout.main_activity);
				layoutPage = true;
			} else {
				set_fragment_layout();
			}
		} else {
			set_fragment_layout();
		}
		canDumpThread = new CanDumpThread();
		canParserThread = new CanParserThread(canDumpThread, this);
		reading = false;
		gsmWork = false;
		indexDebug = 0;
		canParser = new CanParser();
		rl = (RelativeLayout) findViewById(R.id.rl_main_activity);
		if (!binary_added) {
			executeCommand("su -c mount -o rw,remount /");
			File file = new File("/sbin/candump");
			executeCommand("su -c mount -o ro,remount /");
			if (file.exists())
				binary_added = true;
			else {
				executeCommand("su -c mount -o rw,remount /");
				executeCommand("su -c cp /storage/sdcard0/candump2 /sbin/candump");
				executeCommand("su -c cp /storage/sdcard0/cansend2 /sbin/cansend");
				executeCommand("su -c chmod 775 /sbin/candump");
				executeCommand("su -c chmod 775 /sbin/cansend");
				executeCommand("su -c insmod /storage/sdcard0/drive/can.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/can-dev.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/can-raw.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/can-bcm.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/pcan.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/vcan.ko");
				executeCommand("su -c insmod /storage/sdcard0/drive/peak_usb.ko");
				executeCommand("su -c rmmod pcan");
				executeCommand("su -c mount -o ro,remount /");
				binary_added = true;

			}
		}
		new AlertDialog.Builder(this)
				.setTitle("Information")
				.setMessage(
						"Vous pouvez brancher dès à présent l'interface can usb, si elle est déjà branché, rebranchez la.\n"
								+ "Et assurez vous bien que le robot soit allumé et que l'interface can soit allumée avant d'appuyer sur READ.")
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// continue with delete
							}
						}).setIcon(android.R.drawable.ic_dialog_info).show();

	}

	/**
	 * 
	 */
	private void set_fragment_layout() {
		setContentView(R.layout.viewpager);// Création de la liste de
		// Fragments que fera défiler le PagerAdapter
		List fragments = new Vector();
		cptGsm = 0;
		stopTheHandler = false;
		// Ajout des Fragments dans la liste
		fragments.add(Fragment.instantiate(this,
				BlocIMUActivity.class.getName()));
		fragments.add(Fragment.instantiate(this,
				BlocGPSActivity.class.getName()));
		fragments.add(Fragment.instantiate(this,
				BlocIHMActivity.class.getName()));
		fragments.add(Fragment.instantiate(this,
				BlocVerinActivity.class.getName()));
		fragments.add(Fragment.instantiate(this,
				BlocTensionActivity.class.getName()));
		fragments.add(Fragment.instantiate(this,
				BlocErrorCanActivity.class.getName()));
		// Création de l'adapter qui s'occupera de l'affichage de la
		// liste de Fragments

		this.mPagerAdapter = new MyPagerAdapter(
				super.getSupportFragmentManager(), fragments);

		pager = (ViewPager) super.findViewById(R.id.viewpager);
		pager.setOffscreenPageLimit(5);
		// Affectation de l'adapter au ViewPager
		pager.setAdapter(this.mPagerAdapter);

		// pager.getChildAt(4).findViewById(R.id.text_connection).setVisibility(View.VISIBLE);
		layoutPage = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			final Dialog dialog = new Dialog(this);

			dialog.setContentView(R.layout.info_dialog);
			dialog.setTitle(getString(R.string.info_title));

			Button dialogButton = (Button) dialog
					.findViewById(R.id.dialogButtonOK);
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			return true;
		}

		// Allow to change the layout to an another with all in it ( not maj btw
		// )
		/*
		 * if (id == R.id.action_layout) { if (layoutPage) {
		 * change_activity_layout(false); return true; }
		 * change_activity_layout(true); return true; }
		 */

		return super.onOptionsItemSelected(item);
	}

	/**
	 * @param b
	 * 
	 */
	private void change_activity_layout(boolean b) {
		canParserThread.setStop(false);
		canParserThread.interrupt();
		canDumpThread.quit();
		handler.removeCallbacks(runnable);
		Intent intent = getIntent();
		finish();
		intent.putExtra("layout", b);
		startActivity(intent);
	}

	/**
	 * Action performed by the READ button. Run the runnable in 1 ms ( that will
	 * be run in a loop ) This runnable going to read the FIFO filled by a
	 * candump
	 * 
	 * @param v
	 */
	public void button_read_clicked(View v) {
		if (!reading) {
			if (button_connect_clicked(v).contains("can0")) {
				Toast.makeText(
						this,
						"L'interface CAN n'est pas branchée ou n'est pas démarrée",
						Toast.LENGTH_LONG).show();
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return;
			}
			// the sleep here is for avoid the user to press the button
			// multi-time before it changes its state
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			canDumpThread = new CanDumpThread();
			canParserThread = new CanParserThread(canDumpThread);
			if (UNIT_TEST)
				canDumpThread
						.setCmd("su -c /sbin/candump -tz -e vcan0,0:0,#FFFFFFFF");
			else
				canDumpThread
						.setCmd("su -c /sbin/candump -tz -e can0,0:0,#FFFFFFFF");
			stopTheHandler = false;
			canDumpThread.start();
			canParserThread.start();
			pager.getChildAt(1).findViewById(R.id.text_connection)
					.setVisibility(View.GONE);
			pager.getChildAt(2).findViewById(R.id.text_connection)
					.setVisibility(View.GONE);
			pager.getChildAt(3).findViewById(R.id.text_connection)
					.setVisibility(View.GONE);
			pager.getChildAt(4).findViewById(R.id.text_connection)
					.setVisibility(View.GONE);

			// cansend("00F", KEEP_CONTROL_CAN_LOOP_MESSAGE);
			handler.postDelayed(runnable, MILLISECONDS_RUNNABLE);
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("STOP");
			reading = true;
			return;
		}
		reading = false;
		gsmWork = false;
		indexDebug = 0;
		stopTheHandler = true;
		canDumpThread.quit();
		canParserThread.setStop(false);
		canDumpThread.interrupt();
		canParserThread.interrupt();
		pager.getChildAt(1).findViewById(R.id.text_connection)
				.setVisibility(View.VISIBLE);
		pager.getChildAt(2).findViewById(R.id.text_connection)
				.setVisibility(View.VISIBLE);
		pager.getChildAt(3).findViewById(R.id.text_connection)
				.setVisibility(View.VISIBLE);
		pager.getChildAt(4).findViewById(R.id.text_connection)
				.setVisibility(View.VISIBLE);
		handler.removeCallbacks(runnable);
		cptGsm = 0;
		((Button) findViewById(R.id.button_read_main_activity))
				.setText(getString(R.string.imu_button_read));
		// the sleep here is just because there is a sleep when the user press
		// the READ button, so do the STOP.
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// we stop the app when onPause because other app could read the can
		super.onPause();
		if (reading) {
			reading = false;
			if (canDumpThread != null) {
				canDumpThread.quit();
				canDumpThread.interrupt();
			}
			if (canParserThread != null) {
				canParserThread.setStop(false);
				canParserThread.interrupt();
			}
			if (canSendThread != null)
				canSendThread.interrupt();
			gsmWork = false;
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("READ");
			handler.removeCallbacks(runnable);
		}
	}

	@Override
	public void onBackPressed() {
		// When back is pressed, we stop all the thread and hope it's really the
		// case
		super.onBackPressed();
		if (reading) {
			reading = false;
			if (canDumpThread != null) {
				canDumpThread.quit();
				canDumpThread.interrupt();
			}
			if (canParserThread != null) {
				canParserThread.setStop(false);
				canParserThread.interrupt();
			}
			if (canSendThread != null)
				canSendThread.interrupt();
			gsmWork = false;
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("READ");
			handler.removeCallbacks(runnable);
		}
	}

	/**
	 * Action performed by the CONNECT button. Mount the can interface with a
	 * bitrate of 1000K
	 * 
	 * @param v
	 */
	public String button_connect_clicked(View v) {
		if (UNIT_TEST) {
			executeCommand("su -c ip link add dev vcan0 add dev");
			return executeCommand("su -c ip link set up vcan0");
		} else
			return executeCommand("su -c ip link set can0 up type can bitrate 1000000");
	}

	public void disconnect_can() {
		if (UNIT_TEST)
			executeCommand("su -c ip link set vcan0 down");
		else
			executeCommand("su -c ip link set can0 down");
	}

	/**
	 * @return the canParserThread
	 */
	public CanParserThread getCanParserThread() {
		return canParserThread;
	}

	/**
	 * Function call by the runnable, it read the FIFO of CanDumpThread with the
	 * get100Poll function ( which extract 100 values dumped or less if there is
	 * not 100 values ) and parse the data with CanParser to finally call the
	 * action method of the CanFrame class instantiate by the CanParser.
	 */
	private void display_the_can() {
		// display all the informations on screen
		canParserThread.getCanParser().getGpscanframe().display_on(rl, pager);
		canParserThread.getCanParser().getImucanframe().display_on(rl, pager);
		canParserThread.getCanParser().getGsmcanframe().display_on(rl, pager);
		canParserThread.getCanParser().getVerincanframe().display_on(rl, pager);
		canParserThread.getCanParser().getIhmcanframe().display_on(rl, pager);
		canParserThread.getCanParser().getBraincanframe().display_on(rl, pager);
		if (!canParserThread.getCanParser().getErrorcanframe().getError()
				.contentEquals("no error")) {
			if (canParserThread.getCanParser().getErrorcanframe()
					.getComplementError().length() > 2) {
				button_read_clicked(null);
				String errorText = "can error : \n"
						+ canParserThread.getCanParser().getErrorcanframe()
								.getComplementError();
				Toast.makeText(this, errorText, Toast.LENGTH_LONG).show();
				write_in_file(this, errorText);
				disconnect_can();
				return;
			}
		}
		/*
		 * String responseCanSend = null; if((responseCanSend =
		 * canSendThread.getResponse()) != null){ Toast.makeText( this,
		 * "write on can IMPOSSIBRU : \n" + responseCanSend,
		 * Toast.LENGTH_LONG).show(); }
		 */
		keep_control_of_can();
		if (!stopTheHandler)
			handler.postDelayed(runnable, MILLISECONDS_RUNNABLE);
	}

	/**
	 * send a message on the can which disable other programs to send something
	 * on the can
	 */
	private void keep_control_of_can() {
		if (gsmWork) {
			indexDebug++;
			if (indexDebug == KEEP_CONTROL_CAN_LOOP) {
				cansend("00F", KEEP_CONTROL_CAN_LOOP_MESSAGE);
				indexDebug = 0;
			}
		} else {
			indexDebug++;
			if (indexDebug == KEEP_CONTROL_CAN_LOOP) {
				cansend_gsm("AT+CPIN?\r");
				cptGsm++;
			}
			if (indexDebug == 2 * KEEP_CONTROL_CAN_LOOP) {
				if (canParserThread.getCanParser().getGsmcanframe()
						.isGsmWorking()) {
					gsmWork = true;
				}
				indexDebug = 0;
			}
		}
		if (!gsmWork && cptGsm > 5) {
			gsmWork = true;
			indexDebug = 0;
			cptGsm = 0;
			for (int k = 0; k < 2; k++)
				Toast.makeText(this, getString(R.string.toast_gsm_fail),
						Toast.LENGTH_LONG).show();
			write_in_file(this, "GSM not responding");
		}

	}

	/**
	 * Execute a command in a shell
	 * 
	 * @param command
	 * 
	 * @return
	 */
	public String executeCommand(String command) {
		// Only use by the CONNECT button
		StringBuffer output = new StringBuffer();
		Process p;
		String line = "";
		String answer = "";
		try {
			p = Runtime.getRuntime().exec(command);

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			String s = null;

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
				answer += s;
			}
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}

	public void button_send_gsm_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.send_sms_dialog);
		dialog.setTitle(getString(R.string.sms_title));

		final EditText editNumero = (EditText) dialog
				.findViewById(R.id.edittext_numero);
		final EditText editMessage = (EditText) dialog
				.findViewById(R.id.edittext_message);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend_gsm("AT+CMGS=\"" + editNumero.getText().toString()
						+ "\"\r" + editMessage.getText().toString() + "\u001A");
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	/**
	 * Read the unread sms and only the unread ones
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_receive_gsm_clicked(View v) {
		cansend_gsm("AT+CMGL=\"REC UNREAD\"\r");
	}

	/**
	 * Config the sim card in GSM mode ( default mode is IRA )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_config_gsm_clicked(View v) {
		cansend_gsm("AT+CSCS=\"GSM\"\r");
	}

	/**
	 * Open a dialog which allows the user to enter his own command
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_custom_gsm_clicked(View v) {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.custom_at_command_dialog);
		dialog.setTitle(getString(R.string.at_title));

		final EditText editCommand = (EditText) dialog
				.findViewById(R.id.edittext_numero);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String txt = editCommand.getText().toString();
				cansend_gsm("AT+" + txt + "\r");
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	/**
	 * Config the sim card in txt mode ( default is PDU )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_config2_gsm_clicked(View v) {
		cansend_gsm("AT+CMGF=1\r");
	}

	/**
	 * Check if access is granted to the sim card ( response : READY )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_statut_gsm_clicked(View v) {
		cansend_gsm("AT+CPIN?\r");
	}

	/**
	 * Display a dialog asking for the PIN of the sim card
	 * 
	 * @param v
	 * 
	 */
	public void button_config_pin_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.enter_pin_dialog);
		dialog.setTitle(getString(R.string.pin_title));

		final EditText pinCodeTextView = (EditText) dialog
				.findViewById(R.id.edittext_numero);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String pinCode = "";

				if (!pinCodeTextView.getText().toString().isEmpty()) {
					pinCode += pinCodeTextView.getText().toString();
				}
				cansend_gsm("AT+CPIN=\"" + pinCode + "\"\r");
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Display a dialog to set active or not the LEDs and their colors
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_etat_led_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.config_led_dialog);
		dialog.setTitle(getString(R.string.led_title_dialog));

		final Spinner spinnerGauche = (Spinner) dialog
				.findViewById(R.id.spinner_led_gauche);
		final Spinner spinner2 = (Spinner) dialog
				.findViewById(R.id.spinner_led_2);
		final Spinner spinner3 = (Spinner) dialog
				.findViewById(R.id.spinner_led_3);
		final Spinner spinnerDroite = (Spinner) dialog
				.findViewById(R.id.spinner_led_droite);
		final Spinner spinnerCGauche = (Spinner) dialog
				.findViewById(R.id.spinner_led_gauche_couleur);
		final Spinner spinnerC2 = (Spinner) dialog
				.findViewById(R.id.spinner_led_2_couleur);
		final Spinner spinnerC3 = (Spinner) dialog
				.findViewById(R.id.spinner_led_3_couleur);
		final Spinner spinnerCDroite = (Spinner) dialog
				.findViewById(R.id.spinner_led_droite_couleur);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String binary_led = "0000"
						+ spinnerDroite.getSelectedItem().toString()
						+ spinner3.getSelectedItem().toString()
						+ spinner2.getSelectedItem().toString()
						+ spinnerGauche.getSelectedItem().toString();

				binary_led = String.format("%02X",
						Long.parseLong(binary_led, 2));
				String binary_colors = "0000"
						+ spinnerCDroite.getSelectedItemPosition()
						+ spinnerC3.getSelectedItemPosition()
						+ spinnerC2.getSelectedItemPosition()
						+ spinnerCGauche.getSelectedItemPosition();
				binary_colors = String.format("%02X",
						Long.parseLong(binary_colors, 2));

				canParser.setGsmcanframe(new GSMCanFrame());
				cansend("382", binary_led + binary_colors);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void button_magneto_board_clicked(View v) {
		cansend("18F", "R");
	}

	public void button_magneto_version_clicked(View v) {
		cansend("184", "R");
	}

	public void button_magneto_temperature_clicked(View v) {
		cansend("183", "R");
	}

	public void button_retour_position_clicked(View v) {
		cansend("401", "R");
	}

	public void button_lecture_odo_clicked(View v) {
		VerinCanFrame.resetCpt();
	}

	public void button_version_verin_clicked(View v) {
		cansend("405", "R");
	}

	/**
	 * Display a dialog asking for the command in hexa to send to the IHM or the
	 * text to display on the LCD screen
	 * 
	 * @param v
	 * 
	 */
	public void button_envoie_affichage_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_commande_ecran);
		dialog.setTitle(getString(R.string.ecran_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);
		final EditText hexa2 = (EditText) dialog.findViewById(R.id.hexa2);
		final EditText hexa3 = (EditText) dialog.findViewById(R.id.hexa3);
		final EditText ecranText = (EditText) dialog
				.findViewById(R.id.commandecran);
		final EditText ecranText2 = (EditText) dialog
				.findViewById(R.id.commandecran2);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (ecranText.getText().toString().isEmpty()
						&& ecranText2.getText().toString().isEmpty()) {

					if (!hexa1.getText().toString().isEmpty()) {
						dataHexa += hexa1.getText().toString();
					}
					if (!hexa2.getText().toString().isEmpty()) {
						dataHexa += "." + hexa2.getText().toString();
					}
					if (!hexa3.getText().toString().isEmpty()) {
						dataHexa += "." + hexa3.getText().toString();
					}
					cansend("380", dataHexa);
					dialog.dismiss();
				}
				String txtToWrite = ecranText.getText().toString();
				String txtToWrite2 = ecranText2.getText().toString();
				dataHexa += "02";
				int index = 0;
				for (char c : txtToWrite.toCharArray()) {
					cansend("380",
							dataHexa + "." + String.format("%02x", (int) index)
									+ "." + String.format("%02x", (int) c));
					index++;
				}
				index = 40;
				for (char c : txtToWrite2.toCharArray()) {
					cansend("380",
							dataHexa + "." + String.format("%02x", (int) index)
									+ "." + String.format("%02x", (int) c));
					index++;
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Display a dialog asking for the command to send to the buzzer.
	 * 
	 * @param v
	 * 
	 */
	public void button_envoie_buzzer_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_commande_buzzer);
		dialog.setTitle(getString(R.string.buzzer_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);
		final EditText hexa2 = (EditText) dialog.findViewById(R.id.hexa2);
		final EditText hexa3 = (EditText) dialog.findViewById(R.id.hexa3);
		final EditText hexa4 = (EditText) dialog.findViewById(R.id.hexa4);
		final EditText hexa5 = (EditText) dialog.findViewById(R.id.hexa5);
		final EditText hexa6 = (EditText) dialog.findViewById(R.id.hexa6);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				if (!hexa2.getText().toString().isEmpty()) {
					dataHexa += "." + hexa2.getText().toString();
				}
				if (!hexa3.getText().toString().isEmpty()) {
					dataHexa += "." + hexa3.getText().toString();
				}
				if (!hexa4.getText().toString().isEmpty()) {
					dataHexa += "." + hexa4.getText().toString();
				}
				if (!hexa5.getText().toString().isEmpty()) {
					dataHexa += "." + hexa5.getText().toString();
				}
				if (!hexa6.getText().toString().isEmpty()) {
					dataHexa += "." + hexa6.getText().toString();
				}
				cansend("383", dataHexa);
				dialog.dismiss();
			}
		});
		Button dialogButton1 = (Button) dialog
				.findViewById(R.id.button_stop_son);
		dialogButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("383", "03");
			}
		});
		Button dialogButton2 = (Button) dialog
				.findViewById(R.id.button_joue_son);
		dialogButton2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("383", "01.10.10.32");
				dialog.dismiss();
			}
		});
		Button dialogButton3 = (Button) dialog
				.findViewById(R.id.button_joue_son_discontinu);
		dialogButton3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("383", "02.10.10.10.02.32");
			}
		});
		dialog.show();
	}

	public void button_ihm_status_clicked(View v) {
		cansend("384", "R");
	}

	public void button_version_ihm_clicked(View v) {
		cansend("386", "R");
	}

	public void button_board_ihm_clicked(View v) {
		cansend("38F", "R");
	}

	/**
	 * Display a dialog asking for the command to send to the motor/verin
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_commande_moteur_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_one_command);
		dialog.setTitle(getString(R.string.command_verin_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				cansend("402", dataHexa);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Display a dialog asking for the command to send to the LCD screen ( for
	 * changing the contrast )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_contraste_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_one_command);
		dialog.setTitle(getString(R.string.command_contrast_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				cansend("387", dataHexa);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Display a dialog asking for the command to send to the LCD screen ( for
	 * changing the backlight )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_backlight_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_one_command);
		dialog.setTitle(getString(R.string.command_backlight_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				cansend("388", dataHexa);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * Display a dialog asking for the command to send verin/motor ( to move the
	 * verin at a position)
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_requete_position_clicked(View v) {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_one_command);
		dialog.setTitle(getString(R.string.command_position_title));

		final EditText hexa1 = (EditText) dialog.findViewById(R.id.hexa1);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";
				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				cansend("400", dataHexa);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 
	 * Display a dialog which emulate the keyboard of the IHM.
	 * 
	 * @param v
	 * 
	 */
	public void button_etat_clavier_clicked(View v) {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.clavier_ihm_dialog);
		dialog.setTitle(getString(R.string.clavier_title));

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button valider = (Button) dialog.findViewById(R.id.valider);
		valider.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "02");
			}
		});
		Button annuler = (Button) dialog.findViewById(R.id.annuler);
		annuler.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "01");
			}
		});
		Button haut = (Button) dialog.findViewById(R.id.haut);
		haut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "04");
			}
		});
		Button bas = (Button) dialog.findViewById(R.id.bas);
		bas.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "08");
			}
		});
		Button gauche = (Button) dialog.findViewById(R.id.gauche);
		gauche.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "20");
			}
		});
		Button droit = (Button) dialog.findViewById(R.id.droite);
		droit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cansend("381", "10");
			}
		});
		dialog.show();
	}

	public void button_tension_clicked(View v) {
		cansend("406", "R");
	}

	public void button_batterie_clicked(View v) {
		cansend("407", "R");
	}

	/**
	 * Display the file where is written the can error ( and display it in order
	 * )
	 * 
	 */
	public void button_actualiser_erreur_clicked(View v) {
		try {
			String textToParse = getStringFromFile(this);
			String[] textParsed = textToParse.split("-separator-");
			String textFinish = "";

			for (int i = textParsed.length - 1; i >= 0; i--) {
				textFinish += textParsed[i] + "\n";
			}

			((TextView) findViewById(R.id.historique_can)).setText(textFinish);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function specific for sending GSM trame on the can ( all char one by one
	 * )
	 * 
	 * @param command
	 */
	private void cansend_gsm(String command) {
		canParser.setGsmcanframe(new GSMCanFrame());
		if (canSendThread != null) {
			canSendThread = null;
		}
		canSendThread = new CanSendThread();
		canSendThread.addStringCommandForGSM("281", command);
		canSendThread.start();

		String responseCanSend = null;
		if ((responseCanSend = canSendThread.getResponse()) != null) {
			String errorText = "Ecriture sur le can impossible : \n" + command
					+ "\n" + responseCanSend;
			Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
			write_in_file(this, errorText);
			reading = false;
			if (canDumpThread != null) {
				canDumpThread.quit();
				canDumpThread.interrupt();
			}
			if (canParserThread != null) {
				canParserThread.setStop(false);
				canParserThread.interrupt();
			}
			if (canSendThread != null)
				canSendThread.interrupt();
			gsmWork = false;
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("READ");
			stopTheHandler = true;
			handler.removeCallbacks(runnable);
			pager.getChildAt(1).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(2).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(3).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(4).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Function for send on the can a command ( with the id )
	 * 
	 * @param id
	 * @param command
	 */
	private void cansend(String id, String command) {
		if (canSendThread != null) {
			canSendThread = null;
		}
		if (command == null || command.isEmpty())
			return;
		if (command.contentEquals("0"))
			command = "00";
		else if (command.contentEquals("1"))
			command = "01";
		else if (command.contentEquals("2"))
			command = "02";
		else if (command.contentEquals("3"))
			command = "03";
		else if (command.contentEquals("4"))
			command = "04";
		else if (command.contentEquals("5"))
			command = "05";
		else if (command.contentEquals("6"))
			command = "06";
		else if (command.contentEquals("7"))
			command = "07";
		else if (command.contentEquals("8"))
			command = "08";
		else if (command.contentEquals("9"))
			command = "09";
		canSendThread = new CanSendThread();
		canSendThread.addStringCommand(id, command);
		canSendThread.start();
		while (canSendThread.isAlive()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String responseCanSend = null;
		if ((responseCanSend = canSendThread.getResponse()) != null) {
			String errorText = "Ecriture sur le can impossible : \ncommand : @"
					+ id + " =>" + command + "\n" + responseCanSend;
			Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
			write_in_file(this, errorText);
			reading = false;
			if (canDumpThread != null) {
				canDumpThread.quit();
				canDumpThread.interrupt();
			}
			if (canParserThread != null) {
				canParserThread.setStop(false);
				canParserThread.interrupt();
			}
			if (canSendThread != null)
				canSendThread.interrupt();
			gsmWork = false;
			stopTheHandler = true;
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("READ");
			handler.removeCallbacks(runnable);
			pager.getChildAt(1).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(2).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(3).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
			pager.getChildAt(4).findViewById(R.id.text_connection)
					.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * write can error in a file
	 * 
	 * @param ctx
	 * @param error
	 */
	public void write_in_file(Context ctx, String error) {
		File gpxfile = new File(ctx.getFilesDir(), "history_of_crash.naio");
		Date date = new Date();
		FileWriter writer;
		try {
			writer = new FileWriter(gpxfile, true);
			writer.append(date.toString() + "---" + error + "\n-separator-");
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Call the function which delete the content of the error can file
	 * 
	 * @param v
	 */
	public void button_delete_file_clicked(View v) {
		delete_file(this);
		((TextView) findViewById(R.id.historique_can)).setText(" ");
	}

	/**
	 * Delete the content of the error can file
	 * 
	 * @param ctx
	 */
	public void delete_file(Context ctx) {
		File gpxfile = new File(ctx.getFilesDir(), "history_of_crash.naio");
		FileWriter writer;
		try {
			writer = new FileWriter(gpxfile, false);
			writer.append(" ");
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Convert a inputStream to a string
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	/**
	 * Read a file and return the content of it as a string
	 * 
	 * @param ctx
	 * @return
	 * @throws Exception
	 */
	public String getStringFromFile(Context ctx) throws Exception {
		File fl = new File(ctx.getFilesDir(), "history_of_crash.naio");
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		fin.close();
		return ret;
	}
}
