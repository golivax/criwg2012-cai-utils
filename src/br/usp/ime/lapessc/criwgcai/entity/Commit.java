package br.usp.ime.lapessc.criwgcai.entity;

import java.util.ArrayList;
import java.util.List;

public class Commit {

	private String id;
	private List<String> modifiedFiles;
	private double coreness;
	
	public Commit(){
		id = new String();
		modifiedFiles = new ArrayList<String>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void addFile(String file){
		modifiedFiles.add(file);
	}
	
	public List<String> getModifiedFiles() {
		return modifiedFiles;
	}

	public double getCoreness() {
		return coreness;
	}

	public void setCoreness(double coreness) {
		this.coreness = coreness;
	}	
	
}