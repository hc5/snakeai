package snake;
import java.awt.*;
import java.util.*;
import java.util.List;
public class SnakeAI {
	public Board board;
	public Snake s;
	public Point food;
	public double [] weights ;
	public double fitness;
	public ArrayList<Move> moves;
	private boolean startedCoordination=false;
	public SnakeAI(Board b,double [] w){
		board=b;
		this.s=b.snake;
		food=b.food;
		weights = w;
	}
	public boolean isOpp(Point p1,Point p2){
		if(Math.abs(p1.x-p2.x)==2||Math.abs(p1.y-p2.y)==2){
			return true;
		}
		return false;
	}
	public Point nextMove(){
		if(s.length>400&&s.points.peek().x==0&&!startedCoordination){
			int clearRows = s.length/board.width/2;
			boolean cleared = true;
			for(Point p:s.points){
				if(!p.equals(s.points.peek()))
					if(p.x==0)
						cleared = false;
				if(p.y<=clearRows)
					cleared = false;
			}
			startedCoordination = cleared;
			if(startedCoordination)
			System.out.println("started coordination");
		}
		if(startedCoordination)
			return nextCoordination();
		//System.out.println("New Move");
		Point v = new Point();
		Point head = (Point) s.points.peek().clone();
		Point sHead = s.points.peek();
		Point oldV = s.velocity;
		ArrayList<Point> possibleV = new ArrayList<Point>();
		if(oldV.x!=1)
			possibleV.add((new Point(-1,0)));
		if(oldV.x!=-1)
			possibleV.add(new Point(1,0));
		if(oldV.y!=-1)
			possibleV.add(new Point(0,1));
		if(oldV.y!=1)
			possibleV.add(new Point(0,-1));

		moves = new ArrayList<Move>();
		boolean split = false;
		for(Point p:possibleV){
			double score=0;
			head.move(sHead.x+p.x, sHead.y+p.y);
			if(head.equals(food))
				return p;
			if(head.x==board.width||head.y==board.height||head.x==-1||head.y==-1){
				score = -1;
			}
			for(Point pt:s.points){
				//System.out.println(p+":"+head+":"+pt);
				if(head.x==pt.x&&head.y==pt.y){
					score = -1;

				}
			}
			if(score == -1){
				moves.add(new Move(p,-1));
				if(p.equals(oldV))
					split=true;
				continue;
			}
			int dist = getDistance(head,food);
			double score1 = (board.width+board.height-dist)*weights[0];
			score += score1;
			/*int maxx=-1;
			int maxy=-1;
			int minx=Integer.MAX_VALUE;
			int miny=Integer.MAX_VALUE;
			for(int i =0;i<s.points.size()-1;i++){
				Point cur =s.points.get(i);
				if(cur.x<minx)
					minx = cur.x;
				if(cur.y<miny)
					miny = cur.y;
				if(cur.x>maxx)
					maxx = cur.x;
				if(cur.y>maxy)
					maxy = cur.y;
			}
			if(head.x<minx)
				minx = head.x;
			if(head.y<miny)
				miny = head.y;
			if(head.x>maxx)
				maxx = head.x;
			if(head.y>maxy)
				maxy = head.y;
			double score2 = (maxx-minx)*(maxy-miny)*weights[1];*/
			//score += score2;
			//System.out.println(score2);
			//double score3 = Math.pow(s.length*1.0/countEdge()*500,weights[2]);
			//System.out.println(score3);
			//score+=score3;
			if(head.x==0||head.y==0||head.x==board.width-1||head.y==board.height-1){
				//score-=Math.pow(500,weights[3]);
				score-=weights[3];
				if(score<-1)score = 1;
			}
			if(s.length>400){
				int adjacentCount =0;
				HashSet<Point>ptsHash = new HashSet<Point>(s.points);
				for(Point adjacent:getAdjacent(head)){
					if(ptsHash.contains(adjacent)){
						adjacentCount++;
					}
				}

				if(adjacentCount>1){

					double score4 =weights[4];
					score+=score4;
				}
			}

			//System.out.println(p+":"+score1+":"+dist+" - "+board.food+" - "+sHead+" - "+head); 
			moves.add(new Move(p,score));

		}

		Move best;
		//if(!split){
		//Collections.sort(moves);
		//best = moves.get(0);
		//}
		//else{
		best = getBiggerPartition(moves);

		//}
		return best.direction;

	}
	private Point[] getAdjacent(Point p){
		Point [] pts = new Point[4];
		pts[0]= new Point(p.x-1,p.y);
		pts[1]= new Point(p.x+1,p.y);
		pts[2]= new Point(p.x,p.y-1);
		pts[3]= new Point(p.x,p.y+1);
		return pts;
	} 
	
	private Point nextCoordination() {

		Point head = (Point) s.points.peek().clone();
		if(head.x==0&&head.y!=0)
			return new Point(0,-1);
		else if(head.x==1&&head.y==board.height-1)
			return new Point(-1,0);
		else if(head.x==1&&head.y%2==1&&food.y<head.y){
				return new Point(-1,0);
		}
		else if(head.y%2==0&&head.x!=board.width-1)
			return new Point(1,0);
		else if(head.y%2==1&&head.x!=1)
			return new Point(-1,0);
		else if(head.y%2==0&&head.x==board.width-1)
			return new Point(0,1);
		else if(head.y%2==1&&head.x==1&&head.y!=board.height-1)
			return new Point(0,1);
		
		return null;
	}
	private int getPartitionSize(Move m,LinkedList<Point>snakePts){
		if(m.score==-1)
			return -1;
		Point head = (Point) s.points.peek().clone();
		HashSet<Point>m1Shape = new HashSet<Point>();
		m1Shape.add(new Point(head.x+m.direction.x,head.y+m.direction.y));
		int mSize = 0;
		HashSet<Point>m1clone = new HashSet<Point>(); 
		HashSet<Point>ptsHash = new HashSet<Point>(snakePts);
		while(!m1Shape.isEmpty()){

			for(Point p:m1Shape){
				if(p.x<0||p.y<0||p.x>=board.width||p.y>=board.height)
					continue;
				mSize++;
				//g.points.add(new Cell(p,new Color(150,150+m.direction.x*50,150+m.direction.y*50)));

				if(!ptsHash.contains(new Point(p.x-1,p.y))){
					ptsHash.add(new Point(p.x-1,p.y));
					m1clone.add(new Point(p.x-1,p.y));
				}
				if(!ptsHash.contains(new Point(p.x,p.y-1))){
					ptsHash.add(new Point(p.x,p.y-1));
					m1clone.add(new Point(p.x,p.y-1));
				}
				if(!ptsHash.contains(new Point(p.x,p.y+1))){
					ptsHash.add(new Point(p.x,p.y+1));
					m1clone.add(new Point(p.x,p.y+1));
				}
				if(!ptsHash.contains(new Point(p.x+1,p.y))){
					ptsHash.add(new Point(p.x+1,p.y));
					m1clone.add(new Point(p.x+1,p.y));
				}
			}
			m1Shape = (HashSet<Point>) m1clone.clone();
			m1clone.clear();

		}
		return mSize;
	}
	private int getPartitionCount(ArrayList<Move> moves2,LinkedList<Point>snakePts){
		HashSet<Partition> sizes = new HashSet<Partition>();
		for(int i =0;i<moves2.size();i++){
			if(moves.get(i).score!=-1)
				sizes.add(new Partition(getPartitionSize(moves.get(i),snakePts),moves.get(i)));
		}
		return sizes.size();
	}
	private Move getBiggerPartition(ArrayList<Move> moves2) {
		LinkedList<Point>snakePts = (LinkedList<Point>) s.points.clone();
		LinkedList<Partition> sizes = new LinkedList<Partition>();
		for(int i =0;i<moves2.size();i++){
			if(moves.get(i).score!=-1)
				sizes.add(new Partition(getPartitionSize(moves.get(i),snakePts),moves.get(i)));
		}
		if(sizes.size()==0)
			return moves.get(0);
		Collections.sort(sizes);
		int max = sizes.get(0).size;
		ArrayList<Move>bestMoves = new ArrayList<Move>();
		for(Partition p:sizes){
			if(p.size==max){
				bestMoves.add(p.move);
			}
		}
		Collections.sort(bestMoves);

		return bestMoves.get(0);
	}
	private static class Partition implements Comparable{
		public int size;
		public Move move;
		public Partition(int s,Move m){
			size = s;
			move = m;
		}
		@Override
		public int compareTo(Object arg0) {
			return -size+((Partition)arg0).size;

		}
	}
	private int countEdge() {
		int edgeNum = 0;
		HashSet<Point>pts = new HashSet<Point>(s.points);
		for(Point p:pts){
			if(!(pts.contains(new Point(p.x-1,p.y))&&
					pts.contains(new Point(p.x+1,p.y))&&
					pts.contains(new Point(p.x,p.y-1))&&
					pts.contains(new Point(p.x,p.y+1)))){
				edgeNum++;
			}
		}
		return edgeNum;

	}
	private int getDistance(Point head, Point food2) {
		//char [][] m = generateMap(head,food2);
		//return expand(m,s.points);
		return Math.abs(head.x-food2.x)+Math.abs(head.y-food2.y);
	}
	private char [][] generateMap(){
		char [][] map = new char[board.width][board.height];

		for(int i =0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j]='.';
				if(s.points.contains(new Point(i,j))){
					map[i][j]='*';
				}

			}

		}
		return map;
	}
	private static int countAdjacent (char[] [] m, char c, int x, int y)
	{
		int count = 0;
		try
		{
			if (m [x] [y - 1] == c)
				count++;
		}
		catch (Exception e)
		{
		}
		try
		{
			if (m [x] [y + 1] == c)
				count++;
		}
		catch (Exception e)
		{
		}
		for (int i = x - 1 ; i < x + 2 ; i++)
		{
			int j = y;
			try
			{
				if (m [i] [j] == c && !(i == x && j == y))
				{
					count++;
				}
			}
			catch (Exception e)
			{

			}

		}
		return count;
	}
	public static void printArray(char[][]m){
		System.out.println("-------");
		for(int i =0;i<m.length;i++){
			for(int j =0;j<m[0].length;j++){
				System.out.print(m[i][j]);
			}
			System.out.println();
		}

	}


	static class Move implements Comparable{
		Point direction;
		double score;
		public Move(Point d,double s){
			direction = d;
			score = s;
		}
		@Override
		public int compareTo(Object arg0) {
			return (int) (-this.score*10000+((Move)arg0).score*10000);
		}
		@Override
		public String toString(){
			return "Score = "+score+" "+direction.toString();
		}
	}

}
