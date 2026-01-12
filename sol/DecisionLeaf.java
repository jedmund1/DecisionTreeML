package sol;

import src.ITreeNode;
import src.Row;

/**
 * A class representing a leaf in the decision tree.
 */
public class DecisionLeaf implements ITreeNode {

    private String decision;

    /**
     * This is the constructor and I intialize the this.decision
     * @param decision string that is returned if called
     */
    public DecisionLeaf(String decision) {
        this.decision = decision;
    }

    /**
     * This method returns the decision at the leaf
     * @param forDatum the datum to lookup a decision for
     * @return decision at leaf
     */
    @Override
    public String getDecision(Row forDatum) {
        return this.decision;
    }
}
