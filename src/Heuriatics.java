/**
 * Heuristics object  to calculate estimated cost
 */
public class Heuriatics {


    public enum HeurType { BestFirst,UniformCost,Manhathan,TileCounter }

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

            case Manhathan:

                Cost =    manhathanDistance();

                break;

            case TileCounter:

                Cost =    misplacedTileCounter();

                break;

            default: Cost = 0;


        }


    }


    public int getCost() {
        return Cost;
    }

    /**
     * Count number of misplaced titles for Heuristics
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
     * Calculate Manhathan distance for tiles
     * @return distance
     */
    public int manhathanDistance(){

        int distance = 0;


        for (int i = 0; i < this.node.tileList.length; i += 1)
            for (int j = 0; j < goal.tileList.length; j += 1)
                if (this.node.tileList[i] == goal.tileList[j])
                    distance = distance + ((Math.abs(i % puzzle.tilesNumber - j % puzzle.tilesNumber)) + Math.abs(i / puzzle.tilesNumber + j / puzzle.tilesNumber));


        return  distance ;
    }


}
