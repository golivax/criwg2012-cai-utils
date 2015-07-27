package br.usp.ime.lapessc.criwgcai.calculators.graph;

import java.util.TreeMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import br.usp.ime.lapessc.criwgcai.entity.CorePeripheryReport;
import br.usp.ime.lapessc.criwgcai.util.QuartileAnalysis;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.ClosenessCentrality;
import edu.uci.ics.jung.algorithms.scoring.EigenvectorCentrality;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class CorePeripheryCalculator {

	public CorePeripheryReport calculateCorePeriphery(
			Graph<String,String> graph){
		
		//System.out.println("Vertex count: " + graph.getVertexCount());
		//System.out.println("Edge count: " + graph.getEdgeCount());
		
		EigenvectorCentrality<String, String> evc = 
				new EigenvectorCentrality<String,String>(graph);
		
		/**
		 * Eigenvector centrality is not well-defined on graphs which are not 
		 * strongly	connected.  So by default if you pass a graph in for which 
		 * any vertex has no out-edges--which guarantees that the graph is not 
		 * strongly connected--it will fail just as you described.
		 * 
		 * That said, if you want to ignore this condition, 
		 * call "acceptDisconnectedGraph(true) on the EigenvectorCentrality 
		 * instance as soon as you call it (and before you call evaluate()).
		 * [http://sourceforge.net/mailarchive/message.php?msg_id=20927344]
		 */
		
		evc.acceptDisconnectedGraph(true);

		//Evaluate EVC
		evc.evaluate();
		
		//Do a quartile analysis
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		for(String v : graph.getVertices()){
			Double score = evc.getVertexScore(v); 
			//System.out.println("Score for vertex " + v + ": " + score);
			stats.addValue(score);
		}
		
		System.out.println("Betweenness Centrality analysis");
		BetweennessCentrality<String, String> betweenness = 
				new BetweennessCentrality<String,String>(graph);

		for(String v : graph.getVertices()){
			//System.out.println("Score for vertex " + v + ": " + 
			//		betweenness.getVertexScore(v));
		}
				
		System.out.println("Closeness Centrality analysis");
		ClosenessCentrality<String, String> closeness = 
				new ClosenessCentrality<String,String>(graph);
		for(String v : graph.getVertices()){
			//System.out.println("Score for vertex " + v + ": " + 
			//		closeness.getVertexScore(v));
		}
		
		System.out.println("Degree Centrality");
		for(String v : graph.getVertices()){
			//System.out.println("Score for vertex " + v + ": " + 
			//		graph.getNeighborCount(v));
		}
		
		QuartileAnalysis quartileAnalysis = new QuartileAnalysis(stats);
		stats.clear();
		
		CorePeripheryReport report = new CorePeripheryReport(
				new TreeMap<String, Double>(), new TreeMap<String, Double>(),
				new TreeMap<String, Double>());
		
		for(String v : graph.getVertices()){
			Double score = evc.getVertexScore(v); 
			if (score >= quartileAnalysis.getQ3()){
				report.addCoreArtifact(v, score);
			}
			else if (score <= quartileAnalysis.getQ1()){
				report.addPeripheryArtifact(v, score);
			}
			else{
				report.addUndeterminedArtifact(v, score);
			}
		}
		
		System.out.println("Vertex count: " + graph.getVertexCount());
		System.out.println("Vertices in core: " + report.getCoreArtifacts().keySet().size());
		System.out.println("Vertices in periphery: " + report.getPeripheryArtifacts().keySet().size());
		System.out.println("Vertices in undetermined: " + report.getUndeterminedArtifacts().keySet().size());
	
		return report;
	}
	
	public static void main(String[] args) {
		DirectedGraph<String, String> graph = 
				new DirectedSparseGraph<String, String>();
		
		graph.addEdge("1", "v2", "v1");
		graph.addEdge("2", "v3", "v1");
		graph.addEdge("3", "v4", "v2");
		graph.addEdge("4", "v5", "v2");
		graph.addEdge("5", "v6", "v2");
		
		
		CorePeripheryCalculator calc = new CorePeripheryCalculator();
		calc.calculateCorePeriphery(graph);
	}
	
}
