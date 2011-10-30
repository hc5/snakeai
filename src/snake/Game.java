package snake;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Game extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2097532605413208908L;
	public Board board;
	public ArrayList<Cell>points = new ArrayList<Cell>();
	public int cellSize = 7;
	public Game(Board board){
		super();
		this.board = board;
		
		initGUI();
	}
	
	@Override 
	public void paintComponent(Graphics g) {
        super.paintComponent(g); 
		for(Cell c:points){
        	drawAt(c.p.x,c.p.y,c.c,g);
        }
        for(int i =0;i<board.width;i++){
        	for(int j =0;j<board.height;j++){
        		drawAt(i,j,new Color(255,255,255),g);
        	}
        }
        try{
        LinkedList<Point>pts = board.snake.points;
        for(int i =0;i<pts.size();i++){
        	
        	int hue = 80+100*i/pts.size();
        	if(i==0)
        		drawAt(pts.get(i).x,pts.get(i).y,new Color(240,80,80),g);
        	else
        		drawAt(pts.get(i).x,pts.get(i).y,new Color(hue,hue,hue),g);
        }
        }
        catch(Exception e){}
        display(board.width*cellSize-150,30,"Length: "+board.snake.length, g);
        drawAt(board.food.x,board.food.y,new Color(255,100,100),g);
        
	}
	public void drawAt(int x,int y,Color c,Graphics g){
		
		if(!g.getColor().equals(c))
			g.setColor(c);
		g.fillRect(x*cellSize, y*cellSize, cellSize-1, cellSize-1);
		
	}
	public void display(int x,int y,String s,Graphics g){
		g.setFont(new Font("gill sans",Font.PLAIN,20));
		g.setColor(new Color(30,140,200));
		g.drawString(s, x, y);
		
	}
	private void initGUI() {
		try {
			{
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
