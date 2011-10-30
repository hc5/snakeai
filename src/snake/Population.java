package snake;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.io.*;
public class Population {
	LinkedList<Genotype> individuals = new LinkedList<Genotype>();
	public Population(int n){
		for(int i =0;i<n;i++)
			individuals.add(new Genotype(new double[]{
			Math.random()*100,
			Math.random()*100
			,Math.random()*100,
			Math.random()*100,
			Math.random()*100}));
	}
	public void newGen(){
		Collections.sort(individuals);
		try {
			saveGenes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int start = individuals.size()/2;
		ArrayList<Genotype> newGen = new ArrayList<Genotype>();
		for(int i=0;i<start;i+=2){
			newGen.addAll(crossOver(individuals.get(i),individuals.get(i+1)));
		}
		while(individuals.size()>start)
			individuals.pollLast();
		individuals.addAll(newGen);
		System.out.println(individuals.size());
	}
	private void saveGenes() throws IOException{
		File f = new File("genes.txt");
		PrintWriter out = new PrintWriter(new FileWriter(f));
		for(Genotype g:individuals){
			for(double d:g.gene){
				out.printf("%f ", d);
			}
			out.println();
		}
		out.close();
	}
	private ArrayList<Genotype> crossOver(Genotype g1,
			Genotype g2) {
		System.out.println("Crossing over: "+g1+" and \n"+g2);
		ArrayList<Genotype>children = new ArrayList<Genotype>();
		for(int j = 0;j<2;j++){
			double [] father = g1.gene;
			double [] mother = g2.gene;
			double [] child = new double[father.length];
			for(int i = 0 ;i<father.length;i++){
				child[i]=father[i];
				if(Math.random()>0.5){
					child[i]=mother[i];
				}
				if(Math.random()>0.5){
					child[i]+=(Math.random()-0.5)*10;
				}
				if(Math.random()>0.9){
					child[i]*=3;
				}
				if(Math.random()<0.1){
					child[i]/=3;
				}
			}
			children.add(new Genotype(child));
		}
		return children;
	}
	public void stats() {
		System.out.println("Crossing over");
		double d = 0;
		System.out.println(individuals.size());
		for(Genotype g:individuals){
			d+=g.fitness;
			System.out.println(g.fitness+",");
		}
		System.out.println("Average fitness: "+(d/individuals.size()));
		System.out.println("Max fitness: "+individuals.get(0).fitness);
		
	}
}
