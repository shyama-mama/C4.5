package algorithm;

import java.util.ArrayList;
import java.util.Collections;

public class KFold {

	private ArrayList<ArrayList<Double>> attributes; 
	private ArrayList<Integer> classes;
	private ArrayList<ArrayList<Integer>> indices;
	private int K;
	private int size;
	private ArrayList<Double> performance;
	private ArrayList<Integer> treeDepth;
	
	public KFold(ArrayList<ArrayList<Double>> attributes, ArrayList<Integer> classes, int K) {
		this.attributes = attributes;
		this.classes = classes;		
		this.K = K;
		this.size = classes.size();
		indices = new ArrayList<ArrayList<Integer>>();
		partitionIndices();
		performance = new ArrayList<Double>();
		treeDepth = new ArrayList<Integer>();

		
	}
	
	private void partitionIndices() {
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		for(int i = 0; i < size; i++)
			indexList.add(i);
		Collections.shuffle(indexList);
		for(int i = 0; i < K; i++)
		{
			ArrayList<Integer> next = new ArrayList<Integer>();
			int end = size/K + (i<size%K?1:0);
			for(int j = 0; j < end; j++)
				next.add(indexList.remove(0));
			this.indices.add(next);
		}	
	}

	public void fitTree(boolean weighted, int minSamples) {
		for(int i = 0; i < K; i++)
		{
			ArrayList<Integer> next = indices.remove(0);			
			TrainData train = createTrainData(next);
			TestData test = createTestData(next);
			Tree tree = new Tree(train, minSamples);
			tree.prune(weighted);
			//tree.printTree();
			treeDepth.add(tree.getTreeDepth());
			performance.add(test.getPerformance(tree));
		}
	}
	
	public void fitTree(boolean weighted) {
		int minSamples = 10;
		System.out.println(minSamples);
		this.fitTree(weighted, minSamples);
	}
	
	public double getDepth(boolean print) {
		double total = 0.;
		for(double p : treeDepth)
			total += p;
		if(print)
		{
			System.out.println(treeDepth);
			System.out.println("Average tree depth: " + total/K);
		}
		return total/K;
	}
	
	public double getPerformance(boolean print) {
		double total = 0;
		for(double p : performance)
			total += p;
		if(print)
		{
			System.out.println(performance);
			System.out.println("Average performance: " + total/K);
		}
		return total/K;
	}


	private TrainData createTrainData(ArrayList<Integer> indices) {
		ArrayList<ArrayList<Double>> attList = new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> classList = new ArrayList<Integer>();
		for(int i = 0; i < size; i++)
		{
			if(!indices.contains(i))
			{
				attList.add(attributes.get(i));
				classList.add(classes.get(i));
			}
		}
		return new TrainData(attList, classList);
	}

	private TestData createTestData(ArrayList<Integer> indices) {
		ArrayList<ArrayList<Double>> attList = new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> classList = new ArrayList<Integer>();
		while(!indices.isEmpty())
		{
			int next = indices.remove(0);
			attList.add(attributes.get(next));
			classList.add(classes.get(next));
		}
		return new TestData(attList, classList);
	}
	
	public int getSize() {
		return this.size;
	}


}
