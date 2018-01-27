package algorithm;

import java.util.ArrayList;

public class InterNode extends Node {
	
	private Node leftChild;
	private Node rightChild;
	private ArrayList<Integer> classes;
	
	private int attribute;
	private double value;
	
	
	public InterNode(TrainData file, int minSamples) {
		
		this.attribute = file.getSplitIndex();
		this.value = file.getSplitValue();
		
		ArrayList<ArrayList<Double>> attributes = file.getAttributeList();
		this.classes = file.getClassList();
		
		ArrayList<ArrayList<Double>> leftAttributes = new ArrayList<ArrayList<Double>>(), rightAttributes = new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> leftClasses = new ArrayList<Integer>(), rightClasses = new ArrayList<Integer>();
		
		for(int i = 0; i < attributes.size(); i++)
		{
			ArrayList<Double> next = attributes.get(i);
			int nextClass = classes.get(i);
			if(next.get(this.attribute) != null)
			{
				if(next.get(this.attribute) <= this.value)
				{
					leftAttributes.add(next);
					leftClasses.add(nextClass);
				}
				else
				{
					rightAttributes.add(next);
					rightClasses.add(nextClass);
				}
			}
		}
		if(leftClasses.size() < minSamples)
			this.leftChild = new Leaf(leftClasses);
		else if(isUnanimous(leftClasses))
			this.leftChild = new Leaf(leftClasses.get(0), leftClasses.size());
		else this.leftChild = new InterNode(new TrainData(leftAttributes, leftClasses), minSamples);
		
		if(rightClasses.size() < minSamples)
			this.rightChild = new Leaf(rightClasses);
		else if(isUnanimous(rightClasses))
			this.rightChild = new Leaf(rightClasses.get(0), rightClasses.size());
		else this.rightChild = new InterNode(new TrainData(rightAttributes, rightClasses), minSamples);
				
	}
	
	private boolean isUnanimous(ArrayList<Integer> classes) {
		int first = classes.get(0);
		for(int c : classes)
			if(c != first)
				return false;
		return true;
	}
	
	public int getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}

	public void print(String prefix, boolean tail) {
		System.out.println(prefix + (tail ? "n'-- " : "y|-- ") + "Attribute " + attribute + " <= " + value + "?" + " error = " + this.getError());
		leftChild.print(prefix + (tail ? "     " : " |   "), false);
		rightChild.print(prefix + (tail ? "     " : " |   "), true);		
	}

	@Override
	public int testSample(ArrayList<Double> sample) {
		return (sample.get(attribute) <= value ? leftChild.testSample(sample) : rightChild.testSample(sample));
	}
	
	protected Node prune(boolean weighted) {
		leftChild = leftChild.prune(weighted);
		rightChild = rightChild.prune(weighted);
		if(leftChild instanceof Leaf && rightChild instanceof Leaf)
		{
			int leftN = ((Leaf)leftChild).getN(), rightN = ((Leaf)rightChild).getN();
			int N = leftN + rightN;
			double errorBefore = weighted?(leftChild.getError()*leftN + rightChild.getError()*rightN)/N:(leftChild.getError() + rightChild.getError())/2;
			double errorAfter = this.getError();
			if(errorAfter <= errorBefore)
				return new Leaf(classes);
			
		}
		return this;	
	}
	
	protected int getDepth(int count) {
		return Math.max(leftChild.getDepth(count+1), rightChild.getDepth(count+1));
	}

	@Override
	public double getError() {
		return new Leaf(classes).getError();
	}
}
