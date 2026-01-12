package sol;

import java.util.*;

import src.AttributeSelection;
import src.IDataset;
import src.Row;

/**
 * A class representing a training dataset for the decision tree
 */
public class Dataset implements IDataset {

    private List<String> attributeList;
    private List<Row> dataObjects;
    private AttributeSelection selectionType;
    /**
     * Constructor for a Dataset object
     * @param attributeList - a list of attributes
     * @param dataObjects -  a list of rows
     * @param selectionType - an enum for which way to select attributes
     */
    public Dataset(List<String> attributeList, List<Row> dataObjects, AttributeSelection selectionType) {
        this.attributeList = attributeList;
        this.dataObjects = dataObjects;
        this.selectionType = selectionType;
    }

    /**
     * based on the type of selection type, either random, ascending alphabetical or descending
     * alphabetical, it will sort the attribute list to split on
     * @return the sorted attribute list according to the selection type
     */
    public String getAttributeToSplitOn() {
        if (this.selectionType == AttributeSelection.ASCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(0);
        }
        if (this.selectionType == AttributeSelection.DESCENDING_ALPHABETICAL) {
            return this.attributeList.stream().sorted().toList().get(this.attributeList.size() - 1);
        }
        if (this.selectionType == AttributeSelection.RANDOM) {
            Random rand = new Random();
            return this.attributeList.get(rand.nextInt(this.attributeList.size()));
        }

        throw new RuntimeException("Non-Exhaustive Selection Type");
    }

    /**
     * returns number of rows in dataset
     * @return size of data object
     */
    @Override
    public int size(){
        return this.dataObjects.size();
    }

    /**
     * returns copy of attribute list, allows tree nodes to know which
     * attributes are available
     * return copies to avoid accidental mutation
     * @return copy of attribute list
     */
    @Override
    public List<String> getAttributeList() {
        return new ArrayList<>(this.attributeList);
    }

    /**
     * returns the data as a list of rows
     * return copies to avoid accidental mutation
     * @return data as a list of rows
     */
    @Override
    public List<Row> getDataObjects() {
        return new ArrayList<>(this.dataObjects);
    }

    /**
     * returns selectiontype
     * @return selection type, one of the three provided
     */
    @Override
    public AttributeSelection getSelectionType() {
        return this.selectionType;
    }

    /**
     * get the unique labels of a target attribute
     * @param targetAttribute - attribute we are looking at
     * @return the unique label
     */
    public HashSet<String> uniqueLabels(String targetAttribute){
        HashSet<String> uniqueLabels = new HashSet<>();
        for (Row row : this.dataObjects) {
            uniqueLabels.add(row.getAttributeValue(targetAttribute));
        }
        return uniqueLabels;
    }




    /**
     * for given targetAttribute, checks if all rows in the dataset
     * have the same value
     * @param targetAttribute - attribute looking at
     * @param defaultValue - the default value if set is empty
     * @return the same label if all are the same, if not null
     */
    public String allSameLabel(String targetAttribute, String defaultValue){
        // return default value if the set is empty
        if (this.dataObjects.isEmpty()) return defaultValue;
        //get the value of the first row for the attribute
        String first = this.dataObjects.get(0).getAttributeValue(targetAttribute);
        //compare each subsequent row to the first row
        for (Row r : this.dataObjects) {
            if (!r.getAttributeValue(targetAttribute).equals(first)) {
                //if any difference, return null
                return null;
            }
        }
        //no difference, return the attribute
        return first;
    }

    /**
     * gets the most common attribute value for the target attribute
     * THIS WILL BECOME THE DEFAULT LABEL
     * @param targetAttribute - attribute we are looking at
     * @return the most common attribute value
     */
    public String majorityLabel(String targetAttribute){
        //create a hashmap with each value of the target attribute corresponding
        //to the number of times it appears throughout the rows
        HashMap<String, Integer> counts = new HashMap<>();
        //for each row
        for (Row r : this.dataObjects) {
            //identify the specific label/attribute value
            String value = r.getAttributeValue(targetAttribute);
            //if theres a key for value
            if (counts.containsKey(value)) {
                //update its counter by 1
                counts.put(value, counts.get(value) + 1);
            }
            else{
                //if not then set its counter to 1
                counts.put(value, 1);
            }
        }

        //create a majority string a count int to represent our tallies
        String majority = null;
        int maxCount = 0;
        //for each key in counts
        for (String value : counts.keySet()) {
            if (counts.get(value) > maxCount) {
                //if the value is bigger than maxcount set maxcount to its numerical value
                //and rename majority to the value
                maxCount = counts.get(value);
                majority = value;
            }
        }
        //finally return the string with the most occurences
        return majority;
    }


    /**
     * partitions the dataset by taking the given attribute
     * returns a hashmap with each attribute value mapping to a new
     * Dataset only containing those rows
     * @param attribute - attribute it takes in to partition on
     * @return hashmap with each attribute value mapping to the new data set only containing the rows
     */
    public HashMap<String, Dataset> partition(String attribute){
        //first make a temporary map for the specific attribute
        //to the list of rows that have this value
        HashMap<String, List<Row>> buckets = new HashMap<>();

        //iterate thru the rows of the dataset
        for (Row r : this.dataObjects) {
            //look up the rows value for this attribute
            String val = r.getAttributeValue(attribute);
            //if there is no bucket for this attribute, create one
            if(!buckets.containsKey(val)){
                buckets.put(val, new ArrayList<>());
            }
            //add the according row to the bucket created for it (based on value at that attribute)
            buckets.get(val).add(r);
        }

        //this is the map we will actually return
        HashMap<String, Dataset> partitions = new HashMap<>();

        //for each attribute value we looked at, create a new dataset for those rows
        for (String val : buckets.keySet()) {
            //copy of attribute list minus current attribute
            // do this so we dont edit the list one node up the tree
            List<String> newAttributes = new ArrayList<>(this.attributeList);
            newAttributes.remove(attribute);
            //get the rows associated with the specific value were looking at
            //on this pass of the for loop
            List<Row> rowsForVal = buckets.get(val);
            //add attribute value as the key to our hashmap, and as the value we will include
            //the rows stored in a new dataset with the list of new attributes, according rows and selection type
            partitions.put(val, new Dataset(newAttributes, rowsForVal, this.selectionType));
        }
        //return the hashmap after all attribute values and associated rows are accounted for
        return partitions;
    }

}