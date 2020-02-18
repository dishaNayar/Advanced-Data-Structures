public class MinHeap
{
    private BuildingDetails[] heap;
    public int currentSize;
    private int maxSize;

    public MinHeap()
    {
        this.currentSize = 0;
        this.maxSize = 2000;
        heap = new BuildingDetails[maxSize];
        heap[0] = new BuildingDetails(0,0,0);
    }

    // This methods finds the parent of the current element at position
    private int parentOfCurrentNode(int position)
    {
        if(position > 1)
            return position / 2;
        else
            return 0; // parent does not exist
    }

    // This method returns the left child of the element
    private int leftChildOfCurrentNode(int position)
    {
        return (2 * position);
    }

    // This method returns the right child of the element
    private int rightChildOfCurrentNode(int position)
    {
        return (2 * position) + 1;
    }

    // The method checks if the element is a root or not
    private boolean isRoot(int position)
    {
        if(position <= currentSize && currentSize == 1)
            return true;
        return false;
    }

    // The method returns true or false based on element being a leaf or not
    private boolean isLeaf(int position)
    {
        if (position <= currentSize && position > (currentSize / 2))
        {
            return true;
        }
        return false;
    }

    // This method is used to fi the heap
    private void minHeapify(int currentPosition)
    {
        // If the node is a non-leaf node and greater than any of its child
        if (!isLeaf(currentPosition))
        {
            if(heap[leftChildOfCurrentNode(currentPosition)] != null)
            {
                if (heap[currentPosition].executed_time > heap[leftChildOfCurrentNode(currentPosition)].executed_time)
                {
                    //  If execution time of left child is less
                    //  Swap with the left child
                    SwapTwoNodes(currentPosition, leftChildOfCurrentNode(currentPosition));
                    minHeapify(leftChildOfCurrentNode(currentPosition));
                }
                if(heap[currentPosition].executed_time == heap[leftChildOfCurrentNode(currentPosition)].executed_time)
                {
                    if(heap[currentPosition].buildingNum > heap[leftChildOfCurrentNode(currentPosition)].buildingNum)
                        SwapTwoNodes(currentPosition, leftChildOfCurrentNode(currentPosition));
                }
            }
            if(heap[rightChildOfCurrentNode(currentPosition)] != null){
                if (heap[currentPosition].executed_time > heap[rightChildOfCurrentNode(currentPosition)].executed_time)
                {
                    // If the execution time of the right child is less
                    // Swap with the right child
                    SwapTwoNodes(currentPosition, rightChildOfCurrentNode(currentPosition));
                    minHeapify(rightChildOfCurrentNode(currentPosition));
                }
                if(heap[currentPosition].executed_time == heap[rightChildOfCurrentNode(currentPosition)].executed_time)
                {
                    if(heap[currentPosition].buildingNum > heap[rightChildOfCurrentNode(currentPosition)].buildingNum)
                        SwapTwoNodes(currentPosition, rightChildOfCurrentNode(currentPosition));
                }
            }
        }
    }

    /*The method is used to swap two nodes in position 1 and 2.
    It is used to swap the parent and the child during minheapify operation*/
    private void SwapTwoNodes(int currentPosition, int NodeToSwapPosition)
    {
        BuildingDetails temp;
        temp = heap[currentPosition];
        heap[currentPosition] = heap[NodeToSwapPosition];
        heap[NodeToSwapPosition] = temp;
    }

    // The method inserts a new node into the heap and increase its size.
    public void insert(BuildingDetails node)
    {
        if (currentSize >= maxSize)
        {
            return;
        }
        heap[++currentSize] = node;
    }

    // the method removes the min element
    public void remove()
    {
        if (currentSize > 1)
        {
            heap[1] = heap[currentSize--];
            BuildingDetails[] newHeap = new BuildingDetails[maxSize];
            for(int i = 0; i<= currentSize; i++){
                newHeap[i] = heap[i];
            }
            heap = newHeap.clone();
            rearrangeMinHeap();
        }
        else
            currentSize --;
    }

    /*The method calls the minHeapify operation, from the parent of the last node till the root
    This arranges the heap even when multiple changes have been made */
    public void rearrangeMinHeap()
    {
        for (int i = (currentSize / 2); i >= 1; i--)
        {
            minHeapify(i);
        }
    }

    /*this method returns the element at the root if the size of the heap is greater than 0.
    It is used to select the building to start construction on */
    public BuildingDetails workingNode()
    {
        if(currentSize != 0)
            return heap[1];
        else
            return null;
    }
}