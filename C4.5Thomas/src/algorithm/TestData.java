package algorithm;

import java.util.ArrayList;

public class TestData {

	private ArrayList<ArrayList<Double>> attributes;
	private ArrayList<Integer> classes;
	private int size;
	
	public TestData(ArrayList<ArrayList<Double>> attributes, ArrayList<Integer> classes) {
		this.attributes = attributes;
		this.classes = classes;
		this.size = classes.size();
	}
	
	public double getPerformance(Tree tree) {
		int correct = 0;
		for(int i = 0; i < size; i++)
			if(tree.testSample(attributes.get(i)) == classes.get(i))
				correct++;
		return correct/(double)size * 100.;
	}
}
