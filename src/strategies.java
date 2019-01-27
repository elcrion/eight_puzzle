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


		Queue<Node> queue = new LinkedList<Node>();
		Node node = new Node(root.tileList);
		Node currentNode = node;
		int iterations = 0;

		while ( !currentNode.isGoalNode(puzzle.goal)) {
			{
				node.setVisited(true);
				currentNode.expand();

				for (Node successor : currentNode.successors) {

					if (successor.isVisited()) continue;
						successor.setVisited(true);
					((LinkedList<Node>) queue).add(successor);

				}

				currentNode = queue.poll();
				iterations += 1;
			}


		}


		PrintResults(currentNode,iterations,"BFS");
	}


	/**
	 * Depth First Search implementation
	 * @param root Node
	 * @param iterative Deepening solution
	 */
	public static void DFS(Node root, boolean iterative) {




		Node currentNode = root;

        int iterations = 0;
        int currentDepth = 1;
		boolean found = false;
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

						// We remove from stack when there are no unvisited nodes along the path anymore
						if (unvisited == 0) {

							currentNode = stack.pop();
							found = currentNode.isGoalNode(puzzle.goal);
							if(found){
								break;
							}

						}


                		iterations += 1;




            }

			currentDepth++;


        }

		PrintResults(currentNode,iterations,"DFS");


	}


	/**
	 * Cost function strategy.
	 * This will be used for BestFirst, Uniform,A* solutions
	 * @param root Node
	 * @param heurType Heuristics type
	 */
	public static void costSearch(Node root,Heuriatics.HeurType heurType) {


		int time = 0;

		PriorityQueue minHeap =new PriorityQueue(10, new NodeComparator());
		Node currentNode = root;
		while ( !currentNode.isGoalNode(puzzle.goal))  {

			currentNode.setVisited(true);
			currentNode.expand();
			for (Node successor : currentNode.successors) {
				if (successor.isVisited())
					continue;
				successor.setVisited(true);

				//Append heuristics cost to node

				successor.heuristicCost = new Heuriatics(successor,puzzle.goal, heurType).getCost();

				if(heurType == Heuriatics.HeurType.BestFirst) {
					successor.cost = 0;
					successor.totalCost = successor.heuristicCost;

				}else{

					successor.totalCost += successor.heuristicCost;

				}

				minHeap.add(successor);
			}

			currentNode = (Node) minHeap.poll();
			time += 1;
		}

		PrintResults(currentNode,time,"costSearch");

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
	 * Print stats for selected statistics
	 * @param node returned current Node
	 * @param iterations number of iterations
	 * @param strategy selected strategy name
	 */
	public static void PrintResults(Node node, int iterations, String strategy){

		Node node_ = reverseNode(node);

		int depth =0;
		while(node_ !=null){

			System.out.println("Direction :" + node_.direction  + " move , node depth : "+ depth++ +", node cost :" + node_.cost + ", total cost: " + node_.totalCost +", heuristic estimation : " + node_.heuristicCost   );
			node_.printTiles();


			node_ = node_.parentState;

		}

		System.out.println("Overall statics for " +  strategy);
		System.out.println("Iterations : " + iterations);



	}



}




