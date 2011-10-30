package snake;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

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
public class GameContainer extends JFrame{

	public Board board;
	Game ng;
	public GameContainer(){
		super("Snakuuuuuuuuu");
		this.board=board;
		this.setLayout(null);
		this.setSize(600,300);
		this.setUndecorated(true);
		this.setVisible(true);
		//this.setAlwaysOnTop(true);
	}
	public void setBoard(Board b){
		board = b;
		ng = new Game(board);

		this.setContentPane(ng);
		this.setSize(board.width*ng.cellSize,board.height*ng.cellSize);
	}
	public boolean newTurn(){
		board.turnOver= false;
		if(board.snake.move(board.food)==1){

			board.newFood();

		}else{
			if(board.checkLost()){
				return false;
			}
			if(board.snake.length<board.snake.points.size())
				board.snake.points.pollLast();
		}
		//ng.repaint();
		return true;
	}
	
	public static void main(String args[]) throws InterruptedException{
		
		GameCapturer gc = new GameCapturer(null);
		
		
		//uncomment to play locally
		/*GameContainer gc = new GameContainer();
		int popSize = 20;
		Population pop = new Population(popSize);
		int counter = 0;
		Board b = new Board(64, 32, new Point(10,31), new Point(30,30));
		gc.setBoard(b);
		while(true){
			b.reset();
		
			//
			if(counter%popSize==0&&counter>0){
				pop.stats();
				pop.newGen();
				
			}
			SnakeAI ai = new SnakeAI(b,pop.individuals.get(counter%popSize).gene);
			b.snake.velocity.move(0, 1);
			System.out.println(pop.individuals.get(counter%popSize));
			for(;;){
				Point best = ai.nextMove();
				b.snake.velocity.move(best.x, best.y);
				if(gc.newTurn()){
					//Thread.sleep(20);
					gc.ng.repaint();
				}
				else{
					ai.fitness=b.snake.length;
					pop.individuals.get(counter%popSize).fitness=ai.fitness;
					System.out.println("Fitness: "+ai.fitness);
					//gc.dispose();
					break;
					
				}
			}
			counter++;
			
		}*/
	}
	
	private void thisKeyTyped(KeyEvent evt) {
		if(board.turnOver)
			return;
		switch(evt.getKeyCode()){
		case 37:
			if(!board.snake.velocity.equals(new Point(1,0))){
				board.snake.velocity.move(-1,0);
				board.turnOver=true;
			}
			break;
		case 38:
			if(!board.snake.velocity.equals(new Point(0,1))){
				board.snake.velocity.move(0,-1);
				board.turnOver=true;
			}
			break;
		case 39:
			if(!board.snake.velocity.equals(new Point(-1,0))){
				board.snake.velocity.move(1,0);
				board.turnOver=true;
			}
			break;
		case 40:
			if(!board.snake.velocity.equals(new Point(0,-1))){
				board.snake.velocity.move(0,1);
				board.turnOver=true;
			}
			break;
		default:
			break;
		}
	}
}
