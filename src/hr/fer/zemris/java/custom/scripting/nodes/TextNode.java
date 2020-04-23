package hr.fer.zemris.java.custom.scripting.nodes;

public class TextNode extends Node {
	private String text; 
	
	public TextNode(String text) {
		this.text = new String(text);
	}
	
	public String asText() {
		return text;
	}
}
