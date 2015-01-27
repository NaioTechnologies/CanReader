package com.naio.canreader.activities;

import java.lang.Process;
import java.util.List;
import java.util.Vector;

import com.naio.canreader.R;

import com.naio.canreader.canframeclasses.CanFrame;
import com.naio.canreader.canframeclasses.GSMCanFrame;
import com.naio.canreader.canframeclasses.VerinCanFrame;
import com.naio.canreader.utils.CanParser;
import com.naio.canreader.utils.CanDumpThread;
import com.naio.canreader.utils.CanSendThread;
import com.naio.canreader.utils.MyPagerAdapter;

import android.app.Dialog;
import android.support.v4.app.Fragment;
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

/**
 * 
 * MainActivity call a function every 1 ms when the Read button is pressed, this
 * function read the FIFO filled by CanDumpThread and display it on the screen.
 * Also the button connect allow to mount the can interface. And other buttons
 * will be added ...
 * 
 * @author bodereau
 */
public class MainActivity extends FragmentActivity {

	private CanDumpThread canDumpThread;
	private CanSendThread canSendThread;
	Handler handler = new Handler();
	Boolean reading = false;
	CanParser canParser = new CanParser();
	Runnable runnable = new Runnable() {
		public void run() {
			read_the_can();
		}
	};
	private RelativeLayout rl;
	private MyPagerAdapter mPagerAdapter;
	private RelativeLayout rlimu;
	private RelativeLayout rl2;
	private ViewPager pager;
	private static boolean layoutPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.getBoolean("layout", false)) {
				setContentView(R.layout.viewpager);// Création de la liste de
													// Fragments que fera
													// défiler le PagerAdapter
				List fragments = new Vector();

				// Ajout des Fragments dans la liste
				fragments.add(Fragment.instantiate(this,
						BlocIMU.class.getName()));
				fragments.add(Fragment.instantiate(this,
						BlocGPS.class.getName()));
				fragments.add(Fragment.instantiate(this,
						BlocIHM.class.getName()));
				fragments.add(Fragment.instantiate(this,
						BlocVerin.class.getName()));
				// Création de l'adapter qui s'occupera de l'affichage de la
				// liste de
				// Fragments
				this.mPagerAdapter = new MyPagerAdapter(
						super.getSupportFragmentManager(), fragments);

				pager = (ViewPager) super.findViewById(R.id.viewpager);
				pager.setOffscreenPageLimit(3);
				// Affectation de l'adapter au ViewPager
				pager.setAdapter(this.mPagerAdapter);
				layoutPage = false;

			} else {
				setContentView(R.layout.other_rl);
				layoutPage = true;
			}
		} else {
			setContentView(R.layout.other_rl);
			layoutPage = true;
		}
		canDumpThread = new CanDumpThread();
		reading = false;

		canParser = new CanParser();
		rl = (RelativeLayout) findViewById(R.id.rl_main_activity);

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
			dialog.setTitle("INFO");

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
		if (id == R.id.action_layout) {
			if (layoutPage) {
				Intent intent = getIntent();
				finish();
				intent.putExtra("layout", true);
				startActivity(intent);
				return true;

			}
			Intent intent = getIntent();
			finish();
			intent.putExtra("layout", false);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			canDumpThread.setCmd("su -c /sbin/candump -tz can0");
			canDumpThread.start();
			handler.postDelayed(runnable, 1);
			((Button) findViewById(R.id.button_read_main_activity))
					.setText("STOP");
			reading = true;
			return;
		}
		((Button) findViewById(R.id.button_read_main_activity)).setText("READ");
		reading = false;
		canDumpThread = new CanDumpThread();
		handler.removeCallbacks(runnable);
	}

	/**
	 * Action performed by the CONNECT button. Mount the can interface with a
	 * bitrate of 1000K
	 * 
	 * @param v
	 */
	public void button_connect_clicked(View v) {
		executeCommand("su -c ip link set can0 up type can bitrate 1000000");
	}

	/**
	 * Function call by the runnable, it read the FIFO of CanDumpThread with the
	 * get100Poll function ( which extract 100 values dumped or less if there is
	 * not 100 values ) and parse the data with CanParser to finally call the
	 * action method of the CanFrame class instantiate by the CanParser.
	 */
	private void read_the_can() {

		String poll = canDumpThread.get100Poll();
		if (poll == null) {
			handler.postDelayed(runnable, 1);
			return;
		}
		for (CanFrame canframe : canParser.parseFrames(poll)) {
			canframe.action(rl, pager);
		}

		handler.postDelayed(runnable, 1);
	}

	/**
	 * Execute a command in a shell
	 * 
	 * @param command
	 * 
	 * @return
	 */
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();

	}

	public void button_send_gsm_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.send_sms_dialog);
		dialog.setTitle("Send a sms");

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
	 * Read the unread sms
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
	 * Check if access is granted to the sim card ( response : OK )
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_statut_gsm_clicked(View v) {
		cansend_gsm("AT+CPIN=?\r");
	}

	/**
	 * Display a dialog asking for the PIN of the sim card
	 * 
	 * @param v
	 * 
	 * 
	 */
	public void button_config_pin_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.enter_pin_dialog);
		dialog.setTitle("Pin");

		final EditText hexa1 = (EditText) dialog
				.findViewById(R.id.edittext_numero);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String dataHexa = "";

				if (!hexa1.getText().toString().isEmpty()) {
					dataHexa += hexa1.getText().toString();
				}
				cansend_gsm("AT+CPIN=\"" + dataHexa + "\"\r");
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
		dialog.setTitle("LED");

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
				String txt = "0000"
						+ spinnerDroite.getSelectedItem().toString()
						+ spinner3.getSelectedItem().toString()
						+ spinner2.getSelectedItem().toString()
						+ spinnerGauche.getSelectedItem().toString();

				txt = String.format("%02X", Long.parseLong(txt, 2));
				String txt2 = "0000" + spinnerCDroite.getSelectedItemPosition()
						+ spinnerC3.getSelectedItemPosition()
						+ spinnerC2.getSelectedItemPosition()
						+ spinnerCGauche.getSelectedItemPosition();
				txt2 = String.format("%02X", Long.parseLong(txt2, 2));

				canParser.setGsmcanframe(new GSMCanFrame());
				cansend("382", txt + txt2);
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
	 * 
	 */
	public void button_envoie_affichage_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_commande_ecran);
		dialog.setTitle("Send display command");

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
				if (ecranText.getText().toString().isEmpty() && ecranText2.getText().toString().isEmpty()) {

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
	 * 
	 */
	public void button_envoie_buzzer_clicked(View v) {
		// create a Dialog component
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.envoi_commande);
		dialog.setTitle("Send buzzer command");

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
		dialog.setTitle("Motor command");

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
		dialog.setTitle("Enter the contrast in hexa ( 00 to 64)");

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
		dialog.setTitle("Enter the backlight in hexa ( 00 to 64)");

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
		dialog.setTitle("Req position");

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
	 * 
	 * 
	 */
	public void button_etat_clavier_clicked(View v) {
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.clavier_ihm_dialog);
		dialog.setTitle("Clavier");

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

	/**
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

	}

	/**
	 * @param id
	 * @param command
	 */
	private void cansend(String id, String command) {
		if (canSendThread != null) {
			canSendThread = null;
		}
		canSendThread = new CanSendThread();
		canSendThread.addStringCommand(id, command);
		canSendThread.start();
	}

}
