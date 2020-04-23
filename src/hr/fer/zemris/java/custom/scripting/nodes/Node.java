package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.collections.ArrayBackendIndexedCollection;
import hr.fer.zemris.java.custom.scripting.tokens.*;
public class Node {
	private ArrayBackendIndexedCollection children;
	
	public Node() {
		children = new ArrayBackendIndexedCollection();
	}
	public void addChildNode(Node child) {
		children.add(child);
	}
	
	public int numberOfChildren() {
		return children.size();
	}
	
	public Node getChild(int index) {
		Node retval = (Node) children.get(index);
		return retval;
	}

	public String asText() {
		return "";
	}
}
