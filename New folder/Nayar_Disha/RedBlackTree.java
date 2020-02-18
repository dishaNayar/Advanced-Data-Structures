import java.util.ArrayList;

public class RedBlackTree
{
    private RbNode root;
    private RbNode externalNode;

    public RedBlackTree()
    {
        externalNode = new RbNode();
        externalNode.color = RbNode.NodeColor.Black;
        externalNode.leftChild = null;
        externalNode.rightChild = null;
        externalNode.data = new BuildingDetails(0,0,0);
        root = externalNode;
    }

    // This method is used to insert a new element into a red black tree
    public void insert(BuildingDetails element)
    {
        RbNode node = new RbNode();
        node.data = element;
        node.parent = null;
        node.leftChild = externalNode;
        node.rightChild = externalNode;
        node.color = RbNode.NodeColor.Red;

        RbNode subRoot = this.root;
        RbNode parent = null;

        // traversing from top to bottom and finding the parent, where the new node has to be inserted
        while (subRoot != externalNode)
        {
            parent = subRoot;
            if (node.data.buildingNum > subRoot.data.buildingNum)
                subRoot = subRoot.rightChild;
            else
                subRoot = subRoot.leftChild;
        }

        node.parent = parent;

        if (parent == null) {
            root = node;
        } else if (node.data.buildingNum > parent.data.buildingNum) {
            parent.rightChild = node;
        } else {
            parent.leftChild = node;
        }

        // If the new node inserted is a root, then no fixing required
        if (node.parent == null)
        {
            // change the colour of the inserted node to black
            node.color = RbNode.NodeColor.Black;
            return;
        }

        // if the grandparent is null, simply return
        if (node.parent.parent == null)
        {
            return;
        }

        RearrangeTreeAfterInsert(node);
    }

    // This method is used to fix the red black tree after an insert operation has been performed
    public void RearrangeTreeAfterInsert(RbNode node)
    {
        // if the parent of the inserted node is black then do nothing
        if(node.parent.color == RbNode.NodeColor.Black)
        {
            return;
        }

        RbNode uncle;
        // continue operations till the parent is red
        while (node.parent.color == RbNode.NodeColor.Red)
        {
            if (node.parent == node.parent.parent.rightChild)
            {
                uncle = node.parent.parent.leftChild;
                if(uncle.color == RbNode.NodeColor.Black || uncle == externalNode)
                {
                    // if node is the left child of the parent then Perform RL rotation
                    // first rotate right then rotate left
                    if(node == node.parent.leftChild){
                        node = node.parent;
                        rotateToRight(node);
                    }
                    node.parent.color = RbNode.NodeColor.Black;
                    node.parent.parent.color = RbNode.NodeColor.Red;
                    // if the node if the right child of the parent, then RR rotation
                    // rotate left about grand parent
                    rotateToLeft(node.parent.parent);
                }
                // uncle is red, so just do color flip
                // grandparent changes to red, parent and the node changes to black
                else {
                    node.parent.parent.color = RbNode.NodeColor.Red;
                    node.parent.color = RbNode.NodeColor.Black;
                    uncle.color = RbNode.NodeColor.Black;
                    node = node.parent.parent; // change node to grandparent and continue further checks
                }
            }
            else { // if parent is the left child
                uncle = node.parent.parent.rightChild;
                if(uncle.color == RbNode.NodeColor.Black || uncle == externalNode)
                {
                    // if node is the right child, perform LR rotation
                    // Left rotate  followed by right rotate
                    if (node == node.parent.rightChild)
                    {
                        node = node.parent;
                        rotateToLeft(node);
                    }
                    node.parent.color = RbNode.NodeColor.Black;
                    node.parent.parent.color = RbNode.NodeColor.Red;
                    // if the node if the left child of the parent, then LL rotation
                    // rotate right about grand parent
                    rotateToRight(node.parent.parent);
                }
                else{
                    node.parent.parent.color = RbNode.NodeColor.Red;
                    node.parent.color = RbNode.NodeColor.Black;
                    uncle.color = RbNode.NodeColor.Black;
                    node = node.parent.parent;
                }
            }
            if (node == root)
            {
                break;
            }
        }
        // change the colour of the root to black
        root.color = RbNode.NodeColor.Black;

    }

    // This method is used to rotate left about the node parent
    public void rotateToLeft(RbNode parent)
    {
        RbNode insertedNode = parent.rightChild;
        if (insertedNode.leftChild != externalNode)
        {
            parent.rightChild = insertedNode.leftChild;
            insertedNode.leftChild.parent = parent;
        }
        else{
            parent.rightChild = externalNode;
        }

        insertedNode.parent = parent.parent;

        if (parent.parent == null)
        {
            this.root = insertedNode;
        }
        // check if the parent is the right child or the left and change accordingly
        else if (parent == parent.parent.rightChild)
        {
            parent.parent.rightChild = insertedNode;
        }
        else {
            parent.parent.leftChild = insertedNode;
        }
        insertedNode.leftChild = parent;
        parent.parent = insertedNode;
    }

    // This methos is used to rotate right about the node parent
    public void rotateToRight(RbNode parent)
    {
        RbNode insertedNode = parent.leftChild;

        if (insertedNode.rightChild != externalNode)
        {
            parent.leftChild = insertedNode.rightChild;
            insertedNode.rightChild.parent = parent;
        }
        else {
            parent.leftChild = externalNode;
        }
        insertedNode.parent = parent.parent;
        if (parent.parent == null) {
            this.root = insertedNode;
        }
        else if (parent == parent.parent.rightChild) {
            parent.parent.rightChild = insertedNode;
        }
        else {
            parent.parent.leftChild = insertedNode;
        }
        insertedNode.rightChild = parent;
        parent.parent = insertedNode;
    }

    // This method is used to perform delete operations in a red black tree
    public void delete (BuildingDetails element)
    {
        // find the node containing element
        RbNode node = this.root;
        RbNode nodeToDelete = externalNode;
        RbNode childOfDNode, deleteCopy;
        while (node != externalNode){
            if (node.data.buildingNum == element.buildingNum) {
                nodeToDelete = node;
            }

            if (node.data.buildingNum <= element.buildingNum) {
                node = node.rightChild;
            } else {
                node = node.leftChild;
            }
        }

        if (nodeToDelete == externalNode) {
             /*this case will never arise, as the building delete command is called only when the building is present in the heap
             and its execution time is equal to total time*/
            System.out.println("building to be deleted, not found");
            return;
        }

        deleteCopy = nodeToDelete;
        RbNode.NodeColor colorOfDNode = deleteCopy.color;

        // when the node to delete has one child
        if(nodeToDelete.rightChild == externalNode){
            childOfDNode = nodeToDelete.leftChild;
            replaceNode(nodeToDelete, nodeToDelete.leftChild);
        }
        else if (nodeToDelete.leftChild == externalNode){
            childOfDNode = nodeToDelete.rightChild;
            replaceNode(nodeToDelete, nodeToDelete.rightChild);
        }
        // when the node to delete has two children, replace the node to be deleted with the minimum value in the right sub tree
        else {
            deleteCopy = minElement(nodeToDelete.rightChild);
            colorOfDNode = deleteCopy.color;
            childOfDNode = deleteCopy.rightChild;
            // case 1- if the min element has no further right children
            if (deleteCopy.parent == nodeToDelete) {
                childOfDNode.parent = deleteCopy;
            }
            // case 2 - if the min element has further right children, first replace the min element location with its right child
            else {
                replaceNode(deleteCopy, deleteCopy.rightChild);
                deleteCopy.rightChild = nodeToDelete.rightChild;
                deleteCopy.rightChild.parent = deleteCopy;
            }

            // finally replace the min element in the position of the node to delete
            replaceNode(nodeToDelete, deleteCopy);
            deleteCopy.leftChild = nodeToDelete.leftChild;
            deleteCopy.leftChild.parent = deleteCopy;
            deleteCopy.color = nodeToDelete.color;
        }
        // fix the tree if the color of the deleted node is black
        if (colorOfDNode == RbNode.NodeColor.Black){
            RearrangeTreeAfterDelete(childOfDNode);
        }
    }

    // This method is used to fix the red black tree after a delete operation is performed
    public void RearrangeTreeAfterDelete(RbNode node)
    {
        RbNode sibling;
        while(node.color == RbNode.NodeColor.Black && node != root){

            // case : Sibling is the left child
            if(node == node.parent.rightChild){
                sibling = node.parent.leftChild;
                 /*if sibling is red switch colors of sibling and parent
                 then it changes to the case where sibling is black*/
                if (sibling.color == RbNode.NodeColor.Red) {
                    sibling.color = RbNode.NodeColor.Black;
                    node.parent.color = RbNode.NodeColor.Red;
                    rotateToRight(node.parent);
                    sibling = node.parent.leftChild;
                }

                if(sibling == externalNode)
                    return;

                // when the left and the right child of the sibling are black
                if (sibling.leftChild.color == RbNode.NodeColor.Black && sibling.rightChild.color == RbNode.NodeColor.Black) {
                    sibling.color = RbNode.NodeColor.Red;
                    if(node.parent.color == RbNode.NodeColor.Red){
                        node.parent.color = RbNode.NodeColor.Black;
                        return;
                    }
                    else {
                        node = node.parent;
                    }

                }
                // when the left child of the sibling is black
                else {
                    if (sibling.leftChild.color == RbNode.NodeColor.Black) {
                        sibling.color = RbNode.NodeColor.Red;
                        sibling.rightChild.color = RbNode.NodeColor.Black;
                        rotateToLeft(sibling);
                        sibling = node.parent.leftChild;
                    }

                    sibling.leftChild.color = RbNode.NodeColor.Black;
                    node.parent.color = RbNode.NodeColor.Black;
                    sibling.color = node.parent.color;

                    rotateToRight(node.parent);
                    node = root;
                }
            }
            // case : when sibling is the right child
            else{
                sibling = node.parent.rightChild;
                if (sibling.color == RbNode.NodeColor.Red) {
                    sibling.color = RbNode.NodeColor.Black;
                    node.parent.color = RbNode.NodeColor.Red;
                    rotateToLeft(node.parent);
                    sibling = node.parent.rightChild;
                }
                if(sibling == externalNode)
                    return;

                // when both the children of the sibling are black
                if (sibling.leftChild.color == RbNode.NodeColor.Black && sibling.rightChild.color == RbNode.NodeColor.Black) {
                    sibling.color = RbNode.NodeColor.Red;
                    if(node.parent.color == RbNode.NodeColor.Red){
                        node.parent.color = RbNode.NodeColor.Black;
                        return;
                    }
                    else{
                        node = node.parent;
                    }

                }
                // when the right child of the sibling is black
                else {
                    if (sibling.rightChild.color == RbNode.NodeColor.Black) {
                        sibling.color = RbNode.NodeColor.Red;
                        sibling.leftChild.color = RbNode.NodeColor.Black;
                        rotateToRight(sibling);
                        sibling = node.parent.rightChild;
                    }

                    sibling.rightChild.color = RbNode.NodeColor.Black;
                    node.parent.color = RbNode.NodeColor.Black;
                    sibling.color = node.parent.color;
                    rotateToLeft(node.parent);
                    node = root;
                }
            }
        }
    }

    /* This method is used to replace a node, with another node.
    It is used to replace nodes during delete operation*/
    private void replaceNode(RbNode nodeToReplace, RbNode replacingNode){
        if (nodeToReplace.parent == null) {
            root = replacingNode;
        }
        else if (nodeToReplace == nodeToReplace.parent.rightChild){
            nodeToReplace.parent.rightChild = replacingNode;
        } else {
            nodeToReplace.parent.leftChild = replacingNode;
        }
        replacingNode.parent = nodeToReplace.parent;
    }

    // This method returns the node with the least building value on the path from element to the external node.
    public RbNode minElement(RbNode element) {
        while (element.leftChild != externalNode) {
            element = element.leftChild;
        }
        return element;
    }


    // This method is used to print a building with buildingNumber from the red black tree
    public void printBuilding(int buildingNumber) {
        RbNode node =  SearchNodeInTree(this.root, buildingNumber);
        if (node == externalNode){
            System.out.println("(0,0,0)");
        }
        else{
            System.out.println("(" + node.data.buildingNum + "," + node.data.executed_time + "," + node.data.total_time + ")");
        }
    }

    /* This method searches and returns a single building in the red black tree
    It is used while printing a building with a particular building number*/
    public RbNode SearchNodeInTree(RbNode element, int buildingNumber){
        if(element == externalNode){
            return externalNode;
        }
        if (element.data.buildingNum == buildingNumber) {
            return element;
        }

        if (buildingNumber > element.data.buildingNum) {
            return SearchNodeInTree(element.rightChild, buildingNumber);
        }
        return SearchNodeInTree(element.leftChild, buildingNumber);
    }

    // This method prints the buildings in a red black tree within the range number1 to number2
    public void printRange (int number1, int number2){
        ArrayList<BuildingDetails> buildingNumbers = new ArrayList<BuildingDetails>();
        buildingNumbers = SearchRangeInTree(this.root, number1, number2, buildingNumbers);

        if(!buildingNumbers.isEmpty()){
            String outputToPrint = new String();
            for (BuildingDetails bldg : buildingNumbers){
                String t = "(" + bldg.buildingNum + "," + bldg.executed_time + "," + bldg.total_time + "),";
                outputToPrint = outputToPrint.concat(t);
            }
            outputToPrint = outputToPrint.substring(0, outputToPrint.length()-1);
            System.out.println(outputToPrint);
        }
        else{
            System.out.println("(0,0,0)");
        }
    }

    /* This method searches the red black tree and returns a list of building between number1 and number2
    It is used in printing building in a particular range*/
    private ArrayList<BuildingDetails> SearchRangeInTree(RbNode element, int number1, int number2, ArrayList<BuildingDetails> bNums) {
        if (element != externalNode) {
            if(element.leftChild != externalNode){
                if(element.data.buildingNum > number1){
                    SearchRangeInTree(element.leftChild, number1, number2, bNums);
                }
            }
            if(element.data.buildingNum >= number1 && element.data.buildingNum <= number2){
                bNums.add(element.data);
            }
            if(element.rightChild != externalNode){
                if(element.data.buildingNum < number2){
                    SearchRangeInTree(element.rightChild, number1, number2, bNums);
                }
            }
        }
        return bNums;
    }
}
