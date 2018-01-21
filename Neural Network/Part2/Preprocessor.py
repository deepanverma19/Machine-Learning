
'''
@author: deepanverma, mansikukreja
'''

import re
import numpy as np
import pandas as pd
import csv
import sys

from numpy import dtype, float_, float16
from collections import defaultdict
from sqlalchemy.sql.expression import false

##rawDatasetPath is the path of the raw dataset
##preprocessedDatasetPath is the path of the output preprocessedDatasetPath
rawDatasetPath = sys.argv[1]
preprocessedDatasetPath = sys.argv[2]

##Opening the rawDatasetPath streaming
f = open(rawDatasetPath)

##Reading all the lines of f stored using readLines()
lines = f.readlines()
f.close()

##rawDatasetList is the list that stores the entire dataset line by line
rawDatasetList = list()
preprocessedDatasetList = list()

##For each line in lines preprocessing the raw dataset and appending the listOfLines to rawDatasetList
for line in lines:
    if(line.find('?') != -1):
        continue
    if (line[0:1] == ' '):
        line = line[1:]
    if (line.find('\n') != '-1'):
        line = line[0:line.find('\n')]
    listOfLines = re.split(r'[ ,|;"]+',line)
    if ('' in listOfLines):
        continue
    rawDatasetList.append(listOfLines)

##listOfKeyValues is a list that stores the Key & Value as pair and creating a new list called listOfValues that stores only values
listOfKeyValues = defaultdict(list)
listOfValues = list()
length = 0

##Creating a list called temporaryList that stores the values from the listOfKeyValues and checks duplicacy

temporaryList = list()
for eachList in rawDatasetList:
    i = 0
    while i<len(eachList):
        try:
            eachList[i] = float(eachList[i])
        except ValueError:
            temporaryList = listOfKeyValues[i]
            if eachList[i] not in temporaryList:
                if(i == len(eachList)-1):
                    length = length + 1
                listOfKeyValues[i].append(eachList[i])
                eachList[i] = len(listOfKeyValues[i])-1
            else:
                eachList[i] = listOfKeyValues[i].index(eachList[i])
        i = i+1

##if length == 0 then length can be found as given below
if(length==0):
    length=(np.amax(rawDatasetList, axis=0)[len(rawDatasetList[0])-1])-(np.amin(rawDatasetList, axis=0)[len(rawDatasetList[0])-1])

listOfIndices = list(range(0, len(rawDatasetList[0])))
listOfStringIndices = listOfKeyValues.keys()

finalPreprocessingList = [x for x in listOfIndices if x not in listOfStringIndices] 
rawDatasetList = np.array(rawDatasetList)
preprocessedDatasetList = rawDatasetList[:,finalPreprocessingList]

##Initialising a new list called list1 that takes rawDatasetList and list1 = list1/length
list1 = rawDatasetList[:,len(rawDatasetList[0])-1:]
list1 = list1/length

##preprocessedDatasetList takes the preprocessedDatasetList minus mean of the list divided by the standard deviation
preprocessedDatasetList = (preprocessedDatasetList - np.mean(preprocessedDatasetList, axis=0))/np.std(preprocessedDatasetList, axis=0)
preprocessedDatasetList = np.around(preprocessedDatasetList, decimals=2)

for i in listOfStringIndices:
    preprocessedDatasetList = np.hstack((preprocessedDatasetList[:,:i], rawDatasetList[:,[i]], preprocessedDatasetList[:,i:]))
preprocessedDatasetList = np.hstack((preprocessedDatasetList[:,:len(rawDatasetList[0])-1],list1))

##Converting preprocessedDatasetList to list
preprocessedDatasetList=preprocessedDatasetList.tolist()

##Writing the preprocessedData to the output file for Neural Network algorithm
with open(preprocessedDatasetPath, "w") as output:
    writer = csv.writer(output, lineterminator='\n')
    writer.writerows(preprocessedDatasetList)