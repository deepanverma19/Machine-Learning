/**
 * Created by dverma on 12/2/2017.
 */

import java.util.ArrayList;
import java.util.List;
/*
This class Tweets get the tweets with the id, listOfTweets and centroidOfTweets
 */
public class Tweets {

    //Initialising the list of type Data as listOfTweets
    public List<Data> listOfTweets;
    //Intialising another variable as centroidOfTweets of type Data
    public Data centroidOfTweets;
    //Initialising id as type int
    public int id;

    /*
    Declaring a constructor that takes id as parameter and sets this.id as id
    sets listOfTweets as new ArrayList()
    and centroidOfTweets as null
    */

    public Tweets(int id){
        this.id = id;
        this.listOfTweets = new ArrayList();
        this.centroidOfTweets = null;
    }

    /*
    Getters and Setters for centroidOfTweets
     */

    public void setCentroidOfTweets(Data t){
        this.centroidOfTweets = t;
    }

    public Data getCentroidOfTweets(){
        return this.centroidOfTweets;
    }

    /*
    This function printCluster() prints
    a)  clusterTweets -> ID
    b) Centroid -> Getting the centroidOfTweets.getText()
    c)  Points i.e. the listOfTweets
     */

    public void printCluster(){
        System.out.println("[clusterTweets: " + this.id+"]");
        System.out.println("[Centroid: " + this.centroidOfTweets.getText()+ "]");
        System.out.println("[Points:");
        for(Data t : listOfTweets) {
            System.out.print(t.getId() +",");
        }
        System.out.println("]");
    }

    /*
    This function addPoint basically adds the Data
     */

    public void addPoint(Data t) {
        this.listOfTweets.add(t);
    }

    /*
    This function getPoints returns the listOfTweets which is of type List<Data>
     */

    public List<Data> getPoints()
    {
        return this.listOfTweets;
    }

    /*
    This function clears the tweets
     */
    public void clear()
    {
        listOfTweets.clear();
    }
}
