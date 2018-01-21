/*
Created by dverma, mkukreja on 12/01/2017
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
This class KMeans implements the KMeans Clustering algorithm
 */
public class KMeans {

    //Initiating an arraylist called clusterPoints of type <ClusterPoints> which is another class
    public ArrayList<ClusterPoints> clusterPoints;

    //Initializing a default Constructor KMean that initialises the clusterPoints as new ArrayList when created
    KMeans(){
        clusterPoints = new ArrayList<>();
    }

    /*
    Creating a function called storePointsInArrayList that takes:
    a)  String datasetPath
    b)  KMeans cluster as parameters
    returns the appropriate cluster
     */
    public KMeans storePointsInArrayList(String datasetPath, KMeans cluster) throws IOException{
        //Reading a dataset file using fileReader i.e. for reading the inputs using FileReader and BufferedReader
        FileInputStream fileReader = new FileInputStream(datasetPath);
        BufferedReader readInput = new BufferedReader(new InputStreamReader(fileReader));
        //Initialising String line and incrementing count
        String line;
        int count = 0;
        while ((line = readInput.readLine()) != null) {
            if(count == 0){
                count++;
                continue;
            }
            //Creating a string data array by line.split("\t)
            String data[] = line.split("\t");
            //Initialising ClusterPoints class as clusterPoint
            ClusterPoints clusterPoint = new ClusterPoints();
            //Setting clusterID to clusterPoint.setClusterID(Integer.parseInt(data[0])
            clusterPoint.setClusterID(Integer.parseInt(data[0]));
            //Setting xCoordinate to clusterPoint.setxCoordinate(Float.parseFloat(data[1])
            clusterPoint.setxCoordinate(Float.parseFloat(data[1]));
            //Setting yCoordinate to clusterPoint.setyCoordinate(Float.parseFloat(data[2])
            clusterPoint.setyCoordinate(Float.parseFloat(data[2]));
            //Adding clusterPoint to the arrayList clusterPoints of the cluster
            cluster.clusterPoints.add(clusterPoint);
        }
        return cluster;
    }

    /*
    This function calculates the Euclidean Distance between the cluster points which takes:
    a)  Map<Integer,ArrayList<ClusterPoints>> listOfClusters
    b)  ClusterPoints[] clusterCenters
    c)  ClusterPoints clusterpoints
    as the parameters and returns the list of clusters which return cluster no and the cluster points
     */
    public Map<Integer,ArrayList<ClusterPoints>> calcEuclideanDistance(Map<Integer,ArrayList<ClusterPoints>> listOfClusters, ClusterPoints[] clusterCenters, ClusterPoints clusterpoints){
        //Initialising the distance array of type double and size clusterCenters.length
        double distance[] = new double[clusterCenters.length];
        //Setting minDistance as 999999 for comparing the distance between two points
        double minDistance = 999999;
        //Iterating over the loop for i=0;i<clusterCenters.length
        for(int i=0; i<clusterCenters.length; i++){

            //Calculating the xDistance by getting the xCoordinate of clusterPoints - clusterCenters[i] multiplied by the same
            float xDistance = (clusterpoints.getxCoordinate() - clusterCenters[i].getxCoordinate()) * (clusterpoints.getxCoordinate() - clusterCenters[i].getxCoordinate());
            //Calculating the yDistance by getting the yCoordinate of clusterPoints - clusterCenters[i] multiplied by the same
            float yDistance = (clusterpoints.getyCoordinate() - clusterCenters[i].getyCoordinate()) * (clusterpoints.getyCoordinate() - clusterCenters[i].getyCoordinate());

            //Calculating the distance[i] by sqrt of xDistance plus yDistance
            distance[i] = Math.sqrt(xDistance+yDistance);

            //if minDistance > distance[i], then setting minDistance as distance[i]
            if(minDistance > distance[i]){
                minDistance = distance[i];
            }
        }

        //Iterating over the loop for j=0;j<clusterCenters.length
        for(int j=0; j<clusterCenters.length; j++){
            //if minDistance == distance[j], then listOfClusters.get(j).add(clusterpoints)
            if(minDistance == distance[j]){
                listOfClusters.get(j).add(clusterpoints);
            }
        }
        return listOfClusters;
    }

    /*
    This function calculates the calculateSSE that takes:
    a)  ClusterPoints centers
    b)  ClusterPoints clusterpoints
    as the arguments and returns the distance
     */

    public double calculateSSE(ClusterPoints centers, ClusterPoints clusterpoints){
        //Setting distance as zero initially
        double distance = 0;
        //Calculating the xDistance by getting the xCoordinate of clusterPoints - clusterCenters[i] multiplied by the same
        float xDistance = (clusterpoints.getxCoordinate() - centers.getxCoordinate()) * (clusterpoints.getxCoordinate() - centers.getxCoordinate());
        //Calculating the yDistance by getting the yCoordinate of clusterPoints - clusterCenters[i] multiplied by the same
        float yDistance = (clusterpoints.getyCoordinate() - centers.getyCoordinate()) * (clusterpoints.getyCoordinate() - centers.getyCoordinate());
        //Calculating the distance[i] by sqrt of xDistance plus yDistance
        distance = (xDistance+yDistance);
        return distance;
    }

    //Main Method
    public static void main(String args[]){
        //KMeans K
        KMeans K = new KMeans();
        //Reading valueOfK using Integer.parseInt(args[0])
        int valueOfK = Integer.parseInt(args[0]);
        //Creating arrays for newCenters[] and oldCenters[] with size as valueOfK
        ClusterPoints newCenters[] = new ClusterPoints[valueOfK];
        ClusterPoints oldCenters[] = new ClusterPoints[valueOfK];
        try {
            //Getting value of K by passing it in storePointsInArrayList with args[1],K as parameters
            K = K.storePointsInArrayList(args[1], K);
            for(int i=0; i<valueOfK; i++){
                //Generating a random class object as rand
                Random rand = new Random();
                //Getting the value of randomNumber with rand.nextInt
                int  randomNumber = rand.nextInt(K.clusterPoints.size()-1) + 1;
                //Storing the random value as centers[i]
                newCenters[i] = K.clusterPoints.get(randomNumber);
                //Setting oldCenters[i] as new ClusterPoints()
                oldCenters[i] = new ClusterPoints();
            }

            //Iterating for rotation=0;rotation<25;rotation++
            for(int iteration=0;iteration<25;iteration++){
                //Iterating for j=0;j<newCenters.length;j++
                for(int j=0; j<newCenters.length; j++){
                    //Setting the x and y coordinates for the oldcenters with the new centers x and y coordinates
                    oldCenters[j].setxCoordinate(newCenters[j].getxCoordinate());
                    oldCenters[j].setyCoordinate(newCenters[j].getyCoordinate());
                }
                //Taking the hashmap of <Integer,ArrayList<ClusterPoints>> listOfClusters
                Map<Integer,ArrayList<ClusterPoints>> listOfClusters = new HashMap<Integer,ArrayList<ClusterPoints>>();

                //Iterating for i=0;i<newCenters.length;i++
                //And putting listOfCenters.put(i, new ArrayList of ClusterPoints)
                for(int i=0; i<newCenters.length; i++){
                    listOfClusters.put(i, new ArrayList<ClusterPoints>());
                }

                //For each clusterpoints in K.clusterpoints calculating the Euclidean Distance
                for (ClusterPoints clusterPoints : K.clusterPoints) {
                    listOfClusters = K.calcEuclideanDistance(listOfClusters,newCenters,clusterPoints);
                }

                //Iterating for i=0;i<listOfClusters.size();i++
                //sumOfXcoordinate = 0, sumOfYcoordinate = 0, count = 0
                //Calculating the sumOfXcoordinate as the sumOfXcoordinate plus clusterPoints.getXCoordinate()
                //Calculating the sumOfYcoordinate as the sumOfYcoordinate plus clusterPoints.getYCoordinate()
                for (int i=0; i<listOfClusters.size(); i++) {
                    float sumOfXcoordinate = 0, sumOfYcoordinate = 0, count = 0;
                    for (ClusterPoints clusterPoints : listOfClusters.get(i)) {
                        sumOfXcoordinate = sumOfXcoordinate + clusterPoints.getxCoordinate();
                        sumOfYcoordinate = sumOfYcoordinate + clusterPoints.getyCoordinate();
                        count++;
                    }
                    if(count != 0){
                        newCenters[i].setxCoordinate((sumOfXcoordinate/count));
                        newCenters[i].setyCoordinate((sumOfYcoordinate/count));
                    }
                }

                //Initalizing the PrintWriter as writer and error as zero
                PrintWriter writer = new PrintWriter(args[2], "UTF-8");
                double error = 0;
                //Iterating for i=0;i<listOfClusters.size();i++
                for (int i=0; i<listOfClusters.size(); i++) {
                    //Printing to the console
                    writer.print(" "+(i+1)+"\t");
                    //Initializing the count as zero
                    int count=0;
                    //For each clusterPoints in listOfClusters.get(i)
                    for (ClusterPoints clusterPoints : listOfClusters.get(i)) {
                        //Calculating the error with error + K.calculateSSE
                        error = error + K.calculateSSE(newCenters[i], clusterPoints);
                        //If count == 0, then count++ and writer.print(clusterPoints.getClusterID()
                        if(count == 0){
                            count++;
                            writer.print(clusterPoints.getClusterID());
                        }else{
                            writer.print(", "+clusterPoints.getClusterID());
                        }
                    }
                    writer.println("");
                }
                //Printing the Sum of Squared Error
                writer.println("Sum of Squared Error : "+error);
                writer.close();
                //Setting countCenters as zero
                int countCenters = 0;
                //Iterating for x=0;
                for(int x = 0; x < newCenters.length; x++){
                    //If the condition is satisfied, then incrementing countCenters
                    if(newCenters[x].getxCoordinate() == oldCenters[x].getxCoordinate() && newCenters[x].getyCoordinate() == oldCenters[x].getyCoordinate()){
                        countCenters++;
                    }
                }
                //If this condition satisfies then break
                if(countCenters == newCenters.length){
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}