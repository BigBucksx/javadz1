package hr.fer.zemris.java.custom.scripting.tokens;

public class TokenOperator extends Token{
	private String symbol;
	
	public TokenOperator(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String asText() {
		return symbol;
	}

}
