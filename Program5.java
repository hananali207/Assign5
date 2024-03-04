package accidentpack;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.time.LocalDate;

/**
 * @author jasonpic
 *
 * @author hananali
 */
public class Program5 {
	
    private static final String CSV_FILE_PATH = "/Users/jason/Downloads/accidents.csv";

    private Node root;

    public Program5() {

    }
    static Dictionary<String, Node> rootReportDict = new Hashtable<>(); 
    static Program5 program5 = new Program5();
    
    public static void main(String[] args) throws IOException {
        Program5 program = new Program5();
        Report[] reports = Report.readReportsFromFile(CSV_FILE_PATH);
        
        System.out.println(reports.length);
        
        
		
        //program5.printInorder(rootReportDict.get("ME"));
        	
       
        Scanner userInput = new Scanner(System.in);
        
        System.out.println("Enter state initials:");
        String enteredState = userInput.nextLine();
        System.out.println("Enter start date (yyyy-MM-dd):");
        String enteredStartDate = userInput.nextLine();
        ArrayList<Report>thisStatesReports = program5.stateArrayBuilder(reports, enteredState);
        program5.populateTrees(thisStatesReports);
        timeRecursiveFieldCount(enteredState, enteredStartDate);
        timeRecursiveCount(enteredState, enteredStartDate);
        timeNonRecursiveCount(reports, enteredState, enteredStartDate);
    }
    
    //Times the recursive report count method and prints out results
    protected static int timeRecursiveFieldCount(String state, String startDate){
		long startTime = System.nanoTime();
		int counted = program5.reportsOnAndAfterDateRecursive(rootReportDict.get(state), startDate);
		long endTime = System.nanoTime();
        System.out.println("Number of reports on and after " + startDate + " in state " + state + ": " + counted);
		System.out.println((endTime - startTime)/1_000_000_000.0 + " seconds to count reports using fields.");
		return counted;
	}	
    
    //Times the recursive report (without using childrenCount fields) method count and prints out results
    protected static int timeRecursiveCount(String state, String startDate){
		long startTime = System.nanoTime();
		int counted = program5.reportsOnAndAfterDateNoFields(rootReportDict.get(state), startDate);
		long endTime = System.nanoTime();
        System.out.println("Number of reports on and after " + startDate + " in state " + state + ": " + counted);
		System.out.println((endTime - startTime)/1_000_000_000.0 + " seconds to count reports using no fields, recursively.");
		return counted;
	}	
    
    //Times the non-recursive report count method and prints out results
    protected static int timeNonRecursiveCount(Report[] reports, String state, String startDate){
		long startTime = System.nanoTime();
		int counted = program5.reportsOnAndAfterDate(reports, state, startDate);
		long endTime = System.nanoTime();
        System.out.println("Number of reports on and after " + startDate + " in state " + state + ": " + counted);
		System.out.println((endTime - startTime)/1_000_000_000.0 + " seconds to count reports using array.");
		return counted;
	}	
    
    /**
     * Builds an ArrayList of Report objects which contains only Report objects within the given state form the entered reports
     * 
     * @param reports Array of all Report objects
     * @param state State of which we want to filter reports by to receive only reports of state
     * @return thisStateArr The ArrayList of Report objects from state
     */
    public ArrayList stateArrayBuilder(Report[] reports, String state) {
    	ArrayList thisStateArr = new ArrayList<Report>();
    	
    	for(Report r: reports) {
    		if(r.getState().equals(state)) {
    			thisStateArr.add(r);
    		}
    	}
    	return thisStateArr;
    }
    

    /**
     * Counts the number of reports on and after a specified date for a given state.
     *
     * @param reports     The array of reports to search through.
     * @param state       The state to filter reports by.
     * @param startDate   The start date to count reports from.
     * @return The number of reports on and after the given date in the specified state.
     */
    public int reportsOnAndAfterDate(Report[] reports, String state, String startDate) {
        int count = 0;
        for (Report report : reports) {
            if (report.getState().equals(state) && report.getStartTime().compareTo(startDate) >= 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Helper method for `reportsOnAndAfterDate` to recursively traverse the BST.
     *
     * @param root      The current node in the BST traversal.
     * @param startDate The start date to count reports from.
     * @return The number of reports on and after the given date in the subtree rooted at `root`.
     */
    private int reportsOnAndAfterDateRecursive(Node root, String startDate) {
        if (root == null)
            return 0;


        if (startDate.compareTo(root.report.getStartTime()) <= 0) {
            return root.rightChildrenCount + 1 + reportsOnAndAfterDateRecursive(root.left, startDate);
        } else {
            return reportsOnAndAfterDateRecursive(root.right, startDate);
        }
    }
    
    /**
     * Counts amount of reports on or after the date entered for a BST. BST usually represents all of one state's accidents.
     * 
     * @param root A Node object
     * @param startDate the date of which we want all reports on or after
     * @return The integer count of reports on or after given date
     */
    private int reportsOnAndAfterDateNoFields(Node root, String startDate) {
    	  if (root == null)
              return 0;


          if (startDate.compareTo(root.report.getStartTime()) <= 0) {
              return 1 + reportsOnAndAfterDateNoFields(root.left, startDate) + reportsOnAndAfterDateNoFields(root.right, startDate);
          } else {
              return reportsOnAndAfterDateNoFields(root.right, startDate);
          }
    }


    /**
     * Adds a new report to the binary search tree based on the report's start time.
     *
     * @param report The accident report to be added.
     */
    public void add(Report report) {
        String state = report.getState();

        // Check if the state exists in the dictionary
        if (((Hashtable<String, Node>) rootReportDict).containsKey(state)) {
            // If the state exists, retrieve the root node for that state
            root = rootReportDict.get(state);
        } else {
            // If the state doesn't exist, create a new root node for that state
            root = null; // Or initialize with a new Node if necessary
        }

        // Add the report to the binary search tree
        root = addHelp(root, report);

        // Update the root node in the dictionary
        rootReportDict.put(state, root);
    }
  
    /**
     * Helper method for add to recursively insert a report into the BST.
     *
     * @param root   The current node in the BST traversal.
     * @param report The accident report to be inserted.
     * @return The updated root node of the subtree after insertion.
     */
    private Node addHelp(Node root, Report report) {
        if (root == null) {
            return new Node(report);
        }

        if (report.getStartTime().compareTo(root.report.getStartTime()) < 0) {
        	root.leftChildrenCount++;
            root.left = addHelp(root.left, report);
        } else {
            // Insert as right child if start times are strictly equal or greater than root
                root.rightChildrenCount++;
                root.right = addHelp(root.right, report);
        }
        return root;
    }
    
   /**
    * Method which populates the BST for the given ArrayList, usually one states Report objects. 
    * 
    * @param reportArr The ArrayList<Report> to construct a BST for. 
    */
   public void populateTrees(ArrayList<Report> reportArr) {
	   for(Report r : reportArr) {
		   add(r);
	   }
   }
   
    private class Node {
        Report report;
        int leftChildrenCount;
        int rightChildrenCount;
        Node left;
        Node right;

        public Node(Report report) {
            this.report = report;
            this.leftChildrenCount = 0;
            this.rightChildrenCount = 0;
            this.left = null;
            this.right = null;
        }
    }
}
