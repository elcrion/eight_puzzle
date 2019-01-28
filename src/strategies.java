import java.util.*;

/**
 * Class for possible search strategies 
 * @author glebiakovlev
 *
 */
public class strategies {






	/**
	 * Breadth first search implementation
	 * @param root
	 */
	public static void BFS(Node root) {

		Set<String> visited = new HashSet<String>();
		Queue<Node> queue = new LinkedList<Node>();
		Node node = new Node(root.tileList);
		Node currentNode = node;
		int iterations = 0;
		long startTime = System.currentTimeMillis();
		int maxQueueSize = 0;
		int nodesDequeued = 0;

		while ( !currentNode.isGoalNode(puzzle.goal)) {
			{
				visited.add(node.toString());
				currentNode.expand();

				for (Node successor : currentNode.successors) {

					if (visited.contains(successor.toString())){ continue;}
					visited.add(successor.toString());
					((LinkedList<Node>) queue).add(successor);

				}

				//Get maximum queue size
				if(queue.size() > maxQueueSize){
					maxQueueSize = queue.size();
				}


				currentNode = queue.poll();
				iterations += 1;
				nodesDequeued +=1;
			}


		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		PrintResults(currentNode,iterations,"BFS",elapsedTime,maxQueueSize,nodesDequeued);
	}


	/**
	 * Depth First Search implementation
	 * @param root Node
	 * @param iterative Deepening solution
	 */
	public static void DFS(Node root, boolean iterative) {




		Node currentNode = root;
		int maxQueueSize = 0;
        int iterations = 0;
        int currentDepth = 1;
		boolean found = false;
		int nodesDequeued = 0;
		long startTime = System.currentTimeMillis();

        while(!found) {

			Set<String> visited = new HashSet<String>();
			Stack<Node> stack = new Stack<Node>();
			stack.push(currentNode);
			visited.add(currentNode.toString());



            while (!stack.empty() ) {


                Node node = stack.peek();

                node.expand();
                int unvisited = 0;



                        for (Node successor : node.successors) {

                            if (visited.contains(successor.toString())) {
								continue;
							}
                            if(!iterative || successor.depth <= currentDepth) {
								successor.cost = 0;
								successor.totalCost = 0;
								successor.depth = node.depth + 1;
								visited.add(successor.toString());
								stack.push(successor);
								unvisited++;
							}


                        }

						//Get maximum stack size
						if(stack.size() > maxQueueSize){
							maxQueueSize = stack.size();
						}


						// We remove from stack when there are no unvisited nodes along the path anymore
						if (unvisited == 0) {

							currentNode = stack.pop();
							nodesDequeued +=1;
							found = currentNode.isGoalNode(puzzle.goal);

							if(found){
								break;
							}

						}


                		iterations += 1;




            }

			currentDepth++;


        }

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		PrintResults(currentNode,iterations,"DFS",elapsedTime,maxQueueSize,nodesDequeued);


	}


	/**
	 * Cost function strategy.
	 * This will be used for BestFirst, Uniform,A* solutions
	 * @param root Node
	 * @param heurType Heuristics type
	 */
	public static void costSearch(Node root,Heuriatics.HeurType heurType) {


		int time = 0;
		Set<String> visited = new HashSet<String>();
		PriorityQueue minHeap =new PriorityQueue(10, new NodeComparator());
		Node currentNode = root;
		long startTime = System.currentTimeMillis();
		int maxQueueSize = 0;
		int nodesDequeued = 0;

		while ( !currentNode.isGoalNode(puzzle.goal))  {

			visited.add(currentNode.toString());
			currentNode.expand();
			for (Node successor : currentNode.successors) {
				if (visited.contains(successor.toString())) {continue;}
				visited.add(successor.toString());

				//Append heuristics cost to node

				successor.heuristicCost = new Heuriatics(successor,puzzle.goal, heurType).getCost();

				if(heurType == Heuriatics.HeurType.BestFirst) {
					successor.cost = 0;
					successor.totalCost = successor.heuristicCost;

				}else{

					successor.totalCost += successor.heuristicCost;

				}

				minHeap.add(successor);

				//Get maximum PQ size
				if(minHeap.size() > maxQueueSize){
					maxQueueSize = minHeap.size();
				}

			}

			currentNode = (Node) minHeap.poll();
			nodesDequeued +=1;
			time += 1;
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		PrintResults(currentNode,time,"costSearch",elapsedTime,maxQueueSize,nodesDequeued);

	}







	/**
	 *	Min Heap comparator
	 */
	public static class NodeComparator implements Comparator<Node>
	{
		public int compare( Node x, Node y )
		{
			return  x.totalCost - y.totalCost;
		}


	}


	/**
	 * Reverse node to print from the start
	 * @param root current Node
	 * @return reversed Node
	 */
	public static Node reverseNode(Node root) {

        Node prev = null;
        Node current = root;
        Node next = null;
        while (current != null) {
            next = current.parentState;
            current.parentState = prev;
            prev = current;
            current = next;
        }
        root = prev;
        return root;
    }


	/**
	 *
	 * Print stats for selected statistics
	 * @param node returned current Node
	 * @param iterations number of iterations
	 * @param strategy selected strategy name
	 * @param timeMS time in millis
	 * @param maxQueueSize Space : size of queue of its max
	 * @param nodesDequeued Time : Number of nodes popped off the queue
	 */
	public static void PrintResults(Node node, int iterations, String strategy,long timeMS,int maxQueueSize,int nodesDequeued){

		Node node_ = reverseNode(node);

		int depth =0;

		while(node_ !=null){

			depth++ ;
			System.out.println("Direction :" + node_.direction  + " move , node depth : "+ depth +", node cost :" + node_.cost + ", total cost: " + node_.totalCost +", heuristic estimation : " + node_.heuristicCost   );
			node_.printTiles();


			node_ = node_.parentState;

		}

		System.out.println("Overall statics for " +  strategy);
		System.out.println("Iterations : " + iterations);
		System.out.println("Time taken in ms : " + timeMS);
		System.out.println("Length : " + node.depth);
		System.out.println("Cost : " + node.totalCost);
		System.out.println("Time : " + nodesDequeued);
		System.out.println("Space : " + maxQueueSize);

	}



}




