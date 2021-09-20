package edu.handong.csee.isel.clami;

import weka.core.Instances;

import java.util.Arrays;

import weka.core.Instance;

public class Labeler {
	
	public Labeler() {
		super();
		// TODO Auto-generated constructor stub
	}


	private Instances instances;//arff instances
	private Instance instance;//arff instance
	private int row;
	private int column;
	private int[][] transposedMatrix;
	private int[] individualInstance;//instance를 저장할 배열 
	private int[] originalIndividualIns;
	private int median;
	private int[] medians;
	private int[][] location;//violation entry 값 
    private int[] violationNumber;//instance당 violation의 개수 
    private int[] oViolationNumber;//
	private int kFormedians;//index설정을 위한 변수 
	private int kForLocation;//index설정을 위한 변수 
	private int kForGroup;
	private int[] locationInformation;
	private int locationIndex;
	private int kForInformation;
	private String[] bugLabel;
	private int[][] group;
	private int vMedian;//버그 분류할 때 중간값
	
	public void setInstances(String path) {//cluster에서 받아온 arff를 instances에 할당
		instances = Cluster.loadArff(path);
	}
	
	public void setRow() {// instance의 column을 row에 할당 	
		row = instance.numAttributes();
	}
	
	public void setColumn() {//instances의 row를 column에 할당 
		column = instances.size();
	}
	
	public void transposeMarix(String path){//instances의 행과 열을 바꿔 배열에 저장
		setInstances(path);
		setRow();
		setColumn();
		transposedMatrix = new int[row][column];
		for(int i = 0; i < row - 1; i++) {
			for(int j = 0; j < column - 1; j++) {
				instance = instances.get(j);
				transposedMatrix[i][j] = (int)instance.valueSparse(i);
				
				System.out.println(transposedMatrix[i][j] + ",");
			}
		}
		
	}
	
	public void findViolations() {
		median = 0;
		medians = new int[row];
		kFormedians = 0;
		kForInformation = 0;
		
		for(int i = 0; i < row - 1; i++) {
			System.out.println(i + "번 째 metric ");
			for(int j = 0; j < column - 1; j++) {
				individualInstance[j] = transposedMatrix[i][j];//행의 instance를 저장 
				System.out.println(individualInstance[i] + ",");
			}
			
			for(int j = 0; j < column - 1; j++)
				originalIndividualIns[j] = individualInstance[j];//기존 instance정보 저
			
			Arrays.sort(individualInstance);
			
			System.out.println("정렬된 metrices");
			for(int j = 0; j < column - 1; j++) {
				System.out.println(individualInstance[j] + ",");
			}
			
			median = individualInstance[row / 2];
			medians[kFormedians] = median;
			kFormedians++;
			
			System.out.println("median 값: " + median);
			
			for(int j = 0; j < column - 1; j++) {
				if(originalIndividualIns[j] > median) {
					location[i][kForLocation]++;
					violationNumber[j]++;
					kForLocation++;
				}
			}
			
			System.out.println(i + "번 째 metric violation 위치 ");
			for(int j = 0; j < kForLocation; j++) {
				System.out.println(location[i][j] + ",");
			}
			System.out.println("");
			System.out.println(i + " 번 째 metric violation 개수 : " + kForLocation);
			System.out.println("");
			System.out.println("");
			
			locationInformation[kForInformation] = kForLocation;
			kForInformation++;
			
		}
	}
	
	
	public void labelBugs() {
		System.out.println("");
		System.out.println("각각의  metric의  violation 개수 ");
		
		for(int i = 0; i < kForLocation; i++) {
			System.out.print(locationInformation[i] + ",");
			violationNumber[locationInformation[i]]++;
		}
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("instance별 violation개수");//instance별 violation 개수 
		for(int i = 0; i < column - 1; i++) {
			System.out.print(violationNumber[i] + ",");
		}
		
		for(int i = 0; i < column - 1; i++) {
			oViolationNumber[i] = violationNumber[i];
		}
		for(int i = 0; i < column - 1; i++) {
			bugLabel[i] = String.valueOf(violationNumber[i]);
		}
		System.out.println("");
		
		Arrays.sort(violationNumber);
		System.out.println("");
		System.out.println("sort된 violationNumber");//sort된  violationNumber
		for(int i = 0; i < column; i++) {
			System.out.print(violationNumber[i] + ",");
		}
		System.out.println("");
		System.out.println("");
		
		kForGroup = 0;
		System.out.println("groups");
		for(int i = 0; i < column - 1; i++) {//그룹나누기 
			if(locationIndex == -1)
				locationIndex = i;
			
			if(violationNumber[i] != violationNumber[i+1]) {
				
				System.out.println("");
				for(int j = locationIndex; j <= i; j++) {
					group[kForGroup][j] = violationNumber[i];
					System.out.print(group[kForGroup][j] + ",");
					
				}
				kForGroup++;
				locationIndex = -1;
				
		 	
				
			}
			
			
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("나눠진 그룹의 개수: " + kForGroup);
		
		vMedian = kForGroup / 2;
		System.out.println("그룹의 중간값 개수 : " + vMedian);
		System.out.println("");
		
		for(int i = 0; i < column - 1; i++) {//라벨붙이기 
			if(oViolationNumber[i] > 13) {
				bugLabel[i] = "B";
			}
			else {
				bugLabel[i] = "C";
			}
		}
		
		System.out.println("");
		System.out.println("버그분류 ");
		for(int i = 0; i < column - 1; i++) {//버그 라벨 
			System.out.print("instance" + i +  " : "+ bugLabel[i] + ", ");
		}
		System.out.println("");
	}
	
}
