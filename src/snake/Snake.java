package snake;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class Snake {
	public LinkedList<Point> points;
	public int length;
	public Point velocity;
	public int travelLength = 0;
	public Snake(Point snake) {
		points = new LinkedList<Point>();
		points.push(snake);
		length = 1;
		velocity = new Point(0,0);
	}
	public int move(Point food){
		travelLength++;
		int eaten = 1;
		Point curHead = points.peek();
		Point newHead = new Point(curHead.x+velocity.x,curHead.y+velocity.y);
		points.push(newHead);
		if(!newHead.equals(food)){
			
			eaten = 0;
		}
		else{
			travelLength = 0;
			length+=5;
		}
		
		
		return eaten;
	}
	
	

}
