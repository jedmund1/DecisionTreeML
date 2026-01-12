package sol;

import org.junit.Before;
import org.junit.Test;
import src.AttributeSelection;
import src.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests dataset methods
 */
public class DatasetTests {

    private Dataset dataset;

    /**
     * this part sets up the test, sets up the data to be tested later on
     */
    @Before
    public void setUp() {
        // Create Rows
        HashMap<String, String> row1Map = new HashMap<>();
        row1Map.put("color", "red");
        row1Map.put("shape", "circle");
        row1Map.put("size", "small");
        row1Map.put("label", "fruit");
        Row r1 = new Row(row1Map);

        HashMap<String, String> row2Map = new HashMap<>();
        row2Map.put("color", "red");
        row2Map.put("shape", "square");
        row2Map.put("size", "small");
        row2Map.put("label", "fruit");
        Row r2 = new Row(row2Map);

        HashMap<String, String> row3Map = new HashMap<>();
        row3Map.put("color", "blue");
        row3Map.put("shape", "circle");
        row3Map.put("size", "large");
        row3Map.put("label", "vegetable");
        Row r3 = new Row(row3Map);

        // Add rows to list
        List<Row> rows = new ArrayList<>();
        rows.add(r1);
        rows.add(r2);
        rows.add(r3);

        // Create attribute list
        List<String> attributes = new ArrayList<>();
        attributes.add("color");
        attributes.add("shape");
        attributes.add("size");
        attributes.add("label");

        // Initialize Dataset with ASCENDING_ALPHABETICAL selection (arbitrary for test)
        this.dataset = new Dataset(attributes, rows, AttributeSelection.ASCENDING_ALPHABETICAL);
    }

    /**
     * This test tests the test size method, should return 3 here as that is the dataset size
     */
    @Test
    public void testSize() {
        assertEquals(3, this.dataset.size());
    }

    /**
     * Test the unique label methods, should return 2 labels here as 2 unique labels
     */
    @Test
    public void testUniqueLabels() {
        // The "label" column has "fruit" and "vegetable"
        assertEquals(2, this.dataset.uniqueLabels("label").size());
    }


    @Test
    public void testAllSameLabel() {
        // There is no attribute where all rows are same
        assertNull(this.dataset.allSameLabel("color", "default"));
        // If we test a single-row dataset
        List<Row> singleRow = new ArrayList<>();
        singleRow.add(this.dataset.getDataObjects().get(0));
        Dataset singleDataset = new Dataset(this.dataset.getAttributeList(), singleRow, AttributeSelection.RANDOM);
        assertEquals("red", singleDataset.allSameLabel("color", "default"));
    }

    @Test
    public void testMajorityLabel() {
        // "fruit" occurs twice, "vegetable" once
        assertEquals("fruit", this.dataset.majorityLabel("label"));
    }

    @Test
    public void testPartition() {
        // Partition by color
        var partitions = this.dataset.partition("color");
        assertEquals(2, partitions.size());
        assertTrue(partitions.containsKey("red"));
        assertTrue(partitions.containsKey("blue"));

        // Check that "red" partition has 2 rows
        assertEquals(2, partitions.get("red").size());
        // Check that "blue" partition has 1 row
        assertEquals(1, partitions.get("blue").size());
    }

}

