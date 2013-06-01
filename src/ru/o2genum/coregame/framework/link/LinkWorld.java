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

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.util.*;

/* I should have used pools for my objects not to make garbage
 * collector angry. As it freezes the game sometimes, 
 * I avoided some object creations. However, it doesn't help.
 */

public class LinkWorld {
	Random random = new Random();
	Game game;
	private final int DOTS_COUNT = 10;
	// In this case ArrayList is better than LinkedList:
	// list will never be resized.
	public List<LinkItem> linkItems = new ArrayList<LinkItem>(DOTS_COUNT);

	private float time = 0.0F; // in seconds

	public enum GameState {
		Ready, Running, Paused, GameOver
	}

	public GameState state = GameState.Ready;

	// Sounds
	// Dot collides with core
	Sound coreHurt;
	Sound coreHealth;
	Sound coreShield;
	// Dot collides with shield
	Sound shieldCollision;

	Sound gameOver;

	public LinkWorld(Game game) {
		this.game = game;
		loadSounds();
	}

	private void loadSounds() {
		Audio a = game.getAudio();
		coreHurt = a.newSound("core_hurt.wav");
		coreHealth = a.newSound("core_health.wav");
		coreShield = a.newSound("core_shield.wav");
		shieldCollision = a.newSound("shield_collision.wav");
		gameOver = a.newSound("game_over.wav");
	}

	// Restart the game
	public void renew() {
		linkItems.clear();
		time = 0.0F;
		state = GameState.Ready;
		generateNewDot();
	}

	public void update(float deltaTime) {
		if (state == GameState.Ready)
			updateReady(deltaTime);
		if (state == GameState.Running)
			updateRunning(deltaTime);
		if (state == GameState.Paused)
			updatePaused(deltaTime);
		if (state == GameState.GameOver)
			updateGameOver(deltaTime);
	}

	private void doInput() {
		if (game.getInput().isTouchDown()) {
			int touchX = game.getInput().getTouchX();
			int touchY = game.getInput().getTouchY();
		    Canvas c = game.getGraphics().getCanvas();    
			int bitmapWidth = (c.getWidth() - (2 * Utility._edge + (Utility._horizontalLinkItemCount) * Utility._edge))/Utility._horizontalLinkItemCount;

			int x = touchX / (Utility._horizontalLinkItemCount + Utility._edge + bitmapWidth);
			int y = touchY / (Utility._horizontalLinkItemCount + Utility._edge + bitmapWidth);

			Iterator<LinkItem> iterator = linkItems.iterator();
			while (iterator.hasNext()) {
				LinkItem linkitem = iterator.next();
				if(linkitem.GetIndex().x == x && linkitem.GetIndex().y == y)
				{
					linkitem.isSelect = true;
					break;
				}
			}

		} else {
		}
	}
	
	private void updateReady(float deltaTime) {
		if (checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}

	private boolean checkTouchUp() {
		for (TouchEvent event : game.getInput().getTouchEvents()) {
			if (event.type == TouchEvent.TOUCH_UP)
				return true;
		}
		return false;
	}

	private boolean checkMenuUp() {
		for (KeyEvent event : game.getInput().getKeyEvents()) {
			if (event.keyCode == android.view.KeyEvent.KEYCODE_MENU) {
				if (event.type == KeyEvent.KEY_UP)
					return true;
			}

		}
		return false;
	}

	private void updatePaused(float deltaTime) {
		if (checkTouchUp() || checkMenuUp())
			state = GameState.Running;
	}

	private void updateGameOver(float deltaTime) {
		if (checkTouchUp() || checkMenuUp())
			renew();
	}

	private void updateRunning(float deltaTime) {
		checkTouchUp(); // Just to clear touch event buffer

		if (checkMenuUp())
			state = GameState.Paused;

		countTime(deltaTime);

		doInput();
	}
	
	private void generateNewDot() {
		for (int i = 0; i < Utility._horizontalLinkItemCount; i++) {
			for (int j = 0; j < Utility._verticalLinkItemCount; j++) {
				linkItems.add(new LinkItem(LinkItemType.T0, new Point(i, j),
						game.getBitmap().get(0)));
			}
		}
	}

	private void countTime(float deltaTime) {
		time += deltaTime;
	}

	public String getTime() {
		int seconds = (int) time;
		int minutes = seconds / 60;
		seconds %= 60;
		String result = "";
		if (minutes > 0)
			result += minutes + ":";
		result += String.format("%02d", seconds);
		return result;
	}
}
