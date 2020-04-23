package hr.fer.zemris.java.custom.scripting.nodes;
import hr.fer.zemris.java.custom.scripting.tokens.*;

public class ForLoopNode extends Node {
	private TokenVariable variable;
	private Token startExpression;
	private Token endExpression;
	private Token stepExpression;
	
	public ForLoopNode(Token[] tokens) {
		int numberOfTokens = tokens.length;

		this.variable = (TokenVariable) tokens[0];
		this.startExpression = tokens[1];
		this.endExpression = tokens[2];
		if(numberOfTokens > 3) {
			this.stepExpression = tokens[3];
		}
	}

	public TokenVariable getVariable() {
		return variable;
	}

	public Token getStartExpression() {
		return startExpression;
	}

	public Token getEndExpression() {
		return endExpression;
	}

	public Token getStepExpression() {
		return stepExpression;
	}

	public String asText() {
		String body = "[$FOR " + variable.asText() + " ";
		body += startExpression.asText() + " ";
		body += endExpression.asText() + " ";
		if(stepExpression != null) {
			body += stepExpression.asText() + " ";
		}
		body += "$]";

		return body; 
	}
 }
