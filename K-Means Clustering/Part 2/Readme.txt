Data.java
----------
This file uses two attributes i.e. id and text and has getters for both
getId() and getText()

Tweets.java
-----------
This file uses three attributes i.e. listOfTweets, centroidOfTweets and id
and has the getters and setters for all the attributes.

KMeans.java
-----------
This is the main file for KMeans algorithm.

COMPILE: javac KMeans.java
RUN: java KMeans 25 "C:\Users\dverma\Desktop\dxv160430_Assignment6\Part 2\InitialSeeds.txt" "C:\Users\dverma\Desktop\dxv160430_Assignment6\Part 2\Tweets.json" "C:\Users\dverma\Desktop\dxv160430_Assignment6\Part 2\OutputPart2.txt"

java filename noOfClusters InitialSeed.txt Tweets.json OutputfilePath
