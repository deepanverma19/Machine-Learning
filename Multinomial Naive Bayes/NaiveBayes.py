'''
@author deepanverma, mansikukreja
'''

from __future__ import division
from collections import Counter
from collections import defaultdict
from stop_words import get_stop_words
import sys
import os
import string as st
import re
import datetime
import time
import math
import numpy as np

## trainMultinomialNaiveBayes function trains the model using trainingDataset takes listOfNewsgroup and trainingDataset path as arguments
## and returns the refinedVocabulary, prior list, and conditionalProbabilityOfClass

def trainMultinomialNaiveBayes(listOfNewsgroup,trainingDataset):
    count = 1
    ## stop_words consists of all the stop words of the english language
    stop_words = get_stop_words('english')
    ##totalNumberOfDocuments stores the totalNumberOfDocuments present in all the documents
    totalNumberOfDocuments = 0
    ##V is total vocabulary that stores all the words of the dataset
    V = list()
    ##refinedVocabulary is the vocabulary that doesn't include stop_words
    refinedVocabulary = list()
    ##countOfDocumentsInClass is a list that stores the count of Documents in a given class
    countOfDocumentsInClass = list()
    ## prior is a list that stores the words of previous words
    prior = list()
    textOfEachClass = list() 
    for newsgroup in sorted(os.listdir(trainingDataset)):
        if (os.path.isdir(os.path.join(trainingDataset,newsgroup)) and count<=len(listOfNewsgroup)):
            ##localDocuments stores the number of localDocuments of a particular Dataset
            localDocuments = 0
            testClass = ""
            for listOfFiles in os.listdir(os.path.join(trainingDataset,newsgroup)):
                totalNumberOfDocuments = totalNumberOfDocuments + 1
                localDocuments = localDocuments + 1
                f = open(os.path.join(trainingDataset,newsgroup,listOfFiles))
                readFileClass = ""
                readFileClass = re.sub(r'[<|>|?|_|,|!|:|;|(|)|\"|=|-|$|\\|/|*|\'|+|\[|\]|#|$|%|^|?|~|`]', r'', str(f.readlines()))
                testClass = testClass+" "+readFileClass
            V.extend(testClass.split())
            textOfEachClass.append(testClass)
            countOfDocumentsInClass.append(localDocuments)
            count = count + 1
    ##V stores the complete vocabulary of the dataset, storing only the unique words from the vocabulary by taking the set(V)
    V = set(V)
	##refinedVocabulary stores the words of the dataset without the stop_words
    for word in V:
        if word not in stop_words:
		    refinedVocabulary.append(word)
    ##print(len(refinedVocabulary))	
    
	i = 0
	##conditionalProbabilityOfClass is a list that stores the various conditionalProbabilityOfClass
    conditionalProbabilityOfClass = list()
    
    for classes in listOfNewsgroup:
	    ##Tct is a list that appends the cText[word]
        Tct = list()
        conditionalProbabilityOfTerm = defaultdict(list)
        prior.append(countOfDocumentsInClass[i]/totalNumberOfDocuments)
        cText = Counter(textOfEachClass[i].split())
        for word in refinedVocabulary:
            Tct.append(cText[word])
        j = 0
        sumOfTct = sum(Tct)
        lengthOfTct = len(Tct)
        ##Calculating the conditionalProbabilityOfClass by appending ((Tct[j]+1)/(sumOfTct+lengthOfTct))
        for word in refinedVocabulary:
            conditionalProbabilityOfTerm[word].append((Tct[j]+1)/(sumOfTct+lengthOfTct))
            j = j + 1
        conditionalProbabilityOfClass.append(conditionalProbabilityOfTerm)
        i = i + 1
    return refinedVocabulary, prior, conditionalProbabilityOfClass
    
##applyMultinomialNaiveBayes applies the model created by trainMultinomialNaiveBayes to the testDataset
## It takes listOfNewsgroup, V, prior, conditionalProbabilityOfClass, document, testDataset as the arguments
## and returns np.argmax(score) i.e. argmax(score)
def applyMultinomialNaiveBayes(listOfNewsgroup, V, prior, conditionalProbabilityOfClass, document, testDataset):
    ##W is the vocabulary used for applyMultinomialNaiveBayes function
    W = list()
    ##score is the list used for calculating the scores
    score = list()
    f = open(os.path.join(testDataset, newsgroup, document))
    ##tokensString consists of the tokens that are being read
    tokensString  = " ".join(f.readlines())
    listOfTokens  = re.sub(r'[<|>|?|_|,|!|:|;|(|)|\"|=|-|$|\\|/|*|\'|+|\[|\]|#|$|%|^|?|~|`]', r'', tokensString)
    ##appending the words to W 
    for word in listOfTokens.split():
        W.append(word)
    ##Removing the similar words from the vocabulary by storing only unique elements 
    W = set(W)
    m = 0
    for classes in listOfNewsgroup:
        score.append(math.log(prior[m]))
        for word in W:
            if not conditionalProbabilityOfClass[m][word]:
                continue;
            score[m] = score[m] + math.log(conditionalProbabilityOfClass[m][word][0])
        m = m + 1
    ##Calculating the score by np.array(score)
    score = np.array(score)
    return np.argmax(score)
    
    
##trainingDataset is the path of the dataset passed in argv[1] while testDataset is the path of the dataset passed in argv[2]
trainingDataset = sys.argv[1]
testDataset = sys.argv[2]

##listOfNewsgroup is the list of all the newsgroup
listOfNewsgroup = list()

##Setting count = 1 and incrementing the count while it is less than datasetsToBeRead i.e. atleast 5 out 20 datasets are being read
count = 1
datasetsToBeRead = 5
for newsgroup in sorted(os.listdir(trainingDataset)):
    if (os.path.isdir(os.path.join(trainingDataset,newsgroup)) and count <= datasetsToBeRead):
        listOfNewsgroup.append(newsgroup)
        ##Printing the newsgroup
        print(newsgroup)
        count = count + 1

##Setting count = 1 and incrementing the count while it is less than datasetsToBeRead i.e. atleast 5 out 20 datasets are being read
count = 1
##Calculating V, prior, conditionalProbabilityOfClass by calling trainMultinomialNaiveBayes(listOfNewsgroup, trainingDataset)
V, prior, conditionalProbabilityOfClass = trainMultinomialNaiveBayes(listOfNewsgroup,trainingDataset)
##Calculating success and failure values for calculating the accuracy of the model
success = 0
failure = 0

##after applying trainMultinomialNaiveBayes, now applyMultinomialNaiveBayes on the testDataset and calculating success and failure values that help 
## us in calculating the accuracy of the model
for newsgroup in sorted(os.listdir(testDataset)):
    if (os.path.isdir(os.path.join(testDataset,newsgroup)) and count <= datasetsToBeRead):
        for doc in os.listdir(os.path.join(testDataset,newsgroup)):
            folderSelectedForPrediction = applyMultinomialNaiveBayes(listOfNewsgroup,V, prior, conditionalProbabilityOfClass, doc, testDataset)
            if (listOfNewsgroup[folderSelectedForPrediction] == newsgroup):
                success = success + 1
            else:
                failure = failure + 1
    count = count + 1
##Calculating the accuracy of the model
print("Accuracy of the model : "+str((success * 100)/(success+failure)))