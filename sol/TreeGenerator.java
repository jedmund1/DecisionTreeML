package sol;

import src.ITreeGenerator;
import src.ITreeNode;
import src.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class that implements the ITreeGenerator interface used to generate a decision tree
 */
public class TreeGenerator implements ITreeGenerator<Dataset> {

    private ITreeNode root;
    private String targetAttribute;

    /**
     * This is the tree generator constructor that initializes this.root and targetAttribute
     */
    public TreeGenerator() {
        this.root = null;
        this.targetAttribute = null;
    }

    /**
     * This method depends on the helper method to build the tree. The method checks to make
     * sure the data sets are not empty and removes the target attribute from the attribute list.
     * It then calls the helper method on this updated attribute list without the target attribute.
     * @param trainingData    the dataset to train on
     * @param targetAttribute the attribute to predict
     */
    @Override
    public void generateTree(Dataset trainingData, String targetAttribute) {
        if (trainingData == null || trainingData.size() == 0) {
            throw new IllegalStateException("Training data is empty!");
        }
        if (targetAttribute == null) {
            throw new IllegalStateException("Target attribute does not exist!");
        }

        this.targetAttribute = targetAttribute;

        //copy attribute list and remove the target attribute so that we never split on it
        List<String> attributes = new ArrayList<>(trainingData.getAttributeList());
        attributes.remove(targetAttribute);

        //build a new dataset with the updated attributeList which no longer has the target attribute
        Dataset updatedDataSet = new Dataset(attributes, trainingData.getDataObjects(),
                trainingData.getSelectionType());

        this.root = this.generateTreeHelper(updatedDataSet); //store the result in the root
    }

    /**
     * This method returns the decision of the tree generated
     * @param datum the datum to lookup a decision for
     * @return the decision for the tree generated
     */
    @Override
    public String getDecision(Row datum) {
        if (this.root == null) {
            throw new IllegalStateException("Tree has yet to been generated!");
        }
        return this.root.getDecision(datum);
    }

    /**
     * This helper method does most of the work of creating many small sub trees to create one
     * main big tree.
     * @param data - data we are using to generate the subtrees
     * @return the attribute node, or the decision leaf
     */
    private ITreeNode generateTreeHelper(Dataset data) {
        //computes the majority decision and used for base cases and as the default at this node
        String defaultDecision = data.majorityLabel(this.targetAttribute);

        //Base case 1: if all rows share the same target label, make a leaf with that unanimous label
        if (data.allSameLabel(this.targetAttribute, defaultDecision) != null) {
            return new DecisionLeaf(data.allSameLabel(this.targetAttribute, defaultDecision));
        }

        //Base case 2: if no attributes remain to split on, return a leaf with the majority label
        if (data.getAttributeList().isEmpty()) {
            return new DecisionLeaf(defaultDecision);
        }

        //recursive case from here on (the split case when not at a leaf)

        //1. choose an attribute to split on
        String splitAttribute = data.getAttributeToSplitOn();
        //2. partition the rows by that attribute
        HashMap<String,Dataset> parts = data.partition(splitAttribute);
        //3. build each child subtree
        HashMap<String, ITreeNode> children = new HashMap<>(); //build one child subtree per distinct value
        for (String val : parts.keySet()) {
            Dataset childData = parts.get(val); //minidataSet for this value
            ITreeNode childSubtree = this.generateTreeHelper(childData); //recurse
            children.put(val, childSubtree); //put kv pair where val --> subtree
        }
        return new AttributeNode(splitAttribute, defaultDecision, children);
    }


}
