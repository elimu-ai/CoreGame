package ru.o2genum.coregame.link;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class LinkWay {
	private List<Point> _pointsOnWay;
	
	
	public LinkWay()
	{
		_pointsOnWay =  new ArrayList<Point>();
	}

	public List<Point> getWayPoint()
	{
		return _pointsOnWay;
	}
	
	public int getWaySize()
	{
		return _pointsOnWay.size();
	}
	
	public void AddPointToEnd(Point point) {
		_pointsOnWay.add(point);
	}
	
	
	public void RemovePointFromEnd()
	{
		_pointsOnWay.remove(_pointsOnWay.size() - 1);
	}
	
	public void DrawTheWay(Canvas c,Paint paint, int bitmapWidth, Bitmap pointBmp)
	{
//		if(_pointsOnWay.size()>1)
//		{
//			for(int i = 0;i< _pointsOnWay.size();i++)
//			{
//				Point point = _pointsOnWay.get(i);
//				int x = Utility._borderEdge + Utility._edge + (bitmapWidth + Utility._edge ) * point.x;
//				int y = Utility._borderEdge + Utility._edge + (bitmapWidth + Utility._edge ) * point.y;;
//				c.drawBitmap(pointBmp, x,y, paint);
//				
//			}
//		}
	}
}
