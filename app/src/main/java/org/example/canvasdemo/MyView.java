package org.example.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class MyView extends View{

    MainActivity activity;
    public void setActivty(MainActivity activity){
        this.activity = activity;
    }
	
	Bitmap pacman = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);
	Bitmap coinpic = BitmapFactory.decodeResource(getResources(), R.drawable.coin);
    Bitmap enemypic = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
    //The coordinates for our dear pacman: (0,0) is the top-left corner

    static Context context;
	int pacx = 50;
    int pacy = 400;
    int h,w; //used for storing our height and width
    int coincounter = 0;
    int enemycounter = 0;
    boolean newGame = true;
    Random rand = new Random();


    ArrayList<Goldcoin> coins = new ArrayList<Goldcoin>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public void createCoin(){
        coins.add(coincounter, new Goldcoin(true, 1, 1));
        coincounter++;
        MainActivity.updateScore(coincounter);
        coins.add(coincounter, new Goldcoin(false, rand.nextInt(w-coinpic.getWidth()), rand.nextInt(h-coinpic.getHeight())));
        invalidate();
    }
    public void createEnemy(){
        // enemies.add(enemycounter, new Enemy(false, 1, 1, 0));
        enemycounter++;
        enemies.add( new Enemy(true, rand.nextInt(w-enemypic.getWidth()), rand.nextInt(h-enemypic.getHeight()), 0));
        invalidate();
    }
    public boolean colDetect(){
        if(coins != null){
        double xdist = pow((coins.get(coincounter).posx - pacx), 2);
        double ydist = pow((coins.get(coincounter).posy - pacy), 2);
        double yx = sqrt(xdist + ydist);
        if(yx <100){
            return true;
        }
        }
        return false;
    }
    public boolean enemyColDetect(){
        if(enemies != null) {
            for(Enemy enemy : enemies) {
            double exdist = pow((enemy.posx - pacx), 2);
            double eydist = pow((enemy.posy - pacy), 2);
            double eyx = sqrt(exdist + eydist);
            if (eyx < 100) {
                return true;
            }
        }
        }
        return false;
    }
    public void enemyColDirection(int x){
        for(Enemy enemy : enemies) {
            if (enemy.posx + x + enemypic.getWidth() < w) {
                enemy.direction = 2;
            }
            else if (enemy.posx-x > 0) {
                enemy.direction = 1;
            }
            else if (enemy.posy-x > 0) {
                enemy.direction = 4;
            }
            else if (enemy.posy+x+ enemypic.getHeight() < h) {
                enemy.direction = 3;
            }
        }
        // enemies.add( new Enemy(true, rand.nextInt(1000), rand.nextInt(1000), rand.nextInt(4)+1));
    }
    public void enemyDirection(){
        for(Enemy enemy : enemies) {
        enemy.direction = rand.nextInt(4)+1;
        }
    }
    public void resetGame(){
        coins.clear();
        enemies.clear();
        coincounter = 0;
        enemycounter = 0;
        newGame = true;
        pacx = 50;
        pacy = 400;
        activity.pauseGame();
        activity.resetCounter();
        activity.updateScore(coincounter);
        invalidate();
    }

    public void moveRight(int x)
    {
    	//still within our boundaries?
    	if (pacx+x+pacman.getWidth()<w)
    		pacx=pacx+x;
        if(enemyColDetect()){
            activity.endGame();
        }
        else if(colDetect()){
            createCoin();
        }
    	invalidate(); //redraw everything - this ensures onDraw() is called.
    }
	public void moveLeft(int x)
	{
		//still within our boundaries?
		if (pacx-x>0)
			pacx=pacx-x;
        if(enemyColDetect()){
            activity.endGame();
        }
        else if(colDetect()){
            createCoin();
        }
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}
	public void moveUp(int x)
	{
		//still within our boundaries?
		if (pacy-x>0)
			pacy=pacy-x;
        if(enemyColDetect()){
            activity.endGame();
        }
        else if(colDetect()){
            createCoin();
        }
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}
	public void moveDown(int x)
	{
		//still within our boundaries?
		if (pacy+x+pacman.getHeight()<h)
			pacy=pacy+x;
        if(enemyColDetect()){
            activity.endGame();
        }
        else if(colDetect()){
            createCoin();
        }
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}
    public void enemyMove(int x)
    {
        if(enemies != null)
        for(Enemy enemy : enemies) {
                //still within our boundaries?
                if ((enemy.direction == 1) && (enemy.posx + x + enemypic.getWidth() < w)) {
                    enemy.posx = enemy.posx + x;
                }
                 if ((enemy.direction == 2) && (enemy.posx - x > 0)) {
                    enemy.posx = enemy.posx - x;
                }
                 if ((enemy.direction == 3) && (enemy.posy - x > 0)) {
                    enemy.posy = enemy.posy - x;
                }
                 if ((enemy.direction == 4) && (enemy.posy + x + enemypic.getHeight() < h)) {
                    enemy.posy = enemy.posy + x;
                }
                // enemyColDirection(x);
                invalidate(); //redraw everything - this ensures onDraw() is called.
        }
    }

	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public MyView(Context context) {
		super(context);
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		//Here we get the height and weight
        h = canvas.getHeight();
        w = canvas.getWidth();

		//Making a new paint object
		Paint paint = new Paint();
        if(newGame){
            coins.add( new Goldcoin(false, rand.nextInt(w-50), rand.nextInt(h-50)));
            enemies.add( new Enemy(true, rand.nextInt(500), rand.nextInt(500), 0));
            newGame = false;
        }
        for(Goldcoin coin : coins){
            if(coin.taken == false)
            canvas.drawBitmap(coinpic, coins.get(coincounter).posx, coins.get(coincounter).posy, paint);
        }
        for(Enemy enemy : enemies){
            if(enemy.alive)
                canvas.drawBitmap(enemypic, enemy.posx, enemy.posy, paint);
        }

		canvas.drawBitmap(pacman, pacx, pacy, paint);
		super.onDraw(canvas);

	}

}
