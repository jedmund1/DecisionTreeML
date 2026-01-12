package sol;

import java.util.List;
import src.ITreeNode;
import java.util.HashMap;
import src.Row;

/**
 * A class representing an inner node in the decision tree.
 */
public class AttributeNode implements ITreeNode {

    private String attributeName;
    private String defaultDecision;
    private HashMap<String, ITreeNode> children;

    /**
     * Constructor for attributeNode
     * @param attributeName - name of the attribute
     * @param defaultDecision - default decision of that node
     * @param children - where the value "leads" to
     */
    public AttributeNode(String attributeName, String defaultDecision,
                         HashMap<String, ITreeNode> children) {
        this.attributeName = attributeName;
        this.defaultDecision = defaultDecision;
        this.children = children;
    }

    /**
     * This method helps to recurse through the node, if the node value doesn't exist,
     * it returns the defualt decision.
     * @param forDatum the datum to lookup a decision for
     * @return the default decision if node value doesn't exist
     */
    @Override
    public String getDecision(Row forDatum) {
        //get value of the node's attribute for the datum
        String attributeValue = forDatum.getAttributeValue(this.attributeName);

        //if matching child then recurse into it
        if (this.children.containsKey(attributeValue)) {
            return this.children.get(attributeValue).getDecision(forDatum);
        }

        return this.defaultDecision; //return the default decision if doesn't exist
    }


}
