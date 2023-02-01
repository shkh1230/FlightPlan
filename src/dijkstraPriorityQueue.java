import java.io.*;
import java.util.*;

public class dijkstraPriorityQueue
{
    //Declare the private members variables.
    private int type1,type2;
    private String CostInTime[][], SVertex, DVertex;
    private List<String> listOfTheNodes;
    private Set<String> List;
    private List<Root> ListOfVisitedNode;
    private HashMap<String, Integer> minimalDistance;
    private HashMap<String, Integer> distOfVertices;
    private PriorityQueue<City> priorityQueue;

    // prove the definition of the function to create priority queue of the nodes
    public dijkstraPriorityQueue(List<String> listOfTheNodes)
    {
        this.listOfTheNodes = listOfTheNodes;
        minimalDistance = new HashMap<String,Integer>();
        distOfVertices = new HashMap<String,Integer>();
        List = new HashSet<String>();
        ListOfVisitedNode = new ArrayList<Root>();
        priorityQueue = new PriorityQueue<City>(new City());
    }

    // Find the neighboring node in the file
    private void findTheAdjacent(String evaluationNode, Root evaluationNodeList)
    {
        int d = -1;
        int newd = -1;

        for (int i = 0; i<CostInTime.length; i++)
        {
            if(!CostInTime[i][0].equals(evaluationNode))
                continue;
            String target;
            for (int j = 0; j < listOfTheNodes.size(); j++)
            {
                target = listOfTheNodes.get(j);
                if(!CostInTime[i][1].equals(target))
                    continue;
                if (!List.contains(target))
                {
                    d = Integer.parseInt(CostInTime[i][type1]);
                    newd = minimalDistance.get(evaluationNode) + d;
                    if (newd < minimalDistance.get(target))
                    {
                        minimalDistance.replace(target,newd);
                        distOfVertices.replace(target,distOfVertices.get(evaluationNode) +
                                Integer.parseInt(CostInTime[i][type2]));
                        for (Root path : ListOfVisitedNode)
                        {
                            if(path.exists(target))
                                path.delete(target);
                            break;
                        }
                        evaluationNodeList.add(target);
                    }
                    priorityQueue.add(new City(target,minimalDistance.get(target)));
                }
            }
        }
    }

    // return the node which has the minimum distance
    private String getVetex()
    {
        String node = priorityQueue.remove().city;
        return node;
    }

    // Dijkstra,s algorithm definition
    public void dijkastra(String costInTime[][], String requiredPath[])
    {
        String vartexEvaluation;
        SVertex = requiredPath[0];
        DVertex = requiredPath[1];
        if(requiredPath[2].equalsIgnoreCase("C"))
        {
            type1 = 2;
            type2 = 3;
        }
        else
        {
            type1 = 3;
            type2 = 2;
        }


        this.CostInTime = costInTime;


        for (String vertex:listOfTheNodes)
        {
            minimalDistance.put(vertex, Integer.MAX_VALUE);
            distOfVertices.put(vertex, Integer.MAX_VALUE);
        }

        priorityQueue.add(new City(SVertex, 0));
        minimalDistance.replace(SVertex,0);
        distOfVertices.replace(SVertex, 0);
        while (!priorityQueue.isEmpty())
        {
            vartexEvaluation = getVetex();
            Root evaluationNodeList = new Root();
            evaluationNodeList.setNode(vartexEvaluation);
            List.add(vartexEvaluation);
            findTheAdjacent(vartexEvaluation, evaluationNodeList);
            if(!isThereVertex(ListOfVisitedNode, vartexEvaluation))
                ListOfVisitedNode.add(evaluationNodeList);
        }
    }
    // Check the node in the file
    private boolean isThereVertex(List<Root> listOfVisitedVertex, String node)
    {
        for (Root p : ListOfVisitedNode)
        {
            if(p.getNode().equals(node))
                return true;
        }
        return false;
    }

    // Find the destination node in the file to complete the path.
    private static List<String> PathofTheRoot(List<Root> visitedCity, String target)
    {
        List<String> completePath = new ArrayList<String>();
        for( Root path : visitedCity)
        {
            if(!path.exists(target))
                continue;
            completePath = PathofTheRoot(visitedCity, path.getNode());
            completePath.add(target);
            return completePath;
        }
        completePath.add(target);
        return completePath;
    }

    // Start the main method of the program
    public static void main(String[] arg) throws FileNotFoundException
    {
//Declare the variables.
        String timeCost[][],PathList[][];
        BufferedReader dataOfTheFlight, requestedData;
        List<String> listOfTheNode;
        PrintWriter out = new PrintWriter("output1.txt");

        try
        {
// Read the data from the files
            dataOfTheFlight = new BufferedReader(new FileReader("FlightData1.txt"));
            requestedData = new BufferedReader(new FileReader("Requested.txt"));

            String string;

            listOfTheNode = new ArrayList<String>();
            timeCost = new String[Integer.parseInt(dataOfTheFlight.readLine())][4];
            PathList = new String[Integer.parseInt(requestedData.readLine())][3];

            int i=0,j; String _node;

// Make tokens of the data of the flightData file
            while((string = dataOfTheFlight.readLine()) != null)
            {
                j=0;
                StringTokenizer data = new StringTokenizer(string,"|");
                int k = 1;
                while(k<=2)
                {
                    if(!listOfTheNode.contains(_node = data.nextToken()))
                    {
                        timeCost[i][j++] = _node;
                        listOfTheNode.add(_node);
                    }
                    else
                        timeCost[i][j++] = _node;
                    k++;
                }

                while(data.hasMoreTokens())
                {
                    timeCost[i][j++] = data.nextToken();
                }
                i++;
            }
            i=0;

// Make tokens of the data of the requestedFlightData file
            while((string = requestedData.readLine()) != null)
            {
                j=0;
                StringTokenizer data = new StringTokenizer(string,"|");
                while(data.hasMoreTokens())
                    PathList[i][j++] = data.nextToken();
                i++;
            }

            i=1;

// Check the type of the cost
            for(String requsetedPath[] : PathList)
            {
                if(!(listOfTheNode.contains(requsetedPath[0])&& listOfTheNode.contains(requsetedPath[1])))
                {
                    out.println("Path can not be find !!!!!");
                    continue;
                }
                String _type,_otherType;

                if(requsetedPath[2].equals("T"))
                {
                    _type = "Time";
                    _otherType = "Cost";
                }

                else
                {
                    _type = "Cost";
                    _otherType = "Time";
                }

                dijkstraPriorityQueue priorityQueue = new dijkstraPriorityQueue(listOfTheNode);

// Call the dijkstra_algorithm method to run the Dijkstra's algorithm
                priorityQueue.dijkastra(timeCost, requsetedPath);

                out.println("Flight "+i+": "+priorityQueue.SVertex+", "+
                        priorityQueue.DVertex+" ("+_type+")");
                for (String vertex:listOfTheNode)
                {
                    if(!vertex.equals(priorityQueue.DVertex))
                        continue;
                    List<String> list = PathofTheRoot(priorityQueue.ListOfVisitedNode,
                            priorityQueue.DVertex);
                    for (int k = 0; k < list.size(); k++)
                    {
                        if(k == list.size()-1 )
                            out.print(list.get(k)+". ");
                        else
                            out.print(list.get(k)+" --> ");
                    }
                    out.println(_type+": " + priorityQueue.minimalDistance.get(vertex)+" "
                            +_otherType+": "+priorityQueue.distOfVertices.get(vertex));
                    break;
                }
                i++;
            }

        } catch (Exception e)
        {
            System.out.println("Exception has occured:" + e.toString());
        }
        out.close();
    }
}

// Class
class City implements Comparator<City>
{
    public String city;
    public int cost;

    public City()
    {
    }

    // Compare the nodes.
    @Override
    public int compare(City node1, City node2)
    {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }

    public City(String city, int cost)
    {
        this.city = city;
        this.cost = cost;
    }
}



// Create the class
class Root
{
    private List<String> root;
    private String city;
    // Get the node
    public String getNode()
    {
        return this.city;
    }
    // Add the node in the array List
    public void add(String city)
    {
        root.add(city);
    }
    //Delete the node from the list
    public void delete(String city)
    {
        root.remove(city);
    }
    // Check if the node exist or not
    public Boolean exists(String city)
    {
        if(root.contains(city))
            return true;
        return false;
    }
    //Create the arrayList
    public Root()
    {
        root = new ArrayList<String>();
    }
    // Set the node
    public void setNode(String Node)
    {
        this.city = Node;
    }
}
