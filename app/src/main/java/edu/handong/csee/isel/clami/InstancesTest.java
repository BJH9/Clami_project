package edu.handong.csee.isel.clami;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import weka.core.Instances;
import weka.core.Instance;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.Arrays;

import java.util.ArrayList;

public class InstancesTest {
	private String path;
	private Instances ins;
	private Instance in;
	
	private int column;
	private int row;
	private int locationInformation;
	
	private int[][] transposedMatrix; //뒤집 instances
	private int[][] arrayForMetricSelection;
	private int[] individualInstance;//sliced instance
	private int[] originalIndividualIns;//original sliced instance
	private int[][] location; //위치정보 저장 
	private int[] violationNumber;//instance별 violation 개수 
	private int[] oViolationNumber;//original instance별  violation 개수 
	private String[] bugLabel;
	private int[][] group;//violation개수에 따른  gruop분류  
	private int[] numbersInGroup;
	private int numberInGroup;
	
	
	private int median;
	private int[] medians;
	private int vMedian;
	private int k;
	private int t;//gruop[t][]행에 해당 
	private int q;
	private int y;//medians[y]
	private int[] kNumber;
	int p;
	
	public static void main(String[] args) {//main
		InstancesTest runner = new InstancesTest();
		runner.run(args);
	}
	
	public static Instances loadArff(String path){//instance 데이터 수집 
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
		System.out.println(instances);
		return instances;
	}
	
	/**
	 * @param args
	 */
	private void run(String[] args) {
		y = 0;
		q = 0;
		ins = loadArff(args[0]);//ins에 할당 
		in = ins.get(0);//첫줄 instance 할당 
		column = in.numAttributes();//열의 개수 
		row = ins.size();//행의 개수 
		
		numbersInGroup = new int[column];
		numberInGroup = 0;
		locationInformation = -1;
		t = 0;
		group = new int[200][row];
		
		medians = new int[column];
		
		
		
		
		
		violationNumber = new int[row];
		oViolationNumber = new int[row];
		bugLabel = new String[row];
		
		for(int i = 0; i < row; i++) {
			violationNumber[i] = 0; //초기화 
		}
		
		kNumber = new int[row / 2]; //row의 중간값을 입력했다 
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("행의 개수: " + row);
		System.out.println("");
		System.out.println("");
		
		transposedMatrix = new int[column][row];// 행과 열 뒤집은 배열 꺼 여기 저장  
		individualInstance = new int[row];// 뒤집힌 배열, 1행 여기 저장 
		arrayForMetricSelection = new int[column][row];
		
		for(int i = 0; i < column - 1; i++) {
			System.out.println("");
			for(int j = 0; j < row - 1; j++) {
				in = ins.get(j);
				transposedMatrix[i][j] = (int)in.valueSparse(i);
				
					System.out.print(transposedMatrix[i][j] + ",");//행과 열 뒤집음 
				
		}
		}
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("b");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		for(int j = 0; j < column - 1; j++) {//violatio위치를 구한다 .
		
		System.out.println("***   "+ j +  "번 째 metric   ***");
		for(int i = 0; i < row - 1; i++) {
		individualInstance[i] = transposedMatrix[j][i];
		System.out.print(individualInstance[i] + ",");// 뒤집힌 첫번 째 instance 저장 
		}
		
		System.out.println("");
		
		originalIndividualIns = new int[row];//초기화 
		k = 0;
		location = new int[column][row];
		
		for(int i = 0; i < row - 1; i++)
			originalIndividualIns[i] = individualInstance[i];//정렬되지 않ㄷ은 기존의 배열 저장 
		
		Arrays.sort(individualInstance); //정렬 
		
		System.out.println("정렬된 metrices");
		for(int i = 0; i < row - 1; i++){
			System.out.print(individualInstance[i] + ",");
		}
		
		System.out.println("");
		
		median = individualInstance[row / 2]; //median
		medians[y] = median;
		y++;
		
		System.out.println("median 값: " + median);
		
		for(int i = 0; i < row - 1; i++) {//violation 위치 
			if(originalIndividualIns[i] > median) {
				location[j][k] = i;
				violationNumber[i]++;
				k++;//
			}
		}
		
		System.out.println(j + " 번 째 metric violation 위치");
		for(int i =0; i < k; i++) {
			System.out.print(location[j][i] + ",");
		}
		System.out.println("");
		System.out.println(j + " 번 째 metric violation 개수 : " + k);
		System.out.println("");
		System.out.println("");
		
		kNumber[p] = k;
		p++;
		
		}
		
		System.out.println("");
		System.out.println("각각의  metric의  violation 개수 ");
		
		for(int i = 0; i < p; i++) {
			System.out.print(kNumber[i] + ",");
			violationNumber[kNumber[i]]++;
		}
		
		System.out.println("");
		System.out.println("");
		
		System.out.println("instance별 violation개수");//instance별 violation 개수 
		for(int i = 0; i < row - 1; i++) {
			System.out.print(violationNumber[i] + ",");
		}
		
		for(int i = 0; i < row - 1; i++) {
			oViolationNumber[i] = violationNumber[i];
		}
		for(int i = 0; i < row - 1; i++) {
			bugLabel[i] = String.valueOf(violationNumber[i]);
		}
		System.out.println("");
		
		Arrays.sort(violationNumber);
		System.out.println("");
		System.out.println("sort된 violationNumber");//sort된  violationNumber
		for(int i = 0; i < row; i++) {
			System.out.print(violationNumber[i] + ",");
		}
		System.out.println("");
		System.out.println("");
		
		t = 0;
		System.out.println("groups");
		for(int i = 0; i < row - 1; i++) {//그룹나누기 
			if(locationInformation == -1)
				locationInformation = i;
			
			if(violationNumber[i] != violationNumber[i+1]) {
				
				System.out.println("");
				for(int j = locationInformation; j <= i; j++) {
					group[t][j] = violationNumber[i];
					System.out.print(group[t][j] + ",");
					
				}
				t++;
				locationInformation = -1;
				
		 	
				
			}
			
			
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("나눠진 그룹의 개수: " + t);
		
		vMedian = t / 2;
		System.out.println("그룹의 중간값 개수 : " + vMedian);
		System.out.println("");
		
		for(int i = 0; i < row - 1; i++) {//라벨붙이기 
			if(oViolationNumber[i] > 13) {
				bugLabel[i] = "B";
			}
			else {
				bugLabel[i] = "C";
			}
		}
		
		System.out.println("");
		System.out.println("버그분류 ");
		for(int i = 0; i < row - 1; i++) {//버그 라벨 
			System.out.print("instance" + i +  " : "+ bugLabel[i] + ", ");
		}
		System.out.println("");
		
		/*System.out.println("");
		System.out.println("median����");
		for(int i = 0; i < column - 1; i++) {//median����
			System.out.print(i + "�� ° metric�� median: " + medians[i] + ", "
					+ "");
		}
		System.out.println("");
		
		
		
		for(int i = 0; i < column - 1; i++) {//metric selection�� ���� arrayForMetricSelection���� violation�� 1�� �Է�
			for(int j = 0; j < row - 1; j++) {
				System.out.print(".");
				if(v[i][j] < medians[j] && bugLabel[j].equals("B")) {
					arrayForMetricSelection[i][j] = 1;
				}
				if(v[i][j] > medians[j] && bugLabel[j].equals("C")) {
					arrayForMetricSelection[i][j] = 1;
				}
				
			}
		}
		
		
		System.out.println("");
		System.out.println("");
		System.out.println("violation��ġ");
		for(int i = 0; i < column - 1; i++) {
			System.out.println("");
			for(int j = 0; j < row - 1; j++) {
				System.out.print(arrayForMetricSelection[i][j] + ", ");
			}
		}*/
		
	}

}
