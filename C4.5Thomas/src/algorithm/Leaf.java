package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class Leaf extends Node{

	private int majorClass;
	private int majorN;
	private int N;
	private HashMap<Integer,Integer> classmap = new HashMap<Integer,Integer>();
	
	public Leaf(ArrayList<Integer> classes) {
		for(int c : classes)
			classmap.put(c, 1 + (classmap.containsKey(c)?classmap.get(c):0));
		this.majorN = Integer.MIN_VALUE;
		for(int key : classmap.keySet())
		{
			if(classmap.get(key) > majorN)
			{
				this.majorN = classmap.get(key);
				this.majorClass = key;
			}
		}	
		this.N = classes.size();
	}
	
	public Leaf(int classification, int N) {
		classmap.put(classification, N);
		this.majorClass = classification;
		this.majorN = N;
		this.N = N;
	}
	
	public Leaf(double classification, int N) {
		this((int) classification, N);
	}
	
	public int getClassification() {
		return majorClass;
	}

	public void print(String prefix, boolean tail) {
		System.out.println(prefix + (tail ? "n'-- " : "y|-- ") + "Class " + majorClass + " N=" + N + " #=" + majorN + " error = " + this.getError());
		
	}

	@Override
	public int testSample(ArrayList<Double> sample) {
		return majorClass;
	}
	
	public int getN() {
		return N;
	}
	
	public double getError() {
		// z is determined by confidence interval (default for C4.5 is 25%??)
		double z = .32; // for 25% confidence interval
		double f = 1. - (majorN-.5)/(double)N;
		double p = f + z * Math.sqrt(f * (1 - f)/N);
		return p;
	}

	@Override
	protected Node prune(boolean weighted) {
		return this;
	}

	@Override
	protected int getDepth(int count) {
		
		return count+1;
	}

}
