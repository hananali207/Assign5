package accidentpack;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Program5Test {

	private Program5 program;
    private Report[] reports;

    String csvFile = "/Users/hananali/eclipse-workspace/Project1DS/src/accidentpack/accidents_small_sample.csv";

    @BeforeEach
    void setUp() throws IOException {
        program = new Program5();
        reports = Report.readReportsFromFile(csvFile);

    }
    @Test
    void testReportsOnAndAfterDate() {
        // Test case where there are no reports after the specified date in the specified state
    	int reportCount0 = program.reportsOnAndAfterDate(reports, "NY", "2023-01-01");
    	assertEquals(0, reportCount0);
    	
        int reportCount1 = program.reportsOnAndAfterDate(reports, "CA", "2022-09-07");
        assertEquals(3, reportCount1);

        int reportCount2 = program.reportsOnAndAfterDate(reports, "IL", "2022-09-08");
        assertTrue(reportCount2 < 6);

        int reportCount3 = program.reportsOnAndAfterDate(reports, "FL", "2022-09-08");
        assertEquals(5, reportCount3);
    }

}
