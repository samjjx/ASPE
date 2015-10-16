package com.encryption.jjx;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

public class Statistic {

	HashMap<Integer, LinkedList<Integer>> labelIn;
	HashMap<Integer, LinkedList<Integer>> labelOut;
	String dataset="";
	public Statistic(String dataset,int NorL) throws IOException
	{
		this.dataset=dataset;
		labelIn=getLabel(dataset+"(in).txt", NorL);
		labelOut=getLabel(dataset+"(out).txt", NorL);
	}
	public void printInfo()
	{
		int disCenterIn=disCenter(labelIn);
		int disCenterOut=disCenter(labelOut);
		int disCenterTotal=disCenterTotal(labelIn,labelOut);
		int labelInSize=labelSize(labelIn);
		int labelOutSize=labelSize(labelOut);
		System.out.println("**********************************************");
		System.out.println("The graph is: "+dataset);
		System.out.println("The distinct centers in labelIn is "+disCenterIn);
		System.out.println("The distinct centers in labelOut is "+disCenterOut);
		System.out.println("The distinct centers in two labels is "+disCenterTotal);
		System.out.println("The size of the labelIn is :"+ labelInSize);
		System.out.println("The size of the labelOut is :"+ labelOutSize);
		
	}
	public int disCenter(HashMap<Integer, LinkedList<Integer>> label)
	{
		LinkedList<Integer> center=new LinkedList<Integer>();
		Set<Integer> keySet=label.keySet();
		for(int key:keySet)
		{
			LinkedList<Integer> temp=label.get(key);
			for(int vertex:temp)
				if(!center.contains(vertex))
					center.add(vertex);
		}
		return center.size();
	}
	public int disCenterTotal(HashMap<Integer, LinkedList<Integer>> labelIn,HashMap<Integer, LinkedList<Integer>> labelOut)
	{
		LinkedList<Integer> center=new LinkedList<Integer>();
		Set<Integer> keySet=labelIn.keySet();
		for(int key:keySet)
		{
			LinkedList<Integer> temp=labelIn.get(key);
			for(int vertex:temp)
				if(!center.contains(vertex))
					center.add(vertex);
		}
		keySet=labelOut.keySet();
		for(int key:keySet)
		{
			LinkedList<Integer> temp=labelOut.get(key);
			for(int vertex:temp)
				if(!center.contains(vertex))
					center.add(vertex);
		}
		return center.size();
	}
	public int labelSize(HashMap<Integer, LinkedList<Integer>> label)
	{
		int count=0;
		Set<Integer> keySet=label.keySet();
		for(int key:keySet)
			count+=label.get(key).size();
		return count;
	}
	public double averageSize()
	{
		return 0;
	}
	@SuppressWarnings("resource")
	public HashMap<Integer, LinkedList<Integer>> getLabel(String dataset,int LorN) throws IOException
	{
		HashMap<Integer, LinkedList<Integer>> Label=new HashMap<Integer, LinkedList<Integer>>();
		FileInputStream in;
		if(LorN==0)
			in=new FileInputStream("K:/Java/PPTopo/labellevel/"+dataset);
		else if(LorN==1)
			in=new FileInputStream("K:/Java/PPTopo/label/"+dataset);
		else
			in=new FileInputStream("K:/Java/PPTopo/labelByFS/"+dataset);
		
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String tempLine;
		while((tempLine=br.readLine())!=null)
		{
			String[] array=tempLine.split(" ");
			Label.put(Integer.parseInt(array[0]), new LinkedList<Integer>());
			for(int i=1;i<array.length;i++)
				Label.get(Integer.parseInt(array[0])).add(Integer.parseInt(array[i]));
		}
		return Label;
	}
	public static void main(String[] args) throws IOException {
		LinkedList<String> dataList=new LinkedList<String>();
		dataList.add("p2p-Gnutella25");
		for(String dataset:dataList)
		{
			Statistic st=new Statistic(dataset, 1);
			st.printInfo();
		}
	}

}
