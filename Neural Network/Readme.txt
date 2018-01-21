Part1.pdf
---------
It contains the solution for first part.

The folder consists of two python files:

1) Preprocessor.py
--------------------
This python file reads the rawDataset at arg[1] and the path of preprocessedDataset where it needs to be stored.(sample attached in the screenshots in the Report)
It generates the preprocessedDataset which is used for neural network algorithm.

2) NeuralNetwork.py
--------------------
This python file consists of NeuralNetwork algorithm. It generates the total training error and test error.
It takes in various arguments defined below:
arg[1]: path of the preprocessedDataset generated from Preprocessor.py
arg[2]: training percent ie percentage of dataset to be used for training
arg[3]: max iterations i.e max number of iterations the algorithm should run
arg[4]: number of hidden layers
arg[5] and so on number of neurons to be specified in each hidden layers

