package br.usp.ime.lapessc.criwgcai.entity;
import java.util.Map;

public class CorePeripheryReport {

	private Map<String, Double> coreArtifacts;
	private Map<String, Double> peripheryArtifacts;
	private Map<String, Double> undeterminedArtifacts;
	
	public CorePeripheryReport(Map<String, Double> coreArtifacts, 
			Map<String, Double> peripheryArtifacts, 
			Map<String, Double> undeterminedArtifacs){
		
		this.coreArtifacts = coreArtifacts;
		this.peripheryArtifacts = peripheryArtifacts;
		this.undeterminedArtifacts = undeterminedArtifacs;
	}
	
	public void addCoreArtifact(String artifactName, Double score){
		coreArtifacts.put(artifactName, score);
	}
	
	public void addPeripheryArtifact(String artifactName, Double score){
		peripheryArtifacts.put(artifactName, score);
	}
	
	public void addUndeterminedArtifact(String artifactName, Double score){
		undeterminedArtifacts.put(artifactName, score);
	}

	public Map<String, Double> getCoreArtifacts(){
		return coreArtifacts;
	}
	
	public Map<String, Double> getPeripheryArtifacts(){
		return peripheryArtifacts;	
	}
		
	public Map<String, Double> getUndeterminedArtifacts(){
		return undeterminedArtifacts;
	}
	
}