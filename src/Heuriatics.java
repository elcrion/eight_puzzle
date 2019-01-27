/**
 * Heuristics object  to calculate estimated cost
 */
public class Heuriatics {


    public enum HeurType { BestFirst,UniformCost, AstarManhattan, AstarTileCounter, AstarCombined}

    private Node node;
    private Node goal;
    private int Cost;

    /**
     * Constructor to pass node parameters
     * @param node current node
     * @param goal  goal node
     * @param type  Heuristics type
     */
    Heuriatics(Node node, Node goal ,HeurType type){

        this.node = node;
        this.goal = goal;

        switch (type){

            case AstarManhattan:

                Cost =    manhattanDistance();

                break;

            case AstarTileCounter:

                Cost =    misplacedTileCounter();

                break;

            case BestFirst:

                Cost =    manhattanDistance();

                break;

            case AstarCombined:

                Cost =  combinedHeuristics();

                break;

            default: Cost = 0;


        }


    }


    public int getCost() {
        return Cost;
    }

    /**
     * Hamming Distance
     * Count number of misplaced titles between Goal and current Node for Heuristics
     * @return number of misplaced tiles
     */
    public int misplacedTileCounter(){

        int counter = 0;
        for (int i = 0; i < this.node.tileList.length; i += 1)
            if (this.node.tileList[i] != goal.tileList[i])
                counter += 1;


        return counter;


    }


    /**
     * Calculate AstarManhattan distance for tiles
     * Check for the Manhattan distance between current state abd the goal state
     * @return distance
     */
    public int manhattanDistance(){

        int distance = 0;


        for (int i = 0; i < this.node.tileList.length; i += 1)
            for (int j = 0; j < goal.tileList.length; j += 1)
                if (this.node.tileList[i] == goal.tileList[j])
                    distance = distance + ((Math.abs(i % puzzle.tilesNumber - j % puzzle.tilesNumber)) + Math.abs(i / puzzle.tilesNumber + j / puzzle.tilesNumber));


        return  distance ;
    }


    /**
     * Combination of Manhattan distance and number of tiles along the path needed to be moved to reach the goal
     * This should have higher value than manhattan distance hence more Dominant Heuristics
     * @return distance
     */
    public  int combinedHeuristics(){

        int distance = misplacedTileCounter();
        int mDistance = manhattanDistance();



        return distance + mDistance;

    }




}
