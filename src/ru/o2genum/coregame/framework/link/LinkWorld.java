package ru.o2genum.coregame.framework.link;

import java.util.*;

import ru.o2genum.coregame.framework.*;
import ru.o2genum.coregame.framework.impl.*;
import ru.o2genum.coregame.framework.link.Utility.GameLevel;
import ru.o2genum.coregame.framework.link.Utility.LinkItemType;
import ru.o2genum.coregame.framework.Pool.PoolObjectFactory;
import ru.o2genum.coregame.framework.Input.KeyEvent;
import ru.o2genum.coregame.framework.Input.TouchEvent;
import ru.o2genum.coregame.game.Core;
import ru.o2genum.coregame.game.Dot;
import ru.o2genum.coregame.game.VectorF;

import android.graphics.Matrix;
import android.graphics.Point;
import android.util.*;

/* I should have used pools for my objects not to make garbage
 * collector angry. As it freezes the game sometimes, 
 * I avoided some object creations. However, it doesn't help.
 */

public class LinkWorld
{
	Random random = new Random();
	Game game;
	private final int DOTS_COUNT = 10;
	// In this case ArrayList is better than LinkedList:
	// list will never be resized.
	public List<LinkItem> linkItems = new ArrayList<LinkItem>(DOTS_COUNT);
	public float offScreenRadius;
	private final float SHIELD_FACTOR = 20.0F;
	private final float ENERGY_FACTOR = 6.0F;

	private float time = 0.0F; // in seconds

	public enum GameState {Ready, Running, Paused, GameOver}

	public GameState state = GameState.Ready;

	private float difficulty = 0.04F; // Max 0.1F

	// Sounds
	// Dot collides with core
	Sound coreHurt;
	Sound coreHealth;
	Sound coreShield;
	// Dot collides with shield
	Sound shieldCollision;

	Sound gameOver;

	public LinkWorld(Game game)
	{
		this.game = game;
		loadSounds();
	}

	private void loadSounds()
	{
		Audio a = game.getAudio();
		coreHurt = a.newSound("core_hurt.wav");
		coreHealth = a.newSound("core_health.wav");
		coreShield = a.newSound("core_shield.wav");
		shieldCollision = a.newSound("shield_collision.wav");
		gameOver = a.newSound("game_over.wav");
	}

	// Restart the game	
	public void renew()
	{
		linkItems.clear();
		time = 0.0F;
		state = GameState.Ready;
		difficulty = 0.04F;
		generateNewDot(GameLevel.Level1);
	}

	private VectorF generateNewDotCoordsInRandomPlace()
	{
		double angle = random.nextDouble() * 2 * Math.PI;
		VectorF coords = new VectorF((float) Math.cos(angle), 
				(float) Math.sin(angle));
		return coords;
	}

	public void update(float deltaTime)
	{
		if(state == GameState.Ready)
			updateReady(deltaTime);
		if(state == GameState.Running)
			updateRunning(deltaTime);
		if(state == GameState.Paused)
			updatePaused(deltaTime);
		if(state == GameState.GameOver)
			updateGameOver(deltaTime);
	}

	private void doInput()
	{
	    float orientAngle = game.getInput().getAzimuth();
		if(game.getInput().isTouchDown())
		{
			double touchX = (double) game.getInput().getTouchX();
			double touchY = (double) game.getInput().getTouchY();
		}
		else
		{
		}
	}

	// Removes accelerometer noise and makes
	// core shield rotate smooth when user touches / untouches
	// the screen (and game switch accelerometer / touchscreen control).
	// Stabilisation increases, as factor value becomes larger.
	private float stabilizeAngle(float real, float current, float factor)
	{
		real = normAngle(real);
		current = normAngle(current);
		// Stabilisation should choose shortest way
		// (is it better to rotate clockwise or counterclockwise?)
		if(current - real > 180F)
			real += 360;
		if(real - current > 180F)
			real -= 360;
		// (current + current + current ... + real) / numberOfElements
		return normAngle((current * factor + real) / (factor + 1F));
	}

	private void updateReady(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}
	
	private boolean checkTouchUp()
	{
		for(TouchEvent event : game.getInput().getTouchEvents())
		{
			if(event.type == TouchEvent.TOUCH_UP)
				return true;
		}
		return false;
	}

	private boolean checkMenuUp()
	{
		for(KeyEvent event : game.getInput().getKeyEvents())
		{
			if(event.keyCode == android.view.KeyEvent.KEYCODE_MENU)
			{
				if(event.type == KeyEvent.KEY_UP)
					return true;
			}

		}
		return false;
	}

	private void updatePaused(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}

	private void updateGameOver(float deltaTime)
	{
		if(checkTouchUp() || checkMenuUp())
			renew();
	}

	private void updateRunning(float deltaTime)
	{
		checkTouchUp(); // Just to clear touch event buffer

		if(checkMenuUp())
			state = GameState.Paused;

		countTime(deltaTime);

		doInput();
		handleCollisions();
		moveDots(deltaTime);
		decreaseShieldEnergy(deltaTime);
	}

	private void decreaseShieldEnergy(float deltaTime)
	{
	}


	private void increaseDifficulty()
	{
		difficulty += 0.00005F;
	}

	private void generateNewDot(GameLevel level)
	{
		int linkItemCount = 0;
		switch(level)
		{
		case Level1:
			linkItemCount = 10;
			break;
		
		}

		linkItems.add(new LinkItem(LinkItemType.T0,new Point(0,0),game.getBitmap().get(0)));
		linkItems.add(new LinkItem(LinkItemType.T0,new Point(0,1),game.getBitmap().get(1)));
		linkItems.add(new LinkItem(LinkItemType.T0,new Point(1,0),game.getBitmap().get(2)));
		linkItems.add(new LinkItem(LinkItemType.T0,new Point(1,1),game.getBitmap().get(3)));

	}

	private void countTime(float deltaTime)
	{
		time += deltaTime;
	}

	public String getTime()
	{
		int seconds = (int) time;
		int minutes = seconds / 60;
		seconds %= 60;
		String result = "";
		if(minutes > 0)
			result += minutes + ":";
		result += String.format("%02d", seconds);
		return result;
	}

	private VectorF generateNewDotAtOffScreenRadius()
	{
		float angle = random.nextFloat() * ((float)(2 * Math.PI));
		VectorF coords =
		   	new VectorF(offScreenRadius * ((float) Math.cos(angle)),
					offScreenRadius * ((float) Math.sin(angle)));
		return coords;
	}
	private void moveDots(float deltaTime)
	{
//		for(Dot dot : dots)
//		{
//			dot.coords.addToThis(dot.speed.x * deltaTime * 100.0F,
//					dot.speed.y * deltaTime * 100.0F);
//		}
	}

	private void handleCollisions()
	{
//		Iterator<Dot> iterator = dots.iterator();
//		while(iterator.hasNext())
//		{
//			handleCollision(iterator.next(), iterator);
//		}
	}


	private float normAngle(float angle)
	{
		float angle2 = angle;
		while(angle2 < 0.0F)
			angle2 += 360.0F;

		while(angle2 > 360.0F)
			angle2 -= 360.0F;

		return angle2;
	}
}
