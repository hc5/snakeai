package snake;

public class Genotype implements Comparable{
	public double[] gene;
	public double fitness;
	public Genotype(double[] gene) {
		super();
		this.gene = gene;
	}
	@Override
	public int compareTo(Object arg0) {
		return (int)(-fitness*1000+((Genotype)arg0).fitness*1000);
	
	}
	@Override
	public String toString(){
		String temp = "Genes: ";
		for(double d:gene){
			temp+=d+"  ";
		}
		return temp;
	}
	
}
