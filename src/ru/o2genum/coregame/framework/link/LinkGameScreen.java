package ru.o2genum.coregame.framework.link;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

import android.graphics.*;
import android.graphics.drawable.*;
import android.util.Log;
import android.content.res.*;
import android.content.*;
import android.app.*;

import ru.o2genum.coregame.framework.*;
import ru.o2genum.coregame.R;

public class LinkGameScreen extends Screen {
    long startTime = System.nanoTime();
	LinkWorld world;

	Paint paint = new Paint();
	RectF rect = new RectF();

	Context r;
        
    public LinkGameScreen(Game game) {
        super(game);
		r = (Context) game;
		world = new LinkWorld(game);	
		world.renew();

		paint.setAntiAlias(true);
		paint.setStrokeWidth(0.0F);
			
		paint.setTextSize(((float)game.getGraphics().getHeight()) / 16F);
		paint.setTextAlign(Paint.Align.CENTER);
	
    }
    
    @Override
    public void update(float deltaTime) {
		world.update(deltaTime);
    }


	private Point selectedPoint ;
	
	@Override
    public void present(float deltaTime) {
    Canvas c = game.getGraphics().getCanvas();    

//	gradient.draw(c);
	c.drawBitmap(game.getBackGroud(), null, new Rect(0,0,c.getWidth(),c.getHeight()), paint);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
	
	paint.setColor(0xff19dbe2);

	paint.setStyle(Paint.Style.STROKE);
	paint.setColor(0xffffffff);
	paint.setStrokeWidth(0.0F);
	paint.setStyle(Paint.Style.FILL_AND_STROKE);
	Iterator<LinkItem> iterator = world.map.values().iterator();
	int bitmapWidth = (c.getWidth() - (Utility._borderEdge * 2 + (Utility._horizontalLinkItemCount - 1) * Utility._edge))/Utility._horizontalLinkItemCount;

	Bitmap selectedBackGround= Bitmap.createScaledBitmap(game.getSelectBackGroud(),  bitmapWidth, bitmapWidth, true);
	while(iterator.hasNext())
	{
		LinkItem linkitem = iterator.next();
		Bitmap bitmap = linkitem.GetBitmap();
		bitmap = Bitmap.createScaledBitmap(bitmap,  bitmapWidth, bitmapWidth, true);
		int x = Utility._borderEdge + Utility._edge + (bitmapWidth + Utility._edge ) * linkitem.GetIndex().x;
		int y = Utility._borderEdge + Utility._edge + (bitmapWidth + Utility._edge ) * linkitem.GetIndex().y;;
		if(linkitem.isSelect)
		{
				c.drawBitmap(selectedBackGround, x,y, paint);
		}
		
		c.drawBitmap(bitmap ,x,y, paint);
	}

	LinkWay linkWay = world.getLinkWay();
	if(linkWay != null)
	{
		linkWay .DrawTheWay(c, paint, bitmapWidth);
	}
	
	if(world.state == LinkWorld.GameState.Running)
		drawMessage(world.getTime(), c);
	else if(world.state == LinkWorld.GameState.Ready)
		drawMessage(r.getString(R.string.ready), c);
	else if(world.state == LinkWorld.GameState.Paused)
		drawMessage(r.getString(R.string.paused), c);
	else if(world.state == LinkWorld.GameState.GameOver)
		drawMessage(r.getString(R.string.game_over)+
				"\n"+
				r.getString(R.string.your_time) +  " " + world.getTime() +
				"\n\n" + r.getString(R.string.game_url), c);
	}

	private void drawMessage(String message, Canvas c)
	{
		float y = paint.getTextSize();
		for(String line: message.split("\n"))
		{
		// Draw black stroke
		paint.setStrokeWidth(2F);
		paint.setColor(0xff000000);
	    paint.setStyle(Paint.Style.STROKE);

		c.drawText(line, c.getWidth()/2F, y, paint);
		// Draw white text
		paint.setStrokeWidth(0.0F);
		paint.setColor(0xffffffff);
	    paint.setStyle(Paint.Style.FILL);

		c.drawText(line, c.getWidth()/2F, y, paint);

		y += paint.getTextSize();
		}
	}

    @Override
    public void pause() {
		world.state = LinkWorld.GameState.Paused;
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }            
}
