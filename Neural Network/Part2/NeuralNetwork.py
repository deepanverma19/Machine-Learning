
'''
@author deepanverma, mansikukreja
'''
import sys
import numpy as np
import random
import math

from collections import defaultdict
from dask.array import learn
from datashape.coretypes import float64

##argv[1] takes the path of preprocessedData, argv[2] takes percentageOfTrainingData, argv[3] takes the number of iterations to be done, argv[4] takes no of hidden layers
preprocessedData = sys.argv[1]
percentageOfTrainingData = sys.argv[2]
iterations = sys.argv[3]
numberOfHiddenLayers = sys.argv[4]

##Converting iterations taken as string from the console to int for algorithm purpose
iterations = int(iterations)

##Creating the list called hiddenLayerList that is used to store the input of the number of neurons in hidden layer in the next sys.argv[i]
hiddenLayerList = list()
i = 1
while i <= int(numberOfHiddenLayers):
    hiddenLayerList.append(int(sys.argv[4+i]))
    i = i + 1


##This function sigmoidActivationFunction is the activation function that takes x as input and returns x

def sigmoidActivationFunction(x):
    try:
        x = (1 / (1 + math.exp(-x)))
    except OverflowError:
        if x<0:
            x = 1
        else: 
            x = 0
    return x

##This function assigns the weights for neural network either -1,0 or +1 that takes layer, list and inputLength as the inputs and returns the matrixOfWeights

def assignWeights(hiddenLayer, hiddenLists, inputLength):
    matrixOfWeights = defaultdict(list)
    j = 1
    for i in hiddenLists:     
        matrixOfWeights[j].append(np.random.uniform(-1,1,(i,inputLength)))
        inputLength = i + 1
        j = j + 1
    matrixOfWeights[j].append(np.random.uniform(-1,1,(1,inputLength)))
    return matrixOfWeights

###########################################
##FORWARD PASS
###########################################
##This function is the forwardPassAlgorithm algorithm that takes matrixOfWeights and valuesOfInputLayer as input and returns the matrixOfNeuralNetwork	

def forwardPassAlgorithm(matrixOfWeights,valuesOfInputLayer):
    matrixOfNeuralNetwork = defaultdict(list)
    for i in matrixOfWeights.keys():
        matrixOfNeuralNetwork[i].extend(np.matmul(valuesOfInputLayer,np.transpose(matrixOfWeights[i][0])))
        matrixOfNeuralNetwork1 = list()
        for x in matrixOfNeuralNetwork[i]:
            x = sigmoidActivationFunction(x)
            matrixOfNeuralNetwork1.append(x)
        matrixOfNeuralNetwork[i] = matrixOfNeuralNetwork1
        valuesOfInputLayer = [1]+matrixOfNeuralNetwork[i]
    return matrixOfNeuralNetwork

###########################################
##BACKWARD PASS
###########################################

##This function backwardPassAlgorithm takes outputValue, targetValue, matrixOfNeuralNetwork, matrixOfWeights and inputLayer as inputs and returns the updated matrixOfWeights
def backwardPassAlgorithm(outputValue, targetValue, matrixOfNeuralNetwork, matrixOfWeights, inputLayer):
    learningRate = 0.1
    deltaMatrixValues = defaultdict(list)
    i = len(matrixOfWeights.keys())
      
    while i >= 1:
        if i == len(matrixOfWeights.keys()):
            deltaMatrixValues = backwardPassOutputUnit(outputValue, targetValue, matrixOfWeights)
            deltaMatrixValues.get(i)[0] = deltaMatrixValues.get(i)[0][0]
            i = i - 1        
        else:
            deltaMatrixValues = backwardPassHiddenUnit(outputValue, targetValue, matrixOfWeights, matrixOfNeuralNetwork, deltaMatrixValues)
            i = i - 1
    
	k=len(matrixOfNeuralNetwork.keys()) - 1
    
    while k>0:
        matrixOfNeuralNetwork[k-1]=[1]+matrixOfNeuralNetwork[k-1]
        if k==1:
            matrixOfWeights[k][0] += np.outer(deltaMatrixValues[k][0],inputLayer)*learningRate
        else:
            matrixOfWeights[k][0] += np.outer(deltaMatrixValues[k][0],matrixOfNeuralNetwork[k-1])*learningRate
        k-=1
    return matrixOfWeights

##This function backwardPassOutputUnit takes outputValue, targetValue, matrixOfWeights as inputs and returns deltaMatrixValues
def backwardPassOutputUnit(outputValue, targetValue, matrixOfWeights):
    deltaMatrixValues = defaultdict(list)
    deltaOp = list()
    deltaOp.append((targetValue-outputValue)*(1-outputValue)*outputValue)
    deltaMatrixValues[len(matrixOfWeights.keys())].append(deltaOp)
    return deltaMatrixValues   

##This function backwardPassHiddenUnit takes outputValue, targetValue, matrixOfWeights, matrixOfNeuralNetwork and deltaPreviousMatrix as input and returns deltaPreviousMatrix
def backwardPassHiddenUnit(outputValue, targetValue, matrixOfWeights, matrixOfNeuralNetwork, deltaPreviousMatrix):
    deltaMatrixValues = defaultdict(list)
    tot = 0
    i = len(matrixOfWeights.keys())
    while i>=1:
        temp = np.matmul(np.transpose(deltaPreviousMatrix.get(i)[0]),matrixOfWeights.get(i)[0])
        j = len(matrixOfNeuralNetwork[i-1])-1
        temp = temp[1:]
        while j>=0:
            temp[j] = matrixOfNeuralNetwork[i-1][j]*(1-matrixOfNeuralNetwork[i-1][j])*temp[j]
            j = j - 1
        deltaPreviousMatrix[i-1].append(temp)
        i = i -1       
    return deltaPreviousMatrix
    
##This function calculates the Output by taking matrixOfNeuralNetwork as input	
def calculateOutput(matrixOfNeuralNetwork): 
    return (matrixOfNeuralNetwork[len(matrixOfNeuralNetwork.keys())])

##inputLayerLine is the line read from preprocessedData
inputLayerLine = np.loadtxt(preprocessedData, dtype=np.float64, delimiter=',')
inputLayerLine = np.around(inputLayerLine, decimals=2)
##Setting x=0 for iterating over inputLayerLine array
x = 0
##Initialising lists
lists = [1]*len(inputLayerLine)
inputLayerLine = np.column_stack((lists, inputLayerLine))
##Including sample of values in finalList
finalList = random.sample(range(0,len(inputLayerLine)), len(inputLayerLine))
##Setting n=0 for counting
n = 0
##Initialising two lists inputLayerOfNN and testLayerOfNN
inputLayerOfNN = list()
testLayerOfNN = list()

for x in finalList:
    if n < int((len(inputLayerLine)*(float(percentageOfTrainingData)/100))):
        inputLayerOfNN.append(inputLayerLine[x])
    else:
        testLayerOfNN.append(inputLayerLine[x])
    n = n + 1

##Setting inputLayerOfNN and testLayerOfNN
inputLayerOfNN = np.array(inputLayerOfNN)
testLayerOfNN = np.array(testLayerOfNN)
##Initialising inputLayerOfNN1 to store data from inputLayerOfNN and classLabel to store inputLayerOfNN's classLabel
inputLayerOfNN1 = inputLayerOfNN[:,:len(inputLayerOfNN[0])-1]
classLabel = inputLayerOfNN[:,len(inputLayerOfNN[0])-1:]

##Initialising testLayerOfNN1 to store data from testLayerOfNN and classLabelTest to store testLayerOfNN's classLabel
testLayerOfNN1 = testLayerOfNN[:,:len(testLayerOfNN[0])-1]
classLabelTest = testLayerOfNN[:,len(testLayerOfNN[0])-1:]

##Initialising matrixOfWeights and calling assignWeights methods to store the initial values of weights
matrixOfWeights = defaultdict(list)
matrixOfWeights = assignWeights(int(numberOfHiddenLayers),hiddenLayerList,len(inputLayerOfNN1[0]))

##Assigning value of error as 999999
error = 999999
##Continue until error>0 OR iterations>0
while error>0 and iterations>0:
    error = 0
    k = 0
    for valuesOfInputLayer in inputLayerOfNN1:
        matrixOfNeuralNetwork = defaultdict(list)
        matrixOfNeuralNetwork = forwardPassAlgorithm(matrixOfWeights,valuesOfInputLayer)
        outputValue = calculateOutput(matrixOfNeuralNetwork)[0]
        matrixOfWeights = backwardPassAlgorithm(outputValue, classLabel[k], matrixOfNeuralNetwork, matrixOfWeights, valuesOfInputLayer)
        error = error + pow((classLabel[k][0]-outputValue),2)
        k = k + 1
    error = error/(2*int((len(inputLayerLine)*(float(percentageOfTrainingData)/100))))
    iterations = iterations - 1 

##Calculating the total error and test error
trainingError = error
error = 0
s = 0
for testLayerValues in testLayerOfNN1:
    matrixOfNeuralNetwork = defaultdict(list)
    matrixOfNeuralNetwork = forwardPassAlgorithm(matrixOfWeights,testLayerValues)
    outputValue = calculateOutput(matrixOfNeuralNetwork)[0]
    error = error + pow((classLabelTest[s][0]-outputValue),2)
    s = s + 1
error = error/(2*int((len(inputLayerLine)*(float(100-int(percentageOfTrainingData))/100))))
testError = error

##Printing the Hidden Layers and Output Layers generated from the Neural Network algorithm
print('\n')
for x in matrixOfWeights.keys():
    if x == len(matrixOfWeights):
        print("Input Layer ")
    else:
        print("Hidden Layer "+str(x))
    neuron = 1
    for s in matrixOfWeights[x][0]:
        print("Neuron"+str(neuron)+" weights  "+'  '.join(str(e) for e in s))
        neuron = neuron + 1
    print('\n')

##Printing the total training error and test error
print("Total Training Error: "+str(trainingError))
print("Total Test Error: "+str(testError))