package org.example.canvasdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static org.example.canvasdemo.MyView.context;

public class MainActivity extends Activity {

	private static String SETTINGS_HIGHSCOREKEY = "highscore";
	private static String SETTINGS_LEVELKEY = "level";
	private Timer myTimer;
	private Timer timeLeft;
	private Timer eTimer;
	private int counter = 0;
	private int timecounter = 0;
	private int level = 1;
	private boolean running = false;
	private String lastClicked = "";
	MyView myView;
	static TextView score;
	static TextView timer;
	static TextView lvl;
	static TextView hscore;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button rightButton = (Button) findViewById(R.id.moveRightButton);
		Button leftButton = (Button) findViewById(R.id.moveLeftButton);
		Button upButton = (Button) findViewById(R.id.moveUpButton);
		Button downButton = (Button) findViewById(R.id.moveDownButton);
		Button resetButton = (Button) findViewById(R.id.resetButton);
		final Button pauseButton = (Button) findViewById(R.id.pauseButton);
		myView = (MyView) findViewById(R.id.gameView);
		myView.setActivty(this);
		score = (TextView) findViewById(R.id.score);
		timer = (TextView) findViewById(R.id.timer);
		lvl = (TextView) findViewById(R.id.level);
		hscore = (TextView) findViewById(R.id.highscore);
		getHighscore(0,0);

		if(savedInstanceState != null){
			myView.coins = savedInstanceState.getParcelableArrayList("savedCoins");
			myView.enemies = savedInstanceState.getParcelableArrayList("savedEnemies");
			timecounter = savedInstanceState.getInt("timecounter");
			level = savedInstanceState.getInt("level");
			myView.coincounter = savedInstanceState.getInt("coincounter");
			timer.setText("Time: "+timecounter);
			updateScore(myView.coincounter);
			myView.newGame = false;
			running = false;
			myView.invalidate();
		}




		/// /listener of our pacman
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGame();
				lastClicked = "right";
				pauseButton.setText("Pause");
			}
		});
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame();
				lastClicked = "left";
				pauseButton.setText("Pause");
			}
		});
		upButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame();
				lastClicked = "up";
				pauseButton.setText("Pause");
			}
		});
		downButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startGame();
				lastClicked = "down";
				pauseButton.setText("Pause");
			}
		});
		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myView.resetGame();
			}
		});
		pauseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(running) {
					pauseGame();
					pauseButton.setText("Continue");
				}
				else{
					startGame();
					pauseButton.setText("Pause");
				}

			}
		});

		//
		timeLeft = new Timer();
		myTimer = new Timer();
		eTimer = new Timer();

      //Timers
		timeLeft.schedule(new TimerTask() {
			@Override
			public void run() {
				leftTimerMethod();
			}
		}, 0, 1000);
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}
		}, 0, 30);
		eTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EnemyTimer();
			}
		}, 0, 1000);
	}
	public void startGame(){
		running = true;
	}
	public void pauseGame(){
		running = false;
	}
	public void endGame(){
		running = false;
		Toast toastelse1 = Toast.makeText(getApplicationContext(), "Spillet er slut, du fik "+myView.coincounter+" points!", Toast.LENGTH_LONG);
		toastelse1.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);
		toastelse1.show();
		getHighscore(myView.coincounter, level);
		myView.resetGame();
	}
	public void resetCounter(){
		counter = 0;
		timecounter = 0;
		level = 1;
		timer.setText("Time: "+timecounter);
		lvl.setText("Level: "+level);
	}
	public void getHighscore(int score, int level){
		SharedPreferences sp = getSharedPreferences("highscore", Activity.MODE_PRIVATE);
		int gotHighscore = sp.getInt("highscore", -1);
		int gotLevel = sp.getInt("level", -1);
		if(score > gotHighscore){
			updateHighscore(score, level);
		}
		else
		hscore.setText("Highscore: "+gotHighscore +" Level: "+gotLevel);
	}
	public void updateHighscore(int highscore, int level){
		SharedPreferences sp = getSharedPreferences("highscore", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("highscore", highscore);
		editor.putInt("level", level);
//		editor.putString("name", name);
		editor.commit();
		hscore.setText("Highscore: "+highscore +" Level: "+level);
	}

	private void TimerMethod() {this.runOnUiThread(Timer_Tick);}
	private void leftTimerMethod() {this.runOnUiThread(leftTimer_Tick);}
	private void EnemyTimer() {this.runOnUiThread(ETimer_Tick);}

	private Runnable leftTimer_Tick = new Runnable() {
		public void run() {
			//This method runs in the same thread as the UI.
			// so we can draw
			if (running)
			{
				timecounter++;
				timer.setText("Time: "+timecounter);
				lvl.setText("Level: "+level);
//				if(timecounter == 0) {
//					endGame();
//				}
				if(timecounter % 30 == 0)
				{
					level++;
					Toast toastelse1 = Toast.makeText(getApplicationContext(), "Du har nået level "+ level, Toast.LENGTH_LONG);
					toastelse1.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);
					toastelse1.show();
					myView.createEnemy();
				}
			}

		}
	};
	private Runnable Timer_Tick = new Runnable() {
		public void run() {
			//This method runs in the same thread as the UI.
			// so we can draw
			if (running)
			{
				counter++;
				//update the counter - notice this is NOT seconds in this example
				//you need TWO counters - one for the time and one for the pacman
				if(lastClicked == "left"){
					myView.moveLeft(10); //move the pacman.
					myView.enemyMove(9); //move the enemies.
				}
				if(lastClicked == "right"){
					myView.moveRight(10); //move the pacman.
					myView.enemyMove(9); //move the enemies.
				}
				if(lastClicked == "up"){
					myView.moveUp(10); //move the pacman.
					myView.enemyMove(9); //move the enemies.
				}
				if(lastClicked == "down") {
					myView.moveDown(10); //move the pacman.
					myView.enemyMove(9); //move the enemies.
				}
			}

		}
	};
	private Runnable ETimer_Tick = new Runnable() {
		public void run() {
			//This method runs in the same thread as the UI.
			// so we can draw
			if (running)
			{
				myView.enemyDirection();
			}

		}
	};

	public static void updateScore(int coincounter) {
		score.setText("Points: "+ coincounter);
	}

//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//		//Here we create a new dialogbuilder;
//		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//		alert.setTitle("Ny highscore!");
//		alert.setMessage("Indtast et navn til nye highscore.");
//		alert.setString("");
//		alert.setPositiveButton("Ok", pListener);
//
//		return alert.create();
//	}

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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//ALWAYS CALL THE SUPER METHOD - To be nice!
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState");

		/* Here we put code now to save the state */
		//outState.putString("savedName", name);
		ArrayList<Goldcoin> coins;
		ArrayList<Enemy> enemies;

		coins = myView.coins;
		enemies = myView.enemies;

		outState.putParcelableArrayList("savedCoins",  coins);
		outState.putParcelableArrayList("savedEnemies", enemies);
		outState.putInt("timecounter", timecounter);
		outState.putInt("coincounter", myView.coincounter);
		outState.putInt("level", level);
	}
	@Override
	protected void onStop() {
		super.onStop();
		//just to make sure if the app is killed, that we stop the timer.
		myTimer.cancel();
		eTimer.cancel();
		timeLeft.cancel();
	}

}
