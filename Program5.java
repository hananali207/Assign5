package accidentpack;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hananali
 */
public class Program5 {

    private Node root;

    public Program5() {

    }

    public static void main(String[] args) {
        Program5 tree = new Program5();
        String fileName = "accidents_small_sample.csv";
        Report report;

        try {
            Scanner sc = new Scanner(new File(fileName));
            sc.nextLine(); 
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] values = line.split(",");
                if (values.length >= 13) { 
                    int severity = Integer.parseInt(values[1].trim());
                    LocalDate startTime = LocalDate.parse(values[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDate endTime = LocalDate.parse(values[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
					String state = values[7].trim();
					double visibility = Double.parseDouble(values[10].trim());
					String weatherCondition = values[11].trim();
					String city = values[5].trim();
					String street = values[4].trim();
					String county = values[6].trim();
                                        
                                        System.out.println(state);
                } else {
                    System.err.println("Invalid data format in line: " + line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
  

    /**
     * Adds a new report to the binary search tree based on the report's start time.
     *
     * @param report The accident report to be added.
     */
  public void add(Report report) {
        root = addHelp(root, report);
    }

  /**
   * Helper method to add a report to the binary search tree based on start time.
   *
   * @param root   The root node of the subtree.
   * @param report The accident report to be added.
   * @return The root node of the modified subtree.
   */
    private Node addHelp(Node root, Report report) {
        if (root == null) {
            Node newNode = new Node(report);
            return newNode;
        }

        if (report.getStartTime().compareTo(root.report.getStartTime()) <= 0) {
            root.leftChildrenCount++;
            root.left = addHelp(root.left, report);
        } else {
            root.rightChildrenCount++;
            root.right = addHelp(root.right, report);
        }

        return root;
    }
    
    /**
     * Inserts a new report into the binary search tree based on the report's state.
     *
     * @param report The accident report to be inserted.
     */
    public void insertByState(Report report) {
        root = addByStateHelp(root, report);
    }

    /**
     * Helper method to insert a report into the binary search tree based on its state.
     *
     * @param root   The root node of the subtree.
     * @param report The accident report to be inserted.
     * @return The root node of the modified subtree.
     */
    private Node addByStateHelp(Node root, Report report) {
        if (root == null) {
            Node newNode = new Node(report);
            return newNode;
        }

        if (report.getState().compareTo(root.report.getState()) < 0) {
            root.left = addByStateHelp(root.left, report);
        } else if (report.getState().compareTo(root.report.getState()) > 0) {
            root.right = addByStateHelp(root.right, report);
        } else {
            if (root.report.getState().equals(report.getState())) {
                root.right = addHelp(root.right, report); 
            } else {
                root.left = addByStateHelp(root.left, report);
            }
        }

        return root;
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
