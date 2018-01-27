package algorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainData {
	
	private ArrayList<ArrayList<Double>> attributes;
	private ArrayList<Integer> classes;
	private HashMap<Integer, Integer> classCount;
	
	private double entropy;
	private int splitIndex;
	private double splitValue;
	private int size;
	
	public TrainData(ArrayList<ArrayList<Double>> attributes, ArrayList<Integer> classes) {
		this.attributes = attributes;
		this.classes = classes;
		this.classCount = new HashMap<Integer,Integer>();
		for(int c : classes)
			classCount.put(c, 1 + (classCount.containsKey(c)?classCount.get(c):0));		
		this.entropy = computeEntropy(this.classCount);
		this.size = attributes.size();
		getBestSplit();
	}
	
	public void printData() {
		for(int i = 0; i < size; i++) {
			System.out.println(attributes.get(i) + " " + classes.get(i));
		}
		System.out.println("Entropy = " + entropy);
		System.out.println("Size = " + attributes.size());
	}
	
	private double computeEntropy(HashMap<Integer,Integer> classCounts) {
		int nrOfRecords = 0;
		for(int c: classCounts.values())
			nrOfRecords += c;
		double entropy = 0;
		for(double c: classCounts.values())
			entropy += (-1 * (c/nrOfRecords)) * (Math.log((c/nrOfRecords)) / Math.log(2));
		return entropy;
	}


	private Double computeGAINratio(double value, int index) {
		HashMap<Integer,Integer> below = new HashMap<Integer,Integer>(), above = new HashMap<Integer,Integer>();
		int belowCount = 0, aboveCount = 0;
		for(int i = 0; i < size; i++)
		{
			int c = classes.get(i);
			if(attributes.get(i).get(index) != null)
			{
				if(attributes.get(i).get(index) <= value)
				{
					below.put(c, 1 + (below.containsKey(c)?below.get(c):0));
					belowCount++;
				}
				else 
				{
					above.put(c, 1 + (above.containsKey(c)?above.get(c):0));
					aboveCount++;
				}
			}
		}
		int n = belowCount + aboveCount;
		return (this.entropy - (computeEntropy(below)*belowCount/n + computeEntropy(above)*aboveCount/n))/SPLITinfo(aboveCount,belowCount);
	}

	private double SPLITinfo(int aa, int bb) {
		double a = (double)aa, b = (double)bb;
		return -(a/(a+b)*Math.log(a/(a+b))) - (b/(a+b))*Math.log(b/(a+b));
	}

	private double[] computeAttGain(int index) {
		HashMap<Double,Double> GAINS = new HashMap<Double,Double>();
		double maxGain = 0, maxGainValue = 0;
		for(int i = 0; i < size; i++)
		{
			if(attributes.get(i).get(index) != null)
			{
				double value = attributes.get(i).get(index);
				if(!GAINS.containsKey(value))
				{
					double gain = computeGAINratio(value, index);
					GAINS.put(value, gain);
					if(gain > maxGain)
					{
						maxGain = gain;
						maxGainValue = value;
					}
				}
			}
		}
		double[] result = {maxGainValue, maxGain};
		return result;
	}
	
	private void getBestSplit() {
		double highestGain = Double.MIN_VALUE, splitValue = Double.MIN_VALUE;
		int index = -1;
		for(int i = 0; i < attributes.get(0).size(); i++)
		{
			double[] valueAndGain = computeAttGain(i);
			if(valueAndGain[1] > highestGain)
			{
				highestGain = valueAndGain[1];
				index = i; 
				splitValue = valueAndGain[0];
			}
		}
		this.splitIndex = index;
		this.splitValue = splitValue;
	}

	public double getEntropy() {
		return this.entropy;
	}
	
	public ArrayList<ArrayList<Double>> getAttributeList() {
		return attributes;
	}
	
	public ArrayList<Integer> getClassList() {
		return classes;
	}
	
	public int getSplitIndex() {
		return splitIndex;
	}
	
	public double getSplitValue() {
		return splitValue;
	}
	
	public int getSize() {
		return size;
	}

}
