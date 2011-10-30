package snake;

import java.awt.Color;
import java.awt.Point;

public class Cell {
	public Point p;
	public Cell(Point p, Color c) {
		super();
		this.p = p;
		this.c = c;
	}
	public Color c;
}
