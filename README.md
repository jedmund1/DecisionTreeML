# Decision Tree Classifier (Built from Scratch in Java)

## Overview
This project implements a **supervised decision tree classification algorithm from first principles** in Java, without using external machine learning libraries. The objective was to understand and implement the full learning pipeline — from data ingestion and recursive model construction to evaluation on unseen data.

The classifier learns decision rules from labeled, structured datasets and generalizes to new inputs through attribute-based splits.

---

## Key Features
- Supervised **decision tree classifier** implemented entirely from scratch  
- Recursive tree-construction algorithm supporting **arbitrary tree depth**  
- Robust handling of **unseen attribute values** via default decision logic  
- Modular **object-oriented design** separating datasets, nodes, and tree generation  
- Training and evaluation on multiple real-world datasets  
- Comprehensive unit and system-level testing  

---

## Datasets
The model was trained and evaluated on multiple structured datasets, including:
- Mushrooms  
- Villains  
- Songs  
- Candidates  

Each dataset was split into training and test sets to assess generalization performance.

---

## Model Performance
Across evaluated datasets:
- **Training accuracy:** 95–100%  
- **Test accuracy:** 70–85% (dataset-dependent)  

To reduce sensitivity to attribute ordering, the model was trained using **randomized attribute selection** and evaluated over **100 independent iterations per dataset**, with results averaged to estimate expected performance.

---

## Implementation Details
- **Language:** Java  
- **Algorithm:** Classical decision tree classification (supervised machine learning)  
- **Tree construction:** Recursive top-down splitting  
- **Evaluation metric:** Classification accuracy  
- **Testing:** JUnit-based unit and system tests  

---

## Project Structure
```text
src/
├── dataset/
│   ├── Dataset.java
│   ├── IDataset.java
│   └── CSVParser.java
├── tree/
│   ├── TreeGenerator.java
│   ├── ITreeGenerator.java
│   ├── AttributeNode.java
│   └── DecisionLeaf.java
├── tests/
│   ├── DatasetTests.java
│   ├── TreeGeneratorTests.java
│   └── SystemTests.java
