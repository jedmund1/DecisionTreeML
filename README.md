# DecisionTreeML
Decision Tree Classifier (Built from Scratch in Java)
Overview

This project implements a supervised decision tree classification algorithm from first principles in Java, without using external machine learning libraries. The goal was to understand and implement the full learning pipeline — from data ingestion and recursive model construction to evaluation on unseen data.

The classifier learns decision rules from labeled, structured datasets and generalizes to new inputs through recursive attribute-based splits.

Key Features

Supervised decision tree classifier implemented entirely from scratch

Recursive tree-construction algorithm supporting arbitrary tree depth

Robust handling of unseen attribute values via default decision logic

Modular object-oriented design with separate abstractions for datasets, nodes, and tree generation

Training and evaluation on multiple real-world datasets

Comprehensive unit and system-level testing

Datasets

The model was trained and evaluated on multiple structured datasets, including:

Mushrooms

Villains

Songs

Candidates

Each dataset was split into training and test sets to evaluate generalization performance.

Model Performance

Across evaluated datasets:

Training accuracy: 95–100%

Test accuracy: 70–85% (dataset-dependent)

To reduce sensitivity to attribute ordering, the model was trained using randomized attribute selection and evaluated over 100 independent iterations per dataset, with results averaged to estimate expected performance.

Implementation Details

Language: Java

Algorithm: Classical decision tree classification (supervised machine learning)

Tree construction: Recursive top-down splitting

Evaluation metric: Classification accuracy

Testing: JUnit-based unit and system tests

Project Structure
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

Motivation

Rather than relying on machine learning libraries, this project focuses on algorithmic understanding, correctness, and robustness. Implementing the model from scratch required explicit reasoning about recursion, data partitioning, overfitting, and generalization — skills central to quantitative finance, software engineering, and applied data analysis.

Future Improvements

Support for continuous-valued features

Alternative split criteria (e.g., information gain, Gini impurity)

Pruning strategies to reduce overfitting

Performance benchmarking against library-based implementations
