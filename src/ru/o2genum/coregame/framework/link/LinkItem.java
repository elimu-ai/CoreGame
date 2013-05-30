package ru.o2genum.coregame.framework.link;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import ru.o2genum.coregame.framework.link.Utility.LinkItemType;

public class LinkItem {

	private LinkItemType type = LinkItemType.T0;
	
	private int index = -1;
	
	private Bitmap bitmap;

	private PointF pointF = new PointF(100,100);
	
	public LinkItem(LinkItemType type, int index, Bitmap bitmap)
	{
		this.type = type;
		this.index = index;
		this.bitmap = bitmap;
		
	}
	
	public LinkItemType GetLinkItemType()
	{
		return type;
	}
	
	public int GetIndex()
	{
		return this.index;
	}
	
	public void SetPointF(PointF pointF)
	{
		this.pointF = pointF;
	}
	
	public PointF GetPointF()
	{
		return this.pointF;
	}
	
	public Bitmap GetBitmap()
	{
		return bitmap;
	}
}
