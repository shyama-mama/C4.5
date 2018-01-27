package algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		double startTime = System.currentTimeMillis();
		
		boolean useWeightedAverage = false;
		KFold dataset = readFromFile(new File("letters.data"), 0, 16, 0, 10);
		dataset.fitTree(useWeightedAverage, 10);
		dataset.getPerformance(true);
		dataset.getDepth(true);
		
		
/*
		HashMap<Double, ArrayList<Double>> weightedAverage = new HashMap<Double, ArrayList<Double>>();
		HashMap<Double, ArrayList<Double>> unweightedAverage = new HashMap<Double, ArrayList<Double>>();
		int[] ks = {2,5,10};
		int[] minSamples = {10,20,50,100};
		boolean[] weights = {true, false};
		int runs = 1;
		int totalRuns = ks.length * minSamples.length * weights.length;
		int start = 0;
		for(boolean weight:weights){
			System.out.println(weight?"weighted":"unweighted"  + " average:");
			for(int min:minSamples){
				System.out.println("  minSamples = " + min);
				ArrayList<Double> kErrors = new ArrayList<Double>();
				for(int k:ks){
					System.out.println("    K = " + k);
					ArrayList<Double> currErrors = new ArrayList<Double>();
					ArrayList<Double> treeDepths = new ArrayList<Double>();
					for(int i = 0; i < runs; i++){
						//KFold irisSplit = readFromFile(new File("iris.data"), 0, 4, 4, k);
						KFold irisSplit = readFromFile(new File("letters.data"), 0, 16, 0, k);
						irisSplit.fitTree(weight, min);
						currErrors.add(irisSplit.getPerformance(false));
						treeDepths.add(irisSplit.getDepth(false));
					}
					//System.out.println("      Errors:" + currErrors);
					System.out.println("      Tree Depths:" + treeDepths);
					double total = 0.0;
					for(int i = 0; i < runs; i++){
						total += currErrors.get(i);
					}
					kErrors.add(total/runs);
					start++;
					//System.out.println((start/(double)totalRuns * 100) + "% done");
				}
			}
		}
			*/	
		
		
		
				/*
				if(weight){
					weightedAverage.put(min, kErrors);
				} else {
					unweightedAverage.put(min, kErrors);
				}
			}	
		}
		
		System.out.println("Weighted Average");
		for(double key:weightedAverage.keySet()){
			System.out.println("The minSamples is " + key);
			System.out.println("Averages are ");
			System.out.println(weightedAverage.get(key));
		}
		System.out.println();
		System.out.println("Unweighted Average");
		for(double key:unweightedAverage.keySet()){
			System.out.println("The minSamples is " + key);
			System.out.println("Averages are ");
			System.out.println(unweightedAverage.get(key));
		}
		*/
		System.out.println("Runtime: " + (System.currentTimeMillis() - startTime) + " ms");
	}
	
	public static KFold readFromFile(File dataset, int start, int nrOfAttributes, int classIndex, int K) {
		ArrayList<ArrayList<Double>> attributes = new ArrayList<ArrayList<Double>>();
		ArrayList<Integer> classes = new ArrayList<Integer>();
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		int end = start + nrOfAttributes - 1;
		
		Scanner scanner;
		try {
			scanner = new Scanner(dataset);
			String line;
			String[] sepLine;
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				sepLine = line.split(",");
				if(sepLine.length > 1) {
					attributes.add(parseAttributes(sepLine, start, end, classIndex));
					classes.add(parseClass(sepLine[classIndex], map));
				}
			}
		scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("That file does not exist:" + e);
		}
		return new KFold(attributes, classes, K);
	}
	
	private static ArrayList<Double> parseAttributes(String[] sepLine, int start, int end, int classIndex) {
		ArrayList<Double> list = new ArrayList<Double>();
		for(int i = start; i <= end; i++) {
			if(i!=classIndex)
			{
				Double next;
				try {
					next = Double.parseDouble(sepLine[i]);
				}
				catch(NumberFormatException e) {
					next = null;
				}
				list.add(next);
			}
		}
		return list;	
	}

	private static int parseClass(String classString, HashMap<String,Integer> map) {
		int classification;
		try {
			classification = Integer.parseInt(classString);
		}
		catch (NumberFormatException e) {
			if(map.containsKey(classString))
				classification = map.get(classString);
			else {
				classification = map.size() + 1;
				map.put(classString, classification);
			}
		}
		return classification;		
	}
}
