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

				for (Node child : currentNode.childList) {

					if (child.isVisited()) continue;
						child.setVisited(true);
					((LinkedList<Node>) queue).add(child);

				}

				currentNode = queue.poll();
				iterations += 1;
			}


		}


		PrintResults(currentNode,iterations,"BFS");
	}


	/**
	 * Depth First Search implementation
	 * @param root node
	 */
	public static void DFS(Node root) {


		Stack<Node> stack = new Stack<Node>();
		Node currentNode = root;
		stack.push(currentNode);
		int iterations = 0;

		while ( !stack.isEmpty() &&  !currentNode.isGoalNode(puzzle.goal))  {

				Node node = stack.peek();
				node.setVisited(true);
				node.expand();

				for (Node child : node.childList) {

					if (child.isVisited()) continue;

					child.setVisited(true);
					stack.push(child);

				}

				currentNode = stack.pop();
				iterations += 1;

		}


		PrintResults(currentNode,iterations,"DFS");
		

	}


	/**
	 * Cost function strategy.
	 * This will be used for Uniform,A* algorithms
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
			for (Node child : currentNode.childList) {
				if (child.isVisited())
					continue;
				child.setVisited(true);

				//Append heuristics cost to node

				child.heuristicCost = new Heuriatics(child,puzzle.goal, heurType).getCost();
				child.totalCost += child.heuristicCost;
				minHeap.add(child);

			}

			currentNode = (Node) minHeap.poll();
			currentNode.parentState.selectedChild = currentNode;
			time += 1;
		}

		PrintResults(currentNode,time,"costSearch");

	}


	/**
	 * DFS wrapper for iterative deepening
	 * @param root
	 * @param max_depth
	 */
	public static  void iterativeDeepening(Node root, int max_depth){


		for (int i = 1; i < max_depth; i++) {




		}



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

		if (root == null ||
				root.parentState == null) {
			return root;
		}

		Node reversedNode = reverseNode(root.parentState);

		root.parentState.parentState = root;
		root.parentState = null;
		return reversedNode;
	}


	/**
	 * Print stats for selected statistics
	 * @param node returned current Node
	 * @param iterations number of iterations
	 * @param strategy selected strategy name
	 */
	public static void PrintResults(Node node, int iterations, String strategy){

		Node node_ = reverseNode(node);
		System.out.println("Overall statics for " +  strategy);
		System.out.println("Iterations : " + iterations);


		while(node_.parentState !=null){

			System.out.println("Direction :" + node_.direction  + " move ,  node cost :" + node_.cost + ", total cost: " + node_.totalCost +", heuristic estimation : " + node_.heuristicCost   );
			node_.printTiles();


			node_ = node_.parentState;

		}





	}



}




