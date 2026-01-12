package sol;

import org.junit.Assert;
import org.junit.Test;
import src.AttributeSelection;
import src.DecisionTreeCSVParser;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;

/**
 * A class to test basic decision tree functionality on a basic training dataset
 */
public class BasicDatasetTest {
    String trainingPath = "data/animals.csv"; 
    String targetAttribute = "Type";
    TreeGenerator testGenerator;
    Dataset training;
    Row eagle;
    Row bass;
    List<String> attributeList;
    List<Row> dataObjects;
    /**
     * Constructs the decision tree for testing based on the input file and the target attribute.
     */
    @Before
    public void buildTreeForTest() {
        List<Row> dataObjects = DecisionTreeCSVParser.parse(this.trainingPath);


        List<String> attributeList = new ArrayList<>(dataObjects.get(0).getAttributes());
        this.training = new Dataset(attributeList, dataObjects, AttributeSelection.ASCENDING_ALPHABETICAL);
        // builds a TreeGenerator object and generates a tree for "foodType"
        this.testGenerator = new TreeGenerator();
//        this.testGenerator.generateTree(this.training, this.targetAttribute);
        this.eagle = new Row("test row (eagle)");
        this.eagle.setAttributeValue("Legs", "2");
        this.eagle.setAttributeValue("Habitat", "Air");

        this.bass = new Row("test row (bass)");
        this.bass.setAttributeValue("Legs", "0");
        this.bass.setAttributeValue("Habitat", "Water");
        this.bass.setAttributeValue("Diet", "Omnivore");
    }

    /**
     * Tests the expected classification of the "tangerine" row is a fruit
     */
    @Test
    public void testClassification() {
        // makes a new (partial) Row representing the tangerine from the example
        Row eagle = new Row("test row (eagle)");
        eagle.setAttributeValue("Legs", "2");
        eagle.setAttributeValue("Habitat", "Air");
        eagle.setAttributeValue("Diet", "Carnivore");
//       Assert.assertEquals("Bird", this.testGenerator.getDecision(eagle));
        Row bass = new Row("test row (bass)");
        bass.setAttributeValue("Legs", "0");
        bass.setAttributeValue("Habitat", "Water");
        bass.setAttributeValue("Diet", "Omnivore");
//        Assert.assertEquals("Fish", this.testGenerator.getDecision(bass));

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
        Assert.assertEquals(AttributeSelection.ASCENDING_ALPHABETICAL, this.training.getSelectionType());
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
     * test nested attribute nodes
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
}
