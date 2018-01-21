import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by dverma on 9/22/2017.
 *
 * @author deepanverma, mansikukreja
 */


public class ID3 {

    /*
    This function readDataset() reads the path of the dataset and reads the complete dataset
     */

    public DTNode readDataset(String dataset) {

        DTNode node = new DTNode();
        node.no = 1;

        int count = 0, noOfZeroes = 0, noOfOnes = 0;

        try {

            FileReader fileReader = new FileReader(dataset);
            BufferedReader br = new BufferedReader((fileReader));
            String data = br.readLine();
            int totalNumberOfAttributes = Array.getLength(data.split(","));//this variable stores the total number of attributes
            data = br.readLine();

            while (data != null) {

                Row row = new Row();//Creating an object of class Row
                int x[] = new int[totalNumberOfAttributes];//Creating an array of size same as totalNumberOfAttributes
                String[] y = data.split(",");//Splitting each row by "," and storing it into an array of String[]
                int i = 0;
                for (String s : y) {
                    if (s.equalsIgnoreCase("1"))//if String equals 1, then x[i] = 1
                        x[i] = 1;
                    else
                        x[i] = 0;//if String equals 0, then x[i] = 0
                    i++;
                }

                count++;//Incrementing the count
                row.classLabel = x[totalNumberOfAttributes - 1];//Storing the value of class in classLabel variable for every row
                row.rowNumber = count;//Assigning the value of count to each row's rowNumber
                row.values = x;//Assigning the values of each row to an array of row
                node.dataset.add(row);//Adding row to the dataset arraylist
                data = br.readLine();
            }

            node.entropy = entropy(node);
            if (node.entropy == 0)//If node's entropy is zero, then it means that the node is a leaf
                node.leaf = true;

            //Calculating the number of Zeroes and Ones in class

            for (Row rw : node.dataset) {
                if (rw.classLabel == 0)
                    noOfZeroes++;//Incrementing noOfZeroes if classLabel's value is 0
                else
                    noOfOnes++;//Incrementing noOfOnes if classLabel's value is 1
            }

            //If noOfZeroes is greater than noOfOnes, then finalLabel of the node is

            if (noOfZeroes > noOfOnes)
                node.finalLabel = 0;
            else
                node.finalLabel = 1;

            /*
            Creating an arraylist of attributes which just stores the attribute's number instead of storing the attribute's name which is done
            later using a function called attributeName() that converts the integer value back to String's name
             */
            ArrayList<Integer> attributes = new ArrayList<>();
            for (int i = 0; i < totalNumberOfAttributes - 1; i++)
                attributes.add(i);

            //node.attributes store the attributes of the dataset
            node.attributes = new ArrayList<>(attributes);

            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("No File Found");
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }

        return node;
    }

    /*
    This function entropy() calculates the Entropy of a specific node that is taken as input in the function
     */

    public double entropy(DTNode node) {

        double unmatched = 0, matched = 0;//variable unmatched indicates classLabel isn't matched and variable matched indicates that classLabel is matched

        for (Row row : node.dataset) {
            if (row.classLabel == 0)//if classLabel=0, then it is unmatched i.e. increment unmatched
                unmatched++;
            else
                matched++;//if classLabel=1, then it is matched i.e. increment matched
        }


        /*
                total gives us the sum of unmatched and matched labels
                log2 stores the value of log2 to find the entropy
                probabilityOfUnmatched is the probability of unmatched labels over total labels
                probabilityOfMatched is the probability of matched labels over total labels
                logProbabilityOfUnmatched gives the best value using base2 for probabilityOfUnmatched
                logProbabilityOfMatched gives the best value using base2 for probabilityOfMatched
                entropy gives us the value of H(Y)
        */


        double total = unmatched + matched;
        double log2 = Math.log(2);
        double probabilityOfUnmatched = unmatched / total;
        double probabilityOfMatched = matched / total;
        double logProbabilityOfUnmatched = (Math.log(probabilityOfUnmatched) / log2);
        double logProbabilityOfMatched = (Math.log(probabilityOfMatched) / log2);

        if (probabilityOfUnmatched == 0)
            logProbabilityOfUnmatched = 0;
        if (probabilityOfMatched == 0)
            logProbabilityOfMatched = 0;

        //Calculating the entropy of class labels
        double entropy = (-1) * (((probabilityOfMatched) * (logProbabilityOfMatched)) + ((probabilityOfUnmatched) * (logProbabilityOfUnmatched)));

        if (entropy == -0.0)
            entropy = 0;

        return entropy;
    }

    /*
    This function entropy() calculates the entropy by taking node and attribute as input
     */

    public double entropy(DTNode node, int attribute) {

        /*
        firstunmatched -> This stores the unmatched counts if when row.values[attribute] == 0 and row.classLabel = 0
        secondunmatched -> This stores the unmatched counts if when row.values[attribute] == 0 and row.classLabel = 1
        firstmatched -> This stores the first matched counts if when row.values[attribute] == 1 and row.classLabel = 0
        secondmatched -> This stores the second matched counts if when row.values[attribute] == 1 and row.classLabel = 1
         */

        int firstunmatched = 0, secondunmatched = 0, firstmatched = 0, secondmatched = 0;

        for (Row row : node.dataset) {
            if (row.values[attribute] == 0) {
                if (row.classLabel == 0)
                    firstunmatched++;
                else
                    firstmatched++;
            } else {
                if (row.classLabel == 0)
                    secondunmatched++;
                else
                    secondmatched++;
            }
        }

        /*
                firstSum gives us the sum of firstunmatched and firstmatched labels
                log2 stores the value of log2 to find the entropy
                probabilityOfFirstUnmatched is the probability of firstunmatched labels over firstSum labels
                probabilityOfFirstMatched is the probability of firstmatched labels over firstSum labels
                logProbabilityOfFirstUnmatched gives the best value using base2 for probabilityOfFirstUnmatched
                logProbabilityOfFirstMatched gives the best value using base2 for probabilityOfFirstMatched
                entropyFirst gives us the value of H(Y|x = 0)
        */


        double firstSum = firstmatched + firstunmatched;
        double log2 = Math.log(2);
        double probabilityOfFirstMatched = (double) firstmatched / firstSum;
        double probabilityOfFirstUnmatched = (double) firstunmatched / firstSum;
        double logProbabilityOfFirstMatched = (Math.log(probabilityOfFirstMatched) / log2);
        double logProbabilityOfFirstUnmatched = (Math.log(probabilityOfFirstUnmatched) / log2);

        if (probabilityOfFirstUnmatched == 0)
            logProbabilityOfFirstUnmatched = 0;
        if (probabilityOfFirstMatched == 0)
            logProbabilityOfFirstMatched = 0;

        //entropyFirst calculates the entropy when H(Y|x = 0)

        double entropyFirst = (-1) * (((probabilityOfFirstMatched) * (logProbabilityOfFirstMatched)) + ((probabilityOfFirstUnmatched) * (logProbabilityOfFirstUnmatched)));


        /*
                secondSum gives us the sum of firstunmatched and firstmatched labels
                log2 stores the value of log2 to find the entropy
                probabilityOfSecondUnmatched is the probability of secondunmatched labels over secondSum labels
                probabilityOfSecondMatched is the probability of secondmatched labels over secondSum labels
                logProbabilityOfSecondUnmatched gives the best value using base2 for probabilityOfSecondUnmatched
                logProbabilityOfSecondMatched gives the best value using base2 for probabilityOfSecondMatched
                entropySecond gives us the value of H(Y|x = 1)
        */

        double secondSum = secondmatched + secondunmatched;
        double probabilityOfSecondMatched = (double) secondmatched / secondSum;
        double probabilityOfSecondUnmatched = (double) secondunmatched / secondSum;
        double logProbabilityOfSecondMatched = (Math.log(probabilityOfSecondMatched) / log2);
        double logProbabilityOfSecondUnmatched = (Math.log(probabilityOfSecondUnmatched) / log2);

        if (probabilityOfSecondUnmatched == 0)
            logProbabilityOfSecondUnmatched = 0;
        if (probabilityOfSecondMatched == 0)
            logProbabilityOfSecondMatched = 0;

        //entropySecond calculates the entropy when H(Y|x = 1)

        double entropySecond = (-1) * (((probabilityOfSecondMatched) * (logProbabilityOfSecondMatched)) + ((probabilityOfSecondUnmatched) * (logProbabilityOfSecondUnmatched)));

        //totalSum stores the sum of firstSum and secondSum
        double totalSum = firstSum + secondSum;

        //averageEntropy calculates the average entropy using entropyFirst and entropySecond
        double averageEntropy = (((firstSum / totalSum) * (entropyFirst)) + ((secondSum / totalSum) * (entropySecond)));

        if (averageEntropy == -0.0)
            averageEntropy = 0;


        return averageEntropy;
    }

    /*
    This function newSplittingAttribute() takes node as input and returns the index i.e. number of the best splitting attribute by
    calculating the Information Gain and the attribute with max value of Information Gain is chosen as best Splitting attribute
     */
    public int newSplittingAttribute(DTNode node) {
        TreeMap<Integer, Double> tm = new TreeMap<>();
        double minEntropy = 1.0d, entropy;//assigning the minEntropy as 1.0d
        Integer index = 0;
        for (Integer i : node.attributes) {
            entropy = entropy(node, i);//calculating the entropy by passing node and index of the attribute
            if (entropy < minEntropy) {//indirectly calculating the information Gain by comparing the entropies and the attribute with min value of entropy is chosen
                index = i;//assigning the value of best attribute that has minimum entropy
                minEntropy = entropy;
            }
            tm.put(i, entropy);
        }
        return index;//returns the bestSplittingAttribute's number
    }

    public void implementNewNode(DTNode node, int newAttribute) {
        if (node.attributes.size() > 0 && node.leaf != true && node.dataset.size() > 1) {
            DTNode left = new DTNode();
            left.attributes = new ArrayList<>();
            DTNode right = new DTNode();
            right.attributes = new ArrayList<>();
            int numberOfZeroes = 0, numberOfOnes = 0;
            Integer newLeft, newRight;

            for (Row row : node.dataset) {
                if (row.values[newAttribute] == 0)
                    left.dataset.add(row);
                else
                    right.dataset.add(row);
            }

            left.no = 2 * node.no;
            right.no = (2 * node.no) + 1;

            for (Integer i : node.attributes)
                left.attributes.add(i);

            for (Integer i : node.attributes)
                right.attributes.add(i);

            left.attributes.remove(Integer.valueOf(newAttribute));
            right.attributes.remove(Integer.valueOf(newAttribute));

            left.entropy = entropy(left);
            if(left.entropy == 0 || left.dataset.size() < 2 || left.attributes.size() < 1)
                left.leaf = true;

            right.entropy = entropy(right);
            if(right.entropy == 0 || right.dataset.size() < 2 || right.attributes.size() < 1)
                right.leaf = true;

            for(Row row : left.dataset){
                if(row.classLabel == 0)
                    numberOfZeroes++;
                else if(row.classLabel == 1)
                    numberOfOnes++;
            }

            if(numberOfZeroes > numberOfOnes)
                left.finalLabel = 0;
            else if(numberOfOnes > numberOfZeroes)
                left.finalLabel = 1;

            numberOfZeroes = 0;
            numberOfOnes = 0;

            for(Row row : right.dataset){
                if(row.classLabel == 0)
                    numberOfZeroes++;
                else if(row.classLabel == 1)
                    numberOfOnes++;
            }

            if(numberOfZeroes > numberOfOnes)
                right.finalLabel = 0;
            else if(numberOfOnes > numberOfZeroes)
                right.finalLabel = 1;

            if(left.attributes.size() == 1)
                newLeft = left.attributes.get(0);
            else
                newLeft = newSplittingAttribute(left);

            newRight = newSplittingAttribute(right);
            left.splittingAttribute = newLeft;
            right.splittingAttribute = newRight;
            left.parent = node;
            right.parent = node;
            node.leftChild = left;
            node.rightChild = right;
            implementNewNode(left, newLeft);
            implementNewNode(right, newRight);

        }
    }

    /*
    Previously all the attributes calculations have been in the numerical form, hence we have created one more function called
    attributeName() that reads the dataset again and takes in the attribute number and converts all the
    integer values of attributes back to the string values of the attrbute i.e.
    from 0,1,2,3,4,5,6.... to XB,XC,XD,XE,XF,XG,XH......
     */

    public String attributeName(String dataset, int attribute) {
        String namesOfAttributes[];//an array initialised to store the names of attributes

        try {
            FileReader fileReader = new FileReader(dataset);
            BufferedReader br = new BufferedReader(fileReader);
            String data = br.readLine();
            int totalAttributes = Array.getLength(data.split(","));//stores the total number of attributes i.e. 20
            namesOfAttributes = new String[totalAttributes];//gives the array the size 20 based on totalAttributes
            namesOfAttributes = data.split(",");//storing the names of attributes XB,XC,XD,XE,XF,XG,XH...... in the String array
            br.close();
            return namesOfAttributes[attribute];//returning the name of the attribute whose values has been passed
        } catch (FileNotFoundException e) {
            System.out.println("No File Found");
        } catch (IOException e) {
            System.out.println("File cannot be read");
        }

        return null;//return null
    }

    /*
    This function printTree() takes node and dataset as input
    and performs the ID3 algorithm and prints the desired decision tree
     */

    public void printTree(DTNode node, String dataset) {

        if (node.parent != null) {
            int n = (int) (Math.log(node.no) / Math.log(2));//Used for spaces

            for (int i = 0; i < n; i++)
                System.out.print("| ");

            System.out.print("(" + attributeName(dataset, node.parent.splittingAttribute) + "=" + (node.no % 2 == 0 ? 0 : 1) + ") :");//Display the decision tree

            //if node.leaf is true print node.finalLabel
            if (node.leaf)
                System.out.print(node.finalLabel);

            System.out.println();
        }

        //Recursively calling the printTree until node.left !=null
        //OR node.right != null

        if (node.leftChild != null)
            printTree(node.leftChild, dataset);
        if (node.rightChild != null)
            printTree(node.rightChild, dataset);

    }

    /*
    This function finalLabel() takes row of the dataset and node
    as the input and returns the final Label of the node
     */

    public int finalLabel(Row row, DTNode node) {
        //int value = 0;

        if (node == null)
            return 0;

        if (node.leaf == true)
            return node.finalLabel;//if node.leaf is true return node.finalLabel
        else {

            //Recursively calling finalLabel on node.left and node.right
            if (row.values[node.splittingAttribute] == 0)
                return finalLabel(row, node.leftChild);
            else
                return finalLabel(row, node.rightChild);
        }
    }

    /*
    This function calculates the accuracy of the model
    for training, validation and test datasets

    Accuracy  = Number of instances correctly classified/ Total number of instances
     */

    public double accuracy(DTNode node, DTNode node1) {
        double accuracy;
        int trueValues = 0;
        int falseValues = 0;
        int label;
        for (Row row : node1.dataset) {
            label = finalLabel(row, node);
            if (row.classLabel == label)
                trueValues++;
            else
                falseValues++;
        }

        int totalValues = trueValues + falseValues;
        accuracy = (double) trueValues / totalValues;

        return accuracy * 100;
    }

    /*
    This function totalNumberOfNodes takes node as input and
    returns the total number of nodes in the tree
     */

    public int totalNumberOfNodes(DTNode node) {
        int totalNodes = 1;

        if (node.leftChild != null)
            totalNodes += totalNumberOfNodes(node.leftChild);
        if (node.rightChild != null)
            totalNodes += totalNumberOfNodes(node.rightChild);

        return totalNodes;
    }

    /*
    This function totalNumberOfLeafNodes takes node as input and
    returns the total number of child nodes in the tree
     */

    public int totalNumberOfLeafNodes(DTNode node) {
        if (node == null)
            return 0;
        if (node.leaf == true)
            return 1;
        else
            return totalNumberOfLeafNodes(node.leftChild) + totalNumberOfLeafNodes(node.rightChild);
    }

    /*
    This function randomSelect() takes max value as input and
    returns the randomno i.e. random node that could be used for pruning
     */

    public int randomselect(int max) {
        int min = 0;
        int randomno = min + (int) (Math.random() * ((max - min) + 1));
        return randomno;
    }

   /*
   This function arrangeNodes() takes arraylist of nodes and node as input
   and arranges all the nodes in a proper manner after pruning
    */

    public ArrayList<DTNode> arrangeNodes(ArrayList<DTNode> listOfNodes, DTNode node) {
        if (node != null && node.leftChild == null && node.rightChild == null)
            return listOfNodes;
        else {
            listOfNodes.add(node);
            arrangeNodes(listOfNodes, node.leftChild);
            arrangeNodes(listOfNodes, node.rightChild);
        }
        return listOfNodes;
    }

   /*
   This function pruning() takes node as input and is responsible
   for pruning some nodes randomly from the decision tree
    */

    public void pruning(DTNode node) {
        ArrayList<DTNode> listOfNodes = new ArrayList<>();

        listOfNodes = arrangeNodes(listOfNodes, node);
        int randomno = randomselect(listOfNodes.size() - 1);//randomly select any node for pruning

        while (randomno == 0)
            randomno = randomselect(listOfNodes.size() - 1);

        int nodeno = listOfNodes.get(randomno).no;
        switchRandomNode(nodeno, node);
    }

    /*
    This function is used to label the node as leaf node for pruning
    i.e. switches the random node
     */
    public DTNode switchRandomNode(int n, DTNode node) {

        //if node.no is equal to n, then node.leaf = true, node.left = null, node.right = null

        if (node.no == n) {
            node.leaf = true;
            node.leftChild = null;
            node.rightChild = null;
            return node;
        }

        //Recursively calling the switchRandomNode on node.left and node.right
        if (node.leftChild != null)
            switchRandomNode(n, node.leftChild);
        if (node.rightChild != null)
            switchRandomNode(n, node.rightChild);

        return node;
    }
}