package br.usp.ime.lapessc.criwgcai.analyzer.commit;

import java.util.ArrayList;
import java.util.List;

import br.usp.ime.lapessc.criwgcai.calculators.commit.CommitCorenessCalculator;
import br.usp.ime.lapessc.criwgcai.entity.Commit;

public class CommitAnalyzer {

	public List<Commit> getCommits(String csvFile){
		CommitCorenessCalculator corenessCalculator = 
				new CommitCorenessCalculator();
		
		return corenessCalculator.calculate(csvFile);
	}
	
	public List<Commit> getCoreCommits(String csvFile, double threshold){
		
		List<Commit> coreCommits = new ArrayList<Commit>();
		
		for (Commit commit : getCommits(csvFile)){
			if (commit.getCoreness() >= threshold){
				coreCommits.add(commit);
			}
		}
		
		return coreCommits;
	}
	
	public static void main(String[] args) {
		CommitAnalyzer commitAnalyzer = new CommitAnalyzer();
		
		List<Commit> commits = commitAnalyzer.getCommits("commits-criwg.csv");
		
		//Prints the id + coreness of each commit
		for (Commit commit : commits){
			System.out.println(commit.getId() + "\t" + commit.getCoreness());
		}
	}
	
}
