import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<Point>();

        // initialize lists
        HashMap<Point, Node> openList = new HashMap<>();
        HashMap<Point, Node> closedList = new HashMap<>();

        // remember: path should not include start node
        // Add start node to open list and mark as current node
        // use hash map for quick lookup

        Node startNode = new Node(start, null, 0, heuristic(start, end), 0 + heuristic(start, end));
        openList.put(start, startNode);
        Node currentNode = startNode;

        // Priority queue to find lowest f value

        PriorityQueue<Node> sortedList = new PriorityQueue<Node>(Comparator.comparing(Node::getF));
        sortedList.add(startNode);

        while(!sortedList.isEmpty()) {

            // set current node to smallest f-value
            if(!openList.isEmpty())
                currentNode = sortedList.poll();
            else
                return path;

            // build path if reached target

            if(withinReach.test(currentNode.getCurrPt(), end)) {
                return buildPath(currentNode);
            }
            // Analyze all valid adjacent nodes not on the closed list

            List<Point> neighbors = potentialNeighbors.apply(currentNode.getCurrPt())
                    .filter(canPassThrough) // check obstacles
                    .filter(p -> !closedList.containsKey(p)) // not on closed list
                    .collect(Collectors.toList());

            for(Point pt : neighbors) {

                int newG = currentNode.getG() + 1;
                int newH = heuristic(pt, end);
                int newF = newG + newH;
                Node newNode = new Node(pt, currentNode, newG, newH, newF);

                // Add to openList if not already in it

                if(!openList.containsKey(pt)) {
                    openList.put(pt, newNode);
                    sortedList.add(newNode);
                } else { // if already in the list, recalculate g value

                    Node temp = openList.get(pt);

                    // If g-value is better(lower) than previous, replace
                    if (newG < temp.getG()) {
                        sortedList.remove(temp);
                        sortedList.add(newNode);
                        openList.remove(pt);
                        openList.put(pt, newNode);
                    }
                }

                // Move current node to the closed list
                openList.remove(currentNode.getCurrPt());
                closedList.put(currentNode.getCurrPt(), currentNode);
            }

        }

        return path;
    }

    // calculate h value
    public int heuristic(Point start, Point end) {
        return(Math.abs(end.x - start.x) + Math.abs(end.y - start.y));
    }

    // build the actual path
    public List<Point> buildPath(Node parse) {
        List<Point> fullPath = new LinkedList<>(); // array list of points
        while(parse.getPrev() != null) {
            fullPath.add(parse.getCurrPt());
            parse = parse.getPrev();
        }
        Collections.reverse(fullPath);
        return fullPath;
    }
}
