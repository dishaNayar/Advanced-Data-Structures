// This class defines a node in a red black tree
public class RbNode
{
    BuildingDetails data;
    RbNode parent;
    RbNode leftChild;
    RbNode rightChild;
    NodeColor color;

    public enum NodeColor
    {
        Red,
        Black
    }
}