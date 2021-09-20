package edu.handong.csee.isel.clami;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;

public class Cluster {
	
	private static String arffPath;
	private static Instances instancesFile;
	
	public Cluster() {
		arffPath = null;
		
	}
	
	public static Instances loadArff(String path){//instance 데이터 수집 
		
		arffPath = path;
		
		Instances instances=null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			instances = new Instances(reader);
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		instances.setClassIndex(instances.numAttributes()-1);
		instancesFile = instances;
		return instances;
	}
	
	public void printInstances() {
		System.out.println(instancesFile);
	}
	
	public static String getArffPath() {
		return arffPath;
	}

}
