package spell;

public class Trie implements ITrie { //Implements ITrie Interface

    public Trie(){}

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word) {
        word = word.toLowerCase();
        int wLength = word.length();
        TrieNode crawler = root;

		for (int i = 0; i < wLength; i++) {
            char currentChar = word.charAt(i);
			int index = currentChar - 'a';
			TrieNode[] crawlerChildren = crawler.getChildren();
			if (crawlerChildren[index] == null) {
                crawler.addNode(index);
                nodeCount++;
			}
            crawler = crawler.getChildren()[index];

            if ((i + 1) == wLength){
                crawler.addCount();
                if (crawler.getValue() == 1){
                    wordCount++;
                }
            }
		}
    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    public TrieNode find(String word) {
        word = word.toLowerCase();
        int wLength = word.length();
        TrieNode crawler = root;

        for (int i = 0; i < word.length(); i++){
            char currentChar = word.charAt(i);
            int charIndex = currentChar - 'a';
            TrieNode[] crawlerChildren = crawler.getChildren();
            if ((crawlerChildren[charIndex] == null) && (i != (wLength - 1))) {
                return null;
            }
            crawler = crawler.getChildren()[charIndex];
        }
        if ((crawler != null) && (crawler.getValue() > 0)) {
            return crawler;
        }
        else{
            return null;
        }
    }

    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    public int getWordCount() {
        return wordCount;
    }

    /*
     * Returns number of nodes in Dictionary
     */
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int hashCode() {
        int hash  = 7;
        hash = 31 * hash + wordCount;
        hash = 31 * hash + nodeCount;
        TrieNode[] children = root.getChildren();
        for (int i = 0; i < 25; i++){
            if (children[i] != null){
                hash += i;
            }
        }
        return  hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (obj == this){
            return true;
        }
        if (obj.getClass() != this.getClass()){
            return false;
        }
        Trie d = (Trie)obj;
        if (this.wordCount != d.wordCount ||
            this.nodeCount != d.nodeCount){
            return false;
        }
        return equals_Helper(this.root, d.root);
    }

    private boolean equals_Helper(TrieNode n1, TrieNode n2){
        //Compare counts of both nodes
        if (n1.getValue() != n2.getValue()){
            return false;
        }
        //See if nodes have children in all the same indices
        TrieNode[] n1Children = n1.getChildren();
        TrieNode[] n2Children = n2.getChildren();
        for (int i = 0; i < 26; i++) {
            if ((n1Children[i] == null && n2Children[i] != null) ||
                    (n1Children[i] != null && n2Children[i] == null)) {
                return false;
            }
        }
        //Recurse on children and compare child subtrees
        boolean trip = true;
        for (int i = 0; i < 26; i++){
            if (n1Children[i] != null){
                trip =  equals_Helper(n1Children[i], n2Children[i]);
            }
        }
        return trip;
    }

    /*
     * For each word in Dictionary, in alpha-order,
     * <word>\n
     */
    @Override
    public String toString() {

        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();

        toString_Helper(root, curWord, output);

        return output.toString();
    }

    private void toString_Helper(TrieNode n, StringBuilder curWord, StringBuilder output) {
        if (n.getValue() > 0){
            output.append(curWord.toString());
            output.append("\n");
        }
        TrieNode[] children = n.getChildren();
        for (int i = 0; i < children.length; i++){
            TrieNode child = n.getChildren()[i];
            if (child != null){
                char childLetter = (char)('a' + i);
                curWord.append(childLetter);
                toString_Helper(child, curWord, output);

                curWord.deleteCharAt(curWord.length() - 1);
            }
        }
    }

    //Data Members
    private TrieNode root = new TrieNode();
    private int wordCount = 0;
    private int nodeCount = 1;
}