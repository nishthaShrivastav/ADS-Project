/*
 * The class defines Fibonacci heap node  and its attributes
 * word - search word
 * key- frequency of the search word
 * degree= number of children
 * left, right(siblings) , parent , child- fibonacci heap nodes 
 * 
 * */
public class FibonacciHeapNode {
	FibonacciHeapNode left, right, parent, child;
	int degree=0;
	boolean childCut=false;
	String word;
	int key;
	
	public FibonacciHeapNode(String word, int key) {
		this.degree=0;
		this.parent=null;
		this.left=this;
		this.right=this;
		this.word=word;
		this.key=key;
	}
	
	
}
