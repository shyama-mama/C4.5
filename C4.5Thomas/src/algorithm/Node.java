package algorithm;

import java.util.ArrayList;

public abstract class Node {
	
	public abstract void print(String prefix, boolean tail);	
	public abstract int testSample(ArrayList<Double> sample);
	protected abstract Node prune(boolean weighted);
	public abstract double getError();
	protected abstract int getDepth(int count);
}
