package accidentpack;

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
	
    private static final String CSV_FILE_PATH = "/Users/hananali/eclipse-workspace/Project1DS/src/accidentpack/accidents.csv";

    private Node root;

    public Program5() {

    }
    Dictionary<String, Node> rootReportDict = new Hashtable<>(); 
    
    
    public static void main(String[] args) throws IOException {
        Program5 program = new Program5();
        Report[] reports = Report.readReportsFromFile(CSV_FILE_PATH);
        
        System.out.println(reports.length);

       
        	
       
        Scanner userInput = new Scanner(System.in);
        
        System.out.println("Enter state initials:");
        String enteredState = userInput.nextLine();
        System.out.println("Enter start date (yyyy-MM-dd):");
        String enteredStartDate = userInput.nextLine();
     

        int reportCount = program.reportsOnAndAfterDate(reports, enteredState, enteredStartDate);
        System.out.println("Number of reports on and after " + enteredStartDate + " in state " + enteredState + ": " + reportCount);
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
    private int reportsOnAndAfterDateHelp(Node root, String startDate) {
        if (root == null)
            return 0;

        //System.out.println("Checking node: " + root.report.getState() + ", " + root.report.getStartTime());

        if (startDate.compareTo(root.report.getStartTime()) <= 0) {
            return root.rightChildrenCount + 1 + reportsOnAndAfterDateHelp(root.left, startDate);
        } else {
            return reportsOnAndAfterDateHelp(root.right, startDate);
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

        if (report.getStartTime().compareTo(root.report.getStartTime()) <= 0) {
        } else {
            // Insert as right child only if start times are strictly equal
            if (report.getStartTime().equals(root.report.getStartTime())) {
                root.rightChildrenCount++;
                root.right = addHelp(root.right, report);
            } else {
                root.leftChildrenCount++;
                root.left = addHelp(root.left, report);
            }
        }
        return root;
    }
    
   

    
    
   
/*
    public void searchForClosestAfter(LocalDate startDate, Node root) {
    	  if (startDate.compareTo(root.report.getStartTime()) <= 0) {
              searchForClosestAfter(startDate, root.left);
          } else {
              root.rightChildrenCount++;
              root.right = addHelp(root.right, report);
          }
    }
*/
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
