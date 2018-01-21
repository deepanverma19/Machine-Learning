import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by dverma on 9/18/2017.
 *
 * @author deepanverma, mansikukreja
 */


public class Driver {

    public static void main(String[] args) {

        ID3 obj = new ID3();
        DTNode nodeTraining = new DTNode(); // This node is used for training dataset
        DTNode nodeValidation = new DTNode(); // // This node is used for validation dataset
        DTNode nodeTest = new DTNode(); // // This node is used for test dataset

        int splittingAttribute; // This is used to calculate the next attribute for splitting the decision tree

        String trainingSet = args[0]; //Takes path of training dataset as input
        String validationSet = args[1]; ////Takes path of validation dataset as input
        String testSet = args[2]; ////Takes path of test dataset as input
        float pruningFactor = Float.valueOf(args[3]); //Takes value of pruning factor as String and then converts it into float

        nodeTraining = obj.readDataset(trainingSet);//node for Training dataset
        splittingAttribute = obj.newSplittingAttribute(nodeTraining);
        nodeTraining.splittingAttribute = splittingAttribute;
        obj.implementNewNode(nodeTraining, splittingAttribute);

        int totalNumberOfNodes = obj.totalNumberOfNodes(nodeTraining);

        int pruneNode = (int) (totalNumberOfNodes * pruningFactor);

        int trainingAttributes = (nodeTraining.dataset.get(0).values.length);
        int validationAttributes = trainingAttributes;
        int testAttributes = trainingAttributes;

        System.out.println("DECISION TREE PRINTING");
        System.out.println("---------------------------------------");
        obj.printTree(nodeTraining, trainingSet);


        nodeValidation = obj.readDataset(validationSet);//Node for validation dataset
        nodeTest = obj.readDataset(testSet);////Node for test dataset

        //Pre-Pruned Accuracy

        System.out.println("\n\nPre-Pruned Accuracy\n-----------------------------\nNumber of training instances = " + nodeTraining.dataset.size());
        System.out.println("Number of training attributes = " + (trainingAttributes-1));
        System.out.println("Total number of nodes in the tree = " + obj.totalNumberOfNodes(nodeTraining));
        System.out.println("Number of leaf nodes in the tree = " + obj.totalNumberOfLeafNodes(nodeTraining));
        System.out.println("Accuracy of the model on the training dataset = " + obj.accuracy(nodeTraining, nodeTraining));
        System.out.println("\nNumber of validation instances = " + nodeValidation.dataset.size());
        System.out.println("Number of validation attributes = " + (validationAttributes-1));
        System.out.println("Accuracy of the model on the validation dataset = " + obj.accuracy(nodeTraining, nodeValidation));
        System.out.println("\nNumber of testing instances = " + nodeTest.dataset.size());
        System.out.println("Number of testing attributes = " + (testAttributes - 1));
        System.out.println("Accuracy of the model on the testing dataset = "+ obj.accuracy(nodeTraining, nodeTest));

        System.out.println();
        System.out.println("###################################");

        //Post-Pruned Accuracy

        for(int i=0;i<pruneNode;i++)
            obj.pruning(nodeTraining);

        System.out.println("\nPost-Pruned Accuracy\n-----------------------------\nNumber of training instances = " + nodeTraining.dataset.size());
        System.out.println("Number of training attributes = " + (trainingAttributes-1));
        System.out.println("Total number of nodes in the tree = " + obj.totalNumberOfNodes(nodeTraining));
        System.out.println("Number of leaf nodes in the tree = " + obj.totalNumberOfLeafNodes(nodeTraining));
        System.out.println("Accuracy of the model on the training dataset = "+ obj.accuracy(nodeTraining, nodeTraining));
        System.out.println("\nNumber of validation instances = " + nodeValidation.dataset.size());
        System.out.println("Number of validation attributes = " + (validationAttributes-1));
        System.out.println("Accuracy of the model on the validation dataset = "+ obj.accuracy(nodeTraining, nodeValidation));
        System.out.println("\nNumber of testing instances = " + nodeTest.dataset.size());
        System.out.println("Number of testing attributes = " + (testAttributes - 1));
        System.out.println("Accuracy of the model on the testing dataset = "+ obj.accuracy(nodeTraining, nodeTest));

    }
}