package com.encryption.jjx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class OptCenters {
	public void storeLabel(HashMap<Integer, ArrayList<Integer>> Label,String path,String outIn) throws FileNotFoundException 
	{
		path="labellevel/"+path+"("+outIn+").txt";
		 PrintStream out = new PrintStream(path);  
		 System.setOut(out);
		 Set<Integer> keySet=Label.keySet();
		 for(int vertex:keySet)
		 {
			 System.out.print(vertex+" ");
			 for(int center:Label.get(vertex))
				 System.out.print(center+" ");
			 System.out.println();
		 }
		 System.setOut(System.out);
	}
	public void opt(HashMap<Integer, LinkedList<Integer>> labelIn,HashMap<Integer, LinkedList<Integer>> labelOut)
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
		keySet=labelIn.keySet();
		for(int key:keySet)
		{
			LinkedList<Integer> temp=labelIn.get(key);
			for(int vertex : temp)
			{
				
			}
		}
	}
	public static void main(String[] args) throws IOException {
		ArrayList<String> dataset = new ArrayList<String>();
		dataset.add("p2p-Gnutella24");
		for (String data : dataset) {
			Statistic statistic = new Statistic(data, 0);
			
		}
	}

}