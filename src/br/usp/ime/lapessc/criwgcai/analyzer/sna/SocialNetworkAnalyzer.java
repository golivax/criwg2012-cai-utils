package br.usp.ime.lapessc.criwgcai.analyzer.sna;

import br.usp.ime.lapessc.criwgcai.calculators.graph.CorePeripheryCalculator;
import br.usp.ime.lapessc.criwgcai.entity.CorePeripheryReport;
import br.usp.ime.lapessc.criwgcai.util.GraphVisualizer;
import converter.OSSNetworkToJungConverter;
import edu.uci.ics.jung.graph.Graph;

public class SocialNetworkAnalyzer {

	
	public CorePeripheryReport getCorePeripheryReportForCommunicationNetwork(
			String ossNetworkOutput){

		OSSNetworkToJungConverter converter = new OSSNetworkToJungConverter();
		Graph<String,String> graph = converter.convert(ossNetworkOutput);
		
		//Visualize the network
		GraphVisualizer.visualizeGraph(graph);
		
		for(String dev : graph.getVertices()){
			System.out.println("Developer: " + dev);
			System.out.println("Had interactions with: " + 
					graph.getNeighbors(dev));
			System.out.println();
		}
		
		CorePeripheryCalculator corePeripheryCalculator = 
				new CorePeripheryCalculator();
		
		CorePeripheryReport report = 
				corePeripheryCalculator.calculateCorePeriphery(graph);
		
		return report;
	}
	
	public static void main(String[] args) {
		SocialNetworkAnalyzer sna = new SocialNetworkAnalyzer();
		
		CorePeripheryReport report = 
				sna.getCorePeripheryReportForCommunicationNetwork(
				"./resources/TransposeAndTimes.csv");
		
		System.out.println("Core devs: " + report.getCoreArtifacts());
		System.out.println("Peripheral devs: " + report.getPeripheryArtifacts().keySet());
		System.out.println("Undetermined devs: " + report.getUndeterminedArtifacts().keySet());
	}
}