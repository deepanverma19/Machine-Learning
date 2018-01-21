/**
 * Created by dverma, mkukreja on 12/2/2017.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
This file is the main file that contains the main() method and
implements the KMeans algorithm for Tweets clustering Question
 */

public class KMeans {

    //listOfTweets is the variable that is a list of type<Data>
    static private List<Data> listOfTweets;
    //listOfCentroids is the variable that is another list of type<Data>
    static private List<Data> listOfCentroids;
    //clusteredList is the variable that is another list of type<Tweets>
    static private List<Tweets> clusteredList;
    //Intialising K as 25 for the question
    static private int k = 25;

    /*
    This function readingJSONFile takes String datasetPath as the parameter
     */

    private static void readingJSONFile(String datasetPath) throws IOException {

        //Reading the dataset file using fileReader i.e. for reading the inputs using FileReader and BufferedReader
        FileInputStream fileReader = new FileInputStream(datasetPath);
        BufferedReader inputJSONData = new BufferedReader(new InputStreamReader(fileReader));

        //Initialising String line and an ArrayList of type String
        String line;
        ArrayList<String> listOfData = new ArrayList();

        //Reading while line != null and adding it to the ArrayList
        while ((line = inputJSONData.readLine()) != null) {
            listOfData.add(line);
        }

        //For each item of type String in ArrayList
        for (String item : listOfData) {

            //Initialising a new object tweet of type Data
            Data tweet = new Data();

            //Splitting each item by text and id and storing them into arrays of type String
            String[] textArray = item.split("\"text\":");
            String[] idArray = item.split("\"id\":");

            //Setting text and id property of each tweet
            tweet.text = textArray[1].substring(1, textArray[1].indexOf(','));
            tweet.id = Long.parseLong(idArray[1].substring(1, idArray[1].indexOf(',')));

            //Adding the tweet to the arrayList listOfTweets
            listOfTweets.add(tweet);
        }
    }

    /*
    This function getInitialData gets the initial data and takes datasetPath as the parameter
     */

    private static void getInitialData(String initialDatasetPath) throws IOException {

        //Reading a dataset file using fileReader i.e. for reading the inputs using FileReader and BufferedReader
        FileInputStream fileReader = new FileInputStream(initialDatasetPath);
        BufferedReader inputSeed = new BufferedReader(new InputStreamReader(fileReader));

        //Initialising String line and an ArrayList of type String
        String line;
        ArrayList<String> listOfData = new ArrayList();

        //Reading while line != null and adding it to the ArrayList
        while ((line = inputSeed.readLine()) != null) {
            listOfData.add(line.replace(",", ""));
        }

        //Creating a new ArrayList called clusteredList
        clusteredList = new ArrayList<>();

        //Iterating for i=0;i<k i.e. 25
        for (int i=0; i<k; i++) {
            //Iterating for j=0;j<listOfTweets.size()
            for (int j=0; j<listOfTweets.size(); j++) {
                //If the ID matches
                if (Long.parseLong(listOfData.get(i)) == listOfTweets.get(j).id) {
                    //Creating a new object of class Tweets
                    Tweets clusteredTweet = new Tweets(i);
                    //Setting the centroidOfTweets for clusteredTweet
                    clusteredTweet.centroidOfTweets = listOfTweets.get(j);
                    //Adding clusteredTweet in the clusteredList
                    clusteredList.add(clusteredTweet);
                }
            }
        }
    }

    /*
    This function calculateJaccardDistance calculates the Jaccardian Distance and takes
    a)  String centroidTweets and
    b)  String tweet
    as the parameters
    and returns the distance by using the appropriate formula
     */

    private static double calculateJaccardDistance(String centroidOfTweets, String tweet){
        /*
        Creating two new ArrayLists of type String
         a) firstArrayList stores the centroidTweets by converting them to lowercase and using .split()
         b) secondArrayList stores the tweet by converting them to lowercase and using .split()
        */

        List<String> firstArrayList = Arrays.asList(centroidOfTweets.toLowerCase().split(" "));
        List<String> secondArrayList = Arrays.asList(tweet.toLowerCase().split(" "));

        /*
        Creating two HashSets of type String that would store the unionOfTweets
        and intersectionOfTweets of the both the arrayLists
        */

        Set<String> unionOfTweets = new HashSet<String>(firstArrayList);
        unionOfTweets.addAll(secondArrayList);

        Set<String> intersectionOfTweets = new HashSet<String>(firstArrayList);
        intersectionOfTweets.retainAll(secondArrayList);

        //Returning the Jaccard Distance which is (1-(intersected tweets)/(unionOfTweets tweets))
        return (double) (1 - (intersectionOfTweets.size() / (double) unionOfTweets.size()));
    }

    /*
    This function clusteredTweets() takes forms the cluster of Tweets
     */
    private static void clusteredTweets() {

        //Flushing the clusters for formation of more clustered Tweets
        flushClusters();

        //Creating the new arrayList called distanceList that stores the Jaccard Distance between the tweets
        ArrayList<Double[]> distanceList = new ArrayList();

        //Iterating for i=0;i<k i.e. 25
        for (int i=0; i<k; i++) {
            //Creating a new array distance of type Double and of size listOfTweets.size()
            Double[] distance = new Double[listOfTweets.size()];
            //Iterating for j=0;j<listOfTweets.size, computing the distance by calculating Jaccard Distance
            for (int j=0; j<listOfTweets.size(); j++) {
                distance[j] = calculateJaccardDistance(clusteredList.get(i).centroidOfTweets.text, listOfTweets.get(j).text);
            }
            //Adding the distance to the distanceList
            distanceList.add(distance);
        }

        //Iterating for i=0;i<listOfTweets.size()
        for (int i=0; i<listOfTweets.size(); i++) {
            //Initiating a variable called max
            Double maxValue = Double.MAX_VALUE;
            //Initiating a variable called index for storing the exact value of x
            int index = 0;
            for (int x=0; x<KMeans.k; x++) {
                if (distanceList.get(x)[i] < maxValue) {
                    maxValue = distanceList.get(x)[i];
                    index = x;
                }
            }
            //Getting the index and then adding listOfTweets the listOfTweets.get(i)
            clusteredList.get(index).listOfTweets.add(listOfTweets.get(i));
        }
    }

    /*
    This function basically updates the Centroids of the Clusters formed
     */
    private static void updateCentroidsOfClusters(){

        //Iterating for i=0;i<k i.e. set to 25
        for (int i=0; i<k; i++) {
            //Initiating the variable index
            int index = 0;
            //Setting the value of maxValue as Double.MAX_VALUE
            double maxValue = Double.MAX_VALUE;
            //Iterating for j=0;j<clusteredList.get(i).listOfTweets.size()
            for (int j=0; j<clusteredList.get(i).listOfTweets.size(); j++){
                //Initiating a variable called distance that calculates the Jaccard Distance
                double distance = 0;
                //Iterating for k=0;k<clusteredList.get(i).listOfTweets.size()
                for (int k=0; k<clusteredList.get(i).listOfTweets.size(); k++){
                    //Calculating the distance by using calculateJaccardDistance function
                    distance += calculateJaccardDistance(clusteredList.get(i).listOfTweets.get(j).getText(), clusteredList.get(i).listOfTweets.get(k).getText());
                }
                //Comparing the value of distance with maxValue
                if (distance < maxValue) {
                    maxValue = distance;
                    index = j;
                }
            }
            //Getting clusteredList.get(i).listOfTweets.get(index) and assigning it to clusteredList.get(i).centroidOfTweets
            clusteredList.get(i).centroidOfTweets = clusteredList.get(i).listOfTweets.get(index);
        }
    }

    /*
    This function flushCenters clears the tweetList and then adds the centroidTweets to the
    listOfCentroids ArrayList initialized
     */

    private static void flushClusters(){
        //Initializing an arrayList called listOfCentroids
        listOfCentroids = new ArrayList<>();
        //Iterating for i=0;i<clusteredList.size()
        for (int i = 0; i < clusteredList.size(); i++){
            //Clearing the listOfTweets for the clusteredList
            clusteredList.get(i).listOfTweets.clear();
            //Addiing the centroidOfTweets by getting it from the clusteredList and adding it to listOfCentroids
            listOfCentroids.add(clusteredList.get(i).centroidOfTweets);//preserve older centroids
        }
    }

    /*
    This function findSSE() finds the Squared Error
    and returns the sse
     */

    private static double findSSE(){
        //Initialising sse as zero
        double sse = 0;
        //Iterating for every cluster in the clusteredList
        for (Tweets cluster : clusteredList){
            //Initialising both distance and temporaryDistance as zero
            double distance = 0;
            double tempDistance = 0;
            //Iterating for k=0;k<cluster.listOfTweets
            for (int k=0; k<cluster.listOfTweets.size(); k++){
                //Using calculateJaccardDistance calculating the distance and storing it into tempDistance
                tempDistance = calculateJaccardDistance(cluster.centroidOfTweets.text, cluster.listOfTweets.get(k).text);
                //Updating the value of distance by adding tempDistance * tempDistance
                distance += (tempDistance * tempDistance);
            }
            //Updating the value of sse
            sse += distance;
        }
        return sse;
    }

    /*
    This function prints the Cluster Number and SSE
     */
    private static void printClusterNoAndSSE(String outputfilepath, double sse) throws IOException{
        //Initialising PrintWriter class for storing the output to an output file
        try (PrintWriter out = new PrintWriter(new FileWriter(outputfilepath, true))){
            //Displaying the clusterNo and tweetID
            out.println("ClusterNo"  + "\t"+"TweetId");
            //Iterating for i=0;i<clusteredList.size()
            for (int i=0; i<clusteredList.size(); i++){
                //Printing the number
                out.print(i + 1 + "\t\t");
                String s = "";
                //Iterating for j=0;j<clusteredList.get(i).listOfTweets.size
                for (int j=0; j<clusteredList.get(i).listOfTweets.size(); j++) {
                    //Concatenating the tweets generated to the above String initialized
                    s += clusteredList.get(i).listOfTweets.get(j).id.toString() + ",";
                }
                //Printing the String formed in output File along with the SSE
                out.println(s);
            }
            out.println("The Squared Error is: " + sse);
        }
    }

    //This is the main method
    public static void main(String[] args) throws IOException {
        //Initialising an arraylist called listOfTweets
        listOfTweets = new ArrayList<Data>();
        //Taking the value of k by passing the argument[0]
        k = Integer.parseInt(args[0]);
        //Passing the initialSeedData in the argument[1]
        String initialSeedData = args[1];
        //Passing the inputJSONData in the argument[2]
        String inputJSONData = args[2];
        //Passing the path of outputFile in the argument[3]
        String outputFileData = args[3];
        //Calling the function readingJSONFile
        readingJSONFile(inputJSONData);
        //Calling the function getInitialData
        getInitialData(initialSeedData);
        //Iterating for j=0;j<25
        for (int j=0; j<25; j++) {
            //Calling the function clusteredTweets()
            clusteredTweets();
            //Calling the function updateCentroidOfClusters()
            updateCentroidsOfClusters();
            //Initialising an arrayList called currentCentroidList
            List<Data> currentCentroidList = new ArrayList<>();
            //Initialising a variable called jaccardDistance
            double jaccardDistance = 0;
            //Iterating for i=0;i<clusteredList.size()
            for (int i=0; i<clusteredList.size(); i++){
                //Adding the new centroids to the currentCentroidList plus the JaccardDistance in jaccardDistance variable
                currentCentroidList.add(clusteredList.get(i).centroidOfTweets);
                jaccardDistance += calculateJaccardDistance(listOfCentroids.get(i).text, currentCentroidList.get(i).text);
            }
            //In case jaccardDistance is 0, break
            if (jaccardDistance == 0) {
                break;
            }
        }
        //Initialising another variable called sse which basically finds SSE
        double sse = findSSE();
        //Calling the function printClusterNoAndSSE
        printClusterNoAndSSE(outputFileData, sse);
        //Displaying the sse value
        System.out.println("Squared Error is: " + sse);
    }
}
