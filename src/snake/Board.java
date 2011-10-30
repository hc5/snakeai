package snake;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Board {
  public char [][] matrix;
  public Point food;
  public int width;
  public int height;
  public boolean turnOver = false;
  public Snake snake;
  public Board(int width,int height,Point food,Point snake){
	  matrix = new char[width][height];
	  this.width=width;
	  this.height=height;
	  this.food = food;
	  this.snake = new Snake(snake);
  }
  public boolean checkLost(){
	  Point head = snake.points.peek();
	  if(snake.travelLength/snake.length>11100)
		  return true;
	  if(head.x==width||head.y==height||head.x==-1||head.y==-1)
		  return true;
	  HashSet<Point>set = new HashSet<Point>();
	  for(Point p:snake.points){
		  set.add(p);
	  }
	  if(set.size()!=snake.points.size())
		  return true;
	  return false;
  }
  public void reset(){
	  matrix = new char[width][height];
	  this.food = new Point((int)(Math.random()*width),(int)(Math.random()*height));
	  Point snake = new Point((int)(Math.random()*width),(int)(Math.random()*height));
	  while(snake.equals(food))
		  snake = new Point((int)(Math.random()*width),(int)(Math.random()*height));
	  this.snake = new Snake(snake);
  }
  public void newFood(){
	  LinkedList<Point> invalid = snake.points;
	  int total = matrix.length*matrix[0].length-invalid.size();
	  int newFood = (int) (total*Math.random());
	  int counter = 0;
	  for(int i=0;i<matrix.length;i++){
		  for(int j =0;j<matrix[0].length;j++){
			  boolean inv = false;
			  for(Point p:invalid)
				  if(p.equals(new Point(i,j))){
					  inv = true;
				  }
			  if(inv)
				  continue;
			  if (counter==newFood){
				  food.move(i, j);
			  }
			  counter++;
		  }
	  }
  }
}
