package edu.handong.csee.isel.clami;

public class Main {
	private static String path;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.run(args);
	}
	
	public void run(String[] args) {
	Cluster cluster = new Cluster();
	Labeler labeler = new Labeler();
	
	path = "//Users//a21700328//Apache//Apache.arff";
	
	cluster.loadArff(path);
	cluster.printInstances();
	
	labeler.setInstances(path);
	labeler.setRow();
	labeler.setColumn();
	
	labeler.transposeMatrix(path);
	
	labeler.findHigherValues();
	
	labeler.labelBugs();

	}
}
