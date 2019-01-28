import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * Main class for the Puzzle implementation
 * @author glebiakovlev
 *
 */


public class Puzzle {


	public static Node goal;
	public static Node rootNode;
	public static int tilesNumber = 9;
	public static Heuriatics.HeurType selectedHeuristics;


	private static int[] goalPuzzle = {1,2,3,8,0,4,7,6,5};

	private static int[] easyPuzzle = {1,3,4,8,6,2,7,0,5};
	private static int[] mediumPuzzle = {2,8,1,0,4,3,7,6,5};
	private static int[] hardPuzzle = {5,6,7,4,0,8,3,2,1};
	private static  Scanner scanner = new Scanner(System.in);




	/**
	 * TO RUN SET PARAMETERS :
	 * mode : Easy, Medium, Hard
	 * strategy :
     * 1:BFS
     * 2:DFS
     * 3:Iterative Deepening
     * 4:BestFirst search
     * 5: Uniform Cost Search
     * 6:A* Misplaced Tile
     * 7.A* Manhattan
	 * 8.A* Advanced heuristics
	 *
	 *
	 * @param args
	 */



	public static void main(String[] args) {


	while(true) {
		readMode();
		setStrategy();
		System.out.println("\n\n0: select another method\n1: stop.");
		if(scanner.nextLine().equals("1")){

			break;
		}


	}


	}

	private static void setStrategy() {
		System.out.println("Please select a strategy :\n1:BFS\n2:DFS\n3:Iterative Deepening\n4:BestFirst search\n5:Uniform Cost Search\n6:A*1 Misplaced Tile\n7.A*2 Manhattan\n8.A*3 Combined");
		String heur = scanner.nextLine();

		switch (Integer.parseInt(heur)){


			case 1:

				Strategies.BFS(rootNode);

				break;

			case 2:

				Strategies.DFS(rootNode,false);

				break;
            case 3:

                Strategies.DFS(rootNode,true);

                break;

            case 4:

                selectedHeuristics =  Heuriatics.HeurType.BestFirst ;
                Strategies.costSearch(rootNode,selectedHeuristics);
                break;


			case 5:

				selectedHeuristics =  Heuriatics.HeurType.UniformCost ;
				Strategies.costSearch(rootNode,selectedHeuristics);
				break;

			case 6:

				selectedHeuristics =  Heuriatics.HeurType.AstarTileCounter;
				Strategies.costSearch(rootNode,selectedHeuristics);
				break;

			case 7:

				selectedHeuristics =  Heuriatics.HeurType.AstarManhattan;
				Strategies.costSearch(rootNode,selectedHeuristics);

				break;
            case 8:

                selectedHeuristics =  Heuriatics.HeurType.AstarCombined;
                Strategies.costSearch(rootNode,selectedHeuristics);
                break;

            default:

				System.out.println("Can not recognize your input ");



		}
	}

	private static void readMode() {
		System.out.println("Select mode :  \n 1: Easy \n 2: Medium \n 3: Hard  ");
		int mode = Integer.parseInt(scanner.nextLine());
		System.out.println("selected mode : " + mode );

		switch (mode){

			case 1 :

				rootNode = new Node(easyPuzzle);
				break;
			case 2 :

				rootNode = new Node(mediumPuzzle);
				break;

			case 3 :

				rootNode = new Node(hardPuzzle);
				break;

			default:

				System.out.println("Can not recognize your input ");



		}


		goal = new Node(goalPuzzle);
	}


}


/**
 * Node class for the Puzzle
 * This represents a node in the search state diagram 
 * 
 */
class Node {
	
	/**
	 * successors : list of the possible state nodes
	 * parentState : parent node in the diagram
	 * tileList : one dimensional array to store a list of Puzzle tile for current list
	 * empty_tile : current empty index (location) 
	 * cost : cost of the node
	 * totalCost : combined total cost of the path so far
     * heuristicCost : estimation of heuristics for the node
     * depth : depth of the node
     * direction : what direction was chosen to to move
	 */
	

	public ArrayList<Node> successors = new ArrayList<Node>();
	public Node parentState;
	public int[] tileList = new int[Puzzle.tilesNumber];
	public int emptyTile = 0;
	public enum Direction { Left , Right, Up, Down}
	public int cost;
	public int heuristicCost;
	public int totalCost;
	public int depth = 0;
	public  String direction ;




	/**
	 * Feed the node with tiles here
	 * @param list
	 */
	public Node(int[] list) {
		
		for (int i =0 ; i< list.length ; i++ ){
			
			this.tileList[i] = list[i];
		}
		
	}
	
	/**
	 * Make possible move to specified direction
	 * The method will check for the Puzzle boundaries within array
	 * For now assume the Puzzle is quadratic
     * The cost of the move is the value of the tile moved
	 * @param direction :  Left , Right, Up, Down
	 * @param state : tiles array (current board state)
	 * @param i : index of the empty tile
	 */
	public void MakeMove(Direction direction, int [] state, int i){
		
		
		int[] current_state = state.clone();
		
		int newIndex = 0;
		boolean constraint_check  = false;
		int columns = (int) Math.sqrt(Puzzle.tilesNumber);
		String nodeDirection = null;
		
		switch (direction) {
		
			case Left :
				constraint_check = i%columns > 0 ;
				newIndex  = i-1;
				nodeDirection = "Left";
				
				break;
			case Right :
				constraint_check = i%columns < columns - 1 ;
				newIndex  = i+1;
				nodeDirection = "Right";
			
				break;
			case Up :
				constraint_check = i - columns >= 0 ;
				newIndex  = i-3;
				nodeDirection = "Up";
				break;
				
			case Down :
				constraint_check = i+columns < tileList.length ;
				newIndex  = i+3;
				nodeDirection = "Down";
				break;	
				
		
		}
		
		if(constraint_check) {
			int temp_val = current_state[newIndex];
			current_state[newIndex] = current_state[i];
			current_state[i] = temp_val;
			
			Node successor = new Node(current_state);
			successor.depth = depth +1;
			successor.direction = nodeDirection ;

			int cost  =  tileList[newIndex]; // the cost is the tile value
			successor.cost = cost;

			successors.add(successor);
			successor.parentState = this;
		}
		
		
	}
	
	
	/**
	 * Find empty tile and expand (step forward)
	 */
	public void expand(){
		
		for(int i = 0; i< tileList.length; i++){
			
			if(tileList[i] == 0){

				emptyTile = i;
			}		
		}
		
		
		MakeMove(Direction.Left, tileList,emptyTile);
		MakeMove(Direction.Right, tileList,emptyTile);
		MakeMove(Direction.Up, tileList,emptyTile);
		MakeMove(Direction.Down, tileList,emptyTile);
		
		
	}




	/**
	 * Check if the current node is the goal node
	 * @param goal node
	 * @return boolean, true if this is a goal node
	 */
	public boolean isGoalNode(Node goal){


		for(int i =0; i< tileList.length;i++ ){

			if(tileList[i]  != goal.tileList[i]){

				return false;
			}
		}

		return true ;
	}


	/**
	 *  Convert Node to human readable format
	 * @return Node as string matrix representation
	 */
	public String toString(){

		String tiles = "";
		if(tileList.length > 0){

			for(int item:tileList){

				tiles += item + "";

			}

			return tiles;
		}

		return null;
	}



	public void printTiles(){

		int columns = (int) Math.sqrt(Puzzle.tilesNumber);
		int line = 0;
		if(tileList.length > 0){

			for(int i=0; i< columns;i++){

				for(int j=0; j<columns; j++){

					System.out.print(tileList[line] + " ");
					line++;
				}

				System.out.println();
			}


		}
		System.out.println(" ============== ");
	}






	
}