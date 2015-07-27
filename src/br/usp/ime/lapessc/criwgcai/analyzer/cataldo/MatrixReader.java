package br.usp.ime.lapessc.criwgcai.analyzer.cataldo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class MatrixReader {

	
	public MatrixReader(){
		
	}
	
	public Map<String,List<String>> read(String matrixCSV){
		Map<String,List<String>> interactionMap = 
				new HashMap<String, List<String>>();
		
		try {
			CSVReader reader = new CSVReader(new FileReader(matrixCSV), ';');
			List<String[]> matrix = reader.readAll();
			int matrixSize = matrix.size();
					
			Map<Integer,String> devMap = new HashMap<Integer,String>();
			for (int j = 1; j < matrixSize; j++){
				String devName = matrix.get(0)[j];
				devMap.put(j, devName);
			}
						
			for (int i = 1; i < matrixSize; i++){
				String dev = devMap.get(i);
				interactionMap.put(dev, new ArrayList<String>());
				for (int j = 1; j < matrixSize; j++){
					String otherDev = devMap.get(j);
					int value = getValue(i, j, matrix);
					if(!dev.equals(otherDev) && value != 0){
						interactionMap.get(dev).add(otherDev);
					}
				}
			}
			
			//System.out.println("Printing Matrix");
			//for(String dev : interactionMap.keySet()){
			//	System.out.println(dev + ": " + interactionMap.get(dev).size());
			//}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return interactionMap;

	}

	private Integer getValue(int i, int j, List<String[]> matrix){
		Double value = Double.parseDouble(matrix.get(i)[j]);
		return value.intValue();
	}
	
	public static void main(String[] args) {
		CongruenceCalculator congruenceCalculator = new CongruenceCalculator();
		congruenceCalculator.calculate("CoordReq.csv", "Prior.csv");		
	}
	
}