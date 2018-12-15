import java.io.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

/*
 * This class inserts searched keywords and frequencies to a Fibonacci heap 
 * and counts the n most popular keywords at any given time by removeMax method of the heap.
 * 
 * */
public class keywordcounter {

	public static void main(String[] args) {
		String filepath= args[0];
		File file = new File("output_file.txt");
		BufferedWriter writer =null;
	
		//use hashmap to store the search word as key and the heap node as value
		HashMap<String,FibonacciHeapNode> hashMap= new HashMap<>();
		FibonacciHeap fHeap = new FibonacciHeap();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			String string = reader.readLine();
			writer = new BufferedWriter(new FileWriter (file));
			//pattern1 to match type1 input
			//pattern2 to match type2 input
			Pattern pattern1 = Pattern.compile("([$])([\\S]+)(\\s)(\\d+)");
			Pattern pattern2 = Pattern.compile("(\\d+)");
			String stop="stop";
			
			while(string!=null) {
				Matcher word = pattern1.matcher(string);
				Matcher number = pattern2.matcher(string);

				if(word.find()) {
					String keyword= word.group(2);
					int frequency = Integer.parseInt(word.group(4));
					//if hashmap already contains the search word- increase the keyvalue of that node
					//else - add a new node to hashmap and heap
					if(hashMap.containsKey(keyword)) {
						FibonacciHeapNode node = hashMap.get(keyword);
						int increaseKey= node.key + frequency;
						fHeap.increaseKey(node,increaseKey);
					}
					else {
						FibonacciHeapNode node = new FibonacciHeapNode(keyword,frequency);
						fHeap.insert(node);
						hashMap.put(keyword,node);
					}
				}
				// if the input is type 2- find the n max frequency search words
				else if(number.find()){
					ArrayList<FibonacciHeapNode> removedList = new ArrayList<>();
					int removeNumber=Integer.parseInt(number.group(1));
					for(int i=0;i<removeNumber;i++) {
						FibonacciHeapNode removedNode = fHeap.removeMax();
						if(removedNode!=null) {
							hashMap.remove(removedNode.word);

							FibonacciHeapNode newNode = new FibonacciHeapNode(removedNode.word, removedNode.key);
							removedList.add(newNode);

							if(i<removeNumber-1) {
								writer.write(newNode.word + ",");
							}
							else {
								writer.write(newNode.word);
							}

						}

						else {
							break;
						}
					}
					writer.newLine();

					for(FibonacciHeapNode node : removedList) {
						fHeap.insert(node);
						hashMap.put(node.word, node);
					}
				}
				else if(string.equalsIgnoreCase(stop)) {
					break;
				}

				string=reader.readLine();
			}

			reader.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(writer!=null) {
				try {
					writer.flush();
					writer.close();
				}
				catch(IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
	}

}
