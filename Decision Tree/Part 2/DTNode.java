import java.util.ArrayList;

/**
 * Created by dverma on 9/18/2017.
 *
 * @author deepanverma, mansikukreja
 */

/*
 left -> This DTNode points to the left child node
 right -> This DTNode points to the right child node
 parent -> This DTNode points to the parent node
 no -> This variable is used to assign the number to each and every node which is useful in counting the number of nodes
 leaf -> This variable checks whether the node is a leaf node or not
 attributes -> This list is used to store the name of the attributes of the given dataset
 finalLabel -> This is used to store value 0 if zeroes>ones else 1.
 splittingAttribute -> This is used to store the value of splittingAttribute
 entropy -> This variable calculates the entropy
 dataset -> This list is used to store instances of the dataset
 */

public class DTNode {

    DTNode leftChild, rightChild, parent;
    int no;
    Boolean leaf;
    ArrayList<Integer> attributes;
    int finalLabel, splittingAttribute;
    double entropy;
    ArrayList<Row> dataset = new ArrayList<>();

    //Initialising a default constructor DTNode() and setting the default values for leaf(false), left(null), right(null) and parent(null)

    public DTNode() {
        leaf = false;
        leftChild = null;
        rightChild = null;
        parent = null;
    }
}