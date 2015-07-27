package br.usp.ime.lapessc.criwgcai.calculators.commit;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import au.com.bytecode.opencsv.CSVReader;
import br.usp.ime.jdx.app.JDX;
import br.usp.ime.jdx.entity.relationship.dependency.DependencyReport;
import br.usp.ime.jdx.filter.JavaAPIMatcher;
import br.usp.ime.lapessc.criwgcai.calculators.graph.CorePeripheryCalculator;
import br.usp.ime.lapessc.criwgcai.entity.Commit;
import br.usp.ime.lapessc.criwgcai.entity.CorePeripheryReport;
import converter.JDXToJungConverter;
import edu.uci.ics.jung.graph.Graph;

//Everything is hardcoded for Ant (including the location of the source code
//in the hard-drive)

/**
 * Calcula o coreness de cada commit
 * 
 * @param commitsFile Caminho para arquivo CSV com a lista de commits
 * (e respectivos arquivos) do per�odo analisado
 * 
 * @return uma lista de commits com o respectivo coreness calculado
 */
public class CommitCorenessCalculator {

	public List<Commit> calculate(String commitsFile){
			
		List<Commit> commits = new ArrayList<Commit>();
				
		try{
			CSVReader reader = new CSVReader(new FileReader(commitsFile), '	');
			
			System.out.print("Reading CSV...");
			List<String[]> lines = reader.readAll();	
			System.out.print("done");
			
			System.out.println();
			System.out.print("Building commit list...");
			
			commits = buildCommitList(lines);
				
			System.out.print("done");
			System.out.println();
				
			System.out.println(commits.size() + " commits found");
			System.out.println();
				
			System.out.println("Now let's process the commits");
			System.out.println();
			
			int commitIndex = 1;
				
			for (Commit commit : commits){
					
				System.out.println("Processing commit: " + 
						commitIndex + "(" + commit.getId() + ")");
				
				commitIndex++;
								
				System.out.print("Checking out code from svn...");
				
				String commitID = commit.getId();
				
				//Checkout code from SVN at specified revision (commit id) 
				checkoutCodeFromSVN(commitID);
				
				System.out.print("done");
				System.out.println();
				
				System.out.print("Building the call graph...");
				
				//Calculate the (java) call graph for the checked-out code
				Graph<String,String> callGraph = calculateCallGraph();
								
				System.out.print("done");
				System.out.println();
			
				System.out.print("Calculating eigenvector centrality...");
				
				//Generate the core-periphery report for the call-graph		
				CorePeripheryCalculator corePeripheryCalculator = 
						new CorePeripheryCalculator();
				
				CorePeripheryReport report = 
						corePeripheryCalculator.calculateCorePeriphery(
								callGraph); 
				
				System.out.print("done");
				System.out.println();
				
				System.out.print("Determining commit coreness...");
				
				//Determines commit coreness
				double commitCoreness = determineCommitCoreness(commit, report);
				commit.setCoreness(commitCoreness);
											
				System.out.print("done");
				System.out.println();
				
				System.out.println("Commit coreness is: " + commitCoreness);
				System.out.println();
			}		
			
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return commits;
	}

	private double determineCommitCoreness(Commit commit,
			CorePeripheryReport report) {
				
		int commitSize = commit.getModifiedFiles().size();
		int filesInCore = 0;
			
		for (String file : commit.getModifiedFiles()){
				
			//FIXME: Acertar essa meleca
			String classPath = file.replace("/ant/core/trunk","");
			classPath = classPath.replace('/', '\\');
			classPath = "C:\\tmp\\ant" + classPath;
			
			if (report.getCoreArtifacts().containsKey(classPath)){
				filesInCore++;
			}
			
			else if (!report.getPeripheryArtifacts().containsKey(classPath) &&
					!report.getUndeterminedArtifacts().containsKey(classPath)){
				
				System.out.println("INFO: file " + classPath + " is not a "
						+ "vertex in the dependency graph");
			}
		}
			
		double commitCoreness = (double) filesInCore/commitSize;
		return commitCoreness;
	}
	
	private void checkoutCodeFromSVN(String revision) throws IOException {
			
		//Check out revision from SVN
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("svn co -r " + revision + " " + 
				"file:///C:/Users/user/mirrors/asf/ant/core/trunk " + 
				"C:\\tmp\\ant");
			
		String output = IOUtils.toString(process.getInputStream());
		System.out.println(output);
			
		process.destroy();
	}
		
	private Graph<String,String> calculateCallGraph() throws IOException {
			
		//Calculate the callGraph
		
		String rootDir = "C:\\tmp\\ant\\src\\main";
		JDX jdx = new JDX();
		DependencyReport report = jdx.calculateDepsFrom(
				rootDir, true, "*.java", new JavaAPIMatcher(), true);
		
		Graph<String, String> graph = JDXToJungConverter.convert(report);
		System.out.println("Vertices: " + graph.getVertices());
		
		
		return graph;
	}
	
	private List<Commit> buildCommitList(List<String[]> lines) {
			List<Commit> commits = new ArrayList<Commit>();
			
		//Dummy commit
		Commit commit = new Commit();
		
		for (String[] line : lines){
			
			//Revision number
			String commitID = line[0];
			
			if (!commitID.equals(commit.getId())){					
				commits.add(commit);					
				commit = new Commit();
				commit.setId(commitID);
			}
			
			String file = line[1];
			commit.addFile(file);	
		}
		
		//Adds the last commit
		commits.add(commit);
		
		//Removes the dummy commit
		commits.remove(0);
		
		return commits;
	}	
}