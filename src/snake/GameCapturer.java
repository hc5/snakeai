package snake;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import javax.swing.JFrame;


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
public class GameCapturer extends JFrame {
	public Board b;
	private JButton jButton1;
	private Rectangle r;
	//private GameContainer gc = new GameContainer();
	private void initGUI() {
		try {

			this.setLocation(800,0);
			this.setSize(381, 157);
			this.setVisible(true);
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1, BorderLayout.CENTER);
				jButton1.setText("Start");
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButton1ActionPerformed(evt);
					}
				});
			}

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public GameCapturer(Board bd){
		b = bd;
		initGUI();
	}

	private void jButton1ActionPerformed(ActionEvent evt) {
		Point p = this.getLocation();
		Dimension d = this.getSize();
		r = new Rectangle(p,d);

		this.setVisible(false);

		try {
			Thread.sleep(2000);
			Thread t = new Thread(){
				public void run(){
					try {
						capture();
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			};
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void capture() throws Exception{
		Point origin = new Point();
		System.out.println("Waiting for game to start");
		b =analyzeCapture(getArea(),origin);
		System.out.println("Got board, game started!");
		ScanInfo s = getBoard(getArea(),origin);
		//gc.setBoard(b);


		Population pop = new Population(6);
		SnakeAI ai = new SnakeAI(b,pop.individuals.get(0).gene);
		System.out.println("created AI, entering game loop");
		b.snake.velocity.move(0, -1);

		for(;;){

			Point best = ai.nextMove();
			// System.out.println(best);
			SendMove(best);
			SendMove(best);
			SendMove(best);
			b.snake.velocity.move(best.x, best.y);
			//	if(gc.newTurn()){
			//	gc.ng.repaint();
			//}
			//else{
			//	ai.fitness=b.snake.length;
			//	pop.individuals.get(0).fitness=ai.fitness;
			//	System.out.println("Fitness: "+ai.fitness);
			//	gc.dispose();
			//	break;
			//}
			for(;;){
				ScanInfo thisFrame = getBoard(getArea(),origin);
				//System.out.println(thisFrame.s);
				if(s.hashCode()!=thisFrame.hashCode()){

					Point head = getHead(s.s,thisFrame.s);
					if(head==null)continue;
					//System.out.println(head);
					LinkedList<Point>body = new LinkedList<Point>(s.s);
					body.retainAll(thisFrame.s);
					body.push(head);
					b.snake.length=body.size();
					b.snake.travelLength=0;
					b.snake.points=body;
					b.food=thisFrame.food;
					ai.food=thisFrame.food;
					//System.out.println("head: "+b.snake.points.peek()+" body: "+b.snake.points.size() );
					//System.out.println("food: "+b.food);
				
					s = thisFrame;
					if(b.snake.points!=null)
					break;
				}
				Thread.sleep(10);
			}
		}
	}

	private Point getHead(HashSet<Point>old,HashSet<Point>newSet){
		for(Point p:newSet)
			if(!old.contains(p))
				return p;
		return null;
	}
	private void SendMove(Point best) throws Exception {
		Robot r = new Robot();
		if(best.x==1&&best.y==0){
			r.keyPress(KeyEvent.VK_RIGHT);
			r.keyRelease(KeyEvent.VK_RIGHT);
		}
		if(best.x==-1&&best.y==0){
			r.keyPress(KeyEvent.VK_LEFT);
			r.keyRelease(KeyEvent.VK_LEFT);
		}
		if(best.x==0&&best.y==1){
			r.keyPress(KeyEvent.VK_DOWN);
			r.keyRelease(KeyEvent.VK_DOWN);
		}
		if(best.x==0&&best.y==-1){
			r.keyPress(KeyEvent.VK_UP);
			r.keyRelease(KeyEvent.VK_UP);
		}
	}

	public BufferedImage getArea() throws Exception{

		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(r);
		return image;
	}
	public int toRGB(int [] color){
		return color[2]+color[1]*0x100+color[0]*0x10000;
	}
	public ScanInfo getBoard(BufferedImage ra,Point origin){
		//System.out.println("getBoard called");
		char [][] board= new char[64][32];
		int x=0,y=0;
		Point food = new Point();
		HashSet<Point> s=new HashSet<Point>();
		for(int i = origin.x+4;i<origin.x+511;i+=8){
			for(int j = origin.y+5;j<origin.y+255;j+=8){
				if(ra.getRGB(i, j)!=0xFFEEEEEE){
					if(ra.getRGB(i, j)==0xFFFF0000){
					//	System.out.println("Food!"+x+","+y);
						food = new Point(x,y);
					}
					if(ra.getRGB(i, j)==0xFF555588){
					// 	System.out.println("snake!"+x+","+y);
						s.add(new Point(x,y));
					}
					
				}
				y++;
			}
			y=0;
			x++;
		}
		return new ScanInfo(s,food);
	}
	public Board analyzeCapture(BufferedImage ra,Point o)throws Exception{
	//	System.out.println(ra.getHeight()+","+ra.getWidth());
		Point origin=null;
		//ImageIO.write(ra, "png", new File("test.png"));
		for(int i =0;i<ra.getHeight();i++){
			for(int j = 0;j<ra.getWidth();j++){
				int  pixel;

				pixel = ra.getRGB(j,i)&0x00FFFFFF;
				if(pixel==0){
					int below;
					try{
						below = ra.getRGB(j,i+1)&0x00FFFFFF;
						if(below==0){
							try{
								int topLeft =(ra.getRGB(j+1,i))&0x00FFFFFF;
								if(topLeft==0){
									origin = new Point(j,i);
									break;
								}
							}catch(Exception e){}
						}
					}catch(Exception e){}
				}

			}
			//if(origin!=null)break;
		}
		System.out.println(origin);
		o.setLocation(origin);
		int snakeCount = 0;
		Point food = null,snake= null;
		while(snakeCount!=1){
			//System.out.println(snakeCount);
			//System.out.println(food);
			snakeCount = 0;
			int x=0,y=0;
			for(int i = origin.x+4;i<origin.x+511;i+=8){
				y=0;
				for(int j = origin.y+5;j<origin.y+255;j+=8){

					if(ra.getRGB(i, j)==0xFFFF0000)
						food = new Point(x,y);
					else if(ra.getRGB(i, j)==0xFF555588)
					{
						if(snakeCount>1)
							break;
						snakeCount++;
						snake = new Point(x,y);
					}
					y++;
				}
				if(snakeCount>1)break;
				x++;
			}
			if(snakeCount!=1){
				ra = getArea();
			}
		}
		Board b = new Board(64,32,food,snake);
		//System.out.println("Started! food = "+food+", snake = "+snake);
		return b;
	}
}
