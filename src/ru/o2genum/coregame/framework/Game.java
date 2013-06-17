package ru.o2genum.coregame.framework;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;

public interface Game
{
	public Input getInput();

	public Vibration getVibration();

	public Audio getAudio();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();
	
	public List<Bitmap> getBitmap();
	
	public int getBackGroud();
	
	public Bitmap getSelectBackGroud();
}
