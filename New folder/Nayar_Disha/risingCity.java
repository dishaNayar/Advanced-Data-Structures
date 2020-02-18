import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

/*This is the main driver class. It reads the input file, uses the minheap and the red black tree data structures
 to construct buildings.  */
public class risingCity
{

    public static MinHeap minHeap = new MinHeap();
    public static RedBlackTree rbTree = new RedBlackTree();
    public static BuildingDetails workingBuilding = null;

    public static void main(String[] arg)
    {
        // the function enables the output to be written in a .txt file
        enableOutputToFile();
        // variable inputData stores the commands in the input file
        HashMap<Integer,String> inputData = readFromFile(arg[0]);

        // globalTime is the global counter and its value increase by 1 each day.
        int globalTime = 0;
        // currentExecutedTime keeps track of the the number of days the current building is being constructed for
        int currentExecutedTime = 0;

        do{
            //Work on input Data
            if(inputData.containsKey(globalTime)) //  takes O(1) time
            {
                String data = inputData.get(globalTime);
                // insert the new building in the heap and the red black tree.
                if(data.contains("Insert")){
                    String insertData = inputData.get(globalTime);
                    int num = Integer.parseInt(insertData.substring(insertData.indexOf("(")+1, insertData.indexOf(",")));
                    int tTime = Integer.parseInt(insertData.substring(insertData.indexOf(",")+1, insertData.indexOf(")")));
                    BuildingDetails newBuilding = new BuildingDetails(num, 0, tTime);
                    minHeap.insert(newBuilding);
                    rbTree.insert(newBuilding);
                }
                // print the buildings depending on the print statement
                if(data.contains("Print")){
                    String printData = inputData.get(globalTime);
                    String subString = printData.substring(printData.indexOf("(")+1, printData.indexOf(")"));
                    // Case 1 - printing buildings in a range
                    if(subString.contains(",")){
                        String[] rangeValues = subString.split(",");
                        rbTree.printRange(Integer.parseInt(rangeValues[0]), Integer.parseInt(rangeValues[1]));
                    }
                    // case 2 - printing a building
                    else{
                        int buildingNumber = Integer.parseInt(subString);
                        rbTree.printBuilding(buildingNumber);
                    }

                }
                inputData.remove(globalTime);
            }
            //
            workingBuilding = deleteBuilding(workingBuilding, globalTime);
            currentExecutedTime = workOnBuilding(globalTime, currentExecutedTime);
            // increment the global time and repeat till the inputData is not empty
            globalTime++;

        }while(inputData.size() > 0);

       /* The below loop continues the construction of the building even after the input is over.
         The loop will exit when the current size of the heap is 0, i.e all the building have been constructed */
        do{
            deleteBuilding(workingBuilding, globalTime);

            currentExecutedTime = workOnBuilding(globalTime, currentExecutedTime);

            globalTime++;
        }while(minHeap.currentSize != 0);

    }

    // The method delete's the building from the heap and red black tree if its execution time equals total time.
    private static BuildingDetails deleteBuilding(BuildingDetails workingBuilding, int globalTime) {
        if(workingBuilding != null && minHeap.currentSize > 0){
            if(workingBuilding.executed_time == workingBuilding.total_time)
            {
                // print the building number along with its day of completion
                System.out.println("(" + workingBuilding.buildingNum + "," + (globalTime) + ")");
                minHeap.remove();
                rbTree.delete(workingBuilding);
                workingBuilding = null;
            }
        }
        return workingBuilding;
    }

    // The method picks a building to construct and increments the execution time by 1.
    private static int workOnBuilding(int globalTime, int currentExecutedTime) {

        if(minHeap.workingNode() != null){
            // if the working node was deleted, then pick up another node
            if (workingBuilding != minHeap.workingNode()){
                currentExecutedTime = 0;
                workingBuilding = minHeap.workingNode();
            }
            // if the current building has been worked on for 5 days, then rearrange the heap and pick a building again
            else if (currentExecutedTime == 5)
            {
                minHeap.rearrangeMinHeap();
                if(minHeap.workingNode() != null){
                    workingBuilding = minHeap.workingNode();
                    currentExecutedTime = 0;
                }
            }
            // increment the execution time of the current building
            if(workingBuilding != null){
                workingBuilding.executed_time ++;
            }

            currentExecutedTime++;
        }
        return  currentExecutedTime;
    }

    // The method reads the input file and stores the data in a hash map
    private static HashMap<Integer,String> readFromFile(String fileName)
    {
        LinkedHashMap<Integer,String> inputData = new LinkedHashMap<Integer, String>();
        try
        {
            File filePath = new File(fileName);
            Scanner fileData = new Scanner(filePath);
            while(fileData.hasNextLine())
            {
                String[] inputLine = fileData.nextLine().split(": ");
                inputData.put(Integer.parseInt(inputLine[0]), inputLine[1]);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return inputData;
    }

    // The method prints the output in an output.txt file.
    private static void enableOutputToFile()
    {
        try
        {
            // Creating a PrintStream with the output_file.txt path
            PrintStream o = new PrintStream(new File("output_file.txt"));
            System.setOut(o);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
}

