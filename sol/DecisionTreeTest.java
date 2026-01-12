package sol;

import static org.junit.Assert.*;
import static src.DecisionTreeTester.makeDataset;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import src.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class containing the tests for methods in the TreeGenerator and Dataset classes
 */
public class DecisionTreeTest {
    //TODO: Write more unit and system tests! Some basic guidelines that we will be looking for:
    // 1. Small unit tests on the Dataset class testing the IDataset methods - Done
    // 2. Small unit tests on the TreeGenerator class that test the ITreeGenerator methods
    // 3. Tests on your own small dataset (expect 70% accuracy on testing data, 95% on training data)
    // 4. Test on the villawins dataset (expect 70% accuracy on testing data, 95% on training data)
    // 5. Tests on the mushrooms dataset (expect 70% accuracy on testing data, 95% on training data)
    // Feel free to write more unit tests for your own helper methods -- more details can be found in the handout!

    //1.
    String trainingPath = "data/animals.csv"; // TODO: replace with your own input file
    String targetAttribute = "Type"; // TODO: replace with your own target attribute
    TreeGenerator testGenerator;
    Dataset training;
    Row eagle;
    Row bass;
    List<String> attributeList;
    List<Row> dataObjects;
    Dataset dataset;
    /**
     * This test shows syntax for a basic assertEquals assertion -- can be deleted
     */
    @Test
    public void testAssertEqual() {
        assertEquals(2, 1 + 1);
    }

    /**
     * This test shows syntax for a basic assertTrue assertion -- can be deleted
     */
    @Test
    public void testAssertTrue() {
        assertTrue(true);
    }

    /**
     * This test shows syntax for a basic assertFalse assertion -- can be deleted
     */
    @Test
    public void testAssertFalse() {
        assertFalse(false);
    }




    /**
     * This just sets up the tree for resting beforehand to test later on
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);


        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator();
//        TODO: Uncomment this once you've implemented generateTree
//        this.testGenerator.generateTree(training, this.targetAttribute);
        this.eagle = new Row("test row (eagle)");
        this.eagle.setAttributeValue("Legs", "2");
        this.eagle.setAttributeValue("Habitat", "Air");

        this.bass = new Row("test row (bass)");
        this.bass.setAttributeValue("Legs", "0");
        this.bass.setAttributeValue("Habitat", "Water");
        this.bass.setAttributeValue("Diet", "Omnivore");



        //extra dataset tests
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



    @Test
    public void testUniqueLabels1() {
        // The "label" column has "fruit" and "vegetable"
        assertEquals(2, this.dataset.uniqueLabels("label").size());
    }


    @Test
    public void testAllSameLabel1() {
        // There is no attribute where all rows are same
        assertNull(this.dataset.allSameLabel("color", "default"));
        // If we test a single-row dataset
        List<Row> singleRow = new ArrayList<>();
        singleRow.add(this.dataset.getDataObjects().get(0));
        Dataset singleDataset = new Dataset(this.dataset.getAttributeList(), singleRow, AttributeSelection.RANDOM);
        assertEquals("red", singleDataset.allSameLabel("color", "default"));
    }

    @Test
    public void testMajorityLabel1() {
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
    /**
     * test the counter
     */
    @Test
    public void testSize() {
        Assert.assertEquals(10, this.training.size());
    }

    /**
     * test the getter
     */
    @Test
    public void testGetDataObjects() {
        List<Row> dataObjects = this.training.getDataObjects();
        Assert.assertEquals("4",  dataObjects.get(0).getAttributeValue("Legs"));
        Assert.assertEquals("Carnivore",  dataObjects.get(1).getAttributeValue("Diet"));
        Assert.assertEquals("Bird",  dataObjects.get(2).getAttributeValue("Type"));

    }

    /**
     * test the getter, its stored in an unorganized hashSet so we cannot determine
     * order, this will later be fixed by the getAttributeToSplit on function and its implementation
     */
    @Test
    public void testGetAttributeList() {
        List<String> expectedAttributes = new ArrayList<>();
        expectedAttributes.add("Legs");
        expectedAttributes.add("Habitat");
        expectedAttributes.add("Diet");
        expectedAttributes.add("Type");

        List<String> actualAttributes = this.training.getAttributeList();
        for(String attribute : expectedAttributes) {
            Assert.assertTrue(actualAttributes.contains(attribute));
        }
        for(String attribute : actualAttributes) {
            Assert.assertTrue(expectedAttributes.contains(attribute));
        }
    }

    /**
     * test the getter
     */
    @Test
    public void testGetSelectionType(){
        Assert.assertEquals(AttributeSelection.ASCENDING_ALPHABETICAL,
                this.training.getSelectionType());
    }


    /**
     * we want the decisionLeaf to be hardcoded, independent of the row
     */
    @Test
    public void testDecisionLeaf(){
        DecisionLeaf leaf = new DecisionLeaf("Bird");
        Assert.assertEquals("Bird", leaf.getDecision(this.eagle));
        Assert.assertEquals("Bird", leaf.getDecision(this.bass));
    }

    /**
     * use example data to test functionality
     */
    @Test
    public void testAttributeNode(){
        // create child nodes for an attribute split on "Legs", lets say this
        // does lead to any additional attributenodes (this is the case we will test in treegenerator)
        HashMap<String, ITreeNode> legsChildren = new HashMap<>();
        legsChildren.put("2", new DecisionLeaf("Bird"));
        legsChildren.put("4", new DecisionLeaf("Amphibian"));
        legsChildren.put("0", new DecisionLeaf("Fish"));

        //create corresponding attributeNode
        AttributeNode node = new AttributeNode("Legs", "Mammal",  legsChildren);

        //test our cases from before statement
        //this shows, based on the node we've created, each row we parse
        //will return the correct getDecision value based on the node
        Assert.assertEquals("Bird", node.getDecision(this.eagle));
        Assert.assertEquals("Fish", node.getDecision(this.bass));

        //not included in the legs dataset, should point to 4 legs however, so amphibian
        Row turtle = new Row("Turtle");
        turtle.setAttributeValue("Legs", "4");
        Assert.assertEquals("Amphibian", node.getDecision(turtle));

        //not included, nor has a valid number of legs, return default
        Row spider = new Row("Spider");
        spider.setAttributeValue("Legs", "8");
        Assert.assertEquals("Mammal", node.getDecision(spider));
    }

    /**
     * test nested attribute nodes (TESTS GET DECISION)
     */
    @Test
    public void testAttributeNode2(){
        //make a nested call to itreenode that represents taking legs as the first attribute
        //and in the case there is 2 legs, we need to look at habitat to tell what the target value is
        HashMap<String, ITreeNode> habitatChildren = new HashMap<>();
        habitatChildren.put("Air", new DecisionLeaf("Bird"));
        habitatChildren.put("Water", new DecisionLeaf("Amphibian"));
        // create child nodes for an attribute split on "Legs", lets say this
        // does lead to any additional attributenodes (this is the case we will test in treegenerator)
        HashMap<String, ITreeNode> legsChildren = new HashMap<>();
        legsChildren.put("2", new AttributeNode("Habitat", "Mammal", habitatChildren));
        legsChildren.put("4", new DecisionLeaf("Amphibian"));
        legsChildren.put("0", new DecisionLeaf("Fish"));

        AttributeNode root = new AttributeNode("Legs", "Reptile", legsChildren);

        Assert.assertEquals("Bird", root.getDecision(this.eagle));
        Assert.assertEquals("Fish", root.getDecision(this.bass));

        //not included, nor has a valid number of legs, return default for the legs node
        Row spider = new Row("Spider");
        spider.setAttributeValue("Legs", "8");
        Assert.assertEquals("Reptile", root.getDecision(spider));

        //not included, has valid number of legs, but node it points to is habitat
        // and it doesnt have a valid habitat, return default for the habitat node
        Row mystery = new Row("Spider");
        mystery.setAttributeValue("Legs", "2");
        mystery.setAttributeValue("Habitat", "Land");
        Assert.assertEquals("Mammal", root.getDecision(mystery));
    }

    /**
     * test to confirm the function of getAttributeToSplitOn
     * ascending starts from first in alphabet
     * descending starts from last
     * random will take from the attributeList at random
     */
    @Test
    public void testGetAttributeToSplitOn(){
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);
        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        Dataset ds1 = new Dataset(attributeList, dataObjects,  AttributeSelection.ASCENDING_ALPHABETICAL);
        Dataset ds2 = new Dataset(attributeList, dataObjects,  AttributeSelection.DESCENDING_ALPHABETICAL);
        Dataset ds3 = new Dataset(attributeList, dataObjects,  AttributeSelection.RANDOM);
        String ds3chosen = ds3.getAttributeToSplitOn();

        Assert.assertEquals("Diet", ds1.getAttributeToSplitOn());
        Assert.assertEquals("Type", ds2.getAttributeToSplitOn());
        Assert.assertTrue(attributeList.contains(ds3chosen));
    }




    // 2 itreegenerator methods:

    /**
     * test if we can generate a tree and if get decisionworks as expected on a constructed dataset
     */
    @Test
    public void testGenerateTreeSmallDataset() {
        //make data
        List<Row> rows = new ArrayList<>();
        Row frog = new Row("Frog");
        frog.setAttributeValue("Legs", "4");
        frog.setAttributeValue("Habitat", "Water");
        frog.setAttributeValue("Type", "Amphibian");
        Row robin = new Row("Robin");
        robin.setAttributeValue("Legs", "2");
        robin.setAttributeValue("Habitat", "Air");
        robin.setAttributeValue("Type", "Bird");
        rows.add(frog);
        rows.add(robin);

        //make attribute list
        List<String> attributeList = new ArrayList<>();
        attributeList.add("Legs");
        attributeList.add("Habitat");
        attributeList.add("Type");

        //generate tree
        Dataset smallSet = new Dataset(attributeList, rows, AttributeSelection.ASCENDING_ALPHABETICAL);
        TreeGenerator gen = new TreeGenerator();
        gen.generateTree(smallSet, "Type");

        //test outcomes
        Row bird = new Row("Bird");
        bird.setAttributeValue("Legs", "2");
        bird.setAttributeValue("Habitat", "Air");
        Assert.assertEquals("Bird", gen.getDecision(bird));
        Row platypus = new Row("Platypus");
        platypus.setAttributeValue("Legs", "4");
        platypus.setAttributeValue("Habitat", "Water");
        Assert.assertEquals("Amphibian", gen.getDecision(platypus));
    }

    /**
     * test on getdecision, throws exception if called before generatetree
     */
    @Test(expected = IllegalStateException.class)
    public void testGetDecisionBeforeGenerateThrows() {
        this.testGenerator.getDecision(this.eagle);
    }


    /**
     * test generatetree, throws if given an empty dataset
     */
    @Test(expected = IllegalStateException.class)
    public void testGenerateTreeEmptyDatasetThrows() {
        List<Row> emptyData = new ArrayList<>();
        List<String> attributes = new ArrayList<>();
        Dataset emptyDataset = new Dataset(attributes, emptyData, AttributeSelection.ASCENDING_ALPHABETICAL);
        this.testGenerator.generateTree(emptyDataset, this.targetAttribute);
    }

    /**
     * test generatetree, throws if given a null target attribute.
     */
    @Test(expected = IllegalStateException.class)
    public void testGenerateTreeNullTargetThrows() {
        this.testGenerator.generateTree(this.training, null);
    }

    /**
     * test to trust that getdecision will return the default for values we havent seen before (uses csv)
     */
    @Test
    public void testGetDecisionOnUnseenValue() {
        this.testGenerator.generateTree(this.training, this.targetAttribute);
        Row alien = new Row("alien");
        alien.setAttributeValue("Legs", "99");
        alien.setAttributeValue("Diet", "Herbivore");
        alien.setAttributeValue("Habitat", "Land");

        String decision = this.testGenerator.getDecision(alien);

        Assert.assertEquals("Mammal", decision);
    }

    /**
     * This tests for the uniqueLabels method to ensure the size is right and the method
     * collects all the distinct values for type
     */
    @Test
    public void testUniqueLabels() {
        Assert.assertEquals(5, this.training.uniqueLabels("Type").size());
        Assert.assertTrue(this.training.uniqueLabels("Type").contains("Mammal"));
        Assert.assertTrue(this.training.uniqueLabels("Type").contains("Bird"));
        Assert.assertTrue(this.training.uniqueLabels("Type").contains("Fish"));
        Assert.assertTrue(this.training.uniqueLabels("Type").contains("Amphibian"));
        Assert.assertTrue(this.training.uniqueLabels("Type").contains("Reptile"));
    }

    /**
     * Testing the allSameLabel method with an empty data set that should return the default
     */
    @Test
    public void testAllSameLabelEmpty() {
        Dataset empty = new Dataset(
                new ArrayList<>(this.training.getAttributeList()), new ArrayList<>(),
                this.training.getSelectionType());
        Assert.assertEquals("default",
                empty.allSameLabel("Type", "default"));
    }

    /**
     * This test tests the allSameLabel where all types match to ensure the method works properly
     * when the list is not empty
     */
    @Test
    public void testAllSameLabel() {
        List<Row> onlyBirds = new ArrayList<>(); //build dataset of only birds so can test
        for (Row r : this.training.getDataObjects()) {
            if (r.getAttributeValue("Type").equals("Bird")) {
                onlyBirds.add(r);
            }
        }
        Dataset birdsOnly = new Dataset(this.training.getAttributeList(), onlyBirds,
                this.training.getSelectionType());
        Assert.assertEquals("Bird", birdsOnly.allSameLabel("Type",
                "default"));
    }

    /**
     * This test tests the allSameLabel method with mixed labels, so should return null
     */
    @Test
    public void testAllSameLabelMixed() {
        Assert.assertNull(this.training.allSameLabel("Type", "default"));
    }

    /**
     * This test tests the majorityLabel method and should return land as it is the clear
     * majority in the training data
     */
    @Test
    public void testMajorityLabel() {
        Assert.assertEquals("Land", this.training.majorityLabel("Habitat"));
    }

    /**
     * For this test, I test the partition method on legs, please refer to the notes below for
     * more details
     */
    @Test
    public void testPartitionLegs() {
        HashMap<String, Dataset> parts = this.training.partition("Legs");

        //expect 3 different types of keys of the legs numbers, so 0, 2, and 4
        Assert.assertEquals(3, parts.size());
        Assert.assertTrue(parts.containsKey("0"));
        Assert.assertTrue(parts.containsKey("2"));
        Assert.assertTrue(parts.containsKey("4"));

        //this part checks that each subset has correct number of rows
        //2 animals with 0 legs, the fish and the reptile
        Assert.assertEquals(2, parts.get("0").size());
        //4 animals with 2 legs
        Assert.assertEquals(4, parts.get("2").size());
        //4 animals with 4 legs
        Assert.assertEquals(4, parts.get("4").size());

        //legs should be removed from the child attribute list
        assertFalse(parts.get("4").getAttributeList().contains("Legs"));
        assertFalse(parts.get("2").getAttributeList().contains("Legs"));
        assertFalse(parts.get("0").getAttributeList().contains("Legs"));

        //however other attributes remain
        assertTrue(parts.get("4").getAttributeList().contains("Habitat"));
        assertTrue(parts.get("4").getAttributeList().contains("Diet"));
        assertTrue(parts.get("4").getAttributeList().contains("Type"));
    }




    //3 own dataset test

    private static final String TARGET_ATTRIBUTE = "Type";
    private static final String TRAINING_PATH = "data/animals.csv";
    private static final String TESTING_PATH = "data/animals-testing.csv";

    //4 villain dataset

    private static final String TARGET_ATTRIBUTE1 = "isVillain";
    private static final String TRAINING_PATH1 = "data/villains/training.csv";
    private static final String TESTING_PATH1 = "data/villains/testing.csv";

    //5 mushroom dataset

    private static final String TARGET_ATTRIBUTE2 = "isPoisonous";
    private static final String TRAINING_PATH2 = "data/mushrooms/training.csv";
    private static final String TESTING_PATH2 = "data/mushrooms/testing.csv";

    //copied format from the testing in decisiontreetester for the 3 datasets required

    /**
     * Main to test the accuracy
     */
    public static void main(String[] args) {
        //our own dataset
        DecisionTreeTester<TreeGenerator, Dataset> tester;
        try {
            tester = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

            // Load training data
            Dataset trainingData = makeDataset(TRAINING_PATH, Dataset.class);
            double trainingAccuracy =
                    tester.getDecisionTreeAccuracy(trainingData, trainingData, TARGET_ATTRIBUTE);
            System.out.println("Accuracy on training data: " + trainingAccuracy);

            // Load testing data
            Dataset testingData = makeDataset(TESTING_PATH, Dataset.class);
            int numIters = 100;
            double testingAccuracy =
                    tester.getAverageDecisionTreeAccuracy(trainingData, testingData, TARGET_ATTRIBUTE, numIters);
            System.out.println("Accuracy on testing data: " + testingAccuracy);

        } catch (InstantiationException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }


        //4 villain dataset

        DecisionTreeTester<TreeGenerator, Dataset> tester1;
        try {
            tester1 = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

            // Load training data
            Dataset trainingData = makeDataset(TRAINING_PATH1, Dataset.class);
            double trainingAccuracy =
                    tester1.getDecisionTreeAccuracy(trainingData, trainingData, TARGET_ATTRIBUTE1);
            System.out.println("Accuracy on training data: " + trainingAccuracy);

            // Load testing data
            Dataset testingData = makeDataset(TESTING_PATH1, Dataset.class);
            int numIters = 100;
            double testingAccuracy =
                    tester1.getAverageDecisionTreeAccuracy(trainingData, testingData, TARGET_ATTRIBUTE1, numIters);
            System.out.println("Accuracy on testing data: " + testingAccuracy);

        } catch (InstantiationException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }



        //5 mushroom dataset

        DecisionTreeTester<TreeGenerator, Dataset> tester2;
        try {
            tester2 = new DecisionTreeTester<>(TreeGenerator.class, Dataset.class);

            // Load training data
            Dataset trainingData = makeDataset(TRAINING_PATH2, Dataset.class);
            double trainingAccuracy =
                    tester2.getDecisionTreeAccuracy(trainingData, trainingData, TARGET_ATTRIBUTE2);
            System.out.println("Accuracy on training data: " + trainingAccuracy);

            // Load testing data
            Dataset testingData = makeDataset(TESTING_PATH2, Dataset.class);
            int numIters = 100;
            double testingAccuracy =
                    tester2.getAverageDecisionTreeAccuracy(trainingData, testingData, TARGET_ATTRIBUTE2, numIters);
            System.out.println("Accuracy on testing data: " + testingAccuracy);

        } catch (InstantiationException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
