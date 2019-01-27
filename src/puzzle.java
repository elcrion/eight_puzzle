import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * Main class for the Puzzle implementation 
 * 
 * @author glebiakovlev
 *
 */


public class puzzle {


	public static Node goal;
	public static Node rootNode;
	public static int tilesNumber = 9;
	public static Heuriatics.HeurType selectedHeuristics;


	private static int[] goalPuzzle = {1,2,3,8,0,4,7,6,5};

	private static int[] easyPuzzle = {1,3,4,8,6,2,7,0,5};
	private static int[] mediumPuzzle = {2,8,1,0,4,3,7,6,5};
	private static int[] hardPuzzle = {5,6,7,4,0,8,3,2,1};





	/**
	 * TO RUN SET PARAMETERS :
	 * current state as list of tilesitems
	 * goal state as list of tiles
	 * algorythm to use : BFS,DFS , A*
	 * heuristic function : manhattan , other
	 *
	 * @param args
	 */



	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
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

		}


		goal = new Node(goalPuzzle);






		System.out.println("Please select a strategy :\n1:BFS\n2:DFS\n3:Iterative Deepening\n4:BestFirst search\n5: Uniform Cost Search\n6:A*2 Misplaced Tile\n7.A*3 Manhattan");
		String heur = scanner.nextLine();

		switch (Integer.parseInt(heur)){


			case 1:

				strategies.BFS(rootNode);

				break;

			case 2:

				strategies.DFS(rootNode,false);

				break;
            case 3:

                strategies.DFS(rootNode,true);

                break;

            case 4:

                selectedHeuristics =  Heuriatics.HeurType.BestFirst ;
                strategies.costSearch(rootNode,selectedHeuristics);
                break;


			case 5:

				selectedHeuristics =  Heuriatics.HeurType.UniformCost ;
				strategies.costSearch(rootNode,selectedHeuristics);
				break;

			case 6:

				selectedHeuristics =  Heuriatics.HeurType.TileCounter ;
				strategies.costSearch(rootNode,selectedHeuristics);
				break;

			case 7:

				selectedHeuristics =  Heuriatics.HeurType.Manhathan ;
				strategies.costSearch(rootNode,selectedHeuristics);
				break;


		}










	}


	/**
	 *  Read input parameters into appropriate array
	 * @param puzzle
	 */
	private static int[] readPuzzle( String puzzle) {
		String[] tilesString = puzzle.split(" ");
		int [] tilesArr = new int[tilesNumber];
		for (int i = 0; i < tilesNumber; i++) {

			tilesArr[i] = Integer.parseInt(tilesString[i]);
		}

		return tilesArr;
	}



}


/**
 * Node class for the puzzle
 * This represents a node in the search state diagram 
 * 
 */
class Node {
	
	/**
	 * successors : list of the current state nodes
	 * parentState : parent node in the diagram
	 * tileList : one dimensional array to store a list of puzzle tile for current list
	 * empty_tile : current empty index (location) 
	 * 
	 *
	 */
	

	public ArrayList<Node> successors = new ArrayList<Node>();
	public Node parentState;
	public int[] tileList = new int[puzzle.tilesNumber];
	public int emptyTile = 0;
	public enum Direction { Left , Right, Up, Down}
	public int cost;
	public Node selectedChild;
	public int heuristicCost;
	public int totalCost;
	public int depth = 0;
	private  boolean isVisited;
	public  boolean expanded = false;
	public  String direction ;



	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean visited) {
		isVisited = visited;
	}



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
	 * The method will check for the puzzle boundaries within array
	 * For now assume the puzzle is quadratic
	 * @param direction :  Left , Right, Up, Down
	 * @param state : tiles array (current board state)
	 * @param i : index of the empty tile
	 */
	public void MakeMove(Direction direction, int [] state, int i){
		
		
		int[] current_state = state.clone();
		
		int newIndex = 0;
		boolean constraint_check  = false;
		int columns = (int) Math.sqrt(puzzle.tilesNumber);
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

			if(parentState == null){
				successor.totalCost = cost;
			}else {
				successor.totalCost = cost + parentState.totalCost;
			}

			successor.expanded = true;


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

		int columns = (int) Math.sqrt(puzzle.tilesNumber);
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