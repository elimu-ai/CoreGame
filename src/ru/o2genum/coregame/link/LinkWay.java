package ru.o2genum.coregame.link;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
	
	public void DrawTheWay(Canvas c,Paint paint, int linkItemWidth)
	{
		if(_pointsOnWay.size()>1)
		{
			Point start = _pointsOnWay.get(0);
			for(int i = 1;i< _pointsOnWay.size();i++)
			{
				Point point = _pointsOnWay.get(i);
				c.drawLine(start.x * linkItemWidth + linkItemWidth / 2, 
						start.y * linkItemWidth + linkItemWidth / 2, 
						point.x * linkItemWidth +linkItemWidth / 2, 
						point.y * linkItemWidth + linkItemWidth / 2, paint);
				start = point;
			}
		}
	}
}
