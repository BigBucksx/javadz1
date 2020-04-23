package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.scripting.tokens.*;

public class EchoNode extends Node{
	private Token[] tokens;

	public EchoNode(Token[] tokens) {
		this.tokens = tokens;
	}

	public Token[] getTokens() {
		return tokens;
	}

	public String asText() {
		String body = "[$=";
		for(int i = 0; i < tokens.length; i++) {
			body += tokens[i].asText() + " ";
		}
		body = body.substring(0, body.length() - 1);
		return body + "$]";
	}
}
