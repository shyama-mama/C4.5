package algorithm;

import java.util.ArrayList;

public class Tree {

	Node root;
	
	public Tree(TrainData file, int minSamples) {
		this.root = file.getSplitIndex() < 0?new Leaf(file.getSplitValue(), file.getSize()):new InterNode(file, minSamples);
	}
	
	public void printTree() {
		root.print("",true);
	}
	
	public int testSample(ArrayList<Double> sample) {
		return root.testSample(sample);
	}
	
	public void prune(boolean weighted) {
		this.root = root.prune(weighted);
	}
	
	public int getTreeDepth() {
		return root.getDepth(0);
	}
}
