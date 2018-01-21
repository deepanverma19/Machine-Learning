####################################################################
####################################################################

## @authors: dxv160430, mxk174330
##INSTALLING ALL THE PACKAGES AND USING ALL THE REQUIRED LIBRARIES

#install.packages("rpart.plot")
#install.packages("h2o")
#install.packages("deepnet")
#install.packages("ipred")
#install.packages("lattice")
#install.packages("pROC")
#install.packages("adabag")
#install.packages("randomForest")
#install.packages("gbm")
#install.packages("xgboost")
#install.packages("mlbench")
#install.packages("neuralnet")
#install.packages("e1071")
#install.packages("rpart")
#install.packages("class")
#install.packages("caret")

library("pROC")
library("lattice")
library("ipred")
library("caret")
library("mlbench")
library("xgboost")
library("gbm")
library("randomForest")
library("adabag")
library("e1071")
library("class")
library("neuralnet")
library("rpart")
library("rpart.plot")
library("h2o")
library("deepnet")

####################################################################
####################################################################

##READING THE ENTIRE DATASET AND STORING THE DATASET IN THE VARIABLE CALLED DATASET FOR PREPROCESSING PURPOSES
dataset <- read.csv("C:/Users/dverma/Desktop/dxv160430_Assignment5/dataset.csv",header = TRUE)


##PREPROCESSING THE DATASET

##CHECKING THE DATASET IF THERE ARE NULL VALUES IN THE DATASET
apply(dataset, MARGIN = 2, FUN = function(x) sum(is.na(x)))

##CALCULATING THE MAXIMUM AND MINIMUM VALUES FROM THE DATASET AND STORING THEM IN THE RESPECTIVE VARIABLES AND USING THEM FOR SCALING PURPOSES
maxValues = apply(dataset, MARGIN = 2, max)
minValues = apply(dataset, MARGIN = 2, min)

##SCALING THE DATASET AND FORMING A DATA FRAME WHICH IS STORED AS SCALED SET
scaledDataset = as.data.frame(scale(dataset, center = minValues, scale = maxValues-minValues))

##OBTAINING THE CORRELATION MATRIX AND STORING IT IN THE CORRELATION MATRIX
correlationMatrix<-cor(scaledDataset[,1:5])
##PRINTING THE CORRELATION MATRIX
print(correlationMatrix)

##USING THE SCALED PREPROCESSED DATASET FOR APPLYING VARIOUS TECHNIQUES AND USING THE NO OF FOLDS AS 10
scaledDataset<-scaledDataset[c(1,3,4,5)]
noOfFolds<-10

####################################################################
####################################################################

##### 1. DECISION TREE TECHNIQUE######

##INITIATING ACC VARIABLE
accuracyDecisionTree<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##CREATING A FIT DECISION TREE MODEL AND STORING IT AS DECISIONTREEMODEL
  decisonTreeModel <- rpart(class ~ Recency + Time + Monetary , data = trainDataset, method = "class", parms = list(split="information"))
  ##PREDICTING USING PREDICT FUNCTION BY USING THE FIT MODEL AND TEST DATASET ON THE BASIS OF TYPE CLASS
  predict <- predict(decisonTreeModel,testDataset,type="class")
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  decisonTreeROC<-roc(testDataset$class,as.numeric(predict))
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy = mean(testDataset$class == predict)
  ##CALCULATING THE ACCURACY BY STORING THEM INTO ACC VARIABLE
  accuracyDecisionTree<-c(accuracyDecisionTree,accuracy)
}
##CALCULATING THE ACCURACY AND ROC FOR DECISION TREE
print("Accuracy of Decision Tree :")
print(mean(accuracyDecisionTree))
print("ROC for Decision tree :")
print(decisonTreeROC)

####################################################################
####################################################################

###### 2. PERCEPTRON TECHNIQUE #####

##INITIATING accuracyPerceptron VARIABLE
accuracyPerceptron<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING NEURALNET LIBRARY FOR PERCEPTRON TECHNIQUE BY SETTING NUMBER OF HIDDEN LAYERS AS ZERO AND STORING IT INTO VARIBALE perceptron
  perceptron<- neuralnet(class ~ Recency + Time + Monetary , data = trainDataset,hidden = 0, threshold = 0.1,err.fct = "sse", linear.output = FALSE,act.fct = "logistic")
  ##CALCULATING THE PREDICTIONS
  predictions<-compute(perceptron,testDataset[,1:3])$net.result
  predictions<-ifelse(predictions>1,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy1 = mean(testDataset$class == predictions)
  ##CALCULATING THE ACCURACY BY STORING THEM INTO accuracyPerceptron VARIABLE
  accuracyPerceptron<-c(accuracyPerceptron,accuracy1)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  perceptronROC<-roc(testDataset$class,as.numeric(predictions))
}
##CALCULATING THE ACCURACY AND ROC FOR PERCEPTRON
print("Accuracy of Perceptron :")
print(mean(accuracyPerceptron))
print("ROC for perceptron:")
deepLearningROC<-perceptronROC
print(perceptronROC)

####################################################################
####################################################################

##### 3. NEURAL NETWORK TECHNIQUE #####

##INITIATING accuracyNeuralNetwork VARIABLE
accuracyNeuralNetwork<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING NEURALNET LIBRARY FOR NEURAL NETWORK TECHNIQUE BY SETTING NUMBER OF HIDDEN LAYERS AS TWO AND STORING IT INTO VARIBALE NN
  neuralnetwork<- neuralnet(class ~ Recency + Time + Monetary , data = trainDataset,hidden = c(2,2), err.fct = "ce",rep = 5, threshold = 0.01,linear.output = FALSE, learningrate = 0.5,act.fct = "logistic")
  ##CALCULATING THE PREDICTIONS
  predictions<-compute(neuralnetwork,testDataset[,1:3])$net.result
  predictions<-ifelse(predictions>1,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy2 = mean(testDataset$class == predictions)
  ##CALCULATING THE ACCURACY BY STORING THEM INTO accuracyNeuralNetwork VARIABLE
  accuracyNeuralNetwork<-c(accuracyNeuralNetwork,accuracy2)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  neuralNetworkROC<-roc(testDataset$class,as.numeric(predictions))
}
##CALCULATING THE ACCURACY AND ROC FOR NEURAL NETWORK TECHNIQUE
print("Accuracy of Neural Network :")
print(mean(accuracyNeuralNetwork))
print("ROC for Neural Network")
print(neuralNetworkROC)

####################################################################
####################################################################

##### 4. SVM CLASSIFIER #####

##INITIATING accuracySVM VARIABLE
accuracySVM<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING SVM LIBRARY FOR GENERATING SVM MODEL AND STORING IT IN VARIABLE CALLED SVMMODEL
  svmModel <- svm(class ~., data = trainDataset, kernel = "linear", cost = 100, gamma = 1)
  ##CALCULATING THE PREDICTIONS
  svmPrediction<-predict(svmModel,testDataset[,1:3])
  svmPrediction<-ifelse(svmPrediction>0.5,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy3 = mean(testDataset$class == svmPrediction)
  ##CALCULATING THE ACCURACY BY STORING THEM INTO accuracySVM VARIABLE
  accuracySVM<-c(accuracySVM,accuracy3)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  SVM_ROC<-roc(testDataset$class,as.numeric(svmPrediction))
}
##CALCULATING THE ACCURACY AND ROC FOR SVM TECHNIQUE
print("Accuracy of SVM :")
print(mean(accuracySVM))
print("ROC for Support vector machine")
print(SVM_ROC)

####################################################################
####################################################################

##### 5. DEEP LEARNING TECHNIQUE #####

##INITIATING accuracyDeepLearning VARIABLE
accuracyDeepLearning<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING NEURALNET LIBRARY FOR GENERATING DEEP LEARNING MODEL AND STORING IT IN VARIABLE CALLED DEEPLEARNING
  deepLearning<-dbn.dnn.train(as.matrix(trainDataset[,1:3]),trainDataset[,4],numepochs = 100, momentum = 0.5, activationfun = "sigm",learningrate_scale=1)
  ##CALCULATING THE PREDICTIONS
  deepLearningPrediction<-nn.test(deepLearning,as.matrix(testDataset[,1:3]),testDataset[,4],t=0.5)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracyDeepLearning<-c(accuracyDeepLearning,(1-deepLearningPrediction))
  ##CALCULATED THE deepLearningROC PREVIOUSLY IN CASE OF PERCEPTRON
}
##CALCULATING THE ACCURACY AND ROC FOR DEEP LEARNING TECHNIQUE
print("Accuracy of Deep Learning :")
print(mean(accuracyDeepLearning))
print("ROC for Deep learning")
print(deepLearningROC)

####################################################################
####################################################################

##### 6. NAIVE BAYES #####

##INITIATING accuracyNaiveBayes VARIABLE
accuracyNaiveBayes<-c()

##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING NAIVEBAYES LIBRARY FOR GENERATING NAIVE BAYES MODEL AND STORING IT IN VARIABLE CALLED NBMODEL
  nbModel<-naiveBayes(class ~., data = trainDataset,laplace = 3)
  ##CALCULATING THE PREDICTIONS
  nbPrediction<-predict(nbModel,testDataset[,1:3])
  ##CREATING A TABLE FOR NAIVE BAYES ACCURACY AND ROC CALCULATION
  tab<-table(nbPrediction,testDataset$class)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy5 = sum(tab[row(tab)==col(tab)])/sum(tab)
  accuracyNaiveBayes<-c(accuracyNaiveBayes,accuracy5)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  naiveBayesROC<-roc(testDataset$class,as.numeric(nbPrediction))
}
##CALCULATING THE ACCURACY AND ROC FOR NAIVEBAYES TECHNIQUE
print("Accuracy of Naive Bayes :")
print(mean(accuracyNaiveBayes))
print("ROC for Naive bayes")
print(naiveBayesROC)

####################################################################
####################################################################

##### 7. LOGISTIC REGRESSION #####

##INITIATING accuracyLogisticRegression VARIABLE
accuracyLogisticRegression<-c()

##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING GLM LIBRARY FOR GENERATING LOGISTIC REGRESSION MODEL AND STORING IT IN VARIABLE CALLED LRMODEL
  model<-glm(class~ Recency + Time + Monetary ,data = trainDataset,family="binomial")
  ##CALCULATING THE PREDICTIONS
  pred<-predict.glm(model,testDataset[,1:3])
  pred<-ifelse(pred>0.5,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy6 = mean(pred==testDataset$class)
  accuracyLogisticRegression<-c(accuracyLogisticRegression,accuracy6)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  logisticRegressionROC<-roc(testDataset$class,as.numeric(pred))
}
##CALCULATING THE ACCURACY AND ROC FOR LOGISTIC REGRESSION TECHNIQUE
print("Accuracy of Logistic regression :")
print(mean(accuracyLogisticRegression))
print("ROC for Logistic regression")
print(logisticRegressionROC)

####################################################################
####################################################################

##### 8. K-NEAREST NEIGHBOUR #####

##INITIATING accuracyKNN VARIABLE
accuracyKNN<-c()

##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING KNN LIBRARY FOR GENERATING K-NEAREST NEIGHBOUR MODEL AND STORING IT IN VARIABLE CALLED KNNMODEL
  knnModel<-knn(train = trainDataset,test = testDataset, cl=trainDataset$class,k=4,prob = TRUE,use.all = TRUE)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy7 = mean(knnModel==testDataset$class)
  accuracyKNN<-c(accuracyKNN,accuracy7)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  knnROC<-roc(testDataset$class,as.numeric(knnModel))
}
##CALCULATING THE ACCURACY AND ROC FOR K-NEAREST NEIGHBOUR TECHNIQUE
print("Accuracy of KNN :")
print(mean(accuracyKNN))
print("ROC for K-nearest neighbors")
print(knnROC)
####################################################################
####################################################################

##### 9. ADA BOOSTING #####

##INITIATING accuracyAdaBoosting VARIABLE
accuracyAdaBoosting<-c()
##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)
##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING BOOSTING LIBRARY FOR GENERATING ADABOOSTING MODEL AND STORING IT IN VARIABLE CALLED ADABOOSTMODEL
  adaBoostModel<-boosting(class~., data=trainDataset, mfinal = 10,control = rpart.control(maxdepth = 1))
  ##CALCULATING THE PREDICTIONS
  pred<-predict.boosting(adaBoostModel,testDataset)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy8 = mean(pred$class==testDataset$class)
  accuracyAdaBoosting<-c(accuracyAdaBoosting,accuracy8)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  adaBoostingROC<-roc(testDataset$class,as.numeric(pred$class))
}
##CALCULATING THE ACCURACY AND ROC FOR ADABOOSTING TECHNIQUE
print("Accuracy of Ada Boosting :")
print(mean(accuracyAdaBoosting))
print("ROC for ADA Boosting")
print(adaBoostingROC)

####################################################################
####################################################################

##### 10. RANDOM FOREST #####

##INITIATING accuracyRandomForest VARIABLE
accuracyRandomForest<-c()

##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING RANDOMFOREST LIBRARY FOR GENERATING RANDOM FOREST MODEL AND STORING IT IN VARIABLE CALLED RANDOMFORESTMODEL
  randomForestModel<-randomForest(class~., data=trainDataset,importance = TRUE, mtry = 2, ntree = 500)
  ##CALCULATING THE PREDICTIONS
  pred<-predict(randomForestModel,testDataset)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy9 = mean(pred==testDataset$class)
  accuracyRandomForest<-c(accuracyRandomForest,accuracy9)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  rocRandomForest<-roc(testDataset$class,as.numeric(pred))
}
##CALCULATING THE ACCURACY AND ROC FOR RANDOMFOREST TECHNIQUE
print("Accuracy of Random Forest :")
print(mean(accuracyRandomForest))
print("ROC for Random Forest")
print(rocRandomForest)

####################################################################
####################################################################

##### 11. GRADIENT BOOSTING ######

##INITIATING accuracyGradientBoosting VARIABLE
accuracyGradientBoosting<-c()

##FACTORING THE CLASS FOR THE SCALED DATASET
scaledDataset$class <- factor(scaledDataset$class)

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING XGBOOST LIBRARY FOR GENERATING GRADIENT BOOSTING MODEL AND STORING IT IN VARIABLE CALLED GRADIENTBOOSTMODEL
  gradientBoostModel<-xgboost( data = as.matrix(trainDataset[,1:3]), label=trainDataset$class, max.depth=2,nrounds = 2)
  ##CALCULATING THE PREDICTIONS
  pred<-predict(gradientBoostModel,as.matrix(testDataset[,1:3]))
  pred<-ifelse(pred>1,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy10 = mean(pred==testDataset$class)
  accuracyGradientBoosting<-c(accuracyGradientBoosting,accuracy10)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  rocGradientBoosting<-roc(testDataset$class,as.numeric(pred))
}
##CALCULATING THE ACCURACY AND ROC FOR GRADIENT BOOSTING TECHNIQUE
print("Accuracy of Gradient boosting :")
print(mean(accuracyGradientBoosting))
print("ROC for Gradient Boosting")
print(rocGradientBoosting)

####################################################################
####################################################################

##### 12. BAGGING #####

##INITIATING accuracyBagging VARIABLE
accuracyBagging<-c()

##FOR LOOP
for (index in 1:5)
{
  ##SPLITTING THE DATASET INTO TEST DATASET AND TRAINING DATASET
  testDataset <- scaledDataset[(((index-1)*length(scaledDataset[,1])/noOfFolds)+1):((index)*length(scaledDataset[,1])/noOfFolds),]
  trainDataset <- rbind(scaledDataset[0:(((index-1)*length(scaledDataset[,1])/noOfFolds)),],scaledDataset[(((index)*length(scaledDataset[,1])/noOfFolds)+1):length(scaledDataset[,1]),])
  testDataset<- na.omit(testDataset)
  trainDataset<-na.omit(trainDataset)
  ##APPLYING BAGGING LIBRARY FOR GENERATING BAGGING MODEL AND STORING IT IN VARIABLE CALLED BAGGINGMODEL
  baggingModel <- bagging(class~.,ns=275,nbagg=500,control=rpart.control(minsplit=5, cp=0, xval=0,maxsurrogate=0),data=trainDataset)
  ##CALCULATING THE PREDICTIONS
  pred<-predict(baggingModel,(testDataset[,1:3]))
  pred<-ifelse(pred$class>0.5,1,0)
  ##CALCULATING THE ACCURACY BY CALCULATING THE MEAN
  accuracy11 = mean(pred==testDataset$class)
  accuracyBagging<-c(accuracyBagging,accuracy11)
  ##APPLYING ROC USING ROC LIBRARY BY USING TESTDATASET$CLASS
  rocBagging<-roc(testDataset$class,as.numeric(pred))
}
##CALCULATING THE ACCURACY AND ROC FOR BAGGING TECHNIQUE
print("Accuracy of Bagging :")
print(mean(accuracyBagging))
print("ROC for Bagging")
print(rocBagging)