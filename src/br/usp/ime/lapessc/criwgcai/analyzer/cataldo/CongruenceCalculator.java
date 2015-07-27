package br.usp.ime.lapessc.criwgcai.analyzer.cataldo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

public class CongruenceCalculator {

	private Map<String,List<String>> coordReqMap; 
	private Map<String,List<String>> communicMap;
	
	public CongruenceCalculator(){
		
	}
	
	public void calculate(String coordReqCSV, String communicationCSV){
		
		MatrixReader matrixReader = new MatrixReader();
		coordReqMap = matrixReader.read(coordReqCSV);
		communicMap = matrixReader.read(communicationCSV);
		
		System.out.println("Congruence Calculator");
		
		for(String dev : coordReqMap.keySet()){
			
			List<String> neededToTalkTo = coordReqMap.get(dev);
			List<String> talkedTo = communicMap.get(dev);
			
			double congruence = 0;
			
			if (neededToTalkTo != null && talkedTo != null){
			
				Collection<String> intersection = 
					CollectionUtils.intersection(neededToTalkTo, talkedTo);
			
				congruence = (double) intersection.size() / 
						neededToTalkTo.size();
			}
			
			System.out.println(dev + "\t" + congruence);
		}
	}
	
	public static void main(String[] args) {
		CongruenceCalculator congruenceCalculator = new CongruenceCalculator();
		congruenceCalculator.calculate("CoordReqMatrix.csv", "TransposeAndTimes.csv");
	}
	
}
