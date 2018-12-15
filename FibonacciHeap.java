import java.util.*;

/*
This class creates a new FibonacciHeap, implements the methods- insert, remove, 
meld, increaseKey and removeMax
*/
public class FibonacciHeap {
	public FibonacciHeapNode maxHeapNode;

	//inserts a new node in the heap
	public void insert(FibonacciHeapNode node) {

		if (maxHeapNode!=null) {

			//if maxHeapNode is not null add new node as its right sibling  
			node.left = maxHeapNode;
			node.right = maxHeapNode.right;
			maxHeapNode.right = node;

			if(node.right != null) {
				node.right.left = node;
			}
			if(node.right == null) {
				//if the maxHeapNode does not have right sibling
				node.right =  maxHeapNode;
				maxHeapNode.left = node;
			}
			if(node.key>maxHeapNode.key) {
				//change the max pointer to the new node
				maxHeapNode = node;

			}

		}
		else {
			maxHeapNode = node;
		}

	}

	//removes the node from its parent and inserts it as a sibling of the maxHeapNode
	public void removeNode(FibonacciHeapNode node, FibonacciHeapNode parent) {

		node.left.right = node.right; 
		node.right.left = node.left;
		parent.degree--;

		if(parent.degree==0) {
			parent.child=null;
		}
		if(parent.child == node) {
			parent.child = node.right;
		}
		node.left = maxHeapNode;
		node.right = maxHeapNode.right;
		maxHeapNode.right = node;
		if(node.right != null) {
			node.right.left = node;
		}
		if(node.right == null) {
			//if the maxHeapNode does not have right sibling
			node.right =  maxHeapNode;
			maxHeapNode.left = node;
		}
		node.parent=null;
		node.childCut=false;

	}
	//cascading cut
	public void cascadingCut(FibonacciHeapNode node) {

		FibonacciHeapNode parent = node.parent;
		if(parent!=null) {
			if(!parent.childCut) {
				parent.childCut=true;

			}
			else {
				//if childCut field is true remove it from its parent
				removeNode(node , parent);
				cascadingCut(parent);

			}
		}

	}

	//increase key value of an existing node in the heap
	public void increaseKey(FibonacciHeapNode node, int key) {
		if(node.key>key) {
			node.key=key;
		}
		else {
			node.key=key;
			FibonacciHeapNode parent = node.parent;
			if(parent!=null && parent.key<node.key) {
				removeNode(node,parent);
				cascadingCut(node);
			}
			if(node.key>maxHeapNode.key) {
				maxHeapNode=node;
			}

		}
	}
	
	//pairwise combine the trees of same degree after remove max node 
	public void pairWiseCombine() {	
		HashMap<Integer,FibonacciHeapNode> hashMap= new HashMap<>();

		int rootNodes =0;
		FibonacciHeapNode node= maxHeapNode;
		if(node!=null) {
			rootNodes++;
			node=node.right;
			while(node!=maxHeapNode) {
				rootNodes++;
				node=node.right;		
			}
		}
		FibonacciHeapNode iterator =node;
		while(rootNodes>0) {
			//use another node to be the iterator as the right pointers of 'node' will change after meld operation.
			 node = iterator;
			 //iterator stores the right sibling of node 
			 iterator = iterator.right;
			 hashMap=checkForEntryInHashMap(hashMap, node);
			 //use hashmap to store the degree and heap nodes 
			rootNodes--;
		}

		maxHeapNode=null;
		for(Map.Entry<Integer, FibonacciHeapNode> entry : hashMap.entrySet()) {
			FibonacciHeapNode addNode = entry.getValue();
			if(maxHeapNode!=null) {
				addNode.left=maxHeapNode;
				addNode.right=maxHeapNode.right;
				maxHeapNode.right=addNode;
				addNode.right.left=addNode;
				if(addNode.key>maxHeapNode.key) {
					maxHeapNode=addNode;
				}

			}
			else {
				maxHeapNode=addNode;
				maxHeapNode.right=maxHeapNode;
				maxHeapNode.left=maxHeapNode;
			}
		}
			}
	//make node with smaller key the child of node with larger key
	public void meld(FibonacciHeapNode pNode, FibonacciHeapNode cNode,HashMap<Integer,FibonacciHeapNode>hashMap) {
		cNode.parent = pNode;
		cNode.left=cNode;
		cNode.right=cNode;
		if(pNode.child==null) {
			pNode.child=cNode;
		}
		else {
			FibonacciHeapNode child = pNode.child;
			cNode.right=child.right;
			cNode.left=child;
			child.right.left=cNode;
			child.right=cNode;
		}

		hashMap.remove(cNode.degree);
		pNode.degree++;
		cNode.childCut=false;
		checkForEntryInHashMap(hashMap,pNode);
	}
	//remove maxnode of the heap from other rootnodes
	public FibonacciHeapNode removeMax() {
		
		FibonacciHeapNode max= maxHeapNode;
		if(max!=null) {

			int degree= max.degree;
			FibonacciHeapNode child =max.child;
			while(degree>0) {
				// add the child and all of its siblings to the heap roots
				FibonacciHeapNode temp=child.right;
				//remove the child from its sibling
				child.left.right=child.right;
				child.right.left=child.left;
				
				child.left=maxHeapNode;
				child.right=maxHeapNode.right;
				maxHeapNode.right=child;
				child.right.left=child;

				child.parent=null;
				child=temp;
				degree--;

			}
						
			max.left.right=max.right;
			max.right.left=max.left;
			if(max.right==max) {
				maxHeapNode=null;
			}
			else {
				maxHeapNode=max.right;
				pairWiseCombine();
			}
		}
		return max;
	}
	//check if the there is an entry with the degree same as pNode in the hashmap
	//if yes-call meld function
	//else- add the node to hashmap
	public HashMap<Integer,FibonacciHeapNode> checkForEntryInHashMap(HashMap<Integer,FibonacciHeapNode>hashMap,FibonacciHeapNode pNode){
		if(hashMap.containsKey(pNode.degree)) {
			FibonacciHeapNode newNode = hashMap.get(pNode.degree);
			if(pNode.key<newNode.key) {
				FibonacciHeapNode temp= newNode;
				newNode = pNode;
				pNode=temp;
			}
			meld(pNode,newNode,hashMap);
		}
		else {
			hashMap.put(pNode.degree, pNode);
		}
		return hashMap;
	}
}
