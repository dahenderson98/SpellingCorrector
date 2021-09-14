package spell;

public class TrieNode implements INode { //Implements INode Interface

    public TrieNode() {}
    public void addNode(int index) {
        children[index] = new TrieNode();
    }
    public void addCount() {
        count++;
    }
    /**
     * Returns the frequency count for the word represented by the node
     *
     * @return The frequency count for the word represented by the node
     */
    public int getValue() {
        return count;
    }
    public TrieNode[] getChildren() {
        return children;
    }

    //Data Members
    private int count = 0;
    private TrieNode[] children = new TrieNode[26];
}