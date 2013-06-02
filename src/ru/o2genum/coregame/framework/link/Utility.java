package ru.o2genum.coregame.framework.link;

public class Utility {
	public enum LinkItemType {
		T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10
	}

	public enum GameLevel {
		Level1, Level2, Level3
	}

	public enum LinkWayTurn {
		up,down,left,right,none
	}

	public static int _horizontalLinkItemCount = 9;
	public static int _verticalLinkItemCount = 12;
	public static int _edge = 2;

	public static int _borderEdge = 10;
}