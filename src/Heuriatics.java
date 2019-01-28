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
     * @return distance estimation
     */
    public int manhattanDistance(){

        int distance = 0;


        for (int i = 0; i < this.node.tileList.length; i += 1)
            for (int j = 0; j < goal.tileList.length; j += 1)
                if (this.node.tileList[i] == goal.tileList[j])
                    distance = distance + ((Math.abs(i % Puzzle.tilesNumber - j % Puzzle.tilesNumber)) + Math.abs(i / Puzzle.tilesNumber + j / Puzzle.tilesNumber));


        return  distance ;
    }




    /**
     * Combination of Manhattan distance and number of tiles along the path needed to be moved to reach the goal,
     * so calculating total amount of manhattan distances needed
     * where  mDistance is the current manhattan distance
     * and mDistance-1 is the number of tiles to be moved from current position
     * This should have higher value than manhattan distance hence more Dominant Heuristics
     * @return distance estimation
     */
    public  int combinedHeuristics(){

        int distance = 0;
        int mDistance = 0;

        for (int i = 0; i < this.node.tileList.length; i += 1)
            for (int j = 0; j < goal.tileList.length; j += 1)
                if (this.node.tileList[i] == goal.tileList[j])
                    mDistance =  ((Math.abs(i % Puzzle.tilesNumber - j % Puzzle.tilesNumber)) + Math.abs(i / Puzzle.tilesNumber + j / Puzzle.tilesNumber));
        distance = distance + 2 * mDistance - 1;

        return distance;

    }




}
